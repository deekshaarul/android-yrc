package com.yrcvit.bdassist;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Progress Dialog
    private ProgressDialog pDialog;
    String ph;
    private int success=0;
    private JSONObject json;
    private HTTPURLConnection service;
    private String path = "http://www.yrcvit.com/android/completed.php";
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> requestsList;

    // url to get all requests list
    private static String url_all_requests = "http://www.yrcvit.com/android/get_all_req.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_REQUEST = "request";
    private static final String TAG_PNAME = "pname";
    private static final String TAG_AGE = "age";
    private static final String TAG_MOBILE = "mobile";
    private static final String TAG_REASON = "reason";
    private static final String TAG_UNITS = "units";
    private static final String TAG_DATE = "date";
    private static final String TAG_TYPE = "type";
    private static final String TAG_GROUP = "group";
    private static final String TAG_HNAME = "hname";
    //private static final String TAG_NAME = "age";
    public ListView mainListView;
    // requests JSONArray
    JSONArray requests = null;
    private SwipeRefreshLayout swipeContainer;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mainListView = (ListView) findViewById(R.id.list);
        mainListView.setLongClickable(true);
        mainListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                //Do your tasks here
                TextView c = (TextView) v.findViewById(R.id.no);
                ph = c.getText().toString();

                AlertDialog.Builder alert = new AlertDialog.Builder(
                        MainActivity.this);
                alert.setTitle("Completed?");
                alert.setMessage("Sure to continue ?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do your work here
                        service=new HTTPURLConnection();
                        new PostDataTOServer().execute();

                        dialog.dismiss();

                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alert.show();

                return true;
            }
        });

        // Hashmap for ListView
        requestsList = new ArrayList<HashMap<String, String>>();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        // Loading requests in Background Thread
        new LoadAllrequests().execute();
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                new LoadAllrequests().execute();
                swipeContainer.setRefreshing(false);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_req);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddRequest.class);
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }
    private class PostDataTOServer extends AsyncTask<Void, Void, Void> {

        String response = "";
        //Create hashmap Object to send parameters to web service
        HashMap<String, String> postDataParams;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            postDataParams=new HashMap<String, String>();

            postDataParams.put("mobile", ph);

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
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
            else
                Toast.makeText(getApplicationContext(), "Server Error!", Toast.LENGTH_LONG).show();
        }
    }


    // Response from Edit Product Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }



    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllrequests extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading Requests. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All requests from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_requests, "GET", params);
            if(json==null){
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new LoadAllrequests().execute();
                            }
                        });


                snackbar.show();
            }
            else {
                // Check your log cat for JSON reponse
                Log.d("All requests: ", json.toString());

                try {

                    // Checking for SUCCESS TAG
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        // requests found
                        // Getting Array of requests
                        requests = json.getJSONArray(TAG_REQUEST);

                        // looping through All requests
                        for (int i = 0; i < requests.length(); i++) {
                            JSONObject c = requests.getJSONObject(i);

                            // Storing each json item in variable
                            String pname = c.getString(TAG_PNAME);
                            String age = c.getString(TAG_AGE);
                            String mobile = c.getString(TAG_MOBILE);
                            String reason = c.getString(TAG_REASON);
                            String units = c.getString(TAG_UNITS);
                            String date = c.getString(TAG_DATE);
                            String type = c.getString(TAG_TYPE);
                            String group = c.getString(TAG_GROUP);
                            String hname = c.getString(TAG_HNAME);


                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_PNAME, pname);
                            map.put(TAG_AGE, age);
                            map.put(TAG_MOBILE, mobile);
                            map.put(TAG_REASON, reason);
                            map.put(TAG_UNITS, units);
                            map.put(TAG_DATE, date);
                            map.put(TAG_TYPE, type);
                            map.put(TAG_GROUP, group);
                            map.put(TAG_HNAME, hname);


                            // adding HashList to ArrayList
                            requestsList.add(map);
                        }
                    } else {
                        // no requests found
                        // Launch Add New product Activity

                        Intent i = new Intent(getApplicationContext(),
                                AddRequest.class);
                        // Closing all previous activities
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all requests
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    int[] colors = new int[] { 0x30FF0000, 0x300000FF };
                    int colorPos;
                    ListAdapter adapter = new SimpleAdapter(
                            MainActivity.this, requestsList,
                            R.layout.list_item, new String[] { TAG_PNAME,TAG_MOBILE,TAG_UNITS,TAG_DATE,TAG_TYPE,TAG_GROUP,TAG_HNAME},
                            new int[] { R.id.pn, R.id.no, R.id.u, R.id.ldate, R.id.t, R.id.g, R.id.hn });
                    // updating listview
                     colorPos = 0;
                    mainListView.setBackgroundColor(colors[colorPos]);
                    mainListView.setAdapter(adapter);

                }
            });






        }

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_req) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_donor) {
            Intent i = new Intent(getApplicationContext(), DonorMain.class);
            startActivity(i);

        } else if (id == R.id.nav_comp) {
            Intent i = new Intent(getApplicationContext(), Completed.class);
            startActivity(i);

        }  else if (id == R.id.nav_share) {
            Intent i = new Intent(getApplicationContext(), Email.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
