package com.example.ahmeda47.gastracker;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity{
    public static FragmentManager fragmentManager;
    public static MyAppDatabase myAppDatabase;
    private BottomNavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        myAppDatabase = Room.databaseBuilder(getApplicationContext(),MyAppDatabase.class, "transactiondb").build();//addMigrations(MIGRATION_1_2).build();

        Log.e("DB Location", getDatabasePath("transactiondb").getAbsolutePath());
        if(findViewById(R.id.fragment_container) != null){
            if(savedInstanceState != null){
                return;
        }
            fragmentManager.beginTransaction().add(R.id.fragment_container, new HomeFragment()).commit();
        }
        navigationView = (BottomNavigationView) findViewById(R.id.bottom_nav_bar);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                new changeFragment().execute(item.getItemId());
                return true;
            }
        });
    }
    private class changeFragment extends AsyncTask<Integer,Void,Void>{

        @Override
        protected Void doInBackground(Integer... params) {
            switch(params[0]) {
                case R.id.action_home:
                    MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new HomeFragment()).addToBackStack(null).commit();
                    break;
                case R.id.action_add:
                    MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new AddTransaction()).addToBackStack(null).commit();
                    break;
                case R.id.action_view:
                    MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new DisplayTransaction()).addToBackStack(null).commit();
                    break;
                case R.id.action_settings:
                    MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new SettingFragment()).addToBackStack(null).commit();
                    break;
                case R.id.action_ytd:
                     MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, new YearToDate()).addToBackStack(null).commit();
            }
            return null;
        }
    }

}

