package com.hst.osa.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hst.osa.R;
import com.hst.osa.bean.support.Product;
import com.hst.osa.utils.OSAValidator;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BestSellingListAdapter extends RecyclerView.Adapter<BestSellingListAdapter.MyViewHolder> {

    private ArrayList<Product> productArrayList;
    Context context;
    private OnItemClickListener onItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtProductOffer, txtProductName, txtProductPrice, txtProductMRP;
        public LinearLayout productLayout;
        public ImageView productBanner, productLike;
        public RatingBar productRating;
        public MyViewHolder(View view) {
            super(view);
            productLayout = (LinearLayout) view.findViewById(R.id.product_layout);
            productBanner = (ImageView) view.findViewById(R.id.product_img);
            productLike = (ImageView) view.findViewById(R.id.product_like);
            productLike.setOnClickListener(this);
            productLayout.setOnClickListener(this);
            txtProductOffer = (TextView) view.findViewById(R.id.offer);
            txtProductName = (TextView) view.findViewById(R.id.product_name);
            txtProductPrice = (TextView) view.findViewById(R.id.product_price);
            txtProductMRP = (TextView) view.findViewById(R.id.product_mrp);
            productRating = (RatingBar) view.findViewById(R.id.ratingBar);
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


    public BestSellingListAdapter(ArrayList<Product> newsFeedArrayList, OnItemClickListener onItemClickListener) {
        this.productArrayList = newsFeedArrayList;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_grid_product, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Product product = productArrayList.get(position);

        holder.txtProductName.setText(product.getproduct_name());
        if (product.getOffer_status().equalsIgnoreCase("0")) {
            holder.txtProductOffer.setVisibility(View.GONE);
            holder.txtProductMRP.setVisibility(View.GONE);
            holder.txtProductPrice.setText("₹" + product.getprod_actual_price());
        } else {
            holder.txtProductOffer.setText(product.getoffer_percentage() + " % Off");
            holder.txtProductPrice.setText("₹" + product.getprod_actual_price());
            holder.txtProductMRP.setText("₹" + product.getprod_mrp_price());
            holder.txtProductMRP.setPaintFlags(holder.txtProductMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.productRating.setRating(Float.parseFloat(product.getReview_average()));


        if (OSAValidator.checkNullString(product.getproduct_cover_img())) {
            Picasso.get().load(product.getproduct_cover_img()).into(holder.productBanner);
        } else {
//            newsImage.setImageResource(R.drawable.news_banner);
        }
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        /*if ((position + 1) % 7 == 4 || (position + 1) % 7 == 0) {
            return 2;
        } else {
            return 1;
        }*/
        if (productArrayList.get(position) != null || productArrayList.get(position).getSize() > 0)
            return 2;
        else
            return 1;
    }

}
