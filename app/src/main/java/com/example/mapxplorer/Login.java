package com.example.mapxplorer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void click(View view) {
        EditText login = (EditText) findViewById(R.id.editTextTextPersonName);
        EditText password = (EditText) findViewById(R.id.editTextTextPassword);
        String logCheck = String.valueOf(login.getText());
        String passCheck = String.valueOf(password.getText());
        if(logCheck.equals("1111") && passCheck.equals("1234")) {
            Toast.makeText(this,"Welcome " + logCheck ,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this,"Incorrect pass",Toast.LENGTH_LONG).show();
            login.setText("") ;
            password.setText("");
        }
    }
}