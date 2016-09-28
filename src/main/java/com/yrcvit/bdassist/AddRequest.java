package com.yrcvit.bdassist;

import android.app.ActionBar;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import android.widget.Toast;



import java.util.HashMap;
import android.support.v7.app.AppCompatActivity;

import android.widget.ArrayAdapter;

import android.widget.Spinner;


public class AddRequest extends AppCompatActivity  implements
        View.OnClickListener {
    private EditText pname,etMobile, hname ,age, reason , units ,date;
    RadioGroup donated;
    private Button btnSubmit;
    private ProgressDialog pDialog;
    private JSONObject json;
    private int success=0;
    private HTTPURLConnection service;
    private String strname ="", strMobile ="",strhname="",strAge ="", strReason ="",strUnits="",strDate="",strType="",strGroup="";
    //Initialize webservice URL
    private String path = "http://www.yrcvit.com/android/insert_request.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.add_request);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        // spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("A+");
        categories.add("A-");
        categories.add("B+");
        categories.add("B-");
        categories.add("AB+");
        categories.add("AB-");
        categories.add("O+");
        categories.add("O-");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        pname= (EditText) findViewById(R.id.pname);
        age= (EditText) findViewById(R.id.age);
        reason= (EditText) findViewById(R.id.reason);
        units= (EditText) findViewById(R.id.units);
        etMobile= (EditText) findViewById(R.id.phno);
        date= (EditText) findViewById(R.id.date);
        hname= (EditText) findViewById(R.id.hname);
        btnSubmit= (Button) findViewById(R.id.addReq);
        date.setOnClickListener(this);
        donated = (RadioGroup) findViewById(R.id.dona);
        strGroup= spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();


        //Initialize HTTPURLConnection class object
        service=new HTTPURLConnection();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = donated.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton r = (RadioButton) findViewById(selectedId);

                if (!pname.getText().toString().equals("") && !etMobile.getText().toString().equals("")&& !age.getText().toString().equals("")&& !reason.getText().toString().equals("")
                        && !units.getText().toString().equals("")&& !hname.getText().toString().equals("")&& !date.getText().toString().equals("")) {
                    strname = pname.getText().toString();
                    strMobile = etMobile.getText().toString();
                    strAge = age.getText().toString();
                    strReason = reason.getText().toString();
                    strUnits = units.getText().toString();
                    strhname = hname.getText().toString();
                    strDate=date.getText().toString();
                    strType=r.getText().toString();
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
            strDate = mYear +"-"+ mMonth+"-"+ + mDay ;
            // Launch Date Picker Dialog
            DatePickerDialog dpd = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            // Display Selected date in EditText
                            date.setText(dayOfMonth + "-"
                                    + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            dpd.show();

    }
    private class PostDataTOServer extends AsyncTask<Void, Void, Void> {

        String response = "";
        //Create hashmap Object to send parameters to web service
        HashMap<String, String> postDataParams;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(AddRequest.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            postDataParams=new HashMap<String, String>();
            postDataParams.put("pname", strname);
            postDataParams.put("mobile", strMobile);
            postDataParams.put("hname", strhname);
            postDataParams.put("age", strAge);
            postDataParams.put("units", strUnits);
            postDataParams.put("reason", strReason);
            postDataParams.put("date", strDate);
            postDataParams.put("type", strType);
            postDataParams.put("group", strGroup);
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

                Toast.makeText(getApplicationContext(), "Request Added Successfully!", Toast.LENGTH_LONG).show();

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
            else
                Toast.makeText(getApplicationContext(), "Server Error!", Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public void onBackPressed() {

            super.onBackPressed();
            startActivity(new Intent(AddRequest.this, MainActivity.class));
            finish();

    }
}