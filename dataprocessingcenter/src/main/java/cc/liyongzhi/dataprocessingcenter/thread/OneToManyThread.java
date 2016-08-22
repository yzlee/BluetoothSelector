package cc.liyongzhi.dataprocessingcenter.thread;

import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import cc.liyongzhi.dataprocessingcenter.DataProcessingSetting;

/** 一分多线程。将一个queue分为多个，为了防止阻塞线程，offer返回false后清空。
 * Created by lee on 8/22/16.
 */

public class OneToManyThread<E> extends Thread implements Runnable {

    private boolean stopFlag = false;
    private DataProcessingSetting setting;
    private LinkedBlockingQueue<E> queue;
    private ArrayList<LinkedBlockingQueue<E>> outList = new ArrayList<>();

    public OneToManyThread(LinkedBlockingQueue<E> queue, DataProcessingSetting setting) {
        this.queue = queue;
        this.setting = setting;
    }

    public void addQueue(LinkedBlockingQueue<E> out) {
        outList.add(out);
    }

    @Override
    public void run() {

        while (!stopFlag) {

            try {
                E data = queue.take();
                for (LinkedBlockingQueue<E> out : outList) {
                    boolean result = out.offer(data);
                    if (!result) {
                        out.clear();
                        setting.getManager().manageOneToManyThreadPutDataFailed();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public void shutdown() {
        stopFlag = true;
    }

}
