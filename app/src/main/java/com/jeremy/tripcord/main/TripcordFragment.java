/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeremy.tripcord.main;

import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.jeremy.tripcord.app.R;
import com.jeremy.tripcord.common.view.sliding.SlidingUpPanelLayout;

import java.util.ArrayList;

public class TripcordFragment extends Fragment implements SlidingUpPanelLayout.PanelSlideListener {

    private ListView listViewTripHistory;
    private SlidingUpPanelLayout slidingUpPanelLayout;

    private View transparentHeaderView;
    private View transparentView;
    private View spaceView;

    private MapFragment mapFragment;

    private GoogleMap map;

    public static TripcordFragment newInstance() {
        TripcordFragment fragment = new TripcordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public TripcordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_tripcord, container, false);

        listViewTripHistory = (ListView) rootView.findViewById(R.id.list);
        listViewTripHistory.setOverScrollMode(ListView.OVER_SCROLL_NEVER);

        slidingUpPanelLayout = (SlidingUpPanelLayout) rootView.findViewById(R.id.slidingLayout);
        slidingUpPanelLayout.setEnableDragViewTouchEvents(true);

        int mapHeight = getResources().getDimensionPixelSize(R.dimen.map_height);
        slidingUpPanelLayout.setPanelHeight(mapHeight); // you can use different height here
        slidingUpPanelLayout.setScrollableView(listViewTripHistory, mapHeight);

        slidingUpPanelLayout.setPanelSlideListener(this);

        // transparent view at the top of ListView
        transparentView = rootView.findViewById(R.id.transparentView);

        // init header view for ListView
        transparentHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.transparent_header_view, null, false);
        spaceView = transparentHeaderView.findViewById(R.id.space);

        ArrayList<String> testData = new ArrayList<String>(100);
        for (int i = 0; i < 100; i++) {
            testData.add("Item " + i);
        }
        listViewTripHistory.addHeaderView(transparentHeaderView);
        listViewTripHistory.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.simple_list_item, testData));
        listViewTripHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                slidingUpPanelLayout.collapsePane();
            }
        });
        collapseMap();

        mapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mapContainer, mapFragment, "map");
        fragmentTransaction.commit();

        setUpMapIfNeeded();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            // Try to obtain the map from the SupportMapFragment.
            map = mapFragment.getMap();
            // Check if we were successful in obtaining the map.
            if (map != null) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setCompassEnabled(false);
                map.getUiSettings().setZoomControlsEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                CameraUpdate update = getLastKnownLocation();
                if (update != null) {
                    map.moveCamera(update);
                }
            }
        }
    }

    private CameraUpdate getLastKnownLocation() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        String provider = lm.getBestProvider(criteria, true);
        if (provider == null) {
            return null;
        }
        Location loc = lm.getLastKnownLocation(provider);
        if (loc != null) {
            return CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 14.0f));
        }
        return null;
    }

    private void collapseMap() {
        spaceView.setVisibility(View.VISIBLE);
        transparentView.setVisibility(View.GONE);
    }

    private void expandMap() {
        spaceView.setVisibility(View.GONE);
        transparentView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPanelSlide(View view, float v) {
    }

    @Override
    public void onPanelCollapsed(View view) {
        expandMap();
        map.animateCamera(CameraUpdateFactory.zoomTo(14f), 1000, null);
    }

    @Override
    public void onPanelExpanded(View view) {
        collapseMap();
        map.animateCamera(CameraUpdateFactory.zoomTo(11f), 1000, null);
    }

    @Override
    public void onPanelAnchored(View view) {

    }
}
