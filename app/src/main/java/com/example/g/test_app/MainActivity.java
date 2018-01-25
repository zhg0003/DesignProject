package com.example.g.test_app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    EditText num1;
    EditText num2;
    TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        num1 = (EditText)findViewById(R.id.num1);
        num2 = (EditText)findViewById(R.id.num2);
        result = (TextView)findViewById(R.id.result);
        Button add = (Button)findViewById(R.id.add); //casting as a button, telling the code that this view is button, not textview etc
        Button sub = (Button)findViewById(R.id.sub);
        Button mul = (Button)findViewById(R.id.mul);
        Button div = (Button)findViewById(R.id.div);

        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(num1.getText().toString().equals("") | num2.getText().toString().equals(""))
                    result.setText("please fill in the number field");
                else {
                    float a = Float.parseFloat(num1.getText().toString());
                    float b = Float.parseFloat(num2.getText().toString());
                    float r = a + b;
                    result.setText(Float.toString(r));
                }

                //Log.d("debug msg","num1 is null "+ (num1.getText().toString().equals("")));
            }
        });

        sub.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(num1.getText().toString().equals("") | num2.getText().toString().equals(""))
                    result.setText("please fill in the number field");
                else {
                    float a = Float.parseFloat(num1.getText().toString());
                    float b = Float.parseFloat(num2.getText().toString());
                    float r = a - b;
                    result.setText(Float.toString(r));
                }
            }
        });

        mul.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(num1.getText().toString().equals("") | num2.getText().toString().equals(""))
                    result.setText("please fill in the number field");
                else {
                    float a = Float.parseFloat(num1.getText().toString());
                    float b = Float.parseFloat(num2.getText().toString());
                    float r = a * b;
                    result.setText(Float.toString(r));
                }
            }
        });

        div.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(num1.getText().toString().equals("") | num2.getText().toString().equals(""))
                    result.setText("please fill in the number field");
                else {
                    float a = Float.parseFloat(num1.getText().toString());
                    float b = Float.parseFloat(num2.getText().toString());
                    float r = a / b;
                    result.setText(Float.toString(r));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
