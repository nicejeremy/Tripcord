package com.jeremy.tripcord.common.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by asura1983 on 2014. 4. 21..
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public final static String TABLE_NAME_LOCATIONS = "locations";

    public final static String COLUMN_LOCATIONS_LOCATION_SEQ = "locationSeq";
    public final static String COLUMN_LOCATIONS_TRIP_SEQ = "tripSeq";
    public final static String COLUMN_LOCATIONS_LONGITUDE = "longitude";
    public final static String COLUMN_LOCATIONS_LATITUDE = "latitude";
    public final static String COLUMN_LOCATIONS_CREATED = "created";

    private final static String QUERY_CREATE_TABLE_TRIPCORD_LOCATIONS = "CREATE TABLE " + TABLE_NAME_LOCATIONS + " (\n"
            + COLUMN_LOCATIONS_LOCATION_SEQ + " integer primary key autoincrement,\n"
            + COLUMN_LOCATIONS_TRIP_SEQ + " integer not null,\n"
            + COLUMN_LOCATIONS_LATITUDE + " double not null,\n"
            + COLUMN_LOCATIONS_LONGITUDE + " double not null,\n"
            + COLUMN_LOCATIONS_CREATED + " datetime default current_timestamp\n"
            + ");";

    public final static String TABLE_NAME_TRIP_INFOS = "tripinfos";

    public final static String COLUMN_TRIP_INFOS_TRIP_SEQ = "tripSeq";
    public final static String COLUMN_TRIP_INFOS_TITLE = "title";
    public final static String COLUMN_TRIP_INFOS_DESCRIPTION = "description";
    public final static String COLUMN_TRIP_INFOS_FEEL = "feel";
    public final static String COLUMN_TRIP_INFOS_TRANSPORTATION = "transportation";
    public final static String COLUMN_TRIP_INFOS_WEATHER = "weather";
    public final static String COLUMN_TRIP_INFOS_DISTANCE = "distance";
    public final static String COLUMN_TRIP_INFOS_DURING_TIME = "duringTime";
    public final static String COLUMN_TRIP_INFOS_FROM = "travel_from";
    public final static String COLUMN_TRIP_INFOS_TO = "travel_to";
    public final static String COLUMN_TRIP_INFOS_ONGOING = "ongoing";
    public final static String COLUMN_TRIP_INFOS_SNAPSHOT = "snapshot";
    public final static String COLUMN_TRIP_INFOS_CREATED = "created";

    private final static String QUERY_CREATE_TABLE_TRIPCORD_TRIPINFOS = "CREATE TABLE " + TABLE_NAME_TRIP_INFOS + " (\n"
            + COLUMN_TRIP_INFOS_TRIP_SEQ + " integer primary key autoincrement,\n"
            + COLUMN_TRIP_INFOS_TITLE + " text,\n"
            + COLUMN_TRIP_INFOS_DESCRIPTION + " text,\n"
            + COLUMN_TRIP_INFOS_FEEL + " text,\n"
            + COLUMN_TRIP_INFOS_TRANSPORTATION + " text,\n"
            + COLUMN_TRIP_INFOS_WEATHER + " text,\n"
            + COLUMN_TRIP_INFOS_DISTANCE + " integer,\n"
            + COLUMN_TRIP_INFOS_DURING_TIME + " integer,\n"
            + COLUMN_TRIP_INFOS_FROM + " text,\n"
            + COLUMN_TRIP_INFOS_TO + " text,\n"
            + COLUMN_TRIP_INFOS_ONGOING + " boolean not null default true,\n"
            + COLUMN_TRIP_INFOS_SNAPSHOT + " blob,\n"
            + COLUMN_TRIP_INFOS_CREATED + " datetime default current_timestamp"
            + ");";

    public static String TABLE_NAME_PHOTOS = "photos";

    public final static String COLUMN_PHOTOS_SEQ = "photoSeq";
    public final static String COLUMN_PHOTOS_TRIP_SEQ = "tripSeq";
    public final static String COLUMN_PHOTOS_PATH = "path";
    public final static String COLUMN_PHOTOS_LONGITUDE = "longitude";
    public final static String COLUMN_PHOTOS_LATITUDE = "latitude";
    public final static String COLUMN_PHOTOS_CREATED = "created";

    private final static String QUERY_CREATE_TABLE_TRIPCORD_PHOTOS = "CREATE TABLE " + TABLE_NAME_PHOTOS + " (\n"
            + COLUMN_PHOTOS_SEQ + " integer primary key autoincrement,\n"
            + COLUMN_PHOTOS_TRIP_SEQ + " integer not null,\n"
            + COLUMN_PHOTOS_PATH + " text not null,\n"
            + COLUMN_PHOTOS_LONGITUDE + " double not null,\n"
            + COLUMN_PHOTOS_LATITUDE + " double not null,\n"
            + COLUMN_PHOTOS_CREATED + " datetime default current_timestamp"
            + ");";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(QUERY_CREATE_TABLE_TRIPCORD_TRIPINFOS);
        sqLiteDatabase.execSQL(QUERY_CREATE_TABLE_TRIPCORD_LOCATIONS);
        sqLiteDatabase.execSQL(QUERY_CREATE_TABLE_TRIPCORD_PHOTOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        Log.i("Tripcord", "DatabaseHelper >> Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TRIP_INFOS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LOCATIONS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PHOTOS);

        onCreate(sqLiteDatabase);
    }

}
