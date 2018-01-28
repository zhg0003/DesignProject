package com.example.g.luciddreamgenerator;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //UI stuff
    private EditText frequency;
    private EditText amplitude;
    private Button generate;
    private TextView result;

    //AudioTrack related stuff
    private final int sampleRate = 8000;
    private final int duration = 3; // in seconds
    private final int numSamp = duration * sampleRate;
    private final double Sample[] = new double[numSamp];
    private final byte sound[] = new byte[2*numSamp];

    //sound related stuff
    private float Hz ;
    private float amp;

    Handler handle = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frequency  = (EditText)findViewById(R.id.frequency);
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
        });
    }

    void generate()
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
    }
}
