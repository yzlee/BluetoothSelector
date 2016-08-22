package cc.liyongzhi.dataprocessingcenter;

import android.content.Context;
import android.os.Environment;

import cc.liyongzhi.dataprocessingcenter.interf.DataProcessingWarningManager;

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

    /**
     * 未开单但是需要画图时，调用本构造器。然后调用{@link #setPatientID(String)}与{@link #setPatientName(String)}后开始绘图。
     * @param context
     * @param manager
     */
    public DataProcessingSetting(Context context, DataProcessingWarningManager manager) {
        this.manager = manager;
        this.context = context;
    }

    public DataProcessingSetting(Context context, DataProcessingWarningManager manager, String patientID, String patientName) {
        this.context = context;
        this.manager = manager;
        this.patientID = patientID;
        this.patientName = patientName;
    }

    public DataProcessingWarningManager getManager() {
        return manager;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
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
