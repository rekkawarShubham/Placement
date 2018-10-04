package com.project.sdl.placement;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminCompanyAdapter extends RecyclerView.Adapter<AdminCompanyAdapter.CompanyViewHolder> {

    FirebaseFirestore db;

    private Context mCtx;


    private List<Company> companyList;

    String c_name , c_sal ,c_criteria , c_skills;
    //getting the context and product list with constructor
    public AdminCompanyAdapter(Context mCtx, List<Company> companyList) {
        this.mCtx = mCtx;
        this.companyList =companyList;
    }

    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.admin_company_layout, null);
        return new CompanyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyViewHolder companyViewHolder,final int position) {
        final Company company = companyList.get(position);
        companyViewHolder.textViewCompanyName.setText(company.getC_name());
        companyViewHolder.textViewCompanySalary.setText(company.getC_salary());
        companyViewHolder.textViewCompanySkills.setText(company.getC_skills());
        companyViewHolder.textViewCompanyCriteria.setText(company.getC_type());
        companyViewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Position",String.valueOf(position));
                c_name  = companyList.get(position).getC_name();
                c_sal = companyList.get(position).getC_salary();
                c_skills= companyList.get(position).getC_skills();
                c_criteria = companyList.get(position).getC_type();
                remove(c_name,c_sal,c_skills,c_criteria,position);
            }
        });
    }

    private void remove(String c_name, String c_sal, String c_skills, String c_criteria, final int position) {
        db = FirebaseFirestore.getInstance();
        Map<String, Object> companyStatus = new HashMap<>();
        companyStatus.put("company_name",c_name);
        companyStatus.put("company_salary",c_sal);
        companyStatus.put("company_skills",c_skills);
        companyStatus.put("company_criteria",c_criteria);
        companyStatus.put("notfication","Company Issue or Its has Recruited specific students");
        db.collection("Notify_Company").document(c_name)
                .set(companyStatus)
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

        db.collection("All_Companies")
                .document(c_name)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        companyList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, companyList.size());
                        Log.d("Company Removed",String.valueOf(position));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Problem","While Company Removal");
            }
        });

    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public class CompanyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewCompanyName, textViewCompanySalary, textViewCompanyCriteria, textViewCompanySkills;
        ImageView imageView;
        Button remove;

        public CompanyViewHolder(@NonNull View itemView) {
            super(itemView);
            remove = itemView.findViewById(R.id.removeCompany);
            textViewCompanyName = itemView.findViewById(R.id.textViewComapanyName);
            textViewCompanySalary = itemView.findViewById(R.id.textViewCompanySalary);
            textViewCompanyCriteria = itemView.findViewById(R.id.textViewCompanyCriteria);
            textViewCompanySkills = itemView.findViewById(R.id.textViewComapanySkills);
        }
    }
}
