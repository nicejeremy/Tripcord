package com.jeremy.tripcord.record;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.UiLifecycleHelper;
import com.facebook.android.Facebook;
import com.facebook.widget.FacebookDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
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
import com.jeremy.tripcord.common.database.domain.LocationInfo;
import com.jeremy.tripcord.common.database.domain.PhotoInfo;
import com.jeremy.tripcord.common.database.domain.TripInfo;
import com.jeremy.tripcord.common.utils.DistanceUtil;
import com.jeremy.tripcord.common.utils.FacebookUtil;
import com.jeremy.tripcord.common.utils.ImageUtil;
import com.jeremy.tripcord.common.utils.StringUtil;
import com.jeremy.tripcord.common.utils.TimeUtil;
import com.jeremy.tripcord.record.gallery.ImageGalleryActivity;
import com.jeremy.tripcord.record.model.RecordModel;

import java.util.List;

public class RecordDetailActivity extends ActionBarActivity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    private static final int DEFAULT_ZOOM_LEVEL = 13;

    private PolylineOptions polylineOptions;
    private Polyline polyline;
    private TripInfo tripInfo;

    private UiLifecycleHelper uiHelper;

    /*
     * Activity Life Cycle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);

        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);

        int tripSeq = getIntent().getIntExtra(CommonContants.EXTRA_KEY_TRIPSEQ, -1);
        tripInfo = RecordModel.loadTripInfo(getApplicationContext(), tripSeq, 0);

        initViews(tripInfo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
            @Override
            public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
                Log.e("Tripcord", String.format("Error: %s", error.toString()));
            }

            @Override
            public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
                Log.i("Tripcord", "Success!");
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        uiHelper.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.record_detail, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_edit) {

            return true;
        } else if (id == R.id.action_social) {
            showJourneyFacebookPostDialog();
            return true;
        } else if (id == R.id.action_drop) {
            popupDropAlertDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener
     */
    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /*
     * Custom methods
     */
    private void initViews(TripInfo tripInfo) {

        GoogleMap googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map_trip_record_detail)).getMap();
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(false);

        polylineOptions = new PolylineOptions();
        polylineOptions.color(getResources().getColor(R.color.color_line));

        for (LocationInfo locationInfo : tripInfo.getLocationInfoList()) {
            LatLng latLng = new LatLng(locationInfo.getLatitude(), locationInfo.getLongitude());
            drawLine(googleMap, latLng);
        }

        TextView textViewTitle = (TextView) findViewById(R.id.textView_trip_detail_title);
        textViewTitle.setText(tripInfo.getTitle());
        TextView textViewFrom = (TextView) findViewById(R.id.textView_trip_detail_from);
        textViewFrom.setText(getString(R.string.label_from) + tripInfo.getFrom());
        TextView textViewTo = (TextView) findViewById(R.id.textView_trip_detail_to);
        textViewTo.setText(getString(R.string.label_to) + tripInfo.getTo());

        TextView textViewFeeling = (TextView) findViewById(R.id.textView_trip_detail_feeling);
        if (StringUtil.isEmpty(tripInfo.getFeel())) textViewFeeling.setVisibility(View.GONE);
        else textViewFeeling.setText(getString(R.string.label_feeling) + tripInfo.getFeel());

        TextView textViewTransport = (TextView) findViewById(R.id.textView_trip_detail_transport);
        if (StringUtil.isEmpty(tripInfo.getTransportation())) textViewTransport.setVisibility(View.GONE);
        else textViewTransport.setText(getString(R.string.label_transport) + tripInfo.getTransportation());

        TextView textViewWeather = (TextView) findViewById(R.id.textView_trip_detail_weather);
        if (StringUtil.isEmpty(tripInfo.getWeather())) textViewWeather.setVisibility(View.GONE);
        else textViewWeather.setText(getString(R.string.label_weather) + tripInfo.getWeather());


        TextView textViewTime = (TextView) findViewById(R.id.textView_trip_detail_time);
        textViewTime.setText(getString(R.string.label_time) + TimeUtil.getTimeDescription(tripInfo.getDuringTime()));
        TextView textViewDistance = (TextView) findViewById(R.id.textView_trip_detail_distance);
        textViewDistance.setText(getString(R.string.label_distance) + DistanceUtil.getDistance(tripInfo.getDistance()));
        TextView textViewPhoto = (TextView) findViewById(R.id.textView_trip_detail_photo);
        textViewPhoto.setText(getString(R.string.label_photo) + String.valueOf(tripInfo.getPhotoInfoList().size()));

        if (tripInfo.getPhotoInfoList().size() != 0) {
            addPhoto(googleMap, tripInfo.getPhotoInfoList());
        } else {
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout_trip_detail_pictures);
            linearLayout.setVisibility(View.GONE);
        }

        if (tripInfo.getLocationInfoList() != null && tripInfo.getLocationInfoList().size() > 0) {
            LocationInfo firstLocationInfo = tripInfo.getLocationInfoList().get(0);
            LatLng latLng = new LatLng(firstLocationInfo.getLatitude(), firstLocationInfo.getLongitude());
            moveFocus(googleMap, latLng, DEFAULT_ZOOM_LEVEL);
        }
    }

    private void addPhoto(GoogleMap googleMap, List<PhotoInfo> photoInfoList) {

        for(int i = 0; i < photoInfoList.size(); i++) {
            PhotoInfo photoInfo = photoInfoList.get(i);
            addPhotoView(i, photoInfo.getPath());
            addMarker(googleMap, new LatLng(photoInfo.getLatitude(), photoInfo.getLongitude()), photoInfo.getPath());
        }

    }

    private void drawLine(GoogleMap googleMap, LatLng latLng) {

        if (polyline != null) {
            polyline.remove();
        }

        polylineOptions.add(latLng);
        polyline = googleMap.addPolyline(polylineOptions);
    }

    private void addMarker(GoogleMap googleMap, LatLng latLng, String path) {

        Bitmap bm = ImageUtil.decodeSampledBitmapFromUri(path, 50, 50);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bm));

        googleMap.addMarker(markerOptions);
    }

    private void moveFocus(GoogleMap googleMap, LatLng latLng, int zoomLevel) {

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(zoomLevel).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void addPhotoView(int index, String imagePath) {

        Bitmap bm = ImageUtil.decodeSampledBitmapFromUri(imagePath, 100, 100);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout_trip_detail_pictures);
        linearLayout.setGravity(Gravity.CENTER);

        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(bm);
        imageView.setTag(index);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int viewIndex = Integer.valueOf((Integer) v.getTag());

                Intent intent = new Intent(RecordDetailActivity.this, ImageGalleryActivity.class);
                intent.putExtra(CommonContants.EXTRA_KEY_TRIPSEQ, tripInfo.getTripSeq());
                intent.putExtra(CommonContants.EXTRA_KEY_SELECTED_PICTURE_INDEX, viewIndex);
                intent.putExtra(CommonContants.EXTRA_KEY_TRIP_TITLE, tripInfo.getTitle());
                startActivity(intent);
            }
        });

        View view = new View(getApplicationContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(10, 10));

        linearLayout.addView(imageView);
        linearLayout.addView(view);
    }

    private void popupDropAlertDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RecordDetailActivity.this);
        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                int result = RecordModel.deleteCurrentTripInfo(getApplicationContext(), tripInfo.getTripSeq());
                finish();
            }
        });
        alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do Nothing
            }
        });
        alertDialog.setMessage(R.string.ask_for_drop_journey);
        alertDialog.show();
    }

    private void showJourneyFacebookPostDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RecordDetailActivity.this);
        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do Nothing
            }
        });
        alertDialog.setMessage(R.string.post_on_facebook_your_journey);
        alertDialog.show();
    }

}
