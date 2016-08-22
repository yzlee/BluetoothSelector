package cc.liyongzhi.dataprocessingcenter;

import android.os.Environment;
import java.util.concurrent.LinkedBlockingQueue;

import cc.liyongzhi.dataprocessingcenter.thread.cuttingthread.CuttingThread;
import cc.liyongzhi.dataprocessingcenter.thread.SavingThread;

/** 数据处理中心
 * 读取数据后，分为：
 * 1、 12个导联的byte数组
 * 2、 本地保存
 * 线程启动顺序分为：
 * 1、 切割线程： 将每秒数据包分离。每个header和body为一个数据包。
 * 2、 保存线程(由用户手动开启)： 将数据去头后保存到本地。
 * Created by lee on 8/18/16.
 */

public class DataProcessingCenter {

    private static DataProcessingCenter mInstance;

    private DataProcessingSetting mSetting;
    private DataProcessingWarningManager mManager;
    private CuttingThread mCuttingThread;
    private SavingThread mSavingThread;

    private LinkedBlockingQueue<Byte> originDataQueue = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<byte[]> cutDataQueue = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<byte[]> cutDataQueueForSaving = new LinkedBlockingQueue<>();


    private DataProcessingCenter(DataProcessingSetting setting) {
        mSetting = setting;
        mManager = setting.getManager();

        //正序初始化，倒序执行，防止数据丢失。
        mCuttingThread = CuttingThread.getInstance(originDataQueue, cutDataQueue, cutDataQueueForSaving, mSetting);
        mSavingThread = SavingThread.getInstance(cutDataQueueForSaving, mSetting);


        //倒序执行

        mSavingThread.start();
        mCuttingThread.start();
    }

    /**
     * 原始数据入口
     */
    public synchronized void putOriginData(byte b) {
        try {
            originDataQueue.put(b);
        } catch (InterruptedException e) {
            mManager.manageOriginDataQueueWarning(originDataQueue, e);
        }
    }

    public void startSaving() {
        mSavingThread.startSaving();
    }

    public static DataProcessingCenter getInstance(DataProcessingSetting setting) {
        if (mInstance == null) {
            mInstance = new DataProcessingCenter(setting);
        }
        return mInstance;
    }


    public void reset() {
        //正序停止,保证所有数据都能执行完毕
        mCuttingThread.shutdown();
        mSavingThread.shutdown();

        mInstance = null;
    }

}
