package cc.liyongzhi.dataprocessingcenter;

import android.content.Context;
import android.os.ConditionVariable;
import android.os.Environment;

/**
 * Created by lee on 8/19/16.
 */

public class DataProcessingSetting {

    private final int headerLength = 19;
    private final int bodyLength = 4000;
    private final int maxDataFileNum = 3; //最大保存心电文件数据数量
    private final boolean savingHeader = false; // 是否保存header
    private final String fileExtension = ".dat"; //文件名扩展
    private final String rootDir = Environment.getExternalStorageDirectory() + "/"; //根目录
    private final String ecgDataDir = rootDir + "ECG-DATA" + "/"; //心电数据保存目录

    private DataProcessingWarningManager manager;
    private String patientID;
    private String patientName;
    private Context context;

    public DataProcessingSetting(Context context, DataProcessingWarningManager manager, String patientID, String patientName) {
        this.manager = manager;
        this.patientID = patientID;
        this.patientName = patientName;
        this.context = context;
    }

    public DataProcessingWarningManager getManager() {
        return manager;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getPatientName() {
        return patientName;
    }


    public int getHeaderLength() {
        return headerLength;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public String getRootDir() {
        return rootDir;
    }

    public String getEcgDataDir() {
        return ecgDataDir;
    }

    public Context getContext() {
        return context;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public int getMaxDataFileNum() {
        return maxDataFileNum;
    }

    public boolean isSavingHeader() {
        return savingHeader;
    }
}
