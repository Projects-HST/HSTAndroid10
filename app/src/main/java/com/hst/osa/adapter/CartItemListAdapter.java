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
import com.hst.osa.bean.support.CartItem;
import com.hst.osa.bean.support.Product;
import com.hst.osa.utils.OSAValidator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartItemListAdapter extends RecyclerView.Adapter<CartItemListAdapter.MyViewHolder> {

    private ArrayList<CartItem> productArrayList;
    Context context;
    private OnItemClickListener onItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtProductName, txtProductPrice, txtProductMRP;
        public LinearLayout productLayout;
        public ImageView productBanner, productDelete;
        public RatingBar productRating;
        public MyViewHolder(View view) {
            super(view);
            productLayout = (LinearLayout) view.findViewById(R.id.product_layout);
            productBanner = (ImageView) view.findViewById(R.id.product_img);
            productDelete = (ImageView) view.findViewById(R.id.product_like);
            productDelete.setOnClickListener(this);
            productLayout.setOnClickListener(this);
            txtProductName = (TextView) view.findViewById(R.id.product_name);
            txtProductPrice = (TextView) view.findViewById(R.id.product_price);
            txtProductMRP = (TextView) view.findViewById(R.id.product_mrp);
            productRating = (RatingBar) view.findViewById(R.id.ratingBar);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClickNewArrivals(v, getAdapterPosition());
            }
//            else {
//                onClickListener.onClick(Selecttick);
//            }
        }
    }


    public CartItemListAdapter(ArrayList<CartItem> ProductArrayList, CartItemListAdapter.OnItemClickListener onItemClickListener) {
        this.productArrayList = ProductArrayList;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClickNewArrivals(View view, int position);
    }


    @Override
    public CartItemListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_cart, parent, false);

        return new CartItemListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartItemListAdapter.MyViewHolder holder, int position) {
        CartItem product = productArrayList.get(position);

        holder.txtProductName.setText(product.getproduct_name());
        holder.txtProductMRP.setVisibility(View.GONE);
        holder.txtProductPrice.setText("₹" + product.getprice());

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
}