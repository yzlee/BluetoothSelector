package cc.liyongzhi.bluetoothselectordemo;

import android.os.Looper;

/**
 * Created by lee on 4/1/16.
 */
public abstract class BooleanCallBack {

    public abstract void  onCallBack(Boolean b);

    public void internalDone(final Boolean b) {

            if (Looper.myLooper() != Looper.getMainLooper()) {
                if (!Med.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onCallBack(b);
                    }
                })) {

                }
            } else {
                this.onCallBack(b);
            }

    }

}
