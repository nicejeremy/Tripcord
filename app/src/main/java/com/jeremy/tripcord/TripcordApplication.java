package com.jeremy.tripcord;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

import com.jeremy.tripcord.app.R;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;

/**
 * Created by asura1983 on 2014. 7. 6..
 */
public class TripcordApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        setFacebookConfigure();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void setFacebookConfigure() {

        Permission[] permissions = new Permission[] {
                Permission.PUBLIC_PROFILE,
                Permission.EMAIL,
                Permission.USER_FRIENDS
        };

        SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
                .setAppId(getResources().getString(R.string.app_id))
                .setNamespace(getResources().getString(R.string.app_namespace))
                .setPermissions(permissions)
                .setAppSecret(getResources().getString(R.string.app_secret))
                .build();

        Log.d("Tripcord", "TripcordApplication >> Facebook configuration [" + configuration.toString() + "]");

        SimpleFacebook.setConfiguration(configuration);
    }
}
