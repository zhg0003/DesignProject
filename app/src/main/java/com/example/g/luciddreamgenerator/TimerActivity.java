package com.example.g.luciddreamgenerator;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.os.CountDownTimer;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimerActivity extends AppCompatActivity {

    private TextView sound;
    private TextView freq;
    private TextView amp;
    private TextView time;
    private Spinner spinner1;
    private ToggleButton startStop;
    private CountDownTimer my_timer;
    private MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sound = (TextView) findViewById(R.id.textView12);
        freq = (TextView) findViewById(R.id.textView13);
        amp = (TextView) findViewById(R.id.textView14);

        String received_items = getIntent().getStringExtra("SELECTED_VALUES");
        String[] parsed_received_items = received_items.split("~");

        sound.setText(parsed_received_items[0]);
        freq.setText(parsed_received_items[1]);
        amp.setText(parsed_received_items[2]);
        addItemsOnSpinner();
        setUpStartStop();
        setUpWriteDownDreamButton();

    }

    public void addItemsOnSpinner() {

        spinner1 = (Spinner) findViewById(R.id.spinner3);
        List<String> list = new ArrayList<String>();
        list.add("1 hour");
        list.add("2 hours");
        list.add("3 hours");
        list.add("4 hours");
        list.add("5 hours");
        list.add("6 hours");
        list.add("7 hours");
        list.add("8 hours");
        list.add("9 hours");
        list.add("10 hours");
        list.add("11 hours");
        list.add("12 hours");
        list.add("13 hours");
        list.add("14 hours");
        list.add("15 hours");
        list.add("16 hours");
        list.add("17 hours");
        list.add("18 hours");
        list.add("19 hours");
        list.add("20 hours");
        list.add("21 hours");
        list.add("22 hours");
        list.add("23 hours");
        list.add("24 hours");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                setTime(spinner1.getSelectedItemPosition());

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    public void setTime(int i)
    {
        time = (TextView) findViewById(R.id.textView17);
        switch(i)
        {
            case 0: time.setText("01:00:00"); break;
            case 1: time.setText("02:00:00"); break;
            case 2: time.setText("03:00:00"); break;
            case 3: time.setText("04:00:00"); break;
            case 4: time.setText("05:00:00"); break;
            case 5: time.setText("06:00:00"); break;
            case 6: time.setText("07:00:00"); break;
            case 7: time.setText("08:00:00"); break;
            case 8: time.setText("09:00:00"); break;
            case 9: time.setText("10:00:00"); break;
            case 10: time.setText("11:00:00"); break;
            case 11: time.setText("12:00:00"); break;
            case 12: time.setText("13:00:00"); break;
            case 13: time.setText("14:00:00"); break;
            case 14: time.setText("15:00:00"); break;
            case 15: time.setText("16:00:00"); break;
            case 16: time.setText("17:00:00"); break;
            case 17: time.setText("18:00:00"); break;
            case 18: time.setText("19:00:00"); break;
            case 19: time.setText("20:00:00"); break;
            case 20: time.setText("21:00:00"); break;
            case 21: time.setText("22:00:00"); break;
            case 22: time.setText("23:00:00"); break;
            case 23: time.setText("24:00:00"); break;
            default: time.setText("00:00:00"); break;
        }
    }

    public void setUpStartStop(){
        startStop = (ToggleButton) findViewById(R.id.toggleButton);
        startStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!startStop.isChecked()){

                    String selected_mp3 = sound.getText().toString();
                    Log.d("lol", selected_mp3);

                    if (selected_mp3.equals("Ocean"))
                        mp = MediaPlayer.create(TimerActivity.this, R.raw.ocean);
                    else if (selected_mp3 == "Rain")
                        mp = MediaPlayer.create(TimerActivity.this, R.raw.rain);
                    else if (selected_mp3 == "Wind")
                        mp = MediaPlayer.create(TimerActivity.this, R.raw.wind);
                    else
                        mp = MediaPlayer.create(TimerActivity.this, R.raw.rain); // play rain by default

                    mp.start();

                    my_timer = new CountDownTimer((spinner1.getSelectedItemPosition()+1) * 3600 * 1000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            time.setText(formatTime(millisUntilFinished));
                            if (!mp.isPlaying()) // make mp file loop
                                mp.start();
                        }

                        public void onFinish() {
                            if (my_timer != null)
                                my_timer.cancel();
                            stopPlaying();
                            mp = MediaPlayer.create(TimerActivity.this, R.raw.alarm);
                            mp.start();
                            //startActivity(new Intent(TimerActivity.this, JournalActivity.class));

                        }
                    }.start();
                }

                else {
                    my_timer.cancel();
                    stopPlaying();
                }
            }


        });
    }

    public void setUpWriteDownDreamButton() {
        Button writeDownDreamButton = (Button) findViewById(R.id.button9);

        writeDownDreamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (my_timer != null)
                    my_timer.cancel();
                stopPlaying();
                startActivity(new Intent(TimerActivity.this, JournalActivity.class));
            }
        });
    }

    private void stopPlaying() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }

    public String formatTime(long i){
        Integer time = (int) i;
        String stringTime;
        String hours;
        String minutes;
        String seconds;
        String tempString = "0";

        time = time/1000;

        hours = Integer.toString((time/3600));
        minutes = Integer.toString((time%3600)/60);
        seconds = Integer.toString(time%60);

        if (hours.length() == 1) {
            hours = tempString.concat(hours);
            tempString = "0";
        }
        if (minutes.length() == 1){
            minutes = tempString.concat(minutes);
            tempString = "0";
        }
        if (seconds.length() == 1){
            seconds = tempString.concat(seconds);
        }

        stringTime = hours;
        stringTime = stringTime.concat(":");
        stringTime = stringTime.concat(minutes);
        stringTime = stringTime.concat(":");
        stringTime = stringTime.concat(seconds);

        return stringTime;
    }

}
