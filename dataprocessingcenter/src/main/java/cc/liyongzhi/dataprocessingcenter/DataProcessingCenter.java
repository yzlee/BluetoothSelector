package cc.liyongzhi.dataprocessingcenter;

import java.util.concurrent.LinkedBlockingQueue;

import cc.liyongzhi.dataprocessingcenter.interf.DataDrawer;
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
    private OneToManyThread<byte[]> mDup0;
    private OneToManyThread<short[]> mDup1;

    private LinkedBlockingQueue<Byte> originDataQueue = new LinkedBlockingQueue<>();  //原始数据队列
    private LinkedBlockingQueue<byte[]> packetQueue = new LinkedBlockingQueue<>(); //按包打包后的队列
    private LinkedBlockingQueue<byte[]> packetQueueWithHeaderParsed = new LinkedBlockingQueue<>(); //解析过头文件的队列
    private LinkedBlockingQueue<byte[]> packetQueueForSaving = new LinkedBlockingQueue<>(); //用于保存的队列
    private LinkedBlockingQueue<byte[]> packetQueueForDataParsing = new LinkedBlockingQueue<>(); //用于解析body的队列。
    private LinkedBlockingQueue<short[]> dataQueue = new LinkedBlockingQueue<>(); //数据队列，每个元素为一个12大小的数据，保存着12导联的数据。
    private LinkedBlockingQueue<short[]> dataQueueForDrawing = new LinkedBlockingQueue<>();// 用于画图的队列


    private DataProcessingCenter(DataProcessingSetting setting) {
        mSetting = setting;
        mManager = setting.getManager();

        //正序初始化，倒序执行，防止数据丢失。
        mCuttingThread = CuttingThread.getInstance(originDataQueue, packetQueue, mSetting);  //将带有头文件的连续数据按包切割
        mParsingHeaderThread = ParsingHeaderThread.getInstance(packetQueue, packetQueueWithHeaderParsed, mSetting); //分析头文件
        mDup0 = new OneToManyThread<>(packetQueueWithHeaderParsed, mSetting); //复制两份
        mDup0.addQueue(packetQueueForSaving);
        mDup0.addQueue(packetQueueForDataParsing);
        mSavingThread = SavingThread.getInstance(packetQueueForSaving, mSetting); //这份用于保存
        mParsingBodyThread = ParsingBodyThread.getInstance(packetQueueForDataParsing, dataQueue, mSetting); //这份用于将数据转储为12个导联一组的short数组。
        mDup1 = new OneToManyThread<>(dataQueue, mSetting);
        mDup1.addQueue(dataQueueForDrawing);



        //倒序执行
        mDup1.start();
        mParsingBodyThread.start();
        mSavingThread.start();
        mDup0.start();
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

    @Override
    public void setDataDrawer(DataDrawer drawer) {
        drawer.setInputQueue(dataQueueForDrawing);
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
        mDup0.shutdown();
        mSavingThread.shutdown();
        mParsingBodyThread.shutdown();
        mDup1.shutdown();

        mSetting = null;
        mInstance = null;
    }

}
