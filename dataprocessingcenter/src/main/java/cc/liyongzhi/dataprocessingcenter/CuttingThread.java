package cc.liyongzhi.dataprocessingcenter;

import java.util.concurrent.LinkedBlockingQueue;

/**将每个数据包分离
 * Created by lee on 8/19/16.
 */

public class CuttingThread extends Thread implements Runnable {

    private int mHeaderLength;
    private int mBodyLength;
    private LinkedBlockingQueue<Byte> mOriginDataQueue;

    private boolean mRunFlag;

    public CuttingThread(int headerLength, int bodyLength, LinkedBlockingQueue<Byte> originDataQueue) {
        mHeaderLength = headerLength;
        mBodyLength = bodyLength;
        mOriginDataQueue = originDataQueue;
        mRunFlag = true;
    }

    public void run() {

        while (mRunFlag) {
            try {
                byte data = mOriginDataQueue.take();



            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void shutdown() {
        mRunFlag = false;
    }

}
