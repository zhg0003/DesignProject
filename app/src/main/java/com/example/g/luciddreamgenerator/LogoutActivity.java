package com.example.g.luciddreamgenerator;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LogoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        getWindow().setBackgroundDrawableResource(R.drawable.background2);


        Button logoutButton = (Button) findViewById(R.id.logoutBtn);
        Button cancelButton = (Button) findViewById(R.id.cancelBtn);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                ((LucidApp) getApplication()).setLogged(false);
                startActivity(new Intent(LogoutActivity.this, MenuActivity.class));
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogoutActivity.this, MenuActivity.class));
            }
        });
    }
}
