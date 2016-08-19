package cc.liyongzhi.dataprocessingcenter;

import java.util.concurrent.LinkedBlockingQueue;

/**数据处理警告接口
 * Created by lee on 8/19/16.
 */

public interface DataProcessingWarningManager {

    void mangeOriginDataQueueWarning(LinkedBlockingQueue<Byte> originData, Exception e);
    void mangeCuttingThreadQueueWarning(LinkedBlockingQueue<Byte> originData, LinkedBlockingQueue<byte[]> cutDataQueue, Exception e);

}
