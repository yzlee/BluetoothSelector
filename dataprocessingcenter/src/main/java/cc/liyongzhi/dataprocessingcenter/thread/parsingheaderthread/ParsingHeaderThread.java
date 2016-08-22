package cc.liyongzhi.dataprocessingcenter.thread.parsingheaderthread;

import java.util.concurrent.LinkedBlockingQueue;

import cc.liyongzhi.dataprocessingcenter.DataProcessingSetting;

/**
 * Created by lee on 8/22/16.
 */

public class ParsingHeaderThread extends Thread implements Runnable {

    private static ParsingHeaderThread mInstance;
    private LinkedBlockingQueue<byte[]> in;
    private LinkedBlockingQueue<byte[]> out;
    private DataProcessingSetting setting;
    private boolean stopFlag = false;


    private ParsingHeaderThread(LinkedBlockingQueue<byte[]> in, LinkedBlockingQueue<byte[]> out, DataProcessingSetting setting) {
        this.in = in;
        this.out = out;
        this.setting = setting;

    }

    @Override
    public void run() {
        byte[] header = new byte[setting.getHeaderLength()];
        HeaderParser parser = new HeaderParser();

        while (!stopFlag) {
            try {
                byte[] data = in.take();
                System.arraycopy(data, 0, header, 0, 18);
                parser.parse(header);
                out.put(data);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void shutdown() {
        stopFlag = true;
    }

    public static ParsingHeaderThread getInstance(LinkedBlockingQueue<byte[]> in, LinkedBlockingQueue<byte[]> out, DataProcessingSetting setting) {
        if (mInstance == null) {
            mInstance = new ParsingHeaderThread(in, out, setting);
        }
        return mInstance;
    }
}
