package com.project.sdl.placement;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class StudentApplied extends AppCompatActivity {

    FirebaseFirestore db;
    TextView t1,t2,t3,t4;
    Button show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_applied);

        t1 = (TextView)findViewById(R.id.sUser);
        t2 = (TextView)findViewById(R.id.sDivi);
        t3 = (TextView)findViewById(R.id.sRoll);
        t4 = (TextView)findViewById(R.id.sCgpa);

        show = (Button)findViewById(R.id.show);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchstudent();
            }
        });
    }

    public void fetchstudent() {

        db = FirebaseFirestore.getInstance();

        db.collection("Applied_Student")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                t1.setText(document.getString("Username"));
                            }
                        } else {

                        }
                    }
                });

    }
}
