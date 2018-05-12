package com.example.g.luciddreamgenerator;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;
import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Context.POWER_SERVICE;

public class AlarmReceiver extends BroadcastReceiver{
    private static final int uniqueID = 12345;
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i;
        PendingIntent pi;
        NotificationCompat.Builder mBuilder;
        Uri alert = null;
        Bundle extras = intent.getExtras();
        String song = extras.getString("SONG_ID");
        String random = extras.getString("RANDOM_BOOL");
        Random rand = new Random();
        long randNum;
        long interval = 0;
        String message;
        if (random.equals("true")) {
            randNum = (long) rand.nextInt((int) AlarmManager.INTERVAL_HOUR * 2 -
                    (int) AlarmManager.INTERVAL_HALF_HOUR) + AlarmManager.INTERVAL_HALF_HOUR;
            interval = randNum;
            message = "";// + ((interval / 1000) / 60);
        }
        else {
            message = "";
        }

        if (song != null) {
            if (song.equals("Dream")) {
                alert = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.dream);
            }
            else if (song.equals("Notification")) {
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
            else if (song.equals("Coin")) {
                alert = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.coin);
            }
            if (alert == null) {
                // alert is null, using backup
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }

        i = new Intent(context, MenuActivity.class);
        pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            nm.createNotificationChannel(notificationChannel);
        }

        mBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setContentTitle("Am I dreaming?")
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(alert, AudioManager.STREAM_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setVibrate(new long[]{1000, 1000})
                .setAutoCancel(true)
                .setTicker("Are you dreaming?")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pi);
        nm.notify(uniqueID, mBuilder.build());

        if (!song.equals("None")) {
            /*final MediaPlayer mMediaPlayer = new MediaPlayer();
            try {
                mMediaPlayer.setDataSource(context, alert);
                final AudioManager audioManager = (AudioManager) context
                        .getSystemService(Context.AUDIO_SERVICE);
                if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                    mMediaPlayer.prepare();
                    CountDownTimer cdt = new CountDownTimer(3000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            mMediaPlayer.start();
                        }

                        public void onFinish() {
                            mMediaPlayer.stop();
                        }
                    };
                    cdt.start();
                }
            } catch (IOException e) {
                System.out.println("OOPS");
            }*/
            final MediaPlayer player = new MediaPlayer();
            try {
                player.setDataSource(context, alert);
                final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

                if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                        player.setAudioStreamType(AudioManager.STREAM_ALARM);
                        player.setLooping(false);
                        player.prepare();
                        player.start();
                        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            public void onCompletion(MediaPlayer mp) {
                                player.release();
                            }
                        });
                }
            }
            catch (IOException e) {
                System.out.println("OOPS");
            }
        }

        if (random.equals("true") && Build.VERSION.SDK_INT >= 19) {
            final AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            Intent randomIntent = new Intent(context, AlarmReceiver.class);
            Bundle nextExtras = new Bundle();
            nextExtras.putString("SONG_ID", song);
            nextExtras.putString("RANDOM_BOOL", "true");
            randomIntent.putExtras(extras);
            PendingIntent alarmPI = PendingIntent.getBroadcast(context.getApplicationContext(),0,randomIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, alarmPI);
        }
    }
}