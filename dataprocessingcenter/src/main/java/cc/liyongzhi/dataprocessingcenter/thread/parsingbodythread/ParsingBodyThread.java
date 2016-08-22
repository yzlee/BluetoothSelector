package cc.liyongzhi.dataprocessingcenter.thread.parsingbodythread;

import android.icu.text.RelativeDateTimeFormatter;

import java.util.concurrent.LinkedBlockingQueue;

import cc.liyongzhi.dataprocessingcenter.DataProcessingSetting;

/**
 * Created by lee on 8/22/16.
 */

public class ParsingBodyThread extends Thread implements Runnable {

    private LinkedBlockingQueue<byte[]> in;
    private LinkedBlockingQueue<byte[]> out;
    private DataProcessingSetting setting;

    private ParsingBodyThread(LinkedBlockingQueue<byte[]> in, LinkedBlockingQueue<byte[]> out, DataProcessingSetting setting) {
        this.in = in;
        this.out = out;
        this.setting = setting;
    }

    @Override
    public void run() {

    }
}
