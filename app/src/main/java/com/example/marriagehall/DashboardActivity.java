package com.example.marriagehall;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marriagehall.ui.login.LoginActivity;
import com.example.marriagehall.ui.settings.SettingsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DashboardActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    String email = null, name = null;
    int user_id;
    TextView emailid, username;
//    int flag = 0; //to detect from where the data is coming from

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



        //to receive values from City Activity
        Intent in = getIntent();
        user_id = in.getIntExtra("user_id", 0);
        email = in.getStringExtra("email_id");
        name = in.getStringExtra("user_name");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_location){
            Intent intent = new Intent(DashboardActivity.this, CityActivity.class);
            intent.putExtra("user_email", email);
            intent.putExtra("req", 1);
            finish();
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.action_logout){
            //to clear the login shared preference
            SharedPreferences pref1 = getSharedPreferences("myprefs", MODE_PRIVATE);
            pref1.edit().clear().apply();

            //to clear the city shared preference
            SharedPreferences pref2 = getSharedPreferences("prefs", MODE_PRIVATE);
            pref2.edit().clear().apply();

            Toast.makeText(this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
            finish();
            Intent in = new Intent(DashboardActivity.this, LoginActivity.class);
            in.putExtra("req",1);
            startActivity(in);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        emailid = findViewById(R.id.action_emailid);
        username = findViewById(R.id.action_name);
        emailid.setText(email); //to set email id of user in nav bar head
        username.setText(name);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
