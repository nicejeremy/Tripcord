package com.jeremy.tripcord.record.gallery;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jeremy.tripcord.app.R;
import com.jeremy.tripcord.common.contants.CommonContants;
import com.jeremy.tripcord.common.database.domain.PhotoInfo;
import com.jeremy.tripcord.record.model.RecordModel;
import com.jeremy.tripcord.record.thread.GetAddressTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ImageGalleryActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_gallery);

        int selectedIndex = getIntent().getIntExtra(CommonContants.EXTRA_KEY_SELECTED_PICTURE_INDEX, -1);
        int tripSeq = getIntent().getIntExtra(CommonContants.EXTRA_KEY_TRIPSEQ, -1);

        List<PhotoInfo> pathArray = loadPictureInfo(tripSeq);
        initViews(pathArray, selectedIndex);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_gallery, menu);
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


    private List<PhotoInfo> loadPictureInfo(int tripSeq) {

        List<PhotoInfo> photoInfoList = RecordModel.loadTripPictureInfo(this, tripSeq);

        return photoInfoList;
    }

    private void initViews(final List<PhotoInfo> pathArray, int selectedIndex) {

        final TextView textViewTitle = (TextView) findViewById(R.id.textView_gallery_title);
        final TextView textViewLocation = (TextView) findViewById(R.id.textView_gallery_location);
        final TextView textViewDate = (TextView) findViewById(R.id.textView_gallery_date);

        PhotoInfo firstPhotoInfo = pathArray.get(0);
        setPhotoInfo(textViewLocation, textViewDate, firstPhotoInfo);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager_image_gallery);
        ImageGalleryAdapter pagerAdapter = new ImageGalleryAdapter(getSupportFragmentManager(), pathArray);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(selectedIndex);
//        viewPager.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                int visible = textViewTitle.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE;
//                textViewTitle.setVisibility(visible);
//                textViewLocation.setVisibility(visible);
//                textViewDate.setVisibility(visible);
//            }
//        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                PhotoInfo selectedPhotoInfo = pathArray.get(position);
                setPhotoInfo(textViewLocation, textViewDate, selectedPhotoInfo);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setPhotoInfo(TextView textViewLocation, TextView textViewDate, PhotoInfo photoInfo) {

        Location location = new Location("dummyProider");
        location.setLatitude(photoInfo.getLatitude());
        location.setLongitude(photoInfo.getLongitude());
        GetAddressTask getAddressTask = new GetAddressTask(ImageGalleryActivity.this, textViewLocation, new ArrayList<String>(), "");
        getAddressTask.execute(location);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd a hh:mm");
        textViewDate.setText(simpleDateFormat.format(photoInfo.getCreated()));
    }

    private class ImageGalleryAdapter extends FragmentStatePagerAdapter {

        private List<PhotoInfo> imagePaths;

        public ImageGalleryAdapter(FragmentManager fm, List<PhotoInfo> imagePaths) {
            super(fm);
            this.imagePaths = imagePaths;
        }

        @Override
        public Fragment getItem(int position) {
            return ImageFragment.create(imagePaths.get(position));
        }

        @Override
        public int getCount() {
            return this.imagePaths.size();
        }


    }
}
