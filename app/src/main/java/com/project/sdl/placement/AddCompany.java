package com.project.sdl.placement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddCompany extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 71;
    EditText editTextCompanyname, editTextCompanyCriteria, editTextCompanySkills,editTextCompanySalary;
    RadioGroup radioGrouptype;
    ImageView imageButton;
    private Uri filePath;
    public FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;
    String username,password;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company);

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
                i = new Intent(AddCompany.this,MainActivity.class);
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

        imageButton = (ImageView) findViewById(R.id.imagebutton);
         editTextCompanyname = (EditText) findViewById(R.id.editTextCompanyName);
        editTextCompanyCriteria = (EditText) findViewById(R.id.editTextCompanyCriteria);
        editTextCompanySkills = (EditText) findViewById(R.id.editTextCompanySkills);
        editTextCompanySalary =(EditText)findViewById(R.id.editTextCompanyPackage); 
        radioGrouptype = (RadioGroup) findViewById(R.id.radiotype);

        (findViewById(R.id.buttonAddCompany)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCompany();
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

    }

    private void addCompany() {

        final String companyname = editTextCompanyname.getText().toString().trim();
        final String companycriteria = editTextCompanyCriteria.getText().toString().trim();
        final String companyskills = editTextCompanySkills.getText().toString().trim();
        final String companysalary = editTextCompanySalary.getText().toString().trim();
        final String companytype = ((RadioButton) findViewById(radioGrouptype.getCheckedRadioButtonId())).getText().toString();

        //first we will do the validations

        if (TextUtils.isEmpty(companyname)) {
            editTextCompanyname.setError("Please enter username");
            editTextCompanyname.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(companycriteria)) {
            editTextCompanyCriteria.setError("Please enter Company Criteria");
            editTextCompanyCriteria.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(companyskills)) {
            editTextCompanySkills.setError("Enter Skills Required");
            editTextCompanySkills.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(companysalary)) {
            editTextCompanySalary.setError("Enter Salary");
            editTextCompanySalary.requestFocus();
            return;
        }

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        db= FirebaseFirestore.getInstance();

        Map<String,String> companymap= new HashMap<>();
        companymap.put("company_name",companyname);
        companymap.put("company_criteria",companycriteria);
        companymap.put("company_skills",companyskills);
        companymap.put("company_type",companytype);
        companymap.put("company_salary",companysalary);

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("Companies"+"/"+companyname+"/"+"CompanyLogo");
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddCompany.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddCompany.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });


            db.collection("All_Companies").document(companyname).set(companymap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(AddCompany.this,"Company Added",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Exception err=task.getException();
                        Toast.makeText(AddCompany.this,"Company Not Added"+err,Toast.LENGTH_LONG).show();
                    }

                }
            });


        }
        else if(filePath==null)
        {
            Toast.makeText(this, "Please Select Company Logo", Toast.LENGTH_SHORT).show();
        }

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageButton.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
            Toast.makeText(getApplicationContext(), "Use Back Arrow to go back",
                    Toast.LENGTH_LONG).show();
        return false;
    }
}

