package com.project.sdl.placement;

import android.content.Context;
import android.support.annotation.NonNull;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Belal on 10/18/2017.
 */


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    FirebaseFirestore db;

    private Context mCtx;


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
                String seat = productList.get(position).getId();
                String m_name = productList.get(position).getShortdesc();
                String prn_no = productList.get(position).getTitle();
                String sgpa = productList.get(position).getPrice();
                Log.d("USERNAME",seat);
                approveStudents(seat,m_name,prn_no,sgpa,position);
            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Position",String.valueOf(position));
                String seat = productList.get(position).getId();
                String m_name = productList.get(position).getShortdesc();
                String prn_no = productList.get(position).getTitle();
                String sgpa = productList.get(position).getPrice();
                Log.d("USERNAME",seat);
                rejectStudents(seat,m_name,prn_no,sgpa,position);
            }
        });
    }

    private void approveStudents(String seat, String fname, String lname, String sgpa, final int position) {
        db = FirebaseFirestore.getInstance();
        Map<String, Object> studentStatus = new HashMap<>();
        studentStatus.put("username",seat);
        studentStatus.put("mother_name",fname);
        studentStatus.put("PRN_NO_name",lname);
        studentStatus.put("sgpa",sgpa);
        studentStatus.put("notfication","Selected For Interview");
        db.collection("Notify_Students").document(seat)
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

    private void rejectStudents(String seat, String fname, String lname, String sgpa, final int position) {
        db = FirebaseFirestore.getInstance();
        Map<String, Object> studentStatus = new HashMap<>();
        studentStatus.put("username",seat);
        studentStatus.put("mother_name",fname);
        studentStatus.put("PRN_NO_name",lname);
        studentStatus.put("sgpa",sgpa);
        studentStatus.put("notfication","Rejected");
        db.collection("Notify_Students").document(seat)
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

        db.collection("Applied_Student")
                .document(seat)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        productList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, productList.size());
                        Log.d("Deleted",String.valueOf(position));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                    Log.d("Problem","While Deletion");
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
