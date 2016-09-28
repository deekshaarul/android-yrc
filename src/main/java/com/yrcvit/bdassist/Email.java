package com.yrcvit.bdassist;

/**
 * Created by user pc on 24-Mar-16.
 */
import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Email extends AppCompatActivity implements View.OnClickListener {

    EditText personsEmail;
    String emailAdd;
    Button sendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);
        setContentView(R.layout.email);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeVars();
        sendEmail.setOnClickListener(this);
    }

    private void initializeVars() {
        // TODO Auto-generated method stub
        personsEmail = (EditText) findViewById(R.id.etEmails);
        sendEmail = (Button) findViewById(R.id.bSentEmail);
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub

        convertEditTextVarsIntoStringsAndYesThisIsAMethodWeCreated();
        String emailaddress[] = { emailAdd };
        String message = "Hi!\nI wanted to share the YRC-VIT BD Assist application with you.\n\n\nYou can download the same using the"
                +" following link: \n\n<link address> ";
        Intent emailIntent=new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,emailaddress);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Share BD Assist");
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,message);
        startActivity(emailIntent);

    }

    private void convertEditTextVarsIntoStringsAndYesThisIsAMethodWeCreated() {
        // TODO Auto-generated method stub
        emailAdd = personsEmail.getText().toString();


    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

}
