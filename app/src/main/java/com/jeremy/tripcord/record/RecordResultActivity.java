package com.jeremy.tripcord.record;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremy.tripcord.app.R;
import com.jeremy.tripcord.common.contants.CommonContants;
import com.jeremy.tripcord.common.database.domain.TripInfo;
import com.jeremy.tripcord.record.model.RecordModel;

public class RecordResultActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_result);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);

        TripInfo tripInfo = loadTripInfo();
        initViews(tripInfo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.record_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_drop) {
            int tripSeq = (Integer) getIntent().getIntExtra(CommonContants.EXTRA_KEY_TRIPSEQ, -1);
            int result = RecordModel.deleteCurrentTripInfo(getApplicationContext(), tripSeq);
            finish();
            return true;
        } else if (id == R.id.action_done) {
            updateTripDetailInfo();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private TripInfo loadTripInfo() {

        int tripSeq = (Integer) getIntent().getIntExtra(CommonContants.EXTRA_KEY_TRIPSEQ, -1);
        TripInfo tripInfo = RecordModel.loadTripInfo(getApplicationContext(), tripSeq);
        return tripInfo;
    }


    private void initViews(TripInfo tripInfo) {

        TextView textViewFrom = (TextView) findViewById(R.id.textView_trip_result_from);
        textViewFrom.setText("From : " + tripInfo.getFrom());

        TextView textViewTo = (TextView) findViewById(R.id.textView_trip_result_to);
        textViewTo.setText("To : " + tripInfo.getTo());

        TextView textViewPicture = (TextView) findViewById(R.id.textView_trip_result_picture);
        textViewPicture.setText(tripInfo.getPhotoInfoList().size() + " picture(s) was(were) taken");

        TextView textViewDistance = (TextView) findViewById(R.id.textView_trip_result_distance);
        textViewDistance.setText("Total distance\n" + tripInfo.getDistance());

        TextView textViewTime = (TextView) findViewById(R.id.textView_trip_result_time);
        textViewTime.setText("Total time\n" + tripInfo.getDuringTime());
    }

    private void updateTripDetailInfo() {

        int tripSeq = (Integer) getIntent().getIntExtra(CommonContants.EXTRA_KEY_TRIPSEQ, -1);

        EditText editTextTitle = (EditText) findViewById(R.id.edittext_trip_result_title);
        String title = editTextTitle.getText().toString();

        EditText editTextDescription = (EditText) findViewById(R.id.edittext_trip_result_description);
        String description = editTextDescription.getText().toString();

        String feel = "";
        String transportation = "";
        String weather = "";

        int result = RecordModel.updateTripDetailInfo(getApplicationContext(), tripSeq, title, description, feel, transportation, weather);

        if (result > 0) {
            Toast.makeText(getApplicationContext(), R.string.trip_detail_info_save_success, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), R.string.trip_detail_info_save_fail, Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
