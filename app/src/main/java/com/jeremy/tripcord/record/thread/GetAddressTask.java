package com.jeremy.tripcord.record.thread;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.jeremy.tripcord.record.RecordActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by asura1983 on 2014. 5. 27..
 */
public class GetAddressTask extends AsyncTask<Location, Void, String> {

    private Context context;
    private TextView textView;
    private List<String> addresses;

    public GetAddressTask(Context context, TextView textView, List<String> addresses) {
        super();
        this.context = context;
        this.textView = textView;
        this.addresses = addresses;
    }

    /**
    * Get a Geocoder instance, get the latitude and longitude
    * look up the address, and return it
    *
    * @params params One or more Location objects
    * @return A string containing the address of the current
    * location, or an empty string if no address can be found,
    * or an error message
    */
    @Override
    protected String doInBackground(Location... params) {

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        // Get the current location from the input parameter list
        Location loc = params[0];
        // Create a list to contain the result address
        List<Address> addresses = null;
        try {
            /*
            * Return 1 address.
            */
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
        } catch (IOException e1) {
            Log.e("LocationSampleActivity", "IO Exception in getFromLocation()");
            e1.printStackTrace();
            return ("IO Exception trying to get address");
        } catch (IllegalArgumentException e2) {
            // Error message to post in the log
            String errorString = "Illegal arguments " + Double.toString(loc.getLatitude()) + " , " + Double.toString(loc.getLongitude()) + " passed to address service";
            Log.e("LocationSampleActivity", errorString);
            e2.printStackTrace();
            return errorString;
        }
        // If the reverse geocode returned an address
        if (addresses != null && addresses.size() > 0) {
            // Get the first address
            Address address = addresses.get(0);
            /*
             * Format the first line of address (if available),
             * city, and country name.
             */
            String addressText = String.format(
                    "%s, %s, %s",
                    // If there's a street address, add it
                    address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                    // Locality is usually a city
                    address.getLocality(),
                    // The country of the address
                    address.getCountryName());

            // Return the text
            return addressText;
        } else {
            return "No address found";
        }
    }

    @Override
    protected void onPostExecute(String address) {

        Log.d("Tripcord", "GetAddressTask >> onPostExecute :: " + address);
        if (textView != null) {
            textView.setText("Start from : " + address);
        }

        if (!addresses.contains(address)) {
            addresses.add(address);
        }
    }

}
