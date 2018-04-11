package com.example.g.luciddreamgenerator;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
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
//to do
//spawn a pause and stop button once start button is pressed
//add a button that takes user to alarm
//

public class MainActivity extends AppCompatActivity {
    //UI stuff
    private EditText frequency;
    private EditText amplitude;
    private Button generate;
    private Button goToWrite;
    private TextView result;
    private TextView warning;
    private TextView showhz;
    private Spinner spinner1; // sound one
    private SeekBar freq1;
    private SeekBar amp1;
    private Spinner spinner2; // sound two

    //AudioTrack related stuff
    private final int sampleRate = 8000;
    private final int duration = 3; // in seconds
    private final int numSamp = duration * sampleRate;
    private final double Sample[] = new double[numSamp];
    private final byte sound[] = new byte[2*numSamp];
    private float default_Hz = 440;

    private int play_state = 0; //0 means nothing is playing, 1 means default sound playing, 2 means tone is playing

    private AudioTrack audio;
    private boolean ready = false;

    //sound related stuff
    private float Hz ;
    private float amp;
    private MediaPlayer mp;

    Handler handle = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        freq1 = (SeekBar) findViewById(R.id.seekBar);
        showhz = (TextView) findViewById(R.id.textView3);
        warning = findViewById(R.id.warning);
        warning.setText("");

        addItemsOnSpinner();
        setUpStartButton();
        setUpJournalButton();
        setUpCancelButton();
        setUpFrequencyBar();

        /*frequency  = (EditText)findViewById(R.id.frequency);
        amplitude = (EditText)findViewById(R.id.amplitude);
        generate = (Button)findViewById(R.id.generate);
        result = (TextView)findViewById(R.id.result);
        generate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                result.setText("Amp is "+amplitude.getText().toString()+" Hz is "+frequency.getText().toString());
                Hz = Float.parseFloat(frequency.getText().toString());
                amp = Float.parseFloat(amplitude.getText().toString());
                final Thread thread = new Thread(new Runnable() {
                    public void run() {
                        generate();
                        handle.post(new Runnable() {
                            public void run() {
                                play();
                            }
                        });
                    }
                });
                thread.start();
            }
        });*/
    }

    public void setUpFrequencyBar(){
        freq1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (spinner1.getSelectedItem().toString().equals("Tone")) {
                    Hz = (float)i/100*360;
                    showhz.setText(String.valueOf(Hz + " Hz"));
                }
                else
                    showhz.setText("Only Applicable to Tone");
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

    }



    public void setUpCancelButton() {
        Button record_button = (Button) findViewById(R.id.button2);

        record_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop_current_sound();
                startActivity(new Intent(MainActivity.this, MenuActivity.class));
            }
        });
    }

    public void setUpJournalButton() {
        Button journal_button = (Button) findViewById(R.id.button10);

        journal_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, JournalActivity.class));
            }
        });
    }

    public void setUpStartButton(){
        Button start_button = (Button) findViewById(R.id.button);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(spinner1.getSelectedItem().toString() == "Tone"){
                    //audio.stop();
                    if(Hz == 0)
                        warning.setText("Hz cannot be 0");
                    else {
                        warning.setText("");
                        generate_default();
                        play(sound);
                    }
                }

                else {
                    warning.setText("");
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

    void stop_current_sound(){
        if(play_state == 1) {
            mp.stop();
            mp.release();
            mp = null;
        }
        else if(play_state == 2)
            audio.stop();
    }

    void play(byte[] sound)
    {
        //https://stackoverflow.com/questions/8698633/how-to-generate-a-particular-sound-frequency
        stop_current_sound();
        ready = true;
        audio = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                numSamp,
                AudioTrack.MODE_STATIC);

        audio.write(sound, 0, sound.length);
        audio.setLoopPoints(0, sound.length / 4, -1);
        audio.play();
        play_state = 2;
    }


}
