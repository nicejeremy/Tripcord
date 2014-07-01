package com.jeremy.tripcord.record.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;


/**
 * Created by asura1983 on 2014. 5. 26..
 */
public class TimerThread extends Thread {

    private static int WHAT_TIME = 0;

    private Handler handler;

    public TimerThread(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();
        timeProcess();
    }

    private void timeProcess() {

        int i = 0;

        while (true) {

            try {
                Message message = new Message();
                message.what = WHAT_TIME;
                message.obj = i++;
                this.handler.sendMessage(message);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
