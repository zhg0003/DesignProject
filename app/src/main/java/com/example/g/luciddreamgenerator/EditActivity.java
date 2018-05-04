package com.example.g.luciddreamgenerator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;


public class EditActivity extends AppCompatActivity {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences settings = getApplicationContext().getSharedPreferences("editingDream", 0);
        String dreamToBeEdited = settings.getString("editDream", "");

        editText = (EditText) findViewById(R.id.editText);
        editText.setText(dreamToBeEdited);

        setUpSaveButton();
        setUpCancelButton();

    }

    public void setUpSaveButton(){
        Button saveButton = (Button) findViewById(R.id.button11);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getApplicationContext().getSharedPreferences("editingDream", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("editDream", editText.getText().toString());
                editor.apply();
                startActivity(new Intent(EditActivity.this, JournalListActivity.class));

            }
        });
    }

    public void setUpCancelButton(){
        Button cancelButton = (Button) findViewById(R.id.button12);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getApplicationContext().getSharedPreferences("editingDreamIndex", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("editDreamIndex", -1);
                editor.apply();
                startActivity(new Intent(EditActivity.this, JournalListActivity.class));

            }
        });
    }

}
