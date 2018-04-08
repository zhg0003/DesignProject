package com.example.g.luciddreamgenerator;


import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import java.util.Arrays;
import java.util.ArrayList;


import java.util.List;
import java.util.LinkedList;


public class JournalListActivity extends ListActivity {

    private ListView dream_list;
    //String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    List<String> dreams = new LinkedList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setUpBackButton();
        loadDreams();

        if (getIntent().getStringExtra("DREAM_CONTENT") != null) {
            String new_dream = getIntent().getStringExtra("DREAM_CONTENT");
            dreams.add(new_dream);
        }

        /*if (dreams.size() < 1) {
            String empty = "Dream Journal";
            dreams.add(empty);
        }*/

        String[] dreams_array = new String[dreams.size()];
        dreams_array = dreams.toArray(dreams_array);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, dreams_array);

        //dream_list = (ListView) findViewById(R.id.list);
        dream_list = getListView();
        dream_list.setAdapter(adapter);

    }


    protected void loadDreams() {
        String[] temp_loaded_dreams;
        SharedPreferences settings = getApplicationContext().getSharedPreferences("stored_dreams", 0);
        String loaded_dreams = settings.getString("dreams", "");
        temp_loaded_dreams = loaded_dreams.split("~");
        dreams = new ArrayList<String>(Arrays.asList(temp_loaded_dreams));

        if (temp_loaded_dreams[0].isEmpty()) // remove initial empty dream
            dreams.clear();
    }


    protected void saveDreams() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("stored_dreams", 0);
        SharedPreferences.Editor editor = settings.edit();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dreams.size(); i++) {
            sb.append(dreams.get(i)).append("~");
        }
        editor.putString("dreams", sb.toString());
        editor.apply();
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
        //dreams.remove(position);


        //saveDreams();


        //startActivity(getIntent());


    }

    public void setUpBackButton() {
        Button back_button = (Button) findViewById(R.id.backBtn);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDreams();
                startActivity(new Intent(JournalListActivity.this, MenuActivity.class));
            }
        });
    }
}
