package cc.liyongzhi.dataprocessingcenter.thread.parsingbodythread;

import android.icu.text.RelativeDateTimeFormatter;

import java.util.concurrent.LinkedBlockingQueue;

import cc.liyongzhi.dataprocessingcenter.DataProcessingSetting;

/**
 * 将body的数据解析为十进制并存入十二导联的数组。
 * Created by lee on 8/22/16.
 */

public class ParsingBodyThread extends Thread implements Runnable {

    private static ParsingBodyThread instance;
    private LinkedBlockingQueue<byte[]> in;
    private LinkedBlockingQueue<short[]> out;
    private DataProcessingSetting setting;
    private boolean stopFlag = false;

    private ParsingBodyThread(LinkedBlockingQueue<byte[]> in, LinkedBlockingQueue<short[]> out, DataProcessingSetting setting) {
        this.in = in;
        this.out = out;
        this.setting = setting;
    }

    @Override
    public void run() {

        byte[] frameBuf;
        int headerLength = setting.getHeaderLength();
        int bodyLength = setting.getBodyLength();

        while (!stopFlag) {
            try {
                frameBuf = in.take();
                for (int k = 0; k < bodyLength; k += 16) {
                    short[] data = new short[12];
                    data[0] = (short) ((((short) frameBuf[headerLength + k]) & 0xff) * 256 + (((short) frameBuf[headerLength
                            + k + 1]) & 0xff));// I
                    data[1] = (short) ((((short) frameBuf[headerLength + k + 2]) & 0xff) * 256 + (((short) frameBuf[headerLength
                            + k + 3]) & 0xff));// II
                    data[6] = (short) ((((short) frameBuf[headerLength + k + 4]) & 0xff) * 256 + (((short) frameBuf[headerLength
                            + k + 5]) & 0xff));// V1
                    data[7] = (short) ((((short) frameBuf[headerLength + k + 6]) & 0xff) * 256 + (((short) frameBuf[headerLength
                            + k + 7]) & 0xff));// V2
                    data[8] = (short) ((((short) frameBuf[headerLength + k + 8]) & 0xff) * 256 + (((short) frameBuf[headerLength
                            + k + 9]) & 0xff));// V3
                    data[9] = (short) ((((short) frameBuf[headerLength + k + 10]) & 0xff) * 256 + (((short) frameBuf[headerLength
                            + k + 11]) & 0xff));// V4
                    data[10] = (short) ((((short) frameBuf[headerLength + k + 12]) & 0xff) * 256 + (((short) frameBuf[headerLength
                            + k + 13]) & 0xff));// V5
                    data[11] = (short) ((((short) frameBuf[headerLength + k + 14]) & 0xff) * 256 + (((short) frameBuf[headerLength
                            + k + 15]) & 0xff));// V6
                    data[2] = (short) (data[1] - data[0]);// III = II - I
                    data[3] = (short) (0 - (data[0] + data[1]) / 2);// aVR = - (I +
                    // II) / 2
                    data[4] = (short) ((data[0] - data[1]) / 2);// aVL = I - II / 2
                    data[5] = (short) ((data[1] - data[0]) / 2);// aVF = II - I / 2
                    out.put(data);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }


    public void shutdown() {
        stopFlag = true;
    }

    public static ParsingBodyThread getInstance(LinkedBlockingQueue<byte[]> in, LinkedBlockingQueue<short[]> out, DataProcessingSetting setting) {
        if (instance == null) {
            instance = new ParsingBodyThread(in, out, setting);
        }
        return instance;
    }
}
