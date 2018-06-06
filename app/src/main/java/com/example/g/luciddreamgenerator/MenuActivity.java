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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {

    boolean isLoggedin = false;
    boolean recentlyLogged = false;
    String username;
    String currentDate;
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
            recentlyLogged = ((LucidApp) getApplication()).getRecentlyLogged();
            username = user.getEmail();
        }
        catch (NullPointerException e) {}

        /* Set up buttons */
        setUpAudioButton();
        setUpJournalButton();
        setUpTrainingButton();
        setUpSyncButton();
        setUpLoginText();

        if (recentlyLogged)
            pushCurrentDreams();

        if (isLoggedin)
            syncWithDatabase();

        try {
            ((LucidApp) getApplication()).setRecentlyLogged(false);
        }
        catch (NullPointerException e) {}
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
                                dream = dream.concat(sound1 + " at " + freq1 + "hz" + '\n');
                                dream = dream.concat(sound2 + " at " + freq2 + "hz" + '\n');

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

    protected void pushCurrentDreams() {
        String[] temp_loaded_dreams;
        List<String> local_dreams = new LinkedList<String>();
        SharedPreferences settings = getApplicationContext().getSharedPreferences("stored_dreams", 0);
        String loaded_dreams = settings.getString("dreams", "");
        temp_loaded_dreams = loaded_dreams.split("~>_");
        local_dreams = new ArrayList<String>(Arrays.asList(temp_loaded_dreams));

        if (temp_loaded_dreams[0].isEmpty()) // remove initial empty dream
            local_dreams.clear();
        else {
            try {
                for (int c = 0; c < local_dreams.size(); c++) {
                    Map<String, Object> data = new HashMap<>();
                    String[] split_dream_entry = local_dreams.get(c).split("\n- ");
                    for (int i = 0; i < split_dream_entry.length; i++) {
                        if (i == 0) {
                            data.put("date", split_dream_entry[i].substring(1));
                            currentDate = split_dream_entry[i].substring(1);
                        } else if (i == 1) {
                            data.put("exp", split_dream_entry[i]);
                        } else if (i == 2) {
                            String[] split_tones = split_dream_entry[i].split("\n  *");
                            String[] split_first_tone = split_tones[1].split(" ");
                            data.put("sound1", split_first_tone[0]);
                            data.put("freq1", split_first_tone[2].substring(0, split_first_tone[2].length() - 2));
                            if (split_tones.length > 2) {
                                String[] split_second_tone = split_tones[2].split(" ");
                                data.put("sound2", split_second_tone[0]);
                                data.put("freq2", split_second_tone[2].substring(0, split_second_tone[2].length() - 2));
                            }
                            else {
                                data.put("sound2", " ");
                                data.put("freq2", " ");
                            }
                        } else if (i == 3 && split_dream_entry[i].substring(0, 13).compareTo("Custom Fields") == 0) {
                            String[] split_custom = split_dream_entry[i].split("\n  *");
                            data.put("tags", split_custom[1]);

                        } else if (i == 4 || split_dream_entry[i].substring(0, 13).compareTo("Dream Quality") == 0) {
                            String[] split_rating = split_dream_entry[i].split(" ");
                            data.put("rating", split_rating[2]);
                        }
                    }
                    insertDb(data);
                }
            }
            catch(NullPointerException e){
                Snackbar snackbar;
                snackbar = Snackbar.make(getWindow().getDecorView().getRootView(),
                        "Error pushing journal",
                        10000);
                View snackbarView = snackbar.getView();
                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setMaxLines(5);
                snackbar.show();
            }
        }
    }
    protected void insertDb(Map<String, Object> userData) {
        // Add a new document with a generated ID
        db.collection("USERS/" + username + "/records/").document(currentDate)
                .set(userData)

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar snackbar;
                        snackbar = Snackbar.make(getWindow().getDecorView().getRootView(),
                                "Error adding document",
                                10000);
                        View snackbarView = snackbar.getView();
                        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setMaxLines(5);
                        snackbar.show();
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
