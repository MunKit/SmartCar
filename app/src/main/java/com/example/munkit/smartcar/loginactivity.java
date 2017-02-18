package com.example.munkit.smartcar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import static android.R.color.holo_red_light;

public class loginactivity extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        final Button button1 = (Button)findViewById(R.id.button);

        button1.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){

                        TextView respondtext = (TextView) findViewById(R.id.respondtext);
                        EditText username = (EditText) findViewById(R.id.usereditText);
                        EditText password = (EditText) findViewById(R.id.passeditText);
                        String user = username.getText().toString();
                        String pass = password.getText().toString();
                        if (user.equals("User") && pass.equals("pass"))
                        {
                            Intent intent = new Intent(loginactivity.this, MainActivity.class);
                            startActivity(intent);
                            respondtext.setText("");
                        }
                        else
                        {
                            respondtext.setText("Wrong password or username");
                            respondtext.setTextColor(getResources().getColor(holo_red_light));

                        }

                    }
                });
    }


}
