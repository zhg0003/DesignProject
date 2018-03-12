package com.example.g.luciddreamgenerator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.*;
import java.util.Date;
import java.util.Calendar;


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
                CheckBox isLucid = findViewById(R.id.checkBox);
                RatingBar dreamRating = (RatingBar) findViewById(R.id.ratingBar);

                if (content.isEmpty())
                {
                    Toast.makeText(getBaseContext(), "Can't Save Empty Dream Log!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getBaseContext(), JournalListActivity.class);

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDream = df.format(c);
                Toast.makeText(getBaseContext(), formattedDream, Toast.LENGTH_SHORT).show();

                formattedDream = formattedDream.concat(" - \"");
                formattedDream = formattedDream.concat(content);
                formattedDream = formattedDream.concat("\"");

                if (isLucid.isChecked())
                    formattedDream = formattedDream.concat("\nLUCID");
                else
                    formattedDream = formattedDream.concat("\nNOT LUCID");

                if (dreamRating.getRating() != 0)
                    formattedDream = formattedDream.concat("\nDream Quality: " + String.valueOf(dreamRating.getRating()) + " stars.");


                intent.putExtra("DREAM_CONTENT", formattedDream);
                startActivity(intent);
                //startActivity(new Intent(JournalActivity.this, MainActivity.class));
            }
        });
    }

}
