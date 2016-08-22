package cc.liyongzhi.dataprocessingcenter.thread.parsingheaderthread;

/**
 * Created by lee on 8/22/16.
 */

public class HeaderParser {

    private byte[] header;

    public void parse(byte[] header) {
        this.header = header;
        parseBattery();
        parseHeartBeat();
        parseCableState();
    }

    private void parseBattery() {

    }

    private void parseHeartBeat() {

    }

    private void parseCableState() {

    }

}
