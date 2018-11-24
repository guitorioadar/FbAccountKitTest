package com.androvaid.fbaccountkittest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;

public class SuccessActivity extends AppCompatActivity {

    private final String TAG = "SuccessActivity";

    private Button btnLogOut;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        textView = findViewById(R.id.textView);
        btnLogOut = findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountKit.logOut();
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(Account account) {
                StringBuilder sb = new StringBuilder("Id: "+account.getId());
                sb.append(" Phone: ").append(account.getPhoneNumber());
                sb.append(" Email: ").append(account.getEmail());

                textView.setText(sb);

                Log.d(TAG, "onSuccess: "+sb);

            }

            @Override
            public void onError(AccountKitError accountKitError) {
                Log.d(TAG, "onError: "+accountKitError.getUserFacingMessage());
            }
        });

    }
}
