package com.nk.framework.message;


import de.greenrobot.event.EventBus;

/**
 * Created by dax on 2016/7/26.
 */
public class HhMessage {
    private EventBus mBus = null;
    private static HhMessage mInstance;

    public static HhMessage getInstance() {
        if (mInstance == null) {
            synchronized (HhMessage.class) {
                if (mInstance == null) {
                    mInstance = new HhMessage();
                }
            }
        }
        return mInstance;
    }

    private HhMessage() {
        this.mBus = EventBus.getDefault();
    }

    public void registListener(Object listener) {
        if ((listener != null) && (!isRegistered(listener)))
            this.mBus.register(listener);
    }

    public void unRegistListener(Object listener) {
        if (listener != null)
            this.mBus.unregister(listener);
    }

    public void sendMessage(IMessage message) {
        if (message != null)
            this.mBus.post(message);
    }

    public boolean isRegistered(Object listener) {
        return this.mBus.isRegistered(listener);
    }

}
