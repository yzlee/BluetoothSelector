package cc.liyongzhi.bluetoothselectordemo;

public class BLECommand {
	public static String ECG = "ECG,";//设备向手机发送设备类型(ECG)及ID 号
	//public static String OK = "ECG:OK#";//
	public static String ERROR = "ERROR#";//
	public static String SETID = "ECG:SETID$";//
	public static String GETTIME = "ECG:GETTIME#";//获取时间
	public static String SETTIME = "ECG:SETTIME";//设置时间
	public static String GETBATTERYPOWER = "ECG:GETBATTERYPOWER#";//
	public static String GETFILELIST = "ECG:GETFILELIST#";//
	public static String GETFILE = "ECG:GETFILE,";//
	public static String SETMODE_NORMAL = "ECG:SETMODE$NORMAL MODE#";//
	public static String SETMODE_ALAERM = "ECG:SETMODE$ALARM MODE#";//
	public static String GETMODE = "ECG:GETMODE#";//
	public static String START = "ECG:START#";//开始心电传输
	public static String STOP = "ECG:STOP#";//停止心电传输
	
	public static String ECG_POSTFIX = "#";
	public static String ECG_SETWORKHOUR ="ECG:SETWORKHOUR#";//设置工作时长
	public static String ECG_SETDATALENGTH ="ECG:SETDATALENGTH#";//设置数据长度
	public static String ECG_SETALERTHOUR ="ECG:SETALERTHOUR$alerthour#";//设置报警时长
	public static String ECG_SETMAXRATE ="ECG:SETMAXRATE#";//设置过速上限
	public static String ECG_SETMINRATE ="ECG:SETMINRATE#";//设置过速下限
	public static String ECG_SETEXMINRATE ="ECG:SETEXMINRATE#$exminrate#";//设置过缓极低值
	//心率失常报警门限参数设置:1 单个早搏报警每分钟大于多少个早搏;2 联律报警 是否报警;3 停搏报警 停博大于几秒进行报警;4 ST 报警 ST 抬高多少进行报警;5 ST 报警 ST 压低多少进行报警;6 T 波报警 T 波改变是否进行报警 
	public static String ECG_SETALARMTHRESHOLD ="ECG:SETARRHYTHMIA#";
	public static String ECG_SETARRHYTHMIA ="ECG:SETARRHYTHMIA#";//设置分析导联
	public static String ECG_ISPACEMAKER ="ECG:ISPACEMAKER#";//起搏器监护

	//心率失常设置参数$MODE具体如下：
	//$MODE	描述
	public static final String THRESHOLD_EARLYBEAT = "$EARLYBEAT";	//每分钟大于多少个早搏进行报警
	public static final String THRESHOLD_COMBEATRHY = "$COMBEATRHY";	//联律是否报警
	public static final String THRESHOLD_STOPBEAT = "$STOPBEAT";	//停博大于几秒报警
	public static final String THRESHOLD_STHIGH = "$STHIGH";	    //ST抬高多少进行报警
	public static final String THRESHOLD_STLOW = "$STLOW";	    //ST压低多少进行报警
	public static final String THRESHOLD_TWAVE = "$TWAVE";	    //T波改变是否进行报警

}
