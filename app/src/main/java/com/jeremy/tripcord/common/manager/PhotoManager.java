package com.jeremy.tripcord.common.manager;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.jeremy.tripcord.common.database.DatabaseManager;
import com.jeremy.tripcord.common.manager.exceptions.NoRecordingException;
import com.jeremy.tripcord.common.manager.listener.PhotoTakenListener;
import com.jeremy.tripcord.common.utils.GPSUtil;

/**
 * Created by asura1983 on 2014. 5. 27..
 */
public class PhotoManager {

    private static PhotoManager instance = null;
    private PhotoTakenListener photoTakenListener;

    private PhotoManager() {

    }

    public static PhotoManager getInstance() {

        if (instance == null) {
            instance = new PhotoManager();
        }

        return instance;
    }

    public PhotoTakenListener getPhotoTakenListener() {
        return photoTakenListener;
    }

    public void setPhotoTakenListener(PhotoTakenListener photoTakenListener) {
        this.photoTakenListener = photoTakenListener;
    }

    public int insertPhotoInfo(Context context, String imagePath) {


        Location location = GPSUtil.exif2Loc(imagePath);
        int tripSeq = DistanceManager.getInstance().getTripSeq();

        if (location != null) {
            Log.d("Tripcord", "PhotoManager >> insertPhotoInfo :: GPS Latitude [" + location.getLatitude() + "], Longitude [" + location.getLongitude() + "]");
        } else {
            try {
                location = DistanceManager.getInstance().getLastLocation();
            } catch (NoRecordingException e) {
                Log.d("Tripcord", e.toString());
            }
        }

        DatabaseManager databaseManager = new DatabaseManager(context);
        databaseManager.open();
        int generatedPhotoSeq = databaseManager.insertPhoto(tripSeq, imagePath, location);

        Log.d("Tripcord", "PhotoManager >> insertPhotoInfo :: Photo has been inserted :: tripSeq [" + tripSeq + "], photoSeq [" + generatedPhotoSeq + "]");

        return generatedPhotoSeq;
    }

    public void notify(String imagePath) {
        if (getPhotoTakenListener() != null) {
            getPhotoTakenListener().photoIsTaken(imagePath);
        }
    }

}
