package cc.liyongzhi.dataprocessingcenter;

import android.os.Environment;

/**
 * Created by lee on 8/19/16.
 */

public class DataProcessingSetting {

    private final int headerLength = 19;
    private final int bodyLength = 4000;
    private final String rootDir = Environment.getExternalStorageState() + "/";
    private final String ecgDataDir = rootDir + "ECG-DATA" + "/";

    private DataProcessingWarningManager manager;
    private String patientID;
    private String patientName;

    public DataProcessingSetting(DataProcessingWarningManager manager, String patientID, String patientName) {
        this.manager = manager;
        this.patientID = patientID;
        this.patientName = patientName;
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
}
