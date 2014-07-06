package com.jeremy.tripcord.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.jeremy.tripcord.app.R;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnLogoutListener;

import java.util.Arrays;

public class SettingFragment extends android.support.v4.app.Fragment {

//    private SimpleFacebook simpleFacebook;
//    private Button buttonFacebook;

    private UiLifecycleHelper uiHelper;

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

        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);

//        simpleFacebook = SimpleFacebook.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        final boolean isConnectedFacebook = simpleFacebook.isLogin();

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_setting, container, false);
//        buttonFacebook = (Button) rootView.findViewById(R.id.button_setting_facebook_login);
//        buttonFacebook.setText(isConnectedFacebook ? getResources().getString(R.string.disconnect) : getResources().getString(R.string.connect));
//        buttonFacebook.setBackgroundColor(isConnectedFacebook ? getResources().getColor(R.color.color_pause) : getResources().getColor(R.color.color_tripcord));
//        buttonFacebook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isConnectedFacebook) {
//                    simpleFacebook.logout(onLogoutListener);
//                } else {
//                    simpleFacebook.login(onLoginListener);
//                }
//
//                Session.openActiveSession(getActivity(), true, new Session.StatusCallback() {
//
//                    // callback when session changes state
//                    @Override
//                    public void call(Session session, SessionState state, Exception exception) {
//                        if (session.isOpened()) {
//
//                            Log.d("Tripcord", session.toString());
//                            // make request to the /me API
//                            Request.newMeRequest(session, new Request.GraphUserCallback() {
//
//                                // callback after Graph API response with user object
//                                @Override
//                                public void onCompleted(GraphUser user, Response response) {
//                                    if (user != null) {
//                                        TextView welcome = (TextView) findViewById(R.id.welcome);
//                                        welcome.setText("Hello " + user.getName() + "!");
//                                    }
//                                }
//                            }).executeAsync();
//                        }
//                    }
//                });
//            }
//        });


        LoginButton authButton = (LoginButton) rootView.findViewById(R.id.loginbutton_facebook);
        authButton.setReadPermissions(Arrays.asList("public_profile"));
        authButton.setFragment(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
//        simpleFacebook = SimpleFacebook.getInstance(getActivity());

        Session session = Session.getActiveSession();
        if (session != null && (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        simpleFacebook.onActivityResult(getActivity(), requestCode, resultCode, data);
//        Session.getActiveSession().onActivityResult(getActivity(), requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
        Log.d("Tripcord", "SettingFragment >> onActivityResult :: " + data.toString());
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i("Tripcord", "Logged in...");
        } else if (state.isClosed()) {
            Log.i("Tripcord", "Logged out...");
        }
    }

//    OnLoginListener onLoginListener = new OnLoginListener() {
//        @Override
//        public void onLogin() {
//            Log.d("Tripcord", "SettingFragment >> Facebook login success");
//            buttonFacebook.setText(getResources().getString(R.string.disconnect));
//            buttonFacebook.setBackgroundColor(getResources().getColor(R.color.color_pause));
//        }
//
//        @Override
//        public void onNotAcceptingPermissions(Permission.Type type) {
//            Log.d("Tripcord", "SettingFragment >> Facebook login on not acception permissions");
//        }
//
//        @Override
//        public void onThinking() {
//            Log.d("Tripcord", "SettingFragment >> Facebook login on thinking");
//        }
//
//        @Override
//        public void onException(Throwable throwable) {
//            Log.d("Tripcord", "SettingFragment >> Facebook login exception [" + throwable.toString() + "]");
//        }
//
//        @Override
//        public void onFail(String s) {
//            Log.d("Tripcord", "SettingFragment >> Facebook login fail [" + s + "]");
//        }
//    };
//
//    OnLogoutListener onLogoutListener = new OnLogoutListener() {
//        @Override
//        public void onLogout() {
//            Log.d("Tripcord", "SettingFragment >> Facebook logout success");
//            buttonFacebook.setText(getResources().getString(R.string.connect));
//            buttonFacebook.setBackgroundColor(getResources().getColor(R.color.color_tripcord));
//        }
//
//        @Override
//        public void onThinking() {
//            Log.d("Tripcord", "SettingFragment >> Facebook logout on thinking");
//        }
//
//        @Override
//        public void onException(Throwable throwable) {
//            Log.d("Tripcord", "SettingFragment >> Facebook logout exception [" + throwable.toString() + "]");
//        }
//
//        @Override
//        public void onFail(String s) {
//            Log.d("Tripcord", "SettingFragment >> Facebook logout fail [" + s + "]");
//        }
//    };
}
