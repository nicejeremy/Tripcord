package com.jeremy.tripcord.record;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

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
import com.jeremy.tripcord.common.database.domain.TripInfo;
import com.jeremy.tripcord.record.model.RecordModel;

public class RecordDetailActivity extends ActionBarActivity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    private static final int DEFAULT_ZOOM_LEVEL = 13;

    private PolylineOptions polylineOptions;
    private Polyline polyline;

    /*
     * Activity Life Cycle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);

        int tripSeq = getIntent().getIntExtra(CommonContants.EXTRA_KEY_TRIPSEQ, -1);
        TripInfo tripInfo = RecordModel.loadTripInfo(getApplicationContext(), tripSeq);

        initViews(tripInfo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.record_detail, menu);
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

        polylineOptions = new PolylineOptions();
        polylineOptions.color(getResources().getColor(R.color.color_tripcord));

        for (LocationInfo locationInfo : tripInfo.getLocationInfoList()) {
            LatLng latLng = new LatLng(locationInfo.getLatitude(), locationInfo.getLongitude());
            drawLine(googleMap, latLng);
        }

        if (tripInfo.getLocationInfoList() != null && tripInfo.getLocationInfoList().size() > 0) {
            LocationInfo firstLocationInfo = tripInfo.getLocationInfoList().get(0);
            LatLng latLng = new LatLng(firstLocationInfo.getLatitude(), firstLocationInfo.getLongitude());
            moveFocus(googleMap, latLng, DEFAULT_ZOOM_LEVEL);
        }
    }

    private void drawLine(GoogleMap googleMap, LatLng latLng) {

        if (polyline != null) {
            polyline.remove();
        }

        polylineOptions.add(latLng);
        polyline = googleMap.addPolyline(polylineOptions);
    }

    private void addMarker(GoogleMap googleMap, LatLng latLng) {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

        googleMap.addMarker(markerOptions);
    }

    private void moveFocus(GoogleMap googleMap, LatLng latLng, int zoomLevel) {

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(zoomLevel).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
