package com.hst.osa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hst.osa.R;
import com.hst.osa.bean.support.Category;
import com.hst.osa.bean.support.SubCategory;

import java.util.ArrayList;

public class SubCategoryListAdapter extends RecyclerView.Adapter<SubCategoryListAdapter.MyViewHolder> {

    private ArrayList<SubCategory> categoryArrayList;
    Context context;
    private SubCategoryListAdapter.OnItemClickListener onItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtCategoryName;

        public MyViewHolder(View view) {
            super(view);

            txtCategoryName = (TextView) view.findViewById(R.id.txt_sub_category);
            txtCategoryName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, getAdapterPosition());
            }
//            else {
//                onClickListener.onClick(Selecttick);
//            }
        }
    }

    public SubCategoryListAdapter(ArrayList<SubCategory> CategoryArrayList, SubCategoryListAdapter.OnItemClickListener onItemClickListener) {
        this.categoryArrayList = CategoryArrayList;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    @NonNull
    @Override
    public SubCategoryListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_sub_cat, parent, false);

        return new SubCategoryListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryListAdapter.MyViewHolder holder, int position) {

        SubCategory category = categoryArrayList.get(position);
        holder.txtCategoryName.setText(category.getCategory_name());

    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        /*if ((position + 1) % 7 == 4 || (position + 1) % 7 == 0) {
            return 2;
        } else {
            return 1;
        }*/
        if (categoryArrayList.get(position) != null || categoryArrayList.get(position).getSize() > 0)
            return 2;
        else
            return 1;
    }
}