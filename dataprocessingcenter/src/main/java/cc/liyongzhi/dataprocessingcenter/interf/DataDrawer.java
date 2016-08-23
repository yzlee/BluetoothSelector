package cc.liyongzhi.dataprocessingcenter.interf;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by lee on 8/20/16.
 */

public interface DataDrawer {

    void setInputQueue(LinkedBlockingQueue<short[]> queue);

}
