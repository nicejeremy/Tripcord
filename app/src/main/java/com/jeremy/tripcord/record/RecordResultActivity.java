package com.jeremy.tripcord.record;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.jeremy.tripcord.record.imagebutton.ImageRadioButton;
import com.jeremy.tripcord.record.model.RecordModel;

public class RecordResultActivity extends ActionBarActivity {

    private static final int RADIO_BUTTON_WIDTH = 25;
    private static final int RADIO_BUTTON_HEIGHT = 25;

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

        getMenuInflater().inflate(R.menu.record_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_drop) {
            int tripSeq = getIntent().getIntExtra(CommonContants.EXTRA_KEY_TRIPSEQ, -1);
            int result = RecordModel.deleteCurrentTripInfo(getApplicationContext(), tripSeq);
            finish();
            return true;
        } else if (id == R.id.action_done) {
            updateTripDetailInfo();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RecordResultActivity.this);
        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                int tripSeq = getIntent().getIntExtra(CommonContants.EXTRA_KEY_TRIPSEQ, -1);
                int result = RecordModel.deleteCurrentTripInfo(getApplicationContext(), tripSeq);
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

    private TripInfo loadTripInfo() {

        int tripSeq = getIntent().getIntExtra(CommonContants.EXTRA_KEY_TRIPSEQ, -1);
        TripInfo tripInfo = RecordModel.loadTripInfo(getApplicationContext(), tripSeq, 0);
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

        ImageRadioButton imageRadioButtonFeeling = (ImageRadioButton) findViewById(R.id.imageRadioButton_feeling);
        imageRadioButtonFeeling.addButton(R.drawable.feel_fun, R.drawable.selectable_button, RADIO_BUTTON_WIDTH, RADIO_BUTTON_HEIGHT, getString(R.string.feel_fun));
        imageRadioButtonFeeling.addButton(R.drawable.feel_happy, R.drawable.selectable_button, RADIO_BUTTON_WIDTH, RADIO_BUTTON_HEIGHT, getString(R.string.feel_happy));
        imageRadioButtonFeeling.addButton(R.drawable.feel_soso, R.drawable.selectable_button, RADIO_BUTTON_WIDTH, RADIO_BUTTON_HEIGHT, getString(R.string.feel_soso));
        imageRadioButtonFeeling.addButton(R.drawable.feel_unhappy, R.drawable.selectable_button, RADIO_BUTTON_WIDTH, RADIO_BUTTON_HEIGHT, getString(R.string.feel_unhappy));
        imageRadioButtonFeeling.addButton(R.drawable.feel_hard, R.drawable.selectable_button, RADIO_BUTTON_WIDTH, RADIO_BUTTON_HEIGHT, getString(R.string.feel_hard));

        ImageRadioButton imageRadioButtonTransport = (ImageRadioButton) findViewById(R.id.imageRadioButton_transport);
        imageRadioButtonTransport.addButton(R.drawable.transport_walk, R.drawable.selectable_button, RADIO_BUTTON_WIDTH, RADIO_BUTTON_HEIGHT, getString(R.string.transport_walk));
        imageRadioButtonTransport.addButton(R.drawable.transport_car, R.drawable.selectable_button, RADIO_BUTTON_WIDTH, RADIO_BUTTON_HEIGHT, getString(R.string.transport_car));
        imageRadioButtonTransport.addButton(R.drawable.transport_bus, R.drawable.selectable_button, RADIO_BUTTON_WIDTH, RADIO_BUTTON_HEIGHT, getString(R.string.transport_bus));
        imageRadioButtonTransport.addButton(R.drawable.transport_train, R.drawable.selectable_button, RADIO_BUTTON_WIDTH, RADIO_BUTTON_HEIGHT, getString(R.string.transport_train));
        imageRadioButtonTransport.addButton(R.drawable.transport_flight, R.drawable.selectable_button, RADIO_BUTTON_WIDTH, RADIO_BUTTON_HEIGHT, getString(R.string.transport_flight));

        ImageRadioButton imageRadioButtonWeather = (ImageRadioButton) findViewById(R.id.imageRadioButton_weather);
        imageRadioButtonWeather.addButton(R.drawable.weather_sunny, R.drawable.selectable_button, RADIO_BUTTON_WIDTH, RADIO_BUTTON_HEIGHT, getString(R.string.weather_sunny));
        imageRadioButtonWeather.addButton(R.drawable.weather_little_bit_cloudy, R.drawable.selectable_button, RADIO_BUTTON_WIDTH, RADIO_BUTTON_HEIGHT, getString(R.string.weather_little_bit_cloudy));
        imageRadioButtonWeather.addButton(R.drawable.weather_cloudy, R.drawable.selectable_button, RADIO_BUTTON_WIDTH, RADIO_BUTTON_HEIGHT, getString(R.string.weather_cloudy));
        imageRadioButtonWeather.addButton(R.drawable.weather_rainy, R.drawable.selectable_button, RADIO_BUTTON_WIDTH, RADIO_BUTTON_HEIGHT, getString(R.string.weather_rainy));
        imageRadioButtonWeather.addButton(R.drawable.weather_snow, R.drawable.selectable_button, RADIO_BUTTON_WIDTH, RADIO_BUTTON_HEIGHT, getString(R.string.weather_snow));
    }

    private void updateTripDetailInfo() {

        int tripSeq = getIntent().getIntExtra(CommonContants.EXTRA_KEY_TRIPSEQ, -1);

        EditText editTextTitle = (EditText) findViewById(R.id.edittext_trip_result_title);
        String title = editTextTitle.getText().toString();

        EditText editTextDescription = (EditText) findViewById(R.id.edittext_trip_result_description);
        String description = editTextDescription.getText().toString();

        ImageRadioButton imageRadioButtonFeeling = (ImageRadioButton) findViewById(R.id.imageRadioButton_feeling);
        ImageRadioButton imageRadioButtonTransport = (ImageRadioButton) findViewById(R.id.imageRadioButton_transport);
        ImageRadioButton imageRadioButtonWeather = (ImageRadioButton) findViewById(R.id.imageRadioButton_weather);

        String feel = imageRadioButtonFeeling.getSelectedItemValues().toString();
        String transportation = imageRadioButtonTransport.getSelectedItemValues().toString();
        String weather = imageRadioButtonWeather.getSelectedItemValues().toString();

        int result = RecordModel.updateTripDetailInfo(getApplicationContext(), tripSeq, title, description, feel, transportation, weather);

        if (result > 0) {
            Toast.makeText(getApplicationContext(), R.string.trip_detail_info_save_success, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), R.string.trip_detail_info_save_fail, Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
