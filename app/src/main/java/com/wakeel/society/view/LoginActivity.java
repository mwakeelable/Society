package com.wakeel.society.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.wakeel.society.R;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    private static final String EMAIL = "email";
    AccessToken accessToken = AccessToken.getCurrentAccessToken();
    ArrayList permissions;
    boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
    public static String TAG = "SocialLogin";
    @BindView(R.id.login_button)
    LoginButton fbLoginButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        callbackManager = CallbackManager.Factory.create();
        permissions = new ArrayList();
        permissions.add("public_profile");
        permissions.add("email");
        permissions.add("user_friends");
        permissions.add("user_posts");
        fbLoginButton.setReadPermissions(permissions);
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d("fbLogin", loginResult.getAccessToken().getUserId());
                Intent homeIntent = new Intent(LoginActivity.this,HomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("accessToken",loginResult.getAccessToken().getToken());
                homeIntent.putExtras(bundle);
                startActivity(homeIntent);
                LoginActivity.this.finish();
            }

            @Override
            public void onCancel() {
                // App code
                Log.w("fbLoginCancel", "Cancelled");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e("fbLoginError", exception.toString());
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
