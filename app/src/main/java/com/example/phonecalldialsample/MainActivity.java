package com.example.phonecalldialsample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phonecalldialsample.R;

public class MainActivity extends AppCompatActivity {
    private TelephonyManager mTelephonyManager;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    public static final String TAG = MainActivity.class.getSimpleName();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            // Create a telephony manager.
            mTelephonyManager = (TelephonyManager)
                    getSystemService(TELEPHONY_SERVICE);
            if (isTelephonyEnabled()) {
                Log.d(TAG, getString(R.string.telephony_enabled));
                // done : Check for phone permission.
                checkForPhonePermission();
                // ToDo: Register the PhoneStateListener.
            } else {
                Toast.makeText(this, R.string.telephony_not_enabled, Toast.LENGTH_LONG).show();
                Log.d(TAG, "TELEPHONY NOT ENABLED! ");
                // Disable the call button
                disableCallButton();
            }
    }

    private void disableCallButton() {
        Toast.makeText(this,
                "Phone calling disabled", Toast.LENGTH_LONG).show();
        ImageButton callButton = (ImageButton) findViewById(R.id.phone_icon);
        callButton.setVisibility(View.INVISIBLE);
        if (isTelephonyEnabled()) {
            Button retryButton = (Button) findViewById(R.id.button_retry);
            retryButton.setVisibility(View.VISIBLE);

        }
        }
    public void retryApp(View view) {
        enableCallButton();
        Intent intent = getPackageManager()
                .getLaunchIntentForPackage(getPackageName());
        startActivity(intent);
    }

    private void enableCallButton() {
        ImageButton callButton = (ImageButton) findViewById(R.id.phone_icon);
        callButton.setVisibility(View.VISIBLE);
    }

    private boolean isTelephonyEnabled() {
        if (mTelephonyManager != null) {
            if (mTelephonyManager.getSimState() ==
                    TelephonyManager.SIM_STATE_READY) {
                return true;
            }
        }
        return false;
    }

    private void checkForPhonePermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, getString(R.string.permission_not_granted));
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            // Permission already granted. Enable the call button.
            enableCallButton();
        }
    }

    public void callNumber() {
        EditText editText = (EditText) findViewById(R.id.editText_main);
        // Use format with "tel:" and phone number to create phoneNumber.
        String phoneNumber = String.format("tel: %s",
                editText.getText().toString());
        // Log the concatenated phone number for dialing.
        Log.d(TAG, getString(R.string.dial_number) + phoneNumber);
            Toast.makeText(this,
                getString(R.string.dial_number) + phoneNumber,
                Toast.LENGTH_LONG).show();
        // Create the intent.
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        // Set the data for the intent as the phone number.
        callIntent.setData(Uri.parse(phoneNumber));
        // If package resolves to an app, send intent.
        if (callIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(callIntent);
        } else {
            Log.e(TAG, "Can't resolve app for ACTION_CALL Intent.");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        // Check if permission is granted or not for the request.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (permissions[0].equalsIgnoreCase
                        (Manifest.permission.CALL_PHONE)
                        && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted.
                } else {
                    // Permission denied.
                    Log.d(TAG, getString(R.string.failure_permission));
                    Toast.makeText(this,
                            "Failure to obtain permission!",
                            Toast.LENGTH_LONG).show();
                    // Disable the call button
                    disableCallButton();
                }
            }

        }


    }}