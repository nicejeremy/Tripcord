package com.jeremy.tripcord.record.gallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.jeremy.tripcord.app.R;
import com.jeremy.tripcord.common.contants.CommonContants;
import com.jeremy.tripcord.common.database.domain.PhotoInfo;
import com.jeremy.tripcord.record.model.RecordModel;

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

    private void initViews(List<PhotoInfo> pathArray, int selectedIndex) {

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager_image_gallery);
        ImageGalleryAdapter pagerAdapter = new ImageGalleryAdapter(getSupportFragmentManager(), pathArray);
        viewPager.setAdapter(pagerAdapter);
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
