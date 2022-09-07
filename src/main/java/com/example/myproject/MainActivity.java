package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.myproject.TaskDAO.Task;
import com.example.myproject.database.DatabaseHelper;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import Adapter.TaskAdapter;


public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWidgets();


        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_open,R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Setup drawer view
        setupDrawerContent(navigationView);
        navigationView.getMenu().getItem(0).setChecked(true);
        Fragment defaultfragment = new allTasks();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, defaultfragment).commit();




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomsheetFragment addPhotoBottomDialogFragment =
                        bottomsheetFragment.newInstance();
                addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                        "add_task_dialog_fragment");

            }
        });









    }


    private void setupDrawerContent(NavigationView navigationView) {
       navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               selectDrawerItem(item);
               return true;
           }
       });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;

        switch(menuItem.getItemId()) {
            case R.id.today:
                fragment = new today();
                break;
            case R.id.tomorrow:
                fragment = new tomorrow();
                break;
            case R.id.upcoming:
                fragment = new upcoming();
                break;
            /*case R.id.goToNotes:
                Intent intent = new Intent(Intent.ACTION_SEND);

// Always use string resources for UI text.
// This says something like "Share this photo with"
                String title = "Select MyNotes App";
// Create intent to show chooser
                Intent chooser = Intent.createChooser(intent, title);

// Try to invoke the intent.
                try {
                    startActivity(chooser);
                } catch (ActivityNotFoundException e) {
                    // Define what your app should do if no activity can handle the intent.
                    Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                break;*/
            case R.id.all_tasks:

            default:
                fragment = new allTasks();

        }


        if(fragment!=null) {
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

            // Highlight the selected item has been done by NavigationView
            menuItem.setChecked(true);
            // Set action bar title
            toolbar.setTitle(menuItem.getTitle());
            // Close the navigation drawer
            drawerLayout.closeDrawers();
        }



    }



    public void getWidgets(){

        drawerLayout=(DrawerLayout)findViewById(R.id.drawerlayout);
        navigationView=findViewById(R.id.navigationview);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        navigationView= (NavigationView) findViewById(R.id.navigationview);
         fab = findViewById(R.id.fab);



    }


}