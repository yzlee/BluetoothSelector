package cc.liyongzhi.dataprocessingcenter;

import java.lang.ref.PhantomReference;
import java.util.concurrent.LinkedBlockingQueue;

/** 数据处理中心
 * 读取数据后，分为：
 * 1、 12个导联的byte数组
 * 2、 本地保存
 * 线程启动顺序分为：
 * 1、 切割线程： 将每秒数据包分离。每个header和body为一个数据包。
 * Created by lee on 8/18/16.
 */

public class DataProcessingCenter {

    public static final int HEADER_LENGTH = 19;
    public static final int BODY_LENGTH = 4000;

    private static DataProcessingCenter mInstance;
    private DataProcessingWarningManager mManager;


    private LinkedBlockingQueue<Byte> originDataQueue = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<byte[]> cutDataQueue = new LinkedBlockingQueue<>();



    private DataProcessingCenter(DataProcessingWarningManager manager) {
        mManager = manager;

        CuttingThread cuttingThread = new CuttingThread(HEADER_LENGTH, BODY_LENGTH, originDataQueue, cutDataQueue, mManager);

        cuttingThread.start();
    }

    /**
     * 原始数据入口
     */
    public synchronized void putOriginData(byte b) {
        try {
            originDataQueue.put(b);
        } catch (InterruptedException e) {
            mManager.mangeOriginDataQueueWarning(originDataQueue, e);
        }
    }

    public synchronized void putCutData(byte[] bytes) {

    }


    public static DataProcessingCenter getInstance(DataProcessingWarningManager manager) {
        if (mInstance == null) {
            mInstance = new DataProcessingCenter(manager);
        }
        return mInstance;
    }

}
