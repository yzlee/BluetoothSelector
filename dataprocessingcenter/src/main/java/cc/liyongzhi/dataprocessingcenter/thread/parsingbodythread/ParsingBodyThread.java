package cc.liyongzhi.dataprocessingcenter.thread.parsingbodythread;

import android.icu.text.RelativeDateTimeFormatter;

import java.util.concurrent.LinkedBlockingQueue;

import cc.liyongzhi.dataprocessingcenter.DataProcessingSetting;

/** 将body的数据解析为十进制并存入十二导联的数组。
 * Created by lee on 8/22/16.
 */

public class ParsingBodyThread extends Thread implements Runnable {

    private static ParsingBodyThread instance;
    private LinkedBlockingQueue<byte[]> in;
    private LinkedBlockingQueue<int[]> out;
    private DataProcessingSetting setting;

    private ParsingBodyThread(LinkedBlockingQueue<byte[]> in, LinkedBlockingQueue<int[]> out, DataProcessingSetting setting) {
        this.in = in;
        this.out = out;
        this.setting = setting;
    }

    @Override
    public void run() {



    }

    public static ParsingBodyThread getInstance(LinkedBlockingQueue<byte[]> in, LinkedBlockingQueue<int[]> out, DataProcessingSetting setting) {
        if (instance == null) {
            instance = new ParsingBodyThread(in, out, setting);
        }
        return instance;
    }
}
