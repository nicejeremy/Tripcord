package com.jeremy.tripcord.common.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.provider.ContactsContract;

import com.google.android.gms.maps.model.LatLng;
import com.jeremy.tripcord.common.database.domain.LocationInfo;
import com.jeremy.tripcord.common.database.domain.PhotoInfo;
import com.jeremy.tripcord.common.database.domain.TripInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by asura1983 on 2014. 4. 21..
 */
public class DatabaseManager {

    private final static String DATABASE_NAME = "database.db";
    private final static int DATABASE_VERSION = 3;

    DatabaseHelper databaseHelper;
    SQLiteDatabase sqLiteDatabase;

    public DatabaseManager(Context context) {

        databaseHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void open() {

        sqLiteDatabase = databaseHelper.getWritableDatabase();
    }

    public void close() {

        databaseHelper.close();
    }

    private long getCurrentTime() {

        Date date = new Date();

        return date.getTime();
    }

    public int insertTripInfo() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_TRIP_INFOS_ONGOING, true);
        contentValues.put(DatabaseHelper.COLUMN_TRIP_INFOS_CREATED, getCurrentTime());

        long generatedTripSeq = sqLiteDatabase.insert(DatabaseHelper.TABLE_NAME_TRIP_INFOS, null, contentValues);

        return (int) generatedTripSeq;
    }

    public int updateTripSnapShot(int tripSeq, byte[] snapshot) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_TRIP_INFOS_SNAPSHOT, snapshot);
        String where = DatabaseHelper.COLUMN_TRIP_INFOS_TRIP_SEQ + " = " + tripSeq;

        long result = sqLiteDatabase.update(DatabaseHelper.TABLE_NAME_TRIP_INFOS, contentValues, where, null);

        return (int) result;
    }

    public int insertLocation(int tripSeq, LatLng latLng) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_LOCATIONS_TRIP_SEQ, tripSeq);
        contentValues.put(DatabaseHelper.COLUMN_LOCATIONS_LATITUDE, latLng.latitude);
        contentValues.put(DatabaseHelper.COLUMN_LOCATIONS_LONGITUDE, latLng.longitude);

        long generatedLocationSeq = sqLiteDatabase.insert(DatabaseHelper.TABLE_NAME_LOCATIONS, null, contentValues);

        return (int) generatedLocationSeq;
    }

    public int insertPhoto(int tripSeq, String filePath, Location location) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_PHOTOS_TRIP_SEQ, tripSeq);
        contentValues.put(DatabaseHelper.COLUMN_PHOTOS_PATH, filePath);
        contentValues.put(DatabaseHelper.COLUMN_PHOTOS_LATITUDE, location.getLatitude());
        contentValues.put(DatabaseHelper.COLUMN_PHOTOS_LONGITUDE, location.getLongitude());
        contentValues.put(DatabaseHelper.COLUMN_PHOTOS_CREATED, getCurrentTime());

        long generatedPhotoSeq = sqLiteDatabase.insert(DatabaseHelper.TABLE_NAME_PHOTOS, null, contentValues);

        return (int) generatedPhotoSeq;
    }

    public int updateTripInfoAsFinish(int tripSeq, double distance, int duringTime, String arrival, String destination) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_TRIP_INFOS_ONGOING, false);
        contentValues.put(DatabaseHelper.COLUMN_TRIP_INFOS_DISTANCE, distance);
        contentValues.put(DatabaseHelper.COLUMN_TRIP_INFOS_DURING_TIME, duringTime);
        contentValues.put(DatabaseHelper.COLUMN_TRIP_INFOS_FROM, arrival);
        contentValues.put(DatabaseHelper.COLUMN_TRIP_INFOS_TO, destination);

        String where = DatabaseHelper.COLUMN_TRIP_INFOS_TRIP_SEQ + " = " + tripSeq;

        int result = sqLiteDatabase.update(DatabaseHelper.TABLE_NAME_TRIP_INFOS, contentValues, where, null);

        return result;
    }

    public List<TripInfo> selectTripInfos() {

        List<TripInfo> tripInfos = new ArrayList<TripInfo>();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_NAME_TRIP_INFOS +
                " WHERE " + DatabaseHelper.COLUMN_TRIP_INFOS_ONGOING + " = 0" +
                " ORDER BY " + DatabaseHelper.COLUMN_TRIP_INFOS_TRIP_SEQ + " DESC";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        int indexTripSeq = cursor.getColumnIndex(DatabaseHelper.COLUMN_TRIP_INFOS_TRIP_SEQ);
        int indexTitle = cursor.getColumnIndex(DatabaseHelper.COLUMN_TRIP_INFOS_TITLE);
        int indexDescription = cursor.getColumnIndex(DatabaseHelper.COLUMN_TRIP_INFOS_DESCRIPTION);
        int indexFeel = cursor.getColumnIndex(DatabaseHelper.COLUMN_TRIP_INFOS_FEEL);
        int indexTransportation = cursor.getColumnIndex(DatabaseHelper.COLUMN_TRIP_INFOS_TRANSPORTATION);
        int indexWeather = cursor.getColumnIndex(DatabaseHelper.COLUMN_TRIP_INFOS_WEATHER);
        int indexDistance = cursor.getColumnIndex(DatabaseHelper.COLUMN_TRIP_INFOS_DISTANCE);
        int indexDuringTime = cursor.getColumnIndex(DatabaseHelper.COLUMN_TRIP_INFOS_DURING_TIME);
        int indexFrom = cursor.getColumnIndex(DatabaseHelper.COLUMN_TRIP_INFOS_FROM);
        int indexTo = cursor.getColumnIndex(DatabaseHelper.COLUMN_TRIP_INFOS_TO);
        int indexOngoing = cursor.getColumnIndex(DatabaseHelper.COLUMN_TRIP_INFOS_ONGOING);
        int indexSnapshot = cursor.getColumnIndex(DatabaseHelper.COLUMN_TRIP_INFOS_SNAPSHOT);
        int indexCreated = cursor.getColumnIndex(DatabaseHelper.COLUMN_TRIP_INFOS_CREATED);

        if (cursor.moveToFirst()) {

            do {

                TripInfo tripInfo = new TripInfo();

                tripInfo.setTripSeq(cursor.getInt(indexTripSeq));
                tripInfo.setTitle(cursor.getString(indexTitle));
                tripInfo.setDescription(cursor.getString(indexDescription));
                tripInfo.setFeel(cursor.getString(indexFeel));
                tripInfo.setTransportation(cursor.getString(indexTransportation));
                tripInfo.setWeather(cursor.getString(indexWeather));
                tripInfo.setDistance(cursor.getInt(indexDistance));
                tripInfo.setDuringTime(cursor.getInt(indexDuringTime));
                tripInfo.setFrom(cursor.getString(indexFrom));
                tripInfo.setTo(cursor.getString(indexTo));
                tripInfo.setOngoing(cursor.getInt(indexOngoing) > 0);
                tripInfo.setSnapshot(cursor.getBlob(indexSnapshot));
                tripInfo.setCreated(new Date(cursor.getLong(indexCreated)));

                tripInfos.add(tripInfo);

            } while (cursor.moveToNext());

        }

        if (cursor != null) {
            cursor.close();
        }

        return tripInfos;
    }

    public int deleteCurrentTripInfo(int tripSeq) {

        String where = DatabaseHelper.COLUMN_TRIP_INFOS_TRIP_SEQ + " = " + tripSeq;

        int result = sqLiteDatabase.delete(DatabaseHelper.TABLE_NAME_TRIP_INFOS, where, null);

        return result;
    }

    public List<PhotoInfo> selectTripPhotos(int tripSeq, int limit) {

        List<PhotoInfo> photoInfos = new ArrayList<PhotoInfo>();

        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM " + DatabaseHelper.TABLE_NAME_PHOTOS +
                " WHERE " + DatabaseHelper.COLUMN_PHOTOS_TRIP_SEQ + " = " + tripSeq);

        if (limit != 0) {
            queryBuilder.append(" LIMIT 0, " + limit);
        }

        Cursor cursor = sqLiteDatabase.rawQuery(queryBuilder.toString(), null);

        int indexPhotoSeq = cursor.getColumnIndex(DatabaseHelper.COLUMN_PHOTOS_SEQ);
        int indexTripSeq = cursor.getColumnIndex(DatabaseHelper.COLUMN_PHOTOS_TRIP_SEQ);
        int indexPath = cursor.getColumnIndex(DatabaseHelper.COLUMN_PHOTOS_PATH);
        int indexLatitude= cursor.getColumnIndex(DatabaseHelper.COLUMN_PHOTOS_LATITUDE);
        int indexLongitude = cursor.getColumnIndex(DatabaseHelper.COLUMN_PHOTOS_LONGITUDE);
        int indexCreated = cursor.getColumnIndex(DatabaseHelper.COLUMN_PHOTOS_CREATED);

        if (cursor.moveToFirst()) {

            do {

                PhotoInfo photoInfo = new PhotoInfo();

                photoInfo.setPhotoSeq(cursor.getInt(indexPhotoSeq));
                photoInfo.setTripSeq(cursor.getInt(indexTripSeq));
                photoInfo.setPath(cursor.getString(indexPath));
                photoInfo.setLatitude(cursor.getDouble(indexLatitude));
                photoInfo.setLongitude(cursor.getDouble(indexLongitude));
                photoInfo.setCreated(new Date(cursor.getLong(indexCreated)));

                photoInfos.add(photoInfo);

            } while (cursor.moveToNext());

        }

        if (cursor != null) {
            cursor.close();
        }

        return photoInfos;
    }

    public TripInfo selectTripInfo(int tripSeq) {

        TripInfo tripInfo = null;

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_NAME_TRIP_INFOS +
                " WHERE " + DatabaseHelper.COLUMN_TRIP_INFOS_TRIP_SEQ + " = " + tripSeq +
                " ORDER BY " + DatabaseHelper.COLUMN_TRIP_INFOS_TRIP_SEQ + " DESC";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        int indexTripSeq = cursor.getColumnIndex(DatabaseHelper.COLUMN_TRIP_INFOS_TRIP_SEQ);
        int indexTitle = cursor.getColumnIndex(DatabaseHelper.COLUMN_TRIP_INFOS_TITLE);
        int indexDescription = cursor.getColumnIndex(DatabaseHelper.COLUMN_TRIP_INFOS_DESCRIPTION);
        int indexFeel = cursor.getColumnIndex(DatabaseHelper.COLUMN_TRIP_INFOS_FEEL);
        int indexTransportation = cursor.getColumnIndex(DatabaseHelper.COLUMN_TRIP_INFOS_TRANSPORTATION);
        int indexWeather = cursor.getColumnIndex(DatabaseHelper.COLUMN_TRIP_INFOS_WEATHER);
        int indexDistance = cursor.getColumnIndex(DatabaseHelper.COLUMN_TRIP_INFOS_DISTANCE);
        int indexDuringTime = cursor.getColumnIndex(DatabaseHelper.COLUMN_TRIP_INFOS_DURING_TIME);
        int indexFrom = cursor.getColumnIndex(DatabaseHelper.COLUMN_TRIP_INFOS_FROM);
        int indexTo = cursor.getColumnIndex(DatabaseHelper.COLUMN_TRIP_INFOS_TO);
        int indexOngoing = cursor.getColumnIndex(DatabaseHelper.COLUMN_TRIP_INFOS_ONGOING);
        int indexSnapshot = cursor.getColumnIndex(DatabaseHelper.COLUMN_TRIP_INFOS_SNAPSHOT);
        int indexCreated = cursor.getColumnIndex(DatabaseHelper.COLUMN_TRIP_INFOS_CREATED);

        if (cursor.moveToFirst()) {

            do {
                tripInfo = new TripInfo();

                tripInfo.setTripSeq(cursor.getInt(indexTripSeq));
                tripInfo.setTitle(cursor.getString(indexTitle));
                tripInfo.setDescription(cursor.getString(indexDescription));
                tripInfo.setFeel(cursor.getString(indexFeel));
                tripInfo.setTransportation(cursor.getString(indexTransportation));
                tripInfo.setWeather(cursor.getString(indexWeather));
                tripInfo.setDistance(cursor.getInt(indexDistance));
                tripInfo.setDuringTime(cursor.getInt(indexDuringTime));
                tripInfo.setFrom(cursor.getString(indexFrom));
                tripInfo.setTo(cursor.getString(indexTo));
                tripInfo.setOngoing(cursor.getInt(indexOngoing) > 0);
                tripInfo.setSnapshot(cursor.getBlob(indexSnapshot));
                tripInfo.setCreated(new Date(cursor.getLong(indexCreated)));
            } while (cursor.moveToNext());

        }

        if (cursor != null) {
            cursor.close();
        }


        return tripInfo;
    }

    public int updateTripDetailInfo(int tripSeq, String title, String description, String feel, String transportation, String weather) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_TRIP_INFOS_TITLE, title);
        contentValues.put(DatabaseHelper.COLUMN_TRIP_INFOS_DESCRIPTION, description);
        contentValues.put(DatabaseHelper.COLUMN_TRIP_INFOS_FEEL, feel);
        contentValues.put(DatabaseHelper.COLUMN_TRIP_INFOS_TRANSPORTATION, transportation);
        contentValues.put(DatabaseHelper.COLUMN_TRIP_INFOS_WEATHER, weather);
        String where = DatabaseHelper.COLUMN_TRIP_INFOS_TRIP_SEQ + " = " + tripSeq;

        int result = sqLiteDatabase.update(DatabaseHelper.TABLE_NAME_TRIP_INFOS, contentValues, where, null);

        return result;
    }

    public List<LocationInfo> selectTripLocations(int tripSeq) {

        List<LocationInfo> locationInfos = new ArrayList<LocationInfo>();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_NAME_LOCATIONS +
                " WHERE " + DatabaseHelper.COLUMN_LOCATIONS_TRIP_SEQ + " = " + tripSeq;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        int indexLocationSeq = cursor.getColumnIndex(DatabaseHelper.COLUMN_LOCATIONS_LOCATION_SEQ);
        int indexTripSeq = cursor.getColumnIndex(DatabaseHelper.COLUMN_LOCATIONS_TRIP_SEQ);
        int indexLatitude = cursor.getColumnIndex(DatabaseHelper.COLUMN_LOCATIONS_LATITUDE);
        int indexLongitude= cursor.getColumnIndex(DatabaseHelper.COLUMN_LOCATIONS_LONGITUDE);
        int indexCreated = cursor.getColumnIndex(DatabaseHelper.COLUMN_LOCATIONS_CREATED);

        if (cursor.moveToFirst()) {

            do {

                LocationInfo locationInfo = new LocationInfo();

                locationInfo.setLocationSeq(cursor.getInt(indexLocationSeq));
                locationInfo.setTripSeq(cursor.getInt(indexTripSeq));
                locationInfo.setLatitude(cursor.getDouble(indexLatitude));
                locationInfo.setLongitude(cursor.getDouble(indexLongitude));
                locationInfo.setCreated(new Date(cursor.getLong(indexCreated)));

                locationInfos.add(locationInfo);

            } while (cursor.moveToNext());

        }

        if (cursor != null) {
            cursor.close();
        }

        return locationInfos;
    }

    public int deletePhotoInfo(int tripSeq) {

        String where = DatabaseHelper.COLUMN_PHOTOS_TRIP_SEQ + " = " + tripSeq;

        int result = sqLiteDatabase.delete(DatabaseHelper.TABLE_NAME_PHOTOS, where, null);

        return result;
    }

    public int deleteLocationInfo(int tripSeq) {

        String where = DatabaseHelper.COLUMN_LOCATIONS_TRIP_SEQ + " = " + tripSeq;

        int result = sqLiteDatabase.delete(DatabaseHelper.TABLE_NAME_LOCATIONS, where, null);

        return result;
    }
}
