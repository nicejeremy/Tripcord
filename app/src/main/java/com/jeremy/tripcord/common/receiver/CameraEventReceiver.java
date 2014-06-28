package com.jeremy.tripcord.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.util.Log;

import com.jeremy.tripcord.common.manager.DistanceManager;
import com.jeremy.tripcord.common.manager.PhotoManager;
import com.jeremy.tripcord.common.utils.GPSUtil;
import com.jeremy.tripcord.common.manager.exceptions.NoRecordingException;
import com.jeremy.tripcord.common.database.DatabaseManager;

/**
 * Created by asura1983 on 2014. 5. 19..
 */
public class CameraEventReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (DistanceManager.getInstance().getTripSeq() != DistanceManager.INVALID_TRIP_SEQ) {

            Cursor cursor = context.getContentResolver().query(intent.getData(), null, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex("_data"));

            // Ex) New Photo is Saved as : -/storage/extSdCard/DCIM/Camera/20140524_180535.jpg
            Log.d("Tripcord", "CameraEventReceiver >> New Photo is Saved as : -" + imagePath);

            // 1 사진 헤더의 GPS 값 추출
            // 1-1 사진 헤더에 GPS 값이 있으면
            // 1-1-1 Photo Database 에 데이터 입력
            // 1-1-2 RecordActivity 에 알림
            // 1-2 사진 헤더에 GPS 값이 없으면
            // 1-2-1 DistanceManager 에서 마지막 GPS 추출
            // 1-2-2 추출한 값과 함께 Photo Database 에 데이터 입력
            // 1-2-3 RecordActivity 에 알림

            PhotoManager.insertPhotoInfo(context, imagePath);

        } else {

            // Ignore
            Log.d("Tripcord", "CameraEventReceiver >> Tripcord recording is not running");
        }

    }
}
