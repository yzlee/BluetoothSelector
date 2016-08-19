package cc.liyongzhi.dataprocessingcenter.thread;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
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
    private BufferedOutputStream mStream;

    private boolean mRunFlag = true;
    private boolean mSavingFlag = false;
    private boolean mSavingHeader = false;

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

        File file = new File(mDir + mFileName);
        if (!file.exists()) {
            try {
                boolean result = file.createNewFile();
            } catch (IOException e) {
                mSetting.getManager().manageSavingDataThreadFileCreateFailedWarning(e);
            }
        }

        MediaScannerConnection.scanFile(mSetting.getContext().getApplicationContext(), new String[] { file.getAbsolutePath() }, new String[] { "application/octet-stream" }, new MediaScannerConnection.OnScanCompletedListener()
        {
            @Override
            public void onScanCompleted(final String path, final Uri uri)
            {
                // Eureka, your file has been scanned!
            }
        });

        try {
            FileOutputStream fos = new FileOutputStream(file, true);
            mStream = new BufferedOutputStream(fos);
            while (mRunFlag) {
                try {
                    byte[] data = mCutDataQueueForSaving.take();
                    //检测是否需要保存，若不需要则略过
                    if (!mSavingFlag) {
                        continue;
                    }

                    for (int i = 0; i < data.length; i++) {

                        if (!mSavingHeader && i < mSetting.getHeaderLength()) {
                            continue;
                        }

                        mStream.write(data[i]);
                    }
                    mStream.flush();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (mStream != null) {
                try {
                    mStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 开始存数数据，默认不保留头文件
     */
    public void startSaving() {
        startSaving(false);
    }

    /**
     * 开始存数数据
     * @param b true为保留头文件，false为不保留
     */
    public void startSaving(boolean b) {
        mSavingFlag = true;
        mSavingHeader = b;
    }

    public void pauseSaving() {
        mSavingFlag = false;
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
