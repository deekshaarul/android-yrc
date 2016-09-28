package com.yrcvit.bdassist;

import android.app.ActionBar;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity implements View.OnClickListener {

        EditText uname;
        EditText mEtPwd;
        CheckBox mCbShowPwd;
        public static String  PREFS_NAME="yrc";
        public static String PREF_USERNAME="yrcadmin";
        public static String PREF_PASSWORD="yrc";
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                mEtPwd = (EditText) findViewById(R.id.pass);
                // get the show/hide password Checkbox
                mCbShowPwd = (CheckBox) findViewById(R.id.cbShowPwd);
                uname = (EditText) findViewById(R.id.usr);
                // add onCheckedListener on checkbox
                // when user clicks on this checkbox, this is the handler.
                mCbShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                // checkbox status is changed from uncheck to checked.
                                if (!isChecked) {
                                        // show password
                                        mEtPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                } else {
                                        // hide password
                                        mEtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                }
                        }
                });

              Button btnShow = (Button) findViewById(R.id.l);

                btnShow.setOnClickListener(this);
        }


        @Override
        public void onClick(View v)
        {
                String user=mEtPwd.getText().toString();
                String password=uname.getText().toString();
                if (user.equals("yrc") )
                {
                        if(password.toLowerCase().equals("yrcadmin ")||password.toLowerCase().equals("yrcadmin"))
                        {

                                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(i);
                        }
                        else{
                                uname.setError("Invalid Username!");
                        }

                }
                else{
                        mEtPwd.setError("Incorrect Password!");
                }
        }

}