package com.example.g.luciddreamgenerator;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.ArrayList;



import java.util.List;
import java.util.LinkedList;



public class JournalListActivity extends ListActivity {

    private ListView dream_list;
    List<String> dreams = new LinkedList<String>();
    ArrayAdapter<String> adapter;

    boolean isLoggedin = false;
    String username;


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);

        try {
            isLoggedin = ((LucidApp) getApplication()).getLogged();
            username = user.getEmail();
        }
        catch (NullPointerException e) {}

        if (isLoggedin) // Here we load dreams from firebase
            loadDatabaseDreams();
        else
            loadDreams(); // Here we load dreams from local storage

        setUpEditButton();
        setUpDeleteButton();



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


    protected void loadDatabaseDreams() {
        String[] temp_loaded_dreams;
        SharedPreferences settings = getApplicationContext().getSharedPreferences("database_dreams", 0);
        String loaded_dreams = settings.getString("database_dreams", "");
        temp_loaded_dreams = loaded_dreams.split("~>_");
        dreams = new ArrayList<String>(Arrays.asList(temp_loaded_dreams));

        if (temp_loaded_dreams[0].isEmpty()) // remove initial empty dream
            dreams.clear();
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
            if (!isLoggedin)
                saveDreams();

            startActivity(new Intent(JournalListActivity.this, MenuActivity.class));
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
        super.onListItemClick(dream_list, v, position, id);
        Button deleteButton = (Button) findViewById(R.id.deleteBtn);
        Button editButton = (Button) findViewById(R.id.editBtn);
        editButton.setAlpha(1.0f);
        deleteButton.setAlpha(1.0f);
        editButton.setEnabled(true);
        deleteButton.setEnabled(true);

        /*for (int i = 0; i < getListView().getChildCount() - 1; i++) {
            getListAdapter().getView(i, v, null).setBackgroundColor(Color.TRANSPARENT);
            getListAdapter().getView(i, v, null).setSelected(false);
            getListAdapter().getView(i, v, null).setAlpha(1.0f);
        }
        //this.dream_list
        Toast.makeText(getBaseContext(), "position: " + getListView().getChildCount(), Toast.LENGTH_SHORT).show();
        getListAdapter().getView(position, v, null).setBackgroundColor(Color.parseColor("#66ACACAC"));
        getListAdapter().getView(position, v, null).setSelected(true);
        getListAdapter().getView(position, v, null).setAlpha(0.9f);
        //saveDreams();

        //startActivity(getIntent());*/
    }



    public void setUpEditButton(){
        Button editButton = (Button) findViewById(R.id.editBtn);
        editButton.setAlpha(0.5f);
        editButton.setEnabled(false);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggedin) {
                    Toast.makeText(JournalListActivity.this, "Edit/Delete functionality for remotely stored dreams coming soon...", Toast.LENGTH_LONG).show();
                }
                else {
                    SharedPreferences settings = getApplicationContext().getSharedPreferences("editingDream", 0);
                    SharedPreferences settings2 = getApplicationContext().getSharedPreferences("editingDreamIndex", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    SharedPreferences.Editor editor2 = settings2.edit();
                    //for (int i = 0; i < dream_list.getChildCount(); i++) {
                    //if (dream_list.getChildAt(i).equals(selectedView)) {
                    String dreamToBeEdited = dreams.get(dream_list.getCheckedItemPosition());
                    editor.putString("editDream", dreamToBeEdited);
                    editor2.putInt("editDreamIndex", dream_list.getCheckedItemPosition());
                    editor.apply();
                    editor2.apply();
                    //}
                    //}
                    saveDreams();
                    startActivity(new Intent(JournalListActivity.this, EditActivity.class));
                }

            }
        });
    }

    public void setUpDeleteButton(){
        Button deleteButton = (Button) findViewById(R.id.deleteBtn);
        deleteButton.setAlpha(0.5f);
        deleteButton.setEnabled(false);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoggedin){
                    Toast.makeText(JournalListActivity.this, "Edit/Delete functionality for remotely stored dreams coming soon...", Toast.LENGTH_LONG).show();
                }
                else {
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
            }
        });
    }


    public void deleteDream(){

        //String item = String.valueOf(dream_list.getCount());
        //Toast.makeText(this, item + " number of items", Toast.LENGTH_LONG).show();
        //for (int i = 0; i < dream_list.getChildCount(); i++) {
            //if (dream_list.getChildAt(i).equals(selectedView)) {
        //dream_list.getChildAt(dream_list.getCheckedItemPosition()).setBackgroundColor(Color.RED);
        dreams.remove(dream_list.getCheckedItemPosition());
        saveDreams();
        finish();
        startActivity(getIntent());
        //    }
        //}
    }
}
