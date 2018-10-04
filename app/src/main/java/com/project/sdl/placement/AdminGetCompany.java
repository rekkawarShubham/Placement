package com.project.sdl.placement;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminGetCompany extends AppCompatActivity {

    private String c_name;
    private String c_salary;
    private String c_skills;
    private String  c_criteria;
    private int c_image;

    RecyclerView recyclerView;
    FirebaseFirestore db;
    List<Company> companylist;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_get_company);

        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");
        password = extras.getString("password");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(AdminGetCompany.this,MainActivity.class);
                Bundle extras = new Bundle();
                extras.putString("username", username);
                extras.putString("password", password);
                i.putExtras(extras);
                startActivity(i);
            }
        });

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_back);

        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        companylist = new ArrayList<>();
        final AdminCompanyAdapter companyAdapter;
        companyAdapter = new AdminCompanyAdapter(this,companylist);

        db =  FirebaseFirestore.getInstance();
        db.collection("All_Companies")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            for(DocumentSnapshot document : task.getResult())
                            {
                                c_name = document.getString("company_name");
                                c_salary= document.getString("company_salary");
                                c_skills= document.getString("company_skills");
                                c_criteria= document.getString("company_criteria");

                                companylist.add(new Company(
                                        c_name,
                                        c_salary,
                                        c_skills,
                                        c_criteria,
                                        R.drawable.ic_add_company
                                ));
                                recyclerView.setAdapter(companyAdapter);

                            }
                        }
                        else {
                            Exception err=task.getException();
                            Toast.makeText(AdminGetCompany.this,"Error"+err,Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }



    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
            Toast.makeText(getApplicationContext(), "Use Back Arrow to go back",
                    Toast.LENGTH_LONG).show();
        return false;
    }
}
