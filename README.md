# BluetoothSelector
# 蓝牙选择器
---

## 功能

- 通过回调函数执行蓝牙连接成功后和断开后的动作
- 支持多蓝牙设备同时连接
- 选择界面可显示已配对设备，也可以搜索设备
- 可通过输入mac地址或者BluetoothDevice直接连接蓝牙
- 可选择是否出现等待界面（方便后台连接）
- 重连不重复开线程。
- 连接失败后返回Exception e
- 蓝牙选择界面、等待界面自定义（正在完善）


## 截图
| ![](./pictures/S60531-144800.jpg) | ![](./pictures/S60531-144803.jpg) | ![](./pictures/S60531-144806.jpg) |
| -------- | --------- | --------|
| ![](./pictures/S60531-144824.jpg) | ![](./pictures/S60531-144847.jpg) | ![](./pictures/S60531-144859.jpg) |
-----------------

## 引用

#### Gradle:

> compile 'cc.liyongzhi.bluetoothselector:bluetoothselector:1.7'


## 使用

#### 引用：

``` java
/**
 * @param context 上下文
 * @param bluetoothConnectCallback 连接建立和取消连接后调用的回调函数
 */
MedBluetooth.connectBluetooth(Context context, BluetoothConnectCallback bluetoothConnectCallback);

```
或者

``` java
/**
 * @param context 上下文
 * @param mac 如果以前有保存蓝牙mac地址，则可以直接输入
 * @param showConnectBluetoothActivity 是否显示等待界面，若后台有自动重连请设置为false，不然每次连接都转圈圈。。。
 * @param bluetoothConnectCallback 连接建立和取消连接后调用的回调函数
 */
MedBluetooth.connectBluetooth(Context context, String mac, Boolean showConnectBluetoothActivity, BluetoothConnectCallback bluetoothConnectCallback);

```

`BluetoothConnectCallback`有两个方法：
```java
/**
 * 连接成功或失败后调用
 * @param socket 获得的socket
 * @param device 本次连接的设备，可存下来方便下次自动重连，就不用每次都选择了。
 * @param e 错误
 */
public abstract void connected(BluetoothSocket socket, BluetoothDevice device, Exception e);

/**
 * 连接断开后调用，原理为监听系统广播
 */
public abstract void disconnected();
```

#### 示例：
在需要连接蓝牙或者选择设备的地方加入代码，本例为位于OnClickListener里。

- 输入为`Context`、`BluetoothConnectCallback`时：
```java
mButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        //以下为调用本库，输入为Context、BluetoothConnectCallback
        MedBluetooth.connectBluetooth(mContext, new BluetoothConnectCallback() {
            @Override
            // 连接成功或失败后调用。
            public void connected(BluetoothSocket socket, BluetoothDevice device, Exception e) {
                if (e != null) {
                    //连接失败
                } else {
                    //输出为获得的socket，可以自行存到全局变量里进行数据输入输出操作。
                    //device为本次连接的设备，可调用 device.getAddress() 获得mac地址。
                    //e 为错误。
                }
            }
            @Override
            // 连接断开后调用
            public void disconnected() {
                // 原理为通过捕获系统的广播而调用的。
            }
        });
        //调用结束
    }
});
```

- 输入还可以为`Context`、`String（mac地址）`、`Boolean（是否显示等待界面）`、`BluetoothConnectCallback`，可以用于重连，代码几乎同上。

## 更新历史

#### 2016.5.31 version 1.7

可用版本。


## Bug

目前公司几款app均未发现bug。但有一处判断线程是否正在运行时使用了   
```java
if (!thread.isAlive() && thread.getState() != State.RUNNABLE) {
            thread.start();
}
```
不知道对不对。。。

## 作者
微博：这是一条特立独行的猪 http://weibo.com/1881962417/ 欢迎关注！
