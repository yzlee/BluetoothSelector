package cc.liyongzhi.dataprocessingcenter;

import java.util.concurrent.LinkedBlockingQueue;

/**将每个数据包分离
 * Created by lee on 8/19/16.
 */

public class CuttingThread extends Thread implements Runnable {

    private int mHeaderLength;
    private int mBodyLength;
    private LinkedBlockingQueue<Byte> mOriginDataQueue;
    private LinkedBlockingQueue<byte[]> mCutDataQueue;
    private DataProcessingWarningManager mManager;

    private boolean mRunFlag;

    public CuttingThread(int headerLength, int bodyLength, LinkedBlockingQueue<Byte> originDataQueue, LinkedBlockingQueue<byte[]> cutDataQueue, DataProcessingWarningManager manager) {
        mHeaderLength = headerLength;
        mBodyLength = bodyLength;
        mOriginDataQueue = originDataQueue;
        mCutDataQueue = cutDataQueue;
        mManager = manager;
        mRunFlag = true;
    }

    public void run() {

        DataParser parser = DataParser.getInstance(mCutDataQueue);
        while (mRunFlag) {
            try {
                byte data = mOriginDataQueue.take();
                parser.parsePacket(data);
            } catch (InterruptedException e) {
                mManager.mangeCuttingThreadQueueWarning(mOriginDataQueue, mCutDataQueue, e);
            }
        }

    }

    public void shutdown() {
        mRunFlag = false;
    }

}
