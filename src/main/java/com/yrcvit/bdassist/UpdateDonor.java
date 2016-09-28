package com.yrcvit.bdassist;

import android.app.ActionBar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class UpdateDonor extends AppCompatActivity implements
        View.OnClickListener {

    private Button btnSubmit;
    private ProgressDialog pDialog;
    private JSONObject json;
    private int success=0;
    private HTTPURLConnection service;
    private String  strMobile ="",strDate="";
    //Initialize webservice URL
    private String path = "http://www.yrcvit.com/android/update_donor.php";

    private EditText etMobile;
    EditText d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_donor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etMobile = (EditText) findViewById(R.id.donor_phno);
        d = (EditText) findViewById(R.id.d_date);
        etMobile.addTextChangedListener(phone_watcher);
        d.setOnClickListener(this);
        service=new HTTPURLConnection();
        btnSubmit = (Button) findViewById(R.id.update);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etMobile.getText().toString().equals("")&& !d.getText().toString().equals("")) {

                    strMobile = etMobile.getText().toString();

                    strDate=d.getText().toString();
                    //Call WebService
                    new PostDataTOServer().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter all fields", Toast.LENGTH_LONG).show();

                }
            }
        });
    }


    public void onClick(View v) {



        // Process to get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        strDate = mDay+"-"+ mMonth+"-"+mYear  ;
        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in EditText
                        d.setText(dayOfMonth + "-"
                                + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        dpd.show();

    }
    private TextWatcher phone_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        String no=s.toString();
            if(no.length()>10)
            {
                etMobile.setError("Should have 10 digits only!");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            String no=s.toString();
            if(no.length()<10&&no.length()>0)
            {
                etMobile.setError("Should have 10 digits!");
            }
        }
    };

private class PostDataTOServer extends AsyncTask<Void, Void, Void> {

    String response = "";
    //Create hashmap Object to send parameters to web service
    HashMap<String, String> postDataParams;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pDialog = new ProgressDialog(UpdateDonor.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
    }
    @Override
    protected Void doInBackground(Void... arg0) {
        postDataParams=new HashMap<String, String>();

        postDataParams.put("mobile", strMobile);

        postDataParams.put("date", strDate);

        //Call ServerData() method to call webservice and store result in response
        response= service.ServerData(path,postDataParams);
        try {
            json = new JSONObject(response);
            //Get Values from JSONobject
            System.out.println("success=" + json.get("success"));
            success = json.getInt("success");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if (pDialog.isShowing())
            pDialog.dismiss();
        if(success==1) {
            Toast.makeText(getApplicationContext(), "Donor Updated successfully!", Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(), DonorMain.class);
            startActivity(i);
        }
        else
            Toast.makeText(getApplicationContext(), "Server Error!", Toast.LENGTH_LONG).show();
    }
}
    @Override
    public void onBackPressed() {

            super.onBackPressed();
            startActivity(new Intent(UpdateDonor.this, DonorMain.class));
            finish();

    }
}
