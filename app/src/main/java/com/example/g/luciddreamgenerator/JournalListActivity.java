package com.example.g.luciddreamgenerator;


import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.LinkedList;


public class JournalListActivity extends ListActivity {

    private ListView dream_list;
    //String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    List<String> dreams = new LinkedList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setUpBackButton();

        if (getIntent().getStringExtra("DREAM_CONTENT") != null) {
            String new_dream = getIntent().getStringExtra("DREAM_CONTENT");
            dreams.add(new_dream);
        }
        if (dreams.size() < 1) {
            String empty = "Empty";
            dreams.add(empty);
        }
        String[] dreams_array = new String[dreams.size()];
        dreams_array = dreams.toArray(dreams_array);

        //String[] dreams_array = {"Empty"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, dreams_array);

        //dream_list = (ListView) findViewById(R.id.list);
        dream_list = getListView();
        dream_list.setAdapter(adapter);

    }
/*
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
    }
*/
    @Override
    protected void onListItemClick(ListView dream_list, View v, int position, long id) {
        TextView temp = (TextView) v;
        Toast.makeText(this, "" + temp.getText() + " " + position, Toast.LENGTH_SHORT).show();
    }

    public void setUpBackButton() {
        Button back_button = (Button) findViewById(R.id.backBtn);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(JournalListActivity.this, MainActivity.class));
            }
        });
    }
}
