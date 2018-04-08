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
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.SoundPool;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.io.File;
import java.io.InputStream;
import android.media.MediaPlayer;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class TimerActivity extends AppCompatActivity {

    private TextView sound1;
    private TextView freq1;
    //private TextView amp1;
    private TextView time;
    private Spinner spinner1;
    private Spinner spinner2;
    private ToggleButton startStop;
    private CountDownTimer my_timer;
    private MediaPlayer mp;

    private final int sampleRate = 44100;
    private final int duration = 1; // in seconds
    private final int numSamp = duration * sampleRate;
    private final double Sample[] = new double[numSamp];

    private final byte sound[] = new byte[2*numSamp];
    private AudioTrack audio;
    private boolean ready = false;

    private float default_Hz = 440;
    private float Hz ;
    private float amp;
    Handler handle = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sound1 = (TextView) findViewById(R.id.textView12);
        freq1 = (TextView) findViewById(R.id.textView13);
        //amp1 = (TextView) findViewById(R.id.textView14);

        String received_items = getIntent().getStringExtra("SELECTED_VALUES");
        String[] parsed_received_items = received_items.split("~");

        sound1.setText(parsed_received_items[0]);
        freq1.setText(parsed_received_items[1]);
        //amp1.setText(parsed_received_items[2]);
        addItemsOnSpinner1();
        addItemsOnSpinner2();
        setUpStartStop();
        setUpWriteDownDreamButton();

    }



    public void addItemsOnSpinner1() {

        spinner1 = (Spinner) findViewById(R.id.spinner3);
        List<String> list = new ArrayList<String>();
        list.add("0 hours");
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
                setTime();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    public void addItemsOnSpinner2() {

        spinner2 = (Spinner) findViewById(R.id.spinner4);
        List<String> list = new ArrayList<String>();
        list.add("0 minutes");
        list.add("1 minute");
        list.add("2 minutes");
        list.add("3 minutes");
        list.add("4 minutes");
        list.add("5 minutes");
        list.add("6 minutes");
        list.add("7 minutes");
        list.add("8 minutes");
        list.add("9 minutes");
        list.add("10 minutes");
        list.add("11 minutes");
        list.add("12 minutes");
        list.add("13 minutes");
        list.add("14 minutes");
        list.add("15 minutes");
        list.add("16 minutes");
        list.add("17 minutes");
        list.add("18 minutes");
        list.add("19 minutes");
        list.add("20 minutes");
        list.add("21 minutes");
        list.add("22 minutes");
        list.add("23 minutes");
        list.add("24 minutes");
        list.add("25 minutes");
        list.add("26 minutes");
        list.add("27 minutes");
        list.add("28 minutes");
        list.add("29 minutes");
        list.add("30 minutes");
        list.add("31 minutes");
        list.add("32 minutes");
        list.add("33 minutes");
        list.add("34 minutes");
        list.add("35 minutes");
        list.add("36 minutes");
        list.add("37 minutes");
        list.add("38 minutes");
        list.add("39 minutes");
        list.add("40 minutes");
        list.add("41 minutes");
        list.add("42 minutes");
        list.add("43 minutes");
        list.add("44 minutes");
        list.add("45 minutes");
        list.add("46 minutes");
        list.add("47 minutes");
        list.add("48 minutes");
        list.add("49 minutes");
        list.add("50 minutes");
        list.add("51 minutes");
        list.add("52 minutes");
        list.add("53 minutes");
        list.add("54 minutes");
        list.add("55 minutes");
        list.add("56 minutes");
        list.add("57 minutes");
        list.add("58 minutes");
        list.add("59 minutes");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                setTime();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    public void setTime()
    {
        int hours = spinner1.getSelectedItemPosition() - 1;
        int minutes = spinner2.getSelectedItemPosition();
        String tempTime;
        time = (TextView) findViewById(R.id.textView17);
        switch(hours)
        {
            case 0: tempTime = "01:"; break;
            case 1: tempTime = "02:"; break;
            case 2: tempTime = "03:"; break;
            case 3: tempTime = "04:"; break;
            case 4: tempTime = "05:"; break;
            case 5: tempTime = "06:"; break;
            case 6: tempTime = "07:"; break;
            case 7: tempTime = "08:"; break;
            case 8: tempTime = "09:"; break;
            case 9: tempTime = "10:"; break;
            case 10: tempTime = "11:"; break;
            case 11: tempTime = "12:"; break;
            case 12: tempTime = "13:"; break;
            case 13: tempTime = "14:"; break;
            case 14: tempTime = "15:"; break;
            case 15: tempTime = "16:"; break;
            case 16: tempTime = "17:"; break;
            case 17: tempTime = "18:"; break;
            case 18: tempTime = "19:"; break;
            case 19: tempTime = "20:"; break;
            case 20: tempTime = "21:"; break;
            case 21: tempTime = "22:"; break;
            case 22: tempTime = "23:"; break;
            case 23: tempTime = "24:"; break;
            default: tempTime = "00:"; break;
        }

        switch(minutes)
        {
            case 0: tempTime = tempTime.concat("00:00"); break;
            case 1: tempTime = tempTime.concat("01:00"); break;
            case 2: tempTime = tempTime.concat("02:00"); break;
            case 3: tempTime = tempTime.concat("03:00"); break;
            case 4: tempTime = tempTime.concat("04:00"); break;
            case 5: tempTime = tempTime.concat("05:00"); break;
            case 6: tempTime = tempTime.concat("06:00"); break;
            case 7: tempTime = tempTime.concat("07:00"); break;
            case 8: tempTime = tempTime.concat("08:00"); break;
            case 9: tempTime = tempTime.concat("09:00"); break;
            case 10: tempTime = tempTime.concat("10:00"); break;
            case 11: tempTime = tempTime.concat("11:00"); break;
            case 12: tempTime = tempTime.concat("12:00"); break;
            case 13: tempTime = tempTime.concat("13:00"); break;
            case 14: tempTime = tempTime.concat("14:00"); break;
            case 15: tempTime = tempTime.concat("15:00"); break;
            case 16: tempTime = tempTime.concat("16:00"); break;
            case 17: tempTime = tempTime.concat("17:00"); break;
            case 18: tempTime = tempTime.concat("18:00"); break;
            case 19: tempTime = tempTime.concat("19:00"); break;
            case 20: tempTime = tempTime.concat("20:00"); break;
            case 21: tempTime = tempTime.concat("21:00"); break;
            case 22: tempTime = tempTime.concat("22:00"); break;
            case 23: tempTime = tempTime.concat("23:00"); break;
            case 24: tempTime = tempTime.concat("24:00"); break;
            case 25: tempTime = tempTime.concat("25:00"); break;
            case 26: tempTime = tempTime.concat("26:00"); break;
            case 27: tempTime = tempTime.concat("27:00"); break;
            case 28: tempTime = tempTime.concat("28:00"); break;
            case 29: tempTime = tempTime.concat("29:00"); break;
            case 30: tempTime = tempTime.concat("30:00"); break;
            case 31: tempTime = tempTime.concat("31:00"); break;
            case 32: tempTime = tempTime.concat("32:00"); break;
            case 33: tempTime = tempTime.concat("33:00"); break;
            case 34: tempTime = tempTime.concat("34:00"); break;
            case 35: tempTime = tempTime.concat("35:00"); break;
            case 36: tempTime = tempTime.concat("36:00"); break;
            case 37: tempTime = tempTime.concat("37:00"); break;
            case 38: tempTime = tempTime.concat("38:00"); break;
            case 39: tempTime = tempTime.concat("39:00"); break;
            case 40: tempTime = tempTime.concat("40:00"); break;
            case 41: tempTime = tempTime.concat("41:00"); break;
            case 42: tempTime = tempTime.concat("42:00"); break;
            case 43: tempTime = tempTime.concat("43:00"); break;
            case 44: tempTime = tempTime.concat("44:00"); break;
            case 45: tempTime = tempTime.concat("45:00"); break;
            case 46: tempTime = tempTime.concat("46:00"); break;
            case 47: tempTime = tempTime.concat("47:00"); break;
            case 48: tempTime = tempTime.concat("48:00"); break;
            case 49: tempTime = tempTime.concat("49:00"); break;
            case 50: tempTime = tempTime.concat("50:00"); break;
            case 51: tempTime = tempTime.concat("51:00"); break;
            case 52: tempTime = tempTime.concat("52:00"); break;
            case 53: tempTime = tempTime.concat("53:00"); break;
            case 54: tempTime = tempTime.concat("54:00"); break;
            case 55: tempTime = tempTime.concat("55:00"); break;
            case 56: tempTime = tempTime.concat("56:00"); break;
            case 57: tempTime = tempTime.concat("57:00"); break;
            case 58: tempTime = tempTime.concat("58:00"); break;
            case 59: tempTime = tempTime.concat("59:00"); break;
            default: tempTime = tempTime.concat("00:00"); break;

        }

        time.setText(tempTime.toString());
    }

    public void setUpStartStop(){
        startStop = (ToggleButton) findViewById(R.id.toggleButton);
        startStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!startStop.isChecked()){

                    if (sound1.getText().toString().equals("Tone")) {

                        Hz = Float.parseFloat(freq1.getText().toString()) / 10;
                        if (Hz == 0) {
                            Log.d("Tag", "hz cant be 0");
                            //warning.setText("frequency cannot be 0");
                        }
                        else {
                            final Thread thread = new Thread(new Runnable() {
                                public void run() {
                                    generate();
                                    handle.post(new Runnable() {
                                        public void run() {
                                            generate_default();
                                            play(sound);
                                        }
                                    });
                                }
                            });
                            thread.start();
                            Log.d("Tag", "playing tone");
                            //warning.setText("playing");
                        }
                    }
                    else { // mp3
                        String selected_mp3 = sound1.getText().toString();
                        Log.d("Tag", selected_mp3);

                        if (selected_mp3.equals("Ocean"))
                            mp = MediaPlayer.create(TimerActivity.this, R.raw.ocean);
                        else if (selected_mp3.equals("Rain"))
                            mp = MediaPlayer.create(TimerActivity.this, R.raw.rain);
                        else if (selected_mp3.equals("Wind"))
                            mp = MediaPlayer.create(TimerActivity.this, R.raw.wind);
                        else
                            mp = MediaPlayer.create(TimerActivity.this, R.raw.rain); // play rain by default

                        mp.start();
                    }

                        my_timer = new CountDownTimer((spinner1.getSelectedItemPosition()) * 3600 * 1000 + spinner2.getSelectedItemPosition() * 60 * 1000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                time.setText(formatTime(millisUntilFinished));
                                if (mp != null)
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

    void interpolation(int begin, int end, double[] oldSample, double[] newSample,int ratio){//begin and end is the index of newSample
        //calculate the slope
        double slope = (oldSample[end] - oldSample[begin])/(end-begin);

        //copy the end points
        newSample[begin*ratio] = oldSample[begin];
        newSample[end*ratio] = oldSample[end];

        //fill in the gap with slope
        for(int i = begin*ratio+1; i <= end*ratio-1; i++){
            newSample[i] = newSample[i-1]+slope;
        }

        //print out result
        //System.out.println("the end point values and slope are "+oldSample[begin]+" "+oldSample[end]+" "+slope);
    }

    double[] copySample(int start, int end, double[] oldSample){//this is for copying samples from an oldSample, from start index to end index,
        double newSample[] = new double[end-start+1];
        for(int i = start; i<= end;i++){
            newSample[i] = oldSample[i];
//            System.out.println("i is "+i);
        }
        return newSample;
    }

    byte[] generate(){ //apply linear interpolation
        //obtain 1 hz of sample from the audio
        generate_default();
        int ratio = (int)Math.floor(default_Hz/Hz); //ratio of increase of index. ex old index is 1, new index in the newsample will be 1*ratio
        int OldSamplesPerPeriod = sampleRate / (int)default_Hz; //this is the amount of data points we have


        //calculate the new sampleAmount according to user input frequency by oldHz/newHz
        int newSampleAmount = ratio*OldSamplesPerPeriod;
        byte[] sound = new byte[2*newSampleAmount];

        //create new sound sample array and then copy the old samples for manipulation
        double oldSample[] = copySample(0,OldSamplesPerPeriod-1,Sample);
        double newSample[] = new double[newSampleAmount];

        //insert between 2 points
        for(int i = 0; i<oldSample.length-1;i++){
            newSample[i*ratio] = oldSample[i];
            interpolation(i, i+1,oldSample,newSample,ratio);
        }

        //scale to maximum amp
        int idx = 0;
        for (final double dVal : newSample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            sound[idx++] = (byte) (val & 0x00ff);
            sound[idx++] = (byte) ((val & 0xff00) >>> 8);

        }
        return sound;
    }

    void generate_default() //generate the default wave
    {
        //https://stackoverflow.com/questions/2413426/playing-an-arbitrary-tone-with-android
        for(int i = 0;i<numSamp;i++)
        {
            //wave formula, i is t
            Sample[i] = Math.sin(2*Math.PI*i / (sampleRate/Hz));
        }

        int idx = 0;
        for (final double dVal : Sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            sound[idx++] = (byte) (val & 0x00ff);
            sound[idx++] = (byte) ((val & 0xff00) >>> 8);

        }
    }

    void play(byte[] sound)
    {
        //https://stackoverflow.com/questions/8698633/how-to-generate-a-particular-sound-frequency
        ready = true;
        audio = new AudioTrack(AudioManager.STREAM_MUSIC,sampleRate,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                numSamp,
                AudioTrack.MODE_STATIC);

        audio.write(sound,0,sound.length);
        audio.play();
//        while(audio.getPlayState() == 3) {
//            System.out.println("currently playing");
//        }
    }

}
