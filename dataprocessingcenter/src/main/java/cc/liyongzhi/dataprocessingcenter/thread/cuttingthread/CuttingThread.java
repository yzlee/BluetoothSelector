package cc.liyongzhi.dataprocessingcenter.thread.cuttingthread;

import java.util.concurrent.LinkedBlockingQueue;

import cc.liyongzhi.dataprocessingcenter.DataProcessingSetting;
import cc.liyongzhi.dataprocessingcenter.interf.DataProcessingWarningManager;

/**将每个数据包分离
 * Created by lee on 8/19/16.
 */

public class CuttingThread extends Thread implements Runnable {

    private static CuttingThread mInstance;

    private int mHeaderLength;
    private int mBodyLength;
    private LinkedBlockingQueue<Byte> mOriginDataQueue;
    private LinkedBlockingQueue<byte[]> mCutDataQueue;
    private DataProcessingSetting mSetting;
    private DataProcessingWarningManager mManager;

    private boolean mRunFlag;

    private CuttingThread(LinkedBlockingQueue<Byte> originDataQueue,
                          LinkedBlockingQueue<byte[]> cutDataQueue,
                          DataProcessingSetting setting) {

        mOriginDataQueue = originDataQueue;
        mCutDataQueue = cutDataQueue;
        mSetting = setting;

        mHeaderLength = mSetting.getHeaderLength();
        mBodyLength = mSetting.getBodyLength();
        mManager = mSetting.getManager();
        mRunFlag = true;
    }

    public void run() {

        DataParser parser = DataParser.getInstance(mCutDataQueue);
        while (mRunFlag) {
            try {
                byte data = mOriginDataQueue.take();
                parser.parsePacket(data);
            } catch (InterruptedException e) {
                mManager.manageCuttingThreadQueueWarning(mOriginDataQueue, mCutDataQueue, e);
            }
        }
    }

    public void shutdown() {
        mRunFlag = false;
    }

    public static CuttingThread getInstance(LinkedBlockingQueue<Byte> originDataQueue,
                                            LinkedBlockingQueue<byte[]> cutDataQueue,
                                            DataProcessingSetting setting) {
        if (mInstance == null) {
            mInstance = new CuttingThread(originDataQueue, cutDataQueue, setting);
        }
        return mInstance;
    }

}
