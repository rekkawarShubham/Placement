package com.project.sdl.placement;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class ApplyForComapny extends AppCompatActivity {

    FirebaseFirestore db;
    TextView textViewprn, textViewmothername, textViewseat,textViewsgpa;
    EditText editTextskill ,editTextquestion;
    String username,password,user,pass;
    Button apply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_comapny);
        Bundle extras = getIntent().getExtras();
            
        apply =(Button)findViewById(R.id.buttonApply);
        editTextskill = (EditText)findViewById(R.id.edittextViewAddSkills);
        editTextquestion = (EditText)findViewById(R.id.edittextViewAddQuestions); 
        username = extras.getString("username");
        password = extras.getString("password");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(ApplyForComapny.this,StudentActivity.class);
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
        getStudentdata(username);
        
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyToCompany();
            }
        });
    }

    private void applyToCompany() {

        double marks = 8.5;
        double sgpa1;
        final String prnno = textViewprn.getText().toString().trim();
        final String seatno = textViewseat.getText().toString().trim();
        final String sgpa = textViewsgpa.getText().toString().trim();
        final String mothername = textViewmothername.getText().toString().trim();
        final String skills = editTextskill.getText().toString().trim();
        final String questions = editTextquestion.getText().toString().trim();
        //first we will do the validations
        sgpa1 = Double.parseDouble(sgpa);

        if (TextUtils.isEmpty(prnno)) {
            textViewprn.setError("Please enter username");
            textViewprn.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(seatno)) {
            textViewseat.setError("Please enter your seat no.");
            textViewseat.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(sgpa)) {
            textViewsgpa.setError("Criteria Does not match");
            textViewsgpa.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(mothername)) {
            textViewmothername.setError("Enter Mother name");
            textViewmothername.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(skills)) {
            editTextskill.setError("Enter skills");
            editTextskill.requestFocus();
            return;
        }

        if(sgpa1 >= marks) {
            db = FirebaseFirestore.getInstance();
            Map<String, String> appliedstudentmap = new HashMap<>();
            appliedstudentmap.put("PRN_NO", prnno);
            appliedstudentmap.put("seat_no", seatno);
            appliedstudentmap.put("SGPA", sgpa);
            appliedstudentmap.put("mother_name", mothername);
            appliedstudentmap.put("skills", skills);
            appliedstudentmap.put("question", questions);


            db.collection("Applied_Student").document(username).set(appliedstudentmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ApplyForComapny.this, "Applied", Toast.LENGTH_LONG).show();
                    } else {
                        Exception err = task.getException();
                        Toast.makeText(ApplyForComapny.this, "Cannot Apply" + err, Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
        else {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Your SGPA is Less than Criteria");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        }

    }

    public void getStudentdata(final String username) {
        Toast.makeText(ApplyForComapny.this, ""+username, Toast.LENGTH_SHORT).show();
        textViewprn = (TextView) findViewById(R.id.textViewPrnNo);
        textViewmothername = (TextView) findViewById(R.id.textViewMotherName);
        textViewseat = (TextView) findViewById(R.id.textViewSeat);
        textViewsgpa =(TextView)findViewById(R.id.textViewSgpa);
        db= FirebaseFirestore.getInstance();
        db.collection("All_Students")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                user = document.getId().toString();
                                Toast.makeText(ApplyForComapny.this, ""+user + username, Toast.LENGTH_SHORT).show();
                                if(user.equalsIgnoreCase(username))
                                {
                                    Toast.makeText(ApplyForComapny.this, "Success", Toast.LENGTH_LONG).show();
                                    textViewprn.setText(document.getString("PRN_no"));
                                    textViewmothername.setText(document.getString("mother_name"));
                                    textViewseat.setText(document.getString("seat_no"));
                                    textViewsgpa.setText(document.getString("SGPA"));
                                }
                            }
                        } else {
                            Exception err=task.getException();
                            Toast.makeText(ApplyForComapny.this,"Error"+err,Toast.LENGTH_LONG).show();
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
