package com.jeremy.tripcord.record.gallery;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.jeremy.tripcord.app.R;
import com.jeremy.tripcord.common.database.domain.PhotoInfo;
import com.jeremy.tripcord.common.utils.ImageUtil;
import com.jeremy.tripcord.record.thread.GetAddressTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ImageFragment extends Fragment {

    private static final String KEY_ITEM_PATH = "path";
    private static final String KEY_ITEM_LATITUDE = "latitude";
    private static final String KEY_ITEM_LONGITUDE = "longitude";
    private static final String KEY_ITEM_CREATED = "created";

    private PhotoInfo photoInfo;

    public static ImageFragment create(PhotoInfo photoInfo) {

        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(KEY_ITEM_PATH, photoInfo.getPath());
        args.putDouble(KEY_ITEM_LATITUDE, photoInfo.getLatitude());
        args.putDouble(KEY_ITEM_LONGITUDE, photoInfo.getLongitude());
        args.putLong(KEY_ITEM_CREATED, photoInfo.getCreated().getTime());
        fragment.setArguments(args);
        return fragment;
    }

    public ImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        photoInfo = new PhotoInfo();
        photoInfo.setPath(getArguments().getString(KEY_ITEM_PATH));
        photoInfo.setLatitude(getArguments().getDouble(KEY_ITEM_LATITUDE));
        photoInfo.setLongitude(getArguments().getDouble(KEY_ITEM_LONGITUDE));
        photoInfo.setCreated(new Date(getArguments().getLong(KEY_ITEM_CREATED)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_image, container, false);
        final ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView_image_item);
        Bitmap bitmap = ImageUtil.decodeSampledBitmapFromUri(photoInfo.getPath(), imageView.getLayoutParams().width, imageView.getLayoutParams().height);
        imageView.setImageBitmap(bitmap);

        TextView textViewLocation = (TextView) rootView.findViewById(R.id.textView_gallery_location);
        Location location = new Location("dummyProider");
        location.setLatitude(photoInfo.getLatitude());
        location.setLongitude(photoInfo.getLongitude());
        GetAddressTask getAddressTask = new GetAddressTask(getActivity(), textViewLocation, new ArrayList<String>(), "");
        getAddressTask.execute(location);

        TextView textViewDate = (TextView) rootView.findViewById(R.id.textView_gallery_date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd a hh:mm");
        textViewDate.setText(simpleDateFormat.format(photoInfo.getCreated()));

        return rootView;
    }

}
