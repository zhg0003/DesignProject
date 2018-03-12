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

public class MainActivity extends AppCompatActivity {
    //UI stuff
    private EditText frequency;
    private EditText amplitude;
    private Button generate;
    private TextView result;
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

    //sound related stuff
    private float Hz ;
    private float amp;
    private MediaPlayer mp;

    Handler handle = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addItemsOnSpinner();
        setUpStartButton();
        setUpCancelButton();
        freq1 = (SeekBar) findViewById(R.id.seekBar);
        amp1 = (SeekBar) findViewById(R.id.seekBar2);
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



    public void addItemsOnSpinner() {

        spinner1 = (Spinner) findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
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
                stopPlaying();
                startActivity(new Intent(MainActivity.this, MenuActivity.class));
            }
        });
    }

    public void setUpStartButton(){
        Button start_button = (Button) findViewById(R.id.button);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent my_intent = new Intent(getBaseContext(), TimerActivity.class);
                String selected_values = spinner1.getSelectedItem().toString();
                selected_values = selected_values.concat("~");
                selected_values = selected_values.concat(Integer.toString(freq1.getProgress()));
                selected_values = selected_values.concat("~");
                selected_values = selected_values.concat(Integer.toString(amp1.getProgress()));
                my_intent.putExtra("SELECTED_VALUES", selected_values);
                startActivity(my_intent);
                /*stopPlaying();
                String selected_mp3 = spinner1.getSelectedItem().toString();

                if (selected_mp3 == "Ocean")
                    mp = MediaPlayer.create(MainActivity.this, R.raw.ocean);
                else if (selected_mp3 == "Rain")
                    mp = MediaPlayer.create(MainActivity.this, R.raw.rain);
                else if (selected_mp3 == "Wind")
                    mp = MediaPlayer.create(MainActivity.this, R.raw.wind);
                else
                    mp = MediaPlayer.create(MainActivity.this, R.raw.rain); // play rain by default

                mp.start();*/
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

    /*void generate()
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
    void play()
    {
        //https://stackoverflow.com/questions/8698633/how-to-generate-a-particular-sound-frequency
        System.out.print("got here");
        final AudioTrack audio = new AudioTrack(AudioManager.STREAM_MUSIC,sampleRate,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                numSamp,
                AudioTrack.MODE_STATIC);
        System.out.print("done configuration");

        audio.write(sound,0,sound.length);
        audio.play();
    }*/
}
