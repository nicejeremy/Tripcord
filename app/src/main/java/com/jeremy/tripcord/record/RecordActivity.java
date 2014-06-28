package com.jeremy.tripcord.record;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jeremy.tripcord.app.R;
import com.jeremy.tripcord.common.contants.CommonContants;
import com.jeremy.tripcord.common.manager.DistanceManager;
import com.jeremy.tripcord.common.database.DatabaseManager;
import com.jeremy.tripcord.common.manager.PhotoManager;
import com.jeremy.tripcord.common.manager.TimeManager;
import com.jeremy.tripcord.common.utils.ImageUtil;
import com.jeremy.tripcord.record.thread.GetAddressTask;
import com.jeremy.tripcord.record.thread.TimerThread;
import com.jeremy.tripcord.record.view.CountDownObserver;
import com.jeremy.tripcord.record.view.CountDownView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecordActivity extends ActionBarActivity
        implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

    private static final int DEFAULT_ZOOM_LEVEL = 13;
    private static final int MILLISECONDS_PER_SECOND = 1000;
    private static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    private static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;

    private static final int TIME_SECOND_IN_A_MINUTE = 60;
    private static final int TIME_MINUTE_IN_A_HOUR = 60;
    private static final int TIME_HOUR_IN_A_DAY = 24;

    private static final int WHAT_TIME = 0;

    private List<LatLng> locations;
    private List<String> addresses;
    private int tripSeq = 0;
    private boolean isRecording = false;
    private int duringTime = 0;

    private DatabaseManager databaseManager;
    private DistanceManager distanceManager;

    private LocationClient locationClient;
    private LocationManager locationManager;
    private LocationRequest locationRequest;

    private PolylineOptions polylineOptions;
    private Polyline polyline;

    private GoogleMap googleMap;

    private File imageDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        startCountDown(5);
        initModules();
        initViews();
    }

    private void startCountDown(int i) {

        CountDownView countDownView = (CountDownView) findViewById(R.id.view_count_down);
        try {
            countDownView.start(i, countDownObserver);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initModules() {

        locations = new ArrayList<LatLng>();
        addresses = new ArrayList<String>();

        databaseManager = new DatabaseManager(this);
        databaseManager.open();
        tripSeq = databaseManager.insertTripInfo();
        distanceManager = DistanceManager.getInstance();
        distanceManager.setTripSeq(tripSeq);

        locationClient = new LocationClient(RecordActivity.this, RecordActivity.this, RecordActivity.this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.BLUE);

        TimerThread timerThread = new TimerThread(handlerTime);
        Log.d("Tripcord", "RecordActivity >> triavel info generate success [" + tripSeq + "]");
        timerThread.start();
    }

    private void initViews() {

        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d("Tripcord", "GoogleMap >> camera is changed");
            }
        });

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                Log.d("Tripcord", "Map Clicked");
            }
        });

        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {

                Log.d("Tripcord", "My location button Clicked");

                return false;
            }
        });

        Button buttonRecordPause = (Button) findViewById(R.id.button_record_pause);
        buttonRecordPause.setOnClickListener(onClickListenerRecordPause);

        Button buttonRecordStop = (Button) findViewById(R.id.button_record_stop);
        buttonRecordStop.setOnClickListener(onClickListenerRecordStop);

        Button buttonTakeAPicture = (Button) findViewById(R.id.button_take_picture);
        buttonTakeAPicture.setOnClickListener(onClickListenerTakeAPicture);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        locationClient.connect();
        Log.d("Tripcord", "RecordActivity >> location client has been connected");
    }

    @Override
    protected void onStop() {
        Log.d("Tripcord", "RecordActivity >> onStop");
        super.onStop();
    }

    @Override
    public void finish() {
        super.finish();

        DistanceManager.getInstance().reset();
        TimeManager.getInstance().reset();
    }

    @Override
    public void onBackPressed() {

        locationClient.disconnect();
        Log.d("Tripcord", "RecordActivity >> location client has been disconnected");

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RecordActivity.this);
        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                int result = databaseManager.deleteCurrentTripInfo(tripSeq);
                finish();
            }
        });
        alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do Nothing
            }
        });
        alertDialog.setMessage(R.string.back_key_pressed_during_recording);
        alertDialog.show();
    }

    // GooglePlayServiceClient
    @Override
    public void onConnected(Bundle bundle) {

        Log.d("Tripcord", "RecordActivity >> onConnected");

        locationClient.requestLocationUpdates(locationRequest, this);
//        isRecording = true;
    }

    private void takeSnapShot() {

        googleMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap bitmap) {

                byte[] snapshot = ImageUtil.bitmapToByteArray(bitmap);
                int result = databaseManager.updateTripSnapShot(tripSeq, snapshot);

                Log.d("Tripcord", "Snapshot create success [" + result + "]");
            }
        });
    }

    @Override
    public void onDisconnected() {

        Log.d("Tripcord", "RecordActivity >> onDisconnected");
        isRecording = false;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.d("Tripcord", "RecordActivity >> onConnectionFailed");
        isRecording = false;
    }

    // LocationListener
    @Override
    public void onLocationChanged(Location location) {

        String msg = "Updated Location: " + Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude());
        Log.d("Tripcord", "RecordActivity >> onLocationChanged :: " + msg);

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        locations.add(latLng);

        int zoomLevel = 0;
        if (locations.size() == 1) {
            TextView textView = (TextView) findViewById(R.id.textView_start_from);
            GetAddressTask getAddressTask = new GetAddressTask(RecordActivity.this, textView, addresses);
            getAddressTask.execute(location);

            zoomLevel = DEFAULT_ZOOM_LEVEL;
        } else {

            zoomLevel = (int) googleMap.getCameraPosition().zoom;
        }

        moveFocus(latLng, zoomLevel);

        if (isRecording) {

            drawLine(latLng);
            int seq = databaseManager.insertLocation(tripSeq, latLng);
            Log.d("Tripcord", "RecordActivity >> location info insert success [" + seq + "]");
            distanceManager.addLocation(location);
            updateDistance();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch(requestCode) {
                case 0:
                    Log.d("Tripcord", "RecordActivity >> onActivityResult :: data [" + imageDestination.getAbsolutePath() + "]");

                    // Photo info insert
                    PhotoManager.insertPhotoInfo(RecordActivity.this, imageDestination.getAbsolutePath());
                    addPhotoView(imageDestination.getAbsolutePath());

                    break;
            }
        }
    }

    private void updateDistance() {

        TextView textViewTotalDistance = (TextView) findViewById(R.id.textView_total_distance);
        textViewTotalDistance.setText("Total Distance\n" + String.format("%.0f", distanceManager.getTotalDistance()) + " m");

        Log.d("Tripcord", "RecordActivity >> current distance [" + distanceManager.getTotalDistance() + "]");
    }

    private void drawLine(LatLng latLng) {

        if (polyline != null) {
            polyline.remove();
        }

        polylineOptions.add(latLng);
        polyline = googleMap.addPolyline(polylineOptions);
    }

    private void addMarker(LatLng latLng) {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

        googleMap.addMarker(markerOptions);
    }

    private void moveFocus(LatLng latLng, int zoomLevel) {

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(zoomLevel).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void addPhotoView(String imagePath) {

        Bitmap bm = ImageUtil.decodeSampledBitmapFromUri(imagePath, 80, 80);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout_pictures);
        linearLayout.setGravity(Gravity.CENTER);

        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(80, 80));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(bm);

        View view = new View(getApplicationContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(10, 10));

        linearLayout.addView(imageView);
        linearLayout.addView(view);
    }

    View.OnClickListener onClickListenerRecordPause = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (isRecording) {
                locationClient.removeLocationUpdates(RecordActivity.this);
            } else {
                locationClient.requestLocationUpdates(locationRequest, RecordActivity.this);
            }

            isRecording = !isRecording;

            updateRecordPauseButton(isRecording);
        }
    };

    private void updateRecordPauseButton(boolean isRecording) {

        Button buttonRecordPause = (Button) findViewById(R.id.button_record_pause);
        if (!isRecording) {
            buttonRecordPause.setText(R.string.start);
            buttonRecordPause.setBackgroundResource(R.color.color_start);
        } else {
            buttonRecordPause.setText(R.string.pause);
            buttonRecordPause.setBackgroundResource(R.color.color_pause);
        }
    }

    View.OnClickListener onClickListenerRecordStop = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(RecordActivity.this);
            alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    locationClient.removeLocationUpdates(RecordActivity.this);

                    //distance, during_time, from, to, ongoing
                    updateTripInfoAsFinish();
                    takeSnapShot();

                    dialog.dismiss();
                    finish();
                }
            });
            alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Do Nothing
                }
            });
            alertDialog.setMessage(R.string.ask_to_finish_journey);
            alertDialog.show();
        }
    };

    private void updateTripInfoAsFinish() {

        double distance = DistanceManager.getInstance().getTotalDistance();

        String departure = "";
        String destination = "";
        if (addresses != null && addresses.size() != 0) {
            departure = addresses.get(0);
            destination = addresses.get(addresses.size() - 1);
        }

        int result = databaseManager.updateTripInfoAsFinish(tripSeq, distance, duringTime, departure, destination);

        Log.d("Tripcord", "RecordActivity >> Finish travel recording [Result : " + result + ", Distance : " + distance + ", During Time : " + duringTime + ", Departure : " + departure + ", Destination : " + destination + "]");
    }

    View.OnClickListener onClickListenerTakeAPicture = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date currentDate = new Date();
            File directory = new File(Environment.getExternalStorageDirectory() + CommonContants.ROOT_PATH_TRIPCORD);
            if (!directory.exists()) {
                directory.mkdir();
            }
            imageDestination = new File(Environment.getExternalStorageDirectory(), CommonContants.ROOT_PATH_TRIPCORD + "/tripcord_" + simpleDateFormat.format(currentDate) + ".jpg");

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageDestination));
            startActivityForResult(intent, 0);
        }
    };

    Handler handlerTime = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case WHAT_TIME :

                    TextView textViewTotalDistance = (TextView) findViewById(R.id.textView_total_time);
                    duringTime = (Integer) msg.obj;

//                    int minute = timeTaken % TIME_SECOND_IN_A_MINUTE;
//                    int hour = 0;
//                    int day = 0;
                    textViewTotalDistance.setText("Total Time Taken\n" + String.valueOf(duringTime));

                    break;

                default:
                    break;
            }
        }
    };

    CountDownObserver countDownObserver = new CountDownObserver() {

        @Override
        public void onFinishedCountDown() {

            CountDownView countDownView = (CountDownView) findViewById(R.id.view_count_down);
            RelativeLayout relativeLayout = (RelativeLayout) countDownView.getParent();
            relativeLayout.removeView(countDownView);

            isRecording = true;
        }
    };
}
