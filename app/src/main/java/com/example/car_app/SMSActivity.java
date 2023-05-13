package com.example.car_app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.nio.charset.StandardCharsets;

public class SMSActivity extends AppCompatActivity {
    public EditText number, text;
    public Button send;
    public TextView recievedTexts;
    public final short SMS_PORT = 6734;
    public SmsManager smsManager;
    public BroadcastReceiver smsReciever;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (smsReciever != null) {
            unregisterReceiver(smsReciever);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsactivity);
        number = findViewById(R.id.phone);
        text = findViewById(R.id.text);
        send = findViewById(R.id.send);
        recievedTexts = findViewById(R.id.recievedText);
        smsManager = SmsManager.getDefault();
        //System.out.println(savedInstanceState.getString("number"));
        Bundle b = getIntent().getExtras();
        number.setText(b.getString("number"));

        if (smsManager == null) {
            Toast.makeText(this, "No SMS service",
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }, 101);
        }

        smsReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                SmsMessage[] messages = null;
                final StringBuilder str = new StringBuilder();

                try {
                    if (bundle == null) {
                        throw new Exception("No SMS DATA");
                    }
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    messages = new SmsMessage[pdus.length];
                    for (int i = 0; i < pdus.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        str.append("\nfrom: " + messages[i].getOriginatingAddress());
                        str.append("\nmessage: " + new String(messages[i].getUserData(), "UTF-8"));
                    }

                    recievedTexts.append(str.toString());

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        };
        //Register type of messages
        IntentFilter intentFilter = new IntentFilter("android.intent.action.DATA_SMS_RECIEVED");
        intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        intentFilter.addDataScheme("sms");
        intentFilter.addDataAuthority("*", "" + SMS_PORT);
        registerReceiver(smsReciever, intentFilter);

        send.setOnClickListener(view -> {
            try {
                smsManager.sendDataMessage(number.getText().toString(), null, SMS_PORT,
                        text.getText().toString().getBytes(StandardCharsets.UTF_8), null, null);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                                e.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        });

    }

}
