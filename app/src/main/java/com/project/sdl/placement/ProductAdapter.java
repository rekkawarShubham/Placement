package com.project.sdl.placement;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    FirebaseFirestore db;
    String user,email;
    String seat,m_name,prn_no,sgpa;
    private Context mCtx;
    String selection = "Your Application for placement has " +
            "been ACCEPTED by PICT Placement Officer"
            +"Interview Dates will be inform to you";
    String rejection = "Your Application for placement has " +
            "been REJECTED by PICT Placement Officer";

    private List<Product> productList;

    //getting the context and product list with constructor
    public ProductAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.my_layout, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, final int position) {
        //getting the product of the specified position
        final Product product = productList.get(position);

        //binding the data with the viewholder views
        holder.textViewTitle.setText(product.getTitle());
        holder.textViewShortDesc.setText(product.getShortdesc());
        holder.textViewRating.setText(String.valueOf(product.getRating()));
        holder.textViewPrice.setText(String.valueOf(product.getPrice()));
        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Position",String.valueOf(position));
                seat = productList.get(position).getId();
                m_name = productList.get(position).getShortdesc();
                prn_no = productList.get(position).getTitle();
                sgpa = productList.get(position).getPrice();
                Log.d("USERNAME",seat);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mCtx);
                builder1.setMessage("Are You sure you want to Approve "+seat);
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                approveStudents(seat,m_name,prn_no,sgpa,position);
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();


            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Position",String.valueOf(position));
                 seat = productList.get(position).getId();
                 m_name = productList.get(position).getShortdesc();
                 prn_no = productList.get(position).getTitle();
                 sgpa = productList.get(position).getPrice();
                Log.d("USERNAME",seat);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mCtx);
                builder1.setMessage("Are You sure you want to Reject "+seat);
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                rejectStudents(seat,m_name,prn_no,sgpa,position);
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
    }

    //approve student and notify
    private void approveStudents(final String seat, String fname, String lname, String sgpa, final int position) {
        db = FirebaseFirestore.getInstance();
        Map<String, Object> studentStatus = new HashMap<>();
        studentStatus.put("username",seat);
        studentStatus.put("mother_name",fname);
        studentStatus.put("PRN_NO_name",lname);
        studentStatus.put("sgpa",sgpa);
        studentStatus.put("notfication","Selected For Interview");
        db.collection("Approved_Students").document(seat)
                .set(studentStatus)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("done","success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("done","not success");

            }
        });

        DocumentReference docRef = db.collection("Registered_Student").document(seat);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (task.isSuccessful()) {
                        email = document.getString("Email");
                        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto",email, null));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Interview Selection");
                        intent.putExtra(Intent.EXTRA_TEXT, selection);
                        mCtx.startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                    }
                    else {
                        Toast.makeText(mCtx, "Email Not Present", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        db.collection("Applied_Student")
                .document(seat)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        productList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, productList.size());
                        Log.d("Approved",String.valueOf(position));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Problem","While Approval");
            }
        });
    }

    //reject student and notify
    private void rejectStudents(String seat, String fname, String lname, String sgpa, final int position) {
        db = FirebaseFirestore.getInstance();
        Map<String, Object> studentStatus = new HashMap<>();
        studentStatus.put("username",seat);
        studentStatus.put("mother_name",fname);
        studentStatus.put("PRN_NO_name",lname);
        studentStatus.put("sgpa",sgpa);
        studentStatus.put("notfication","Rejected");
        db.collection("Rejected_Students").document(seat)
                .set(studentStatus)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("done","success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("done","not success");

            }
        });
        DocumentReference docRef = db.collection("Registered_Student").document(seat);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (task.isSuccessful()) {
                        email = document.getString("Email");
                        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto",email, null));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Interview Rejection");
                        intent.putExtra(Intent.EXTRA_TEXT, rejection);
                        mCtx.startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                    }
                    else {
                        Toast.makeText(mCtx, "Email Not Present", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }



    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {
        FirebaseFirestore adb ;
        TextView textViewTitle, textViewShortDesc, textViewRating, textViewPrice;
        ImageView imageView;
        Button approve ;
        Button reject;


        public ProductViewHolder(View itemView) {
            super(itemView);
            approve=  itemView.findViewById(R.id.approve);
            reject = itemView.findViewById(R.id.Reject);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);
            textViewRating = itemView.findViewById(R.id.textViewRating);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);

        }


    }
}
