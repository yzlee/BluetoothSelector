package cc.liyongzhi.bluetoothselectordemo;

import java.io.Serializable;
import java.util.ArrayList;

public class DataPacket implements Serializable {
	/**
	 * default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	//return code:0-sucess, other-fail
	private int retCode;
	
	private int id;

	
	// frame type
		private byte type;
		// protocol version
		private byte version;
		// patient id
		private int patient_id;
		// packet id
		private int packet_id;
		// timestamp
		private long time;
		// heart rate
		private int is_recorede;
		// lead config
		private byte config;
		// alert information
		private byte alert_ble = 0;
		private byte alert_st = 0;
		private byte alert_t = 0;
		private byte alert_overload = 0;
		private byte alert_disconnect = 0;

        private int power = 0;
		
	//data		
	private String data;
	private ArrayList<short[]> list = new ArrayList();
	public void init () {
		//return code:0-sucess, other-fail
		 retCode= 0;
		 id= 0;
		// frame type
		  type= 0;
		// protocol version
		  version= 0;
		// patient id
		 patient_id= 0;
		// packet id
		 packet_id= 0;
		// timestamp
		 long time= 0;
		// heart rate
		  is_recorede = 0;
		// lead config
		  config= 0;
		// alert information
		  alert_st = 0;
		  alert_t = 0;
		  alert_overload = 0;
		  alert_disconnect = 0;
		//data
			
		 data= "";
		 list.clear();

        //power
        power = 0;
	}
	public byte getAlertBle() {
		return alert_ble;
	}
	public void setAlertBle(byte ble) {
		alert_ble = ble;
	}
	public byte getAlerts() {
		byte alert = 0;
		alert = (byte) ((alert_st << 7) 
				| (alert_t << 6) 
				| (alert_overload << 5) 
				| (alert_disconnect << 4));
		return alert;
	}
	
	public void setRetCode(int ret) {
		retCode = ret;
	}
	
	public int getRetCode() {
		return retCode;
	}
	
	public  void push2DataList(short[] data) {
		list.add(data);
	}
	
	public ArrayList<short[]> getDataList() {
		return list;
	}
	public void setData(String mdata) {
		data = mdata;
	}
	
	public String getData() {
		return data;
	}

	/**
     * @return the type
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the type to set
     */
    public void setId(int id) {
        this.id = id;
    }
	/**
     * @return the type
     */
    public byte getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(byte type) {
        this.type = type;
    }

    /**
     * @return the version
     */
    public byte getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(byte version) {
        this.version = version;
    }

    /**
     * @return the patient_id
     */
    public int getPatient_id() {
        return patient_id;
    }

    /**
     * @param patient_id the patient_id to set
     */
    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    /**
     * @return the packet_id
     */
    public int getPacket_id() {
        return packet_id;
    }

    /**
     * @param packet_id the packet_id to set
     */
    public void setPacket_id(int packet_id) {
        this.packet_id = packet_id;
    }

    /**
     * @return the time
     */
    public long getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * @return the is_recorede
     */
    public int getIsRecorded() {
        return is_recorede;
    }

    /**
     * @param isRecorded the isRecorded to set
     */
    public void setIsRecorded(int isRecorded) {
        this.is_recorede = isRecorded;
    }

    /**
     * @return the config
     */
    public byte getConfig() {
        return config;
    }

    /**
     * @param config the config to set
     */
    public void setConfig(byte config) {
        this.config = config;
    }

    /**
     * @return the alert_st
     */
    public byte getAlert_st() {
        return alert_st;
    }

    /**
     * @param alert_st the alert_st to set
     */
    public void setAlert_st(byte alert_st) {
        this.alert_st = alert_st;
    }

    /**
     * @return the alert_t
     */
    public byte getAlert_t() {
        return alert_t;
    }

    /**
     * @param alert_t the alert_t to set
     */
    public void setAlert_t(byte alert_t) {
        this.alert_t = alert_t;
    }

    /**
     * @return the alert_overload
     */
    public byte getAlert_overload() {
        return alert_overload;
    }

    /**
     * @param alert_overload the alert_overload to set
     */
    public void setAlert_overload(byte alert_overload) {
        this.alert_overload = alert_overload;
    }

    /**
     * @return the alert_disconnect
     */
    public byte getAlert_disconnect() {
        return alert_disconnect;
    }

    /**
     * @param alert_disconnect the alert_disconnect to set
     */
    public void setAlert_disconnect(byte alert_disconnect) {
        this.alert_disconnect = alert_disconnect;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }
}
