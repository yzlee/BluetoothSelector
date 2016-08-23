package cc.liyongzhi.dataprocessingcenter;

import cc.liyongzhi.dataprocessingcenter.interf.DataDrawer;

/** 总接口
 * Created by lee on 8/22/16.
 */

public interface DataProcessingInterface {

    /**
     * 向加工中心放入原始数据
     * @param b
     */
    void putOriginData(byte b);

    /**
     * 开始保存（需要在配置文件{@link DataProcessingSetting}中设置好用户名）
     */
    void startSaving();


    /**
     * 设置画图控件
     * @param drawer 画图控件
     */
    void setDataDrawer(DataDrawer drawer);

    /**
     * 关单后调用，结束所有线程，重置所有参数。
     */
    void reset();

}
