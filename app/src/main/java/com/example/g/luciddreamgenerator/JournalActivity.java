package com.example.g.luciddreamgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class JournalActivity extends AppCompatActivity {

    EditText myEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpSaveButton();
        setUpCancelButton();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void setUpCancelButton() {
        Button cancel_button = (Button) findViewById(R.id.button4);

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(JournalActivity.this, MainActivity.class));
            }
        });
    }

    public void setUpSaveButton() {
        Button save_button = (Button) findViewById(R.id.button3);

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myEdit = (EditText)findViewById(R.id.editText2);
                String content = myEdit.getText().toString();
                if (content.isEmpty())
                {
                    Toast.makeText(getBaseContext(), "Can't Save Empty Dream Log!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getBaseContext(), JournalListActivity.class);

                intent.putExtra("DREAM_CONTENT", content);
                startActivity(intent);
                //startActivity(new Intent(JournalActivity.this, MainActivity.class));
            }
        });
    }

}
