package com.project.sdl.placement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class ProfileActivity extends AppCompatActivity {

    TextView  textViewUsername, textViewEmail, textViewGender;
    ImageView imageView;
    String username,password,user,pass;
    Button logout;
    public FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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
                Intent i =new Intent(ProfileActivity.this,StudentActivity.class);
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

        imageView = (ImageView)findViewById(R.id.profile);
        logout =  (Button)findViewById(R.id.buttonLogout);
         getProfile(username);
        getStudentdata(username,password);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            }
        });
    }

    public void getStudentdata(final String username, final String password) {

        textViewUsername = (TextView) findViewById(R.id.textViewUsername);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewGender = (TextView) findViewById(R.id.textViewGender);
        db= FirebaseFirestore.getInstance();
        db.collection("Registered_Student")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                user = document.getString("Username");
                                pass = document.getString("Password");
                                if(user.equalsIgnoreCase(username) && pass.equalsIgnoreCase(password)) {
                                    Toast.makeText(ProfileActivity.this, "Success", Toast.LENGTH_LONG).show();
                                    textViewEmail.setText(document.getString("Email"));
                                    textViewUsername.setText(document.getString("Username"));
                                    textViewGender.setText(document.getString("Gender"));
                                }
                            }
                        } else {
                            Exception err=task.getException();
                            Toast.makeText(ProfileActivity.this,"Error"+err,Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
    public void getProfile(String username)
    {
        StorageReference mImageRef =
                FirebaseStorage.getInstance().getReference(username+"/"+"ProfilePhoto");
        final long ONE_MEGABYTE = 1024 * 1024;
        mImageRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        DisplayMetrics dm = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(dm);

                        imageView.setMinimumHeight(dm.heightPixels);
                        imageView.setMinimumWidth(dm.widthPixels);
                        imageView.setImageBitmap(bm);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
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