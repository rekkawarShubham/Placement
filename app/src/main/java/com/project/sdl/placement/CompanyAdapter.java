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

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder> {

    FirebaseFirestore db;

    private Context mCtx;


    private List<Company> companyList;

    String c_name , c_sal ,c_criteria , c_skills;
    //getting the context and product list with constructor
    public CompanyAdapter(Context mCtx, List<Company> companyList) {
        this.mCtx = mCtx;
        this.companyList =companyList;
    }

    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.company_layout, null);
        return new CompanyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyViewHolder companyViewHolder,final int position) {
        final Company company = companyList.get(position);
        companyViewHolder.textViewCompanyName.setText(company.getC_name());
        companyViewHolder.textViewCompanySalary.setText(company.getC_salary());
        companyViewHolder.textViewCompanySkills.setText(company.getC_skills());
        companyViewHolder.textViewCompanyCriteria.setText(company.getC_type());
//        companyViewHolder.apply.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("Position",String.valueOf(position));
//                c_name  = companyList.get(position).getC_name();
//                c_sal = companyList.get(position).getC_salary();
//                c_skills= companyList.get(position).getC_skills();
//                c_criteria = companyList.get(position).getC_type();
//              //  apply(c_name,c_sal,c_skills,c_criteria,position);
//            }
//        });
    }

//    private void apply(String c_name, String c_sal, String c_skills, String c_criteria, int position) {
//
//        companyList.remove(position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, companyList.size());
//        Log.d("Deleted",String.valueOf(position));
//
//    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public class CompanyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewCompanyName, textViewCompanySalary, textViewCompanyCriteria, textViewCompanySkills;
        ImageView imageView;
        Button apply;

        public CompanyViewHolder(@NonNull View itemView) {
            super(itemView);
            //apply = itemView.findViewById(R.id.applybutton);
            textViewCompanyName = itemView.findViewById(R.id.textViewComapanyName);
            textViewCompanySalary = itemView.findViewById(R.id.textViewCompanySalary);
            textViewCompanyCriteria = itemView.findViewById(R.id.textViewCompanyCriteria);
            textViewCompanySkills = itemView.findViewById(R.id.textViewComapanySkills);
        }
    }
}
