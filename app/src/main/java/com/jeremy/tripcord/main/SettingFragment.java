package com.jeremy.tripcord.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jeremy.tripcord.app.R;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnLogoutListener;

public class SettingFragment extends android.support.v4.app.Fragment {

    private SimpleFacebook simpleFacebook;
    private Button buttonFacebook;

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        simpleFacebook = SimpleFacebook.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final boolean isConnectedFacebook = simpleFacebook.isLogin();

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_setting, container, false);
        buttonFacebook = (Button) rootView.findViewById(R.id.button_setting_facebook_login);
        buttonFacebook.setText(isConnectedFacebook ? getResources().getString(R.string.disconnect) : getResources().getString(R.string.connect));
        buttonFacebook.setBackgroundColor(isConnectedFacebook ? getResources().getColor(R.color.color_pause) : getResources().getColor(R.color.color_tripcord));
        buttonFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnectedFacebook) {
                    simpleFacebook.logout(onLogoutListener);
                } else {
                    simpleFacebook.login(onLoginListener);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        simpleFacebook = SimpleFacebook.getInstance(getActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        simpleFacebook.onActivityResult(getActivity(), requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    OnLoginListener onLoginListener = new OnLoginListener() {
        @Override
        public void onLogin() {
            Log.d("Tripcord", "SettingFragment >> Facebook login success");
            buttonFacebook.setText(getResources().getString(R.string.disconnect));
            buttonFacebook.setBackgroundColor(getResources().getColor(R.color.color_pause));
        }

        @Override
        public void onNotAcceptingPermissions(Permission.Type type) {
            Log.d("Tripcord", "SettingFragment >> Facebook login on not acception permissions");
        }

        @Override
        public void onThinking() {
            Log.d("Tripcord", "SettingFragment >> Facebook login on thinking");
        }

        @Override
        public void onException(Throwable throwable) {
            Log.d("Tripcord", "SettingFragment >> Facebook login exception [" + throwable.toString() + "]");
        }

        @Override
        public void onFail(String s) {
            Log.d("Tripcord", "SettingFragment >> Facebook login fail [" + s + "]");
        }
    };

    OnLogoutListener onLogoutListener = new OnLogoutListener() {
        @Override
        public void onLogout() {
            Log.d("Tripcord", "SettingFragment >> Facebook logout success");
            buttonFacebook.setText(getResources().getString(R.string.connect));
            buttonFacebook.setBackgroundColor(getResources().getColor(R.color.color_tripcord));
        }

        @Override
        public void onThinking() {
            Log.d("Tripcord", "SettingFragment >> Facebook logout on thinking");
        }

        @Override
        public void onException(Throwable throwable) {
            Log.d("Tripcord", "SettingFragment >> Facebook logout exception [" + throwable.toString() + "]");
        }

        @Override
        public void onFail(String s) {
            Log.d("Tripcord", "SettingFragment >> Facebook logout fail [" + s + "]");
        }
    };
}
