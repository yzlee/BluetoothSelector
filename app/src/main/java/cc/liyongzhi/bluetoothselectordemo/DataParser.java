package cc.liyongzhi.bluetoothselectordemo;

import android.util.Log;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataParser {

    private static DataParser parser = null;
    FileOutputStream output = null;
    private static int HEAD_LEN = 19;
    public enum HEADER_STATE
    {
        FAIL, FOUND;
    }
    private static HEADER_STATE headerState = HEADER_STATE.FAIL; // 包头捕获状态
    private static HEADER_STATE tailState = HEADER_STATE.FAIL;   // 包捕获状态
    private int index = 0;


    private final int BUFFER_SIZE = 4096;
    private byte[] headBuf = new byte[4];               // 头部缓存区
    private byte[] frameBuf = new byte[BUFFER_SIZE];    // 数据缓存区
    private int confiLength = 0;   //包长度



    /**
     *  捕获初始化  初始化包头解析状态，缓存区，index
     */
    private void init(HEADER_STATE tail, HEADER_STATE header, int index)
    {
        tailState = tail;
        confiLength=0;
        headerState = header;
        this.index = index;
        headBuf = new byte[4];
        frameBuf = new byte[BUFFER_SIZE];
    }

    public DataParser() {
        /*
        file = new File("/DCIM/ecgdata.log");
        try {
            output = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        */
    }



    int headcounter =0;
    private void parseHead(byte in)
    {
        tailState = HEADER_STATE.FAIL;

        frameBuf[0] = frameBuf[1];
        frameBuf[1] = frameBuf[2];
        frameBuf[2] = frameBuf[3];
        frameBuf[3] = in;


        if (frameBuf[0] == 127 && frameBuf[1] == -128 && frameBuf[2] == 127
                && frameBuf[3] == -128)
        {
            headerState = HEADER_STATE.FOUND;
            headcounter++;
            Log.i("counter", "headcounter = "+ Integer.toString(headcounter));
        }

    }

    public DataPacket parsePacket(byte in)
    {
        byte[] inByte = new byte[1];
        inByte[0] = in;
        if (headerState == HEADER_STATE.FAIL)
        {
            //find the header
            parseHead(in);
            index = 3;
        }
        else
        {
            //parse the packet;
            return  parseCompletePacket(inByte);
        }
        return null;

    }
    private DataPacket parseCompletePacket(byte[] inbyte)
    {
        index = index + 1;
        System.arraycopy(inbyte, 0, frameBuf, index, 1);
        if (index == 5)
        {            //0xff无符号
            confiLength =  ((((int)frameBuf[index-1  ])&0xff )*256 + (((int)frameBuf[index  ])&0xff )) - 13;
        }
        //检查是否出现header
        headBuf[0] = headBuf[1];
        headBuf[1] = headBuf[2];
        headBuf[2] = headBuf[3];
        headBuf[3] = inbyte[0];



        if (headBuf[0] == 127 && headBuf[1] == -128 && headBuf[2] == 127
                && headBuf[3] == -128) {
            headerState = HEADER_STATE.FOUND;
            headcounter++;
            if (index == 4000 + 18 + 4) {
                putPacket();
            } else {
                makeAFakePacketAndPut();
            }
            init(HEADER_STATE.FAIL, HEADER_STATE.FOUND, 3);
            initHeader();
            return null;
        }
        if (index > 4080) {
            makeAFakePacketAndPut();
            init(HEADER_STATE.FAIL, HEADER_STATE.FAIL, 0);
            return null;
        }
        //检查是否出现header结束
        return null;
    }
    private void putPacket() {
        byte[] inQueue = new byte[4000 + HEAD_LEN];
        System.arraycopy(frameBuf, 0, inQueue, 0, confiLength + HEAD_LEN);
        GlobalData.putPacket(inQueue);

    }
    private void makeAFakePacketAndPut() {

        byte[] inQueue = new byte[4000 + HEAD_LEN];
        for (int i = 19; i < 4000 + HEAD_LEN; i++) {
            frameBuf[i] = 126;
        }
        System.arraycopy(frameBuf, 0, inQueue, 0, 4000 + HEAD_LEN);
        GlobalData.putPacket(inQueue);
    }
    private void initHeader() {
        frameBuf[0] = 127;
        frameBuf[1] = -128;
        frameBuf[2] = 127;
        frameBuf[3] = -128;
    }
    public static DataParser getInstance() {
        if (parser == null)
            parser = new DataParser();
        return parser;
    }

    public HEADER_STATE getTailState(){
        return tailState;
    }


}