package com.example.g.luciddreamgenerator;

import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.sql.BatchUpdateException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import android.media.MediaPlayer;
import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //UI stuff
    private EditText frequency;
    private EditText amplitude;

    private CheckBox left;
    private CheckBox right;
    private CheckBox left2;
    private CheckBox right2;
    private CheckBox disableSound2;

    private Button inc01;
    private Button dec01;
    private Button inc01_2;
    private Button dec01_2;

    private Spinner spinner1; // sound one
    private Spinner sound2List;

    private SeekBar freq1;
    private SeekBar freq2;

    private SeekBar amp1;

    private TextView editHz;
    private TextView editHz2;

    //AudioTrack related stuff
    private final int sampleRate = 44100;
    private final int duration = 3; // in seconds
    //private final int numSamp = duration * sampleRate;

    private final int numSamp = sampleRate;
    private final double Sample[] = new double[numSamp];
    private final byte sound[] = new byte[2*numSamp];
    private final int bufferSize = sampleRate;


    private float default_Hz = 440;

    /*
     *0 means nothing is playing, 1 means default sound playing, 2 means tone is playing
     *3 means both sound 1 and 2 are playing
     */
    private int play_state = 0;

    private AudioTrack audio;
    private AudioTrack audio2;

    private Thread toneGen;
    private Thread toneGen2;
    //sound related stuff
    private float Hz ;
    private float Hz2;

    private float amp;
    private MediaPlayer mp;

    Handler handle = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        audio = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize,
                AudioTrack.MODE_STREAM);

        audio2 = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize,
                AudioTrack.MODE_STREAM);

        setUpCheckBoxes();
        addItemsOnSpinner();
        setUpStartButton();
        setUpJournalButton();
        setUpCancelButton();
        setUpFrequencyBar();
        setUpHzInput();
        setUpAdjustButtons();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            startActivity(new Intent(MainActivity.this, MenuActivity.class));
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setUpAdjustButtons(){
        inc01=findViewById(R.id.inc01);
        dec01=findViewById(R.id.dec01);
        inc01_2=findViewById(R.id.inc01_2);
        dec01_2=findViewById(R.id.dec01_2);
        dec01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((freq1.getProgress()-1)<=0)
                    freq1.setProgress(1);
                else
                    freq1.setProgress(freq1.getProgress()-1);
                //startActivity(new Intent(MainActivity.this, MenuActivity.class));
            }
        });

        inc01.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if((freq1.getProgress()+1)<=0)
                    freq1.setProgress(10000);
                else
                    freq1.setProgress(freq1.getProgress()+1);
                //startActivity(new Intent(MainActivity.this, MenuActivity.class));
            }
        });
        dec01_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((freq2.getProgress()-1)<=0)
                    freq2.setProgress(1);
                else
                    freq2.setProgress(freq2.getProgress()-1);
                //startActivity(new Intent(MainActivity.this, MenuActivity.class));
            }
        });

        inc01_2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if((freq2.getProgress()+1)<=0)
                    freq2.setProgress(10000);
                else
                    freq2.setProgress(freq2.getProgress()+1);
                //startActivity(new Intent(MainActivity.this, MenuActivity.class));
            }
        });
    }

    public void setUpHzInput(){
        editHz = findViewById(R.id.enterHz);
        editHz2 = findViewById(R.id.enterHz2);

        editHz.setText(String.valueOf(Hz));
        editHz.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //warning.setText("in here");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //freq1.setProgress((int)Float.parseFloat(editHz.getText().toString())*10);
                //warning.setText("in ontextchange");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editHz.getText().length() == 0){
                    editHz.setText("1");
                }

                freq1.setProgress((int)(Float.parseFloat(editHz.getText().toString())*10));
            }
        });

        editHz2.setText(String.valueOf(Hz2));
        editHz2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //warning.setText("in here");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //freq1.setProgress((int)Float.parseFloat(editHz.getText().toString())*10);
                //warning.setText("in ontextchange");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editHz2.getText().length() == 0){
                    editHz2.setText("1");
                }

                freq2.setProgress((int)(Float.parseFloat(editHz2.getText().toString())*10));
            }
        });
    }
    public void setUpCheckBoxes(){
        disableSound2=findViewById(R.id.disableSound2);

        left=findViewById(R.id.leftChannel);
        right=findViewById(R.id.rightChannel);
        left.setChecked(true);
        right.setChecked(true);

        left2=findViewById(R.id.leftChannel2);
        right2=findViewById(R.id.rightChannel2);
        left2.setChecked(true);
        right2.setChecked(true);

//        left.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(left.isChecked() && !right.isChecked()) {
//                    left.setChecked(true);
//                }
//            }
//        });
//
//        right.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(right.isChecked() && !left.isChecked()) {
//                    right.setChecked(false);
//                }
//
//            }
//        });
//
//        left2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(left2.isChecked()) {
//                    right2.setChecked(false);
//                }
//            }
//        });
//        right2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(right2.isChecked()) {
//                    left2.setChecked(false);
//                }
//            }
//        });
    }

    public void setUpFrequencyBar(){
        freq1 = (SeekBar) findViewById(R.id.seekBar);
        freq2 = findViewById(R.id.seekBar2);

        freq1.setProgress(700);
        freq2.setProgress(700);

        Hz = (float)freq1.getProgress()/10;
        Hz2 = (float)freq1.getProgress()/10;
        freq1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (spinner1.getSelectedItem().toString().equals("Tone")) {
                    Hz = (float)freq1.getProgress()/10;

                    editHz.setText(String.valueOf(Hz));
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        freq2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (sound2List.getSelectedItem().toString().equals("Tone")) {
                    Hz2 = (float)freq2.getProgress()/10;
                    editHz2.setText(String.valueOf(Hz2));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void addItemsOnSpinner() {
        sound2List = findViewById(R.id.sound2item);
        spinner1 = (Spinner) findViewById(R.id.spinner);

        List<String> list = new ArrayList<String>();
        list.add("Tone");
        list.add("Ocean");
        list.add("Rain");
        list.add("Wind");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter);
        sound2List.setAdapter(dataAdapter);
//        List<String> list2 = new ArrayList<String>();
//        list2.add("Tone");
//        list2.add("Ocean");
//        list2.add("Rain");
//        list2.add("Wind");
//        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, list2);
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner1.setAdapter(dataAdapter);


    }



    public void setUpCancelButton() {
        Button record_button = (Button) findViewById(R.id.button2);

        record_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop_current_sound();
                //startActivity(new Intent(MainActivity.this, MenuActivity.class));
            }
        });
    }

    public void setUpJournalButton() {
        Button journal_button = (Button) findViewById(R.id.button10);

        journal_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop_current_sound();

                SharedPreferences sound_settings = getApplicationContext().getSharedPreferences("sound", 0);
                SharedPreferences freq_settings = getApplicationContext().getSharedPreferences("freq", 0);

                SharedPreferences.Editor sound_editor = sound_settings.edit();
                SharedPreferences.Editor freq_editor = freq_settings.edit();

                sound_editor.putString("firstSound", (String) spinner1.getSelectedItem());
                freq_editor.putString("firstFreq", Float.toString((float)freq1.getProgress()/10));

                if (!disableSound2.isChecked()) {
                    sound_editor.putString("secondSound", (String) sound2List.getSelectedItem());
                    freq_editor.putString("secondFreq", Float.toString((float)freq2.getProgress()/10));
                }
                else {
                    sound_editor.putString("secondSound", "");
                    freq_editor.putString("secondFreq", "");
                }

                sound_editor.apply();
                freq_editor.apply();

                startActivity(new Intent(MainActivity.this, JournalActivity.class));
            }
        });
    }

    public void setUpStartButton(){
        Button start_button = (Button) findViewById(R.id.button);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(play_state != 0)
                    stop_current_sound();

                if(spinner1.getSelectedItem().toString() == "Tone"){
                    //audio.stop();
//                        warning.setText("");
                        final byte[] samples1 = generate_default(Hz,1);
                        Runnable toneStream = new Runnable() {
                            @Override
                            public void run() {
                                play_state = 2;
                                System.out.println("in sound 1 thread, play state is "+play_state);
                                while (play_state == 2 || play_state == 3) {
                                    audio.write(samples1, 0, samples1.length);
                                }
                                System.out.println("thread ending for sound 1 current state is "+play_state);
                            }
                        };
                        toneGen = new Thread(toneStream);
                        //if(audio.getPlayState())
                        play(1);
                        toneGen.start();


                        if(!disableSound2.isChecked()){
                            play_state = 3;
                            final byte[] samples2 = generate_default(Hz2,2);
                            Runnable toneStream2 = new Runnable() {
                                @Override
                                public void run() {
                                    play_state = 3;
                                    System.out.println("in sound 2 thread, state is "+play_state);
                                    while (play_state == 3) {
                                        audio2.write(samples2, 0, samples2.length);
                                    }
                                    System.out.println("thread ending for sound 2 current state is "+play_state);
                                }
                            };
                            toneGen2 = new Thread(toneStream2);
                            play(2);
                            toneGen2.start();
                        }
                }


                else {
//                  warning.setText("");
                    stop_current_sound();
                    String selected_mp3 = spinner1.getSelectedItem().toString();

                    if (selected_mp3 == "Ocean") {
                        mp = MediaPlayer.create(MainActivity.this, R.raw.ocean);
                    }
                    else if (selected_mp3 == "Rain") {
                        mp = MediaPlayer.create(MainActivity.this, R.raw.rain);
                    }
                    else if (selected_mp3 == "Wind") {
                        mp = MediaPlayer.create(MainActivity.this, R.raw.wind);
                    }
                    else {
                        mp = MediaPlayer.create(MainActivity.this, R.raw.rain); // play rain by default
                    }
                    mp.setLooping(true);
                    mp.start();
                    play_state = 1;
                }
            }
        });
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
        generate_default(Hz,1);
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

    byte[] generate_default(float hz, int soundn) //generate the default wave, and write it into the corresponding audio object based on soundn
    {
        //https://stackoverflow.com/questions/2413426/playing-an-arbitrary-tone-with-android
        int x = (int)( (double)bufferSize * hz / sampleRate ); // added
        int mSampleCount = (int)( (double)x * sampleRate / hz ); // added

        byte[] samples = new byte[ mSampleCount ]; // changed from bufferSize

        for( int i = 0; i != mSampleCount; ++i ) { // changed from bufferSize
            double t = (double)i * (1.0/sampleRate);
            double f = Math.sin( t * 2*Math.PI * hz );
            samples[i] = (byte)(f * 127);
        }
//        for(int i = 0;i<numSamp;i++)
//        {
//            //wave formula, i is t
//            Sample[i] = Math.sin(2*Math.PI*i / (sampleRate/Hz));
//        }
//
//        int idx = 0;
//        for (final double dVal : Sample) {
//            // scale to maximum amplitude
//            final short val = (short) ((dVal * 32767));
//            // in 16 bit wav PCM, first byte is the low order byte
//            sound[idx++] = (byte) (val & 0x00ff);
//            sound[idx++] = (byte) ((val & 0xff00) >>> 8);
//
//        }
        if(soundn == 1) {
            audio.write(samples, 0, samples.length);
        }

        if(soundn == 2) {
            audio2.write(samples, 0, samples.length);
        }
        return samples;
    }

    void stop_current_sound(){
        if(play_state == 1) {
            mp.stop();
            mp.release();
            mp = null;
        }
        else if(play_state == 2)
            audio.stop();
        else if(play_state == 3){
            audio.stop();
            audio2.stop();
        }
        play_state = 0;
    }

    void play(int soundn)
    {
        //https://stackoverflow.com/questions/8698633/how-to-generate-a-particular-sound-frequency



        //int buffer=AudioTrack.getMinBufferSize(8000,AudioFormat.CHANNEL_IN_STEREO,AudioFormat.ENCODING_PCM_16BIT);
        if(soundn == 1) {
            if (right.isChecked() && !left.isChecked() )
                audio.setStereoVolume(0, 0.7f);
            else if (left.isChecked() && !right.isChecked())
                audio.setStereoVolume(0.7f, 0);
            else if(left.isChecked() && right.isChecked())
                audio.setStereoVolume(0.7f,0.7f);
            else if(!left.isChecked() && !right.isChecked())
                audio.setStereoVolume(0,0);
            audio.play();
            System.out.println("current state before changing is "+play_state);
            //play_state = 2;
            System.out.println("end of play function for sound 1");
        }
        else{
            if (right2.isChecked() && !left2.isChecked())
                audio2.setStereoVolume(0, 0.7f);
            else if (left2.isChecked() && !right2.isChecked())
                audio2.setStereoVolume(0.7f, 0);
            else if(left2.isChecked() && right2.isChecked())
                audio2.setStereoVolume(0.7f,0.7f);
            else if(!left2.isChecked() && !right2.isChecked())
                audio2.setStereoVolume(0,0);
            audio2.play();
            System.out.println("current state before changing is "+play_state);
            //play_state = 2;
            System.out.println("end of play function for sound 2");
        }
    }
}
