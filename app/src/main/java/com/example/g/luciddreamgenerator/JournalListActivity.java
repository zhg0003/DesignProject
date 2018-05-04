package com.example.g.luciddreamgenerator;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.KeyEvent;
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
    List<String> dreams = new LinkedList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);

        setUpBackButton();
        setUpEditButton();
        setUpDeleteButton();

        loadDreams(); // Here we load dreams from local storage into ram

        if (getIntent().getStringExtra("DREAM_CONTENT") != null) { // If we just wrote down a new dream, get that new dream
            String new_dream = getIntent().getStringExtra("DREAM_CONTENT");
            dreams.add(new_dream);
        }

        SharedPreferences settings = getApplicationContext().getSharedPreferences("editingDream", 0);
        SharedPreferences settings2 = getApplicationContext().getSharedPreferences("editingDreamIndex", 0);

        int editedIndex = settings2.getInt("editDreamIndex", -1);
        if (editedIndex >= 0) { // dream was edited
            dreams.set(editedIndex, settings.getString("editDream", ""));
            SharedPreferences.Editor editor = settings2.edit();
            editor.putInt("editDreamIndex", -1);
            editor.apply();

        }

        String[] dreams_array = new String[dreams.size()]; // The rest of onCreate sets up dream_list
        dreams_array = dreams.toArray(dreams_array);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, dreams_array);

        dream_list = getListView();
        dream_list.setAdapter(adapter);

    }
    

    protected void loadDreams() {
        String[] temp_loaded_dreams;
        SharedPreferences settings = getApplicationContext().getSharedPreferences("stored_dreams", 0);
        String loaded_dreams = settings.getString("dreams", "");
        temp_loaded_dreams = loaded_dreams.split("~>_");
        dreams = new ArrayList<String>(Arrays.asList(temp_loaded_dreams));

        if (temp_loaded_dreams[0].isEmpty()) // remove initial empty dream
            dreams.clear();
    }


    protected void saveDreams() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("stored_dreams", 0);
        SharedPreferences.Editor editor = settings.edit();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dreams.size(); i++) {
            sb.append(dreams.get(i)).append("~>_");
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
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            saveDreams();
            startActivity(new Intent(JournalListActivity.this, JournalActivity.class));
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
     *  When you click on an item (dream), highlight it, and set its setSelected value to true
     */
    @Override
    protected void onListItemClick(ListView dream_list, View v, int position, long id) { // Position is 0 indexed
        //dreams.remove(position);

        Button deleteButton = (Button) findViewById(R.id.deleteBtn);
        Button editButton = (Button) findViewById(R.id.editBtn);
        editButton.setAlpha(1.0f);
        deleteButton.setAlpha(1.0f);
        editButton.setClickable(true);
        deleteButton.setClickable(true);

        for (int i = 0; i < dream_list.getCount(); i++) {
            dream_list.getChildAt(i).setBackgroundColor(Color.WHITE);
            dream_list.getChildAt(i).setSelected(false);
            dream_list.getChildAt(i).setAlpha(1.0f);
        }
        //this.dream_list
        dream_list.getChildAt(position).setBackgroundColor(Color.LTGRAY);
        dream_list.getChildAt(position).setSelected(true);
        dream_list.getChildAt(position).setAlpha(0.9f);
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

    public void setUpEditButton(){
        Button editButton = (Button) findViewById(R.id.editBtn);
        editButton.setAlpha(0.5f);
        editButton.setClickable(false);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getApplicationContext().getSharedPreferences("editingDream", 0);
                SharedPreferences settings2 = getApplicationContext().getSharedPreferences("editingDreamIndex", 0);

                SharedPreferences.Editor editor = settings.edit();
                SharedPreferences.Editor editor2 = settings2.edit();

                for (int i = 0; i < dream_list.getCount(); i++)
                {
                    if (dream_list.getChildAt(i).getAlpha() == 0.9f) {
                        String dreamToBeEdited = dreams.get(i);
                        editor.putString("editDream", dreamToBeEdited);
                        editor2.putInt("editDreamIndex", i);
                        editor.apply();
                        editor2.apply();
                    }
                }
                saveDreams();
                startActivity(new Intent(JournalListActivity.this, EditActivity.class));

            }
        });
    }

    public void setUpDeleteButton(){
        Button deleteButton = (Button) findViewById(R.id.deleteBtn);
        deleteButton.setAlpha(0.5f);
        deleteButton.setClickable(false);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder deleteAlert = new AlertDialog.Builder(JournalListActivity.this);
                deleteAlert.setMessage("Are you sure you want to delete this dream?").setCancelable(false);
                deleteAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDream();
                    }
                });

                deleteAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert = deleteAlert.create();
                alert.setTitle("Delete dream");
                alert.show();
            }
        });
    }


    public void deleteDream(){

        //String item = String.valueOf(dream_list.getCount());
        //Toast.makeText(this, item + " number of items", Toast.LENGTH_LONG).show();

        for (int i = 0; i < dream_list.getCount(); i++)
        {
            if (dream_list.getChildAt(i).getAlpha() == 0.9f) {
                dream_list.getChildAt(i).setBackgroundColor(Color.RED);
                dreams.remove(i);
                saveDreams();
                finish();
                startActivity(getIntent());
            }
        }
    }
}
