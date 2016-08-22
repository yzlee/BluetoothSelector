package cc.liyongzhi.dataprocessingcenter;

import java.util.concurrent.LinkedBlockingQueue;

import cc.liyongzhi.dataprocessingcenter.interf.DataProcessingWarningManager;
import cc.liyongzhi.dataprocessingcenter.thread.OneToManyThread;
import cc.liyongzhi.dataprocessingcenter.thread.cuttingthread.CuttingThread;
import cc.liyongzhi.dataprocessingcenter.thread.SavingThread;
import cc.liyongzhi.dataprocessingcenter.thread.parsingbodythread.ParsingBodyThread;
import cc.liyongzhi.dataprocessingcenter.thread.parsingheaderthread.ParsingHeaderThread;

/** 数据处理中心
 * 读取数据后，分为：
 * 1、 12个导联的byte数组
 * 2、 本地保存
 * 线程启动顺序分为：
 * 1、 切割线程： 将每秒数据包分离。每个header和body为一个数据包。
 * 2、 保存线程(由用户手动开启)： 将数据去头后保存到本地。
 * Created by lee on 8/18/16.
 */

public class DataProcessingCenter implements DataProcessingInterface {

    private static DataProcessingCenter mInstance;

    private DataProcessingSetting mSetting;
    private DataProcessingWarningManager mManager;
    private CuttingThread mCuttingThread;
    private ParsingHeaderThread mParsingHeaderThread;
    private SavingThread mSavingThread;
    private ParsingBodyThread mParsingBodyThread;
    private OneToManyThread<byte[]> mDupHeaderParsedQueue;

    private LinkedBlockingQueue<Byte> originDataQueue = new LinkedBlockingQueue<>();  //原始数据队列
    private LinkedBlockingQueue<byte[]> packetQueue = new LinkedBlockingQueue<>(); //按包打包后的队列
    private LinkedBlockingQueue<byte[]> packetQueueWithHeaderParsed = new LinkedBlockingQueue<>(); //解析过头文件的队列
    private LinkedBlockingQueue<byte[]> packetQueueForSaving = new LinkedBlockingQueue<>(); //用于保存的队列
    private LinkedBlockingQueue<byte[]> packetQueueForDataParsing = new LinkedBlockingQueue<>(); //用于解析body的队列。
    private LinkedBlockingQueue<short[]> dataQueue = new LinkedBlockingQueue<>(); //数据队列，每个元素为一个12大小的数据，保存着12导联的数据。


    private DataProcessingCenter(DataProcessingSetting setting) {
        mSetting = setting;
        mManager = setting.getManager();

        //正序初始化，倒序执行，防止数据丢失。
        mCuttingThread = CuttingThread.getInstance(originDataQueue, packetQueue, mSetting);
        mParsingHeaderThread = ParsingHeaderThread.getInstance(packetQueue, packetQueueWithHeaderParsed, mSetting);
        mDupHeaderParsedQueue = new OneToManyThread<>(packetQueueWithHeaderParsed, mSetting);
        mDupHeaderParsedQueue.addQueue(packetQueueForSaving);
        mDupHeaderParsedQueue.addQueue(packetQueueForDataParsing);
        mSavingThread = SavingThread.getInstance(packetQueueForSaving, mSetting);
        mParsingBodyThread = ParsingBodyThread.getInstance(packetQueueForDataParsing, dataQueue, mSetting);



        //倒序执行
        mParsingBodyThread.start();
        mSavingThread.start();
        mDupHeaderParsedQueue.start();
        mParsingHeaderThread.start();
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


    /**
     * 更换病人后需重置
     */
    public void reset() {
        //正序停止,保证所有数据都能执行完毕
        mCuttingThread.shutdown();
        mParsingHeaderThread.shutdown();
        mDupHeaderParsedQueue.shutdown();
        mSavingThread.shutdown();
        mParsingBodyThread.shutdown();

        mSetting = null;
        mInstance = null;
    }

}
