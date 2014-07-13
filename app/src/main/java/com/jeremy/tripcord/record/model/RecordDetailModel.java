package com.jeremy.tripcord.record.model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.jeremy.tripcord.common.contants.CommonContants;
import com.jeremy.tripcord.common.database.DatabaseManager;
import com.jeremy.tripcord.common.database.domain.TripInfo;

/**
 * Created by asura1983 on 2014. 7. 13..
 */
public class RecordDetailModel {

    public static void loadTripInfo(Context context, Handler handler, int tripSeq, int limit) {

        RecordDetailModelRunnable recordDetailModelRunnable = new RecordDetailModelRunnable(context, handler, tripSeq, limit);
        recordDetailModelRunnable.run();
    }

    private static class RecordDetailModelRunnable implements Runnable {

        private Context context;
        private Handler handler;
        private int tripSeq;
        private int limit;

        public RecordDetailModelRunnable(Context context, Handler handler, int tripSeq, int limit) {
            this.context = context;
            this.handler = handler;
            this.tripSeq = tripSeq;
            this.limit = limit;
        }

        @Override
        public void run() {

            DatabaseManager databaseManager = new DatabaseManager(context);
            databaseManager.open();

            TripInfo tripInfo = databaseManager.selectTripInfo(tripSeq);
            tripInfo.setPhotoInfoList(databaseManager.selectTripPhotos(tripSeq, limit));
            tripInfo.setLocationInfoList(databaseManager.selectTripLocations(tripSeq));

            Message message = new Message();
            message.what = CommonContants.WHAT_LOAD_TRIP_DETAIL;
            message.obj = tripInfo;
            handler.sendMessage(message);
        }
    }

}
