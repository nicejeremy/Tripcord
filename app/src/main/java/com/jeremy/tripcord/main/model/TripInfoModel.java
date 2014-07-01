package com.jeremy.tripcord.main.model;

import android.content.Context;

import com.jeremy.tripcord.common.database.DatabaseManager;
import com.jeremy.tripcord.common.database.domain.PhotoInfo;
import com.jeremy.tripcord.common.database.domain.TripInfo;

import java.util.List;

/**
 * Created by asura1983 on 2014. 6. 28..
 */
public class TripInfoModel {

    public static List<TripInfo> loadTripInfoList(Context context) {

        DatabaseManager databaseManager = new DatabaseManager(context);
        databaseManager.open();

        List<TripInfo> tripInfos = databaseManager.selectTripInfos();

        for(TripInfo tripInfo : tripInfos) {
            List<PhotoInfo> photoInfos = databaseManager.selectTripPhotos(tripInfo.getTripSeq(), 5);
            tripInfo.setPhotoInfoList(photoInfos);
        }

        return tripInfos;
    }

}
