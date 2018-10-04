package com.project.sdl.placement;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetAppliedStudents extends AppCompatActivity {

    //a list to store all the products
    List<Product> productList;
    String TAG = "Hello Shubham";
    private String id;
    private String title;
    private String shortdesc;
    private String rating;
    private String price;
    private int image;
    //the recyclerview
    RecyclerView recyclerView;
    FirebaseFirestore db;
    private String username,password;
    Intent i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_applied_students);

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
                i = new Intent(GetAppliedStudents.this,MainActivity.class);
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
        final ProductAdapter adapter;
        //initializing the productlist


        productList = new ArrayList<>();

        adapter = new ProductAdapter(this, productList);
        db= FirebaseFirestore.getInstance();
        db.collection("Applied_Student")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                id = document.getString("seat_no");
                                title = document.getString("PRN_NO");
                                shortdesc = document.getString("mother_name");
                                rating = document.getString("skills");
                                price = document.getString("SGPA");

                                productList.add(
                                        new Product(
                                                id,
                                                title,
                                                shortdesc,
                                                rating,
                                                price,
                                                R.drawable.ic_menu));
                                Toast.makeText(GetAppliedStudents.this, ""+id, Toast.LENGTH_SHORT).show();
                                recyclerView.setAdapter(adapter);
                            }
                        } else {
                            Exception err=task.getException();
                            Toast.makeText(GetAppliedStudents.this,"Error"+err,Toast.LENGTH_LONG).show();
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


