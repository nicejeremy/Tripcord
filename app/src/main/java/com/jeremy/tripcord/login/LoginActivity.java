package com.jeremy.tripcord.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.jeremy.tripcord.app.R;

public class LoginActivity extends Activity {

    private EditText editTextId;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonFacebook;
    private Button buttonSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        initializeView();
    }

    private void initializeView() {

        editTextId = (EditText) findViewById(R.id.editText_id);
        editTextPassword = (EditText) findViewById(R.id.editText_password);
        buttonLogin = (Button) findViewById(R.id.button_login);
        buttonFacebook = (Button) findViewById(R.id.button_facebook);
        buttonSignup = (Button) findViewById(R.id.button_signup);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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

}
