package com.androvaid.fbaccountkittest;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "fbackit";

    public static int APP_REQUEST_CODE = 99;
    private Button btnLogin;
    private Button btnPhoneLogin, btnEmailLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: ");

        //getKeyHash();

        btnPhoneLogin = findViewById(R.id.btnPhoneLogin);
        btnEmailLogin = findViewById(R.id.btnEmailLogin);

        btnPhoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoginPage(LoginType.PHONE);
            }
        });

        btnEmailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoginPage(LoginType.EMAIL);
            }
        });


    }

    private void startLoginPage(LoginType loginType) {
        if ( loginType == LoginType.EMAIL ) {
            Intent intent = new Intent(this, AccountKitActivity.class);
            AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                    new AccountKitConfiguration.AccountKitConfigurationBuilder(
                            LoginType.EMAIL,
                            AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
            // ... perform additional configuration ...
            intent.putExtra(
                    AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                    configurationBuilder.build());
            startActivityForResult(intent, APP_REQUEST_CODE);

        } else if ( loginType == LoginType.PHONE ) {

            Intent intent = new Intent(this, AccountKitActivity.class);
            AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                    new AccountKitConfiguration.AccountKitConfigurationBuilder(
                            LoginType.PHONE,
                            AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
            // ... perform additional configuration ...
            intent.putExtra(
                    AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                    configurationBuilder.build());
            startActivityForResult(intent, APP_REQUEST_CODE);
        }
    }

    private void getKeyHash() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.androvaid.fbaccountkittest", PackageManager.GET_SIGNATURES);

            for ( Signature signature : info.signatures ) {

                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d(TAG, Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        } catch ( PackageManager.NameNotFoundException e ) {
            e.printStackTrace();
        } catch ( NoSuchAlgorithmException e ) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == APP_REQUEST_CODE ){
            AccountKitLoginResult result = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if ( result.getError() != null ){
                Toast.makeText(this, ""+result.getError().getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }else if ( result.wasCancelled() ){
                Toast.makeText(this, "Cancle", Toast.LENGTH_SHORT).show();
            }else {

                if(result.getAccessToken() != null) {
                    Toast.makeText(this, "Success ! " + result.getAccessToken().getAccountId(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onActivityResult: "+"Success ! " + result.getAccessToken().getAccountId());
                }else {
                    Toast.makeText(this, "Success ! "+ result.getAuthorizationCode(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onActivityResult: "+"Success ! "+ result.getAuthorizationCode());
                }
                startActivity(new Intent(this,SuccessActivity.class));
            }
        }

    }

    public void logOut(View view) {
        AccountKit.logOut();
    }
}
