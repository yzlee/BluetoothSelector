package cc.liyongzhi.dataprocessingcenter.thread;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.LinkedBlockingQueue;

import cc.liyongzhi.dataprocessingcenter.DataProcessingSetting;

/**将心电信息保存到本地的类。
 * Created by lee on 8/19/16.
 */

public class SavingThread extends Thread implements Runnable {

    private static SavingThread mInstance;
    private LinkedBlockingQueue<byte[]> mCutDataQueueForSaving;
    private String mDir;
    private String mFileName;
    private DataProcessingSetting mSetting;

    private boolean mRunFlag = true;


    private SavingThread(LinkedBlockingQueue<byte[]> cutDataQueueForSaving, DataProcessingSetting setting) {
        //赋值
        mCutDataQueueForSaving = cutDataQueueForSaving;
        mSetting = setting;

        //初始化
        mDir = mSetting.getEcgDataDir();
        mFileName = generateFileName();
        mRunFlag = true;


    }

    private String generateFileName() {
        return mSetting.getPatientName();
    }

    public void run() {

        File dir = new File(mDir);
        if (!dir.exists()) {
            boolean result = dir.mkdirs();
        }

        File file = new File(mFileName);
        if (!file.exists()) {
            try {
                boolean result = file.createNewFile();
            } catch (IOException e) {
                mSetting.getManager().manageSavingDataThreadFileCreateFailedWarning(e);
            }
        }

        try {
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
            while (mRunFlag) {
                try {
                    byte[] data = mCutDataQueueForSaving.take();
                    for (int i = 0; i < data.length; i++) {
                        if (i < mSetting.getHeaderLength()) {
                            continue;
                        }
                        writer.write(data[i]);
                    }
                    writer.flush();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void shutdown() {
        mRunFlag = false;
    }


    public static SavingThread getInstance(LinkedBlockingQueue<byte[]> cutDataQueueForSaving, DataProcessingSetting setting) {
        if (mInstance == null) {
            mInstance = new SavingThread(cutDataQueueForSaving, setting);
        }
        return mInstance;
    }

}
