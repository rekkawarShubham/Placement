    package com.project.sdl.placement;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

    public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    String username,password;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");
        password = extras.getString("password");

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        if(menuItem.getTitle().equals("Company"))
                        {
                            i = new Intent(MainActivity.this,AdminGetCompany.class);
                            Bundle extras = new Bundle();
                            extras.putString("username", username);
                            extras.putString("password", password);
                            i.putExtras(extras);
                            startActivity(i);
                        }
                        if(menuItem.getTitle().equals("Applied Students"))
                        {
                            i = new Intent(MainActivity.this,GetAppliedStudents.class);
                            Bundle extras = new Bundle();
                            extras.putString("username", username);
                            extras.putString("password", password);
                            i.putExtras(extras);
                            startActivity(i);
                        }
                        if(menuItem.getTitle().equals("Add Company"))
                        {
                            i = new Intent(MainActivity.this,AddCompany.class);
                            Bundle extras = new Bundle();
                            extras.putString("username", username);
                            extras.putString("password", password);
                            i.putExtras(extras);
                            startActivity(i);

                        }
                        if(menuItem.getTitle().equals("Logout"))
                        {
                            Toast.makeText(MainActivity.this, "Logged Out Successfully",
                                    Toast.LENGTH_LONG).show();
                            Intent i = new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(i);
                            finish();

                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}