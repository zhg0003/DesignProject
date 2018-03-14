package com.example.g.luciddreamgenerator;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.IOException;
import java.util.Random;

public class ToolsActivity extends AppCompatActivity {
    Button alarmBtn;
    RadioGroup intervalSelect;
    long interval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);
        intervalSelect = (RadioGroup) findViewById(R.id.radioGroup);
        setUpBackButton();

        final AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(ToolsActivity.this, AlarmReceiver.class);
        final PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),0,i,0);

        intervalSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                switch(rb.getId()) {
                    case R.id.halfRbtn:
                        interval = AlarmManager.INTERVAL_HALF_HOUR;
                        break;
                    case R.id.hourRbtn:
                        interval = AlarmManager.INTERVAL_HOUR;
                        break;
                    case R.id.twoRbtn:
                        interval = AlarmManager.INTERVAL_HOUR * 2;
                        break;
                    case R.id.randRbtn:
                        Random rand = new Random();
                        long randNum = (long) rand.nextInt((int) AlarmManager.INTERVAL_HOUR * 2 -
                                (int) AlarmManager.INTERVAL_HALF_HOUR) + AlarmManager.INTERVAL_HALF_HOUR;
                        interval = randNum;
                        break;
                }
            }
        });

        alarmBtn = (Button) findViewById(R.id.alarmBtn);

        alarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pi);
            }
        });

        Button stopBtn = (Button) findViewById(R.id.stopBtn);

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                am.cancel(pi);
            }
        });
    }

    public void setUpBackButton() {
        Button back_button = (Button) findViewById(R.id.backBtn);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ToolsActivity.this, MenuActivity.class));
            }
        });
    }
}
