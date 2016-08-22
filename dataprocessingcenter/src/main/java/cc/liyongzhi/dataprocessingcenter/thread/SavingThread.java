package cc.liyongzhi.dataprocessingcenter.thread;

import android.media.MediaScannerConnection;
import android.net.Uri;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import cc.liyongzhi.dataprocessingcenter.DataProcessingSetting;


/**
 * 将心电信息保存到本地的类。
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
    private final boolean mSavingHeader;

    private SavingThread(LinkedBlockingQueue<byte[]> cutDataQueueForSaving, DataProcessingSetting setting) {
        //赋值
        mCutDataQueueForSaving = cutDataQueueForSaving;
        mSetting = setting;

        //初始化
        mSavingHeader = mSetting.isSavingHeader();
        mDir = mSetting.getEcgDataDir();
        mFileName = generateFileName();
        mRunFlag = true;
    }

    private String generateFileName() {
        return mSetting.getPatientName() + mSetting.getFileExtension();
    }

    public void run() {

        File dir = new File(mDir);
        if (!dir.exists()) {
            boolean result = dir.mkdirs();
        } else {
            FilenameFilter filenameFilter = new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    return s.endsWith(mSetting.getFileExtension());
                }
            };
            int number = dir.listFiles(filenameFilter).length;
            if (number > mSetting.getMaxDataFileNum()) {
                File[] files = dir.listFiles();
                File firstFile = files[0];
                for (int i = 1; i < number; i++) {
                    if (files[i].lastModified() < firstFile.lastModified()) {
                        firstFile = files[i];
                    }
                }
                if (firstFile.exists()) {
                    if (firstFile.isFile()) {
                        boolean result = firstFile.delete();
                    }
                }
            }

        }

        File file = new File(mDir + mFileName);
        if (!file.exists()) {
            try {
                boolean result = file.createNewFile();
            } catch (IOException e) {
                mSetting.getManager().manageSavingDataThreadFileCreateFailedWarning(e);
            }
        }

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
                    MediaScannerConnection.scanFile(mSetting.getContext().getApplicationContext(), new String[]{file.getAbsolutePath()}, new String[]{"application/octet-stream"}, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(final String path, final Uri uri) {
                            // Eureka, your file has been scanned!
                        }
                    });
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
        mSavingFlag = true;
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
