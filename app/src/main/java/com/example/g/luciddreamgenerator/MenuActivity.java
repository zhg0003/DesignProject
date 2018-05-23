package com.example.g.luciddreamgenerator;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.ActionBar;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        /* Set up buttons */
        setUpAudioButton();
        setUpJournalButton();
        setUpTrainingButton();
        //setUpHelpButton();
        setUpSyncButton();
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
        final boolean loggedIn = ((LucidApp) getApplication()).getLogged();
        if (loggedIn) {
            b.setText("Desync from Website");
        }
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loggedIn) {
                    startActivity(new Intent(MenuActivity.this, LogoutActivity.class));
                }
                else {
                    startActivity(new Intent(MenuActivity.this, LoginActivity.class));
                }
            }
        });
    }


}
