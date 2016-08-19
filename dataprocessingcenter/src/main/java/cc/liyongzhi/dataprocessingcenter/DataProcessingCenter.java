package cc.liyongzhi.dataprocessingcenter;

import java.util.concurrent.LinkedBlockingQueue;

/** 数据处理中心
 * 读取数据后，分为：
 * 1、 12个导联的byte数组
 * 2、 本地保存
 * 线程启动顺序分为：
 * 1、 切割线程： 将每秒数据包分离。每个header和body为一个数据包。
 * Created by lee on 8/18/16.
 */

public class DataProcessingCenter {

    public static LinkedBlockingQueue<Byte> originDataQueue = new LinkedBlockingQueue<Byte>();

    public static void init(int headerLength, int bodyLength) {

    }

    /**
     * 原始数据入口
     */
    public synchronized static void putOriginData(byte b) {
        try {
            originDataQueue.put(b);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void putCutData(byte[] bytes) {

    }

}
