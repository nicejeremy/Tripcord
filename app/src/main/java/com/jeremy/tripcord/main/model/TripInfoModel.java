package com.jeremy.tripcord.main.model;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.jeremy.tripcord.common.contants.CommonContants;
import com.jeremy.tripcord.common.database.DatabaseManager;
import com.jeremy.tripcord.common.database.domain.PhotoInfo;
import com.jeremy.tripcord.common.database.domain.TripInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by asura1983 on 2014. 6. 28..
 */
public class TripInfoModel {

    public static void loadTripInfoList(Context context, Handler handler) {

        TripInfoListRunnable tripInfoListRunnable = new TripInfoListRunnable(context, handler);
        tripInfoListRunnable.run();
    }

    private static class TripInfoListRunnable implements Runnable {

        private Context context;
        private Handler handler;

        public TripInfoListRunnable(Context context, Handler handler) {
            this.context = context;
            this.handler = handler;
        }

        @Override
        public void run() {

            DatabaseManager databaseManager = new DatabaseManager(context);
            databaseManager.open();

            List<TripInfo> tripInfos = databaseManager.selectTripInfos();

            for(TripInfo tripInfo : tripInfos) {
                List<PhotoInfo> photoInfos = databaseManager.selectTripPhotos(tripInfo.getTripSeq(), 5);
                tripInfo.setPhotoInfoList(photoInfos);
            }

            Message message = new Message();
            message.what = CommonContants.WHAT_LOAD_TRIP_LIST;
            message.obj = tripInfos;
            handler.sendMessage(message);
        }
    }

}
