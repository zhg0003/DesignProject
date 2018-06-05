package com.example.g.luciddreamgenerator;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.ActionBar;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    boolean isLoggedin = false;
    String username;
    List<String> dreams = new LinkedList<String>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        try {
            isLoggedin = ((LucidApp) getApplication()).getLogged();
            username = user.getEmail();
        }
        catch (NullPointerException e) {}

        /* Set up buttons */
        setUpAudioButton();
        setUpJournalButton();
        setUpTrainingButton();
        setUpSyncButton();
        setUpLoginText();

        if (isLoggedin)
            syncWithDatabase();
    }

    protected void syncWithDatabase(){
        // Toast.makeText(this, "syncing database", Toast.LENGTH_LONG).show();
        dreams = new ArrayList<String>();

        db.collection("USERS/" + username + "/records/")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String date = document.get("date").toString();
                                String exp = document.get("exp").toString();
                                String freq1 = document.get("freq1").toString();
                                String freq2 = document.get("freq2").toString();
                                String sound1 = document.get("sound1").toString();
                                String sound2 = document.get("sound2").toString();

                                String dream = date;
                                dream = dream.concat(" - ");
                                dream = dream.concat(exp + '\n');
                                dream = dream.concat("Sound(s) Played During Dream:" + '\n');
                                dream = dream.concat(" -Tone at " + freq1 + "hz" + '\n');
                                dream = dream.concat(" -Tone at " + freq2 + "hz" + '\n');

                                dreams.add(dream);


                                /*
                                Snackbar snackbar;
                                snackbar = Snackbar.make(getWindow().getDecorView().getRootView(),
                                        document.getId() + " => " + document.get("date"),
                                        10000);
                                View snackbarView = snackbar.getView();
                                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setMaxLines(5);
                                snackbar.show();
                                                    */
                            }
                        } else {
                            Snackbar snackbar;
                            snackbar = Snackbar.make(getWindow().getDecorView().getRootView(),
                                    "Error getting documents.",
                                    10000);
                            View snackbarView = snackbar.getView();
                            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setMaxLines(5);
                            snackbar.show();
                        }

                        SharedPreferences settings = getApplicationContext().getSharedPreferences("database_dreams", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < dreams.size(); i++) {
                            sb.append(dreams.get(i)).append("~>_");
                        }
                        editor.putString("database_dreams", sb.toString());
                        editor.apply();

                    }
                });
    }

    public void setUpAudioButton() {
        Button b = (Button) findViewById(R.id.button5);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, MainActivity.class));
            }
        });
    }

    public void setUpJournalButton() {
        Button b = (Button) findViewById(R.id.button6);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, JournalListActivity.class));
            }
        });
    }

    public void setUpTrainingButton() {
        Button b = (Button) findViewById(R.id.button7);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, ToolsActivity.class));
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*public void setUpHelpButton(){
        Button b = (Button) findViewById(R.id.button8);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Help option coming in version 2", Toast.LENGTH_LONG).show();
            }
        });
    }*/

    public void setUpSyncButton(){
        Button b = (Button) findViewById(R.id.button13);
        if (isLoggedin) {
            b.setText("Logout");
        }
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggedin) {
                    startActivity(new Intent(MenuActivity.this, LogoutActivity.class));
                }
                else {
                    startActivity(new Intent(MenuActivity.this, LoginActivity.class));
                }
            }
        });
    }

    public void setUpLoginText(){
        TextView loginTxt = (TextView) findViewById(R.id.loginTxt);
        if (isLoggedin) {
            loginTxt.setText(username + " is logged in");
        }
        else {
            loginTxt.setText("");
        }
    }

}
