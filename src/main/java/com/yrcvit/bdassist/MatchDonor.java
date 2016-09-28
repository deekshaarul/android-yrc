package com.yrcvit.bdassist;

import android.app.ActionBar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
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
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MatchDonor extends AppCompatActivity {


    private String strGen="",strGroup="";

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> requestsList;
    private ProgressDialog pDialog;
    private JSONObject json;
    private int success=0;
    private HTTPURLConnection service;
    // url to get all requests list
    private static String url_all_donors = "http://www.yrcvit.com/android/match_donor.php";
    private static String path = "http://www.yrcvit.com/android/match_donor.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MATCH = "match";
    private static final String TAG_NAME = "name";

    private static final String TAG_MOBILE = "mobile";

    private static final String TAG_GEN = "gender";
    private static final String TAG_GROUP = "bg";

    public ListView mainListView;
    // requests JSONArray
    JSONArray requests = null;
    private SwipeRefreshLayout swipeContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_donor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        RadioGroup donated = (RadioGroup) findViewById(R.id.gender);

        int id = donated.getCheckedRadioButtonId();

        if (id == R.id.m){
            strGen = "male";
        }
        else if (id == R.id.b){
            strGen = "both";
        }
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

        strGroup= spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();


        Button btnSubmit = (Button) findViewById(R.id.match);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setContentView(R.layout.donor_content);
                mainListView = (ListView) findViewById(R.id.llist);
                // Hashmap for ListView
                requestsList = new ArrayList<HashMap<String, String>>();
                new PostDataTOServer().execute();

                // Loading requests in Background Thread
                //new LoadAllrequests().execute();
                swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

                swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // Your code to refresh the list here.
                        // Make sure you call swipeContainer.setRefreshing(false)
                        // once the network request has completed successfully.
                        //new LoadAllrequests().execute();
                        new PostDataTOServer().execute();
                        swipeContainer.setRefreshing(false);
                    }
                });
                // Configure the refreshing colors
                swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                        android.R.color.holo_green_light,
                        android.R.color.holo_orange_light,
                        android.R.color.holo_red_light);


            }
        });


    }

     private class PostDataTOServer extends AsyncTask<Void, Void, Void> {

        String response = "";
        //Create hashmap Object to send parameters to web service
        HashMap<String, String> postDataParams;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MatchDonor.this);
            pDialog.setMessage("Loading Requests. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
         @Override
        protected Void doInBackground(Void... arg0) {
             List<NameValuePair> params = new ArrayList<NameValuePair>(2);
             params.add(new BasicNameValuePair("bg", strGroup));
             params.add(new BasicNameValuePair("gen", strGen));



            //Call ServerData() method to call webservice and store result in response
            //response = service.ServerData(path, postDataParams);
             json = jParser.makeHttpRequest(path, "GET", params);
             Log.d("All requests: ", json.toString());
            try {

                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // requests found
                    // Getting Array of requests
                    requests = json.getJSONArray(TAG_MATCH);

                    // looping through All requests
                    for (int i = 0; i < requests.length(); i++) {
                        JSONObject c = requests.getJSONObject(i);

                        // Storing each json item in variable
                        String name = c.getString(TAG_NAME);

                        String mobile = c.getString(TAG_MOBILE);

                        String gen = c.getString(TAG_GEN);
                        String group = c.getString(TAG_GROUP);


                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_NAME, name);
                        map.put(TAG_MOBILE, mobile);


                        map.put(TAG_GEN, gen);
                        map.put(TAG_GROUP, group);


                        // adding HashList to ArrayList
                        requestsList.add(map);
                    }
                }

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

             // updating UI from Background Thread
             runOnUiThread(new Runnable() {
                 public void run() {
                     /**
                      * Updating parsed JSON data into ListView
                      * */

                     ListAdapter adapter = new SimpleAdapter(
                             MatchDonor.this, requestsList,
                             R.layout.donor_list_item, new String[] { TAG_NAME,TAG_MOBILE,TAG_GEN,TAG_GROUP},
                             new int[] { R.id.dname, R.id.ph, R.id.g, R.id.bg });
                     // updating listview
                     mainListView.setBackgroundColor(Color.parseColor("#ffe3a0"));
                     mainListView.setAdapter(adapter);
                 }
             });
         }


    }


    @Override
    public void onBackPressed() {

            super.onBackPressed();
            startActivity(new Intent(MatchDonor.this, DonorMain.class));
            finish();

    }
    }

