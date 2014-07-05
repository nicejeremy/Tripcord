package com.jeremy.tripcord.record.countdown;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremy.tripcord.app.R;

/**
 * TODO: document your custom view class.
 */
public class CountDownView extends RelativeLayout {

    private TextView textViewCountDown;
    private int time;
    private CountDownObserver countDownObserver;

    public CountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView();
    }

    private void initView() {

        inflate(getContext(), R.layout.view_count_down, this);
        textViewCountDown = (TextView) findViewById(R.id.textView_count_down);
        textViewCountDown.setText(getContext().getString(R.string.ready));
    }

    public void start(int second, CountDownObserver countDownObserver) throws InterruptedException {

        this.time = second;
        this.countDownObserver = countDownObserver;

        threadCountDown.start();
    }

    Thread threadCountDown = new Thread() {

        @Override
        public void run() {
            super.run();
            timeProcess();
        }
    };

    private void timeProcess() {

        while (time != -1) {

            try {
                Message message = new Message();
                message.what = 0;
                message.obj = time;
                handler.sendMessage(message);
                Thread.sleep(1000);
                time--;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Message messageFinish = new Message();
        messageFinish.what = 1;
        handler.sendMessage(messageFinish);
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 0 :
                    int second = (Integer) msg.obj;

                    if (second == 0) {
                        textViewCountDown.setText(getContext().getString(R.string.enjoy_your_journey));
                    } else {
                        textViewCountDown.setText(String.valueOf(second));
                    }
                    break;
                case 1 :
                    countDownObserver.onFinishedCountDown();

                    break;
                default:
                    break;
            }
        }
    };


}
