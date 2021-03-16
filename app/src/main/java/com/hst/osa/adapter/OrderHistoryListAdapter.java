package com.hst.osa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hst.osa.R;
import com.hst.osa.activity.CartActivity;
import com.hst.osa.bean.support.CartItem;
import com.hst.osa.bean.support.OrderHistory;
import com.hst.osa.helpers.ProgressDialogHelper;
import com.hst.osa.servicehelpers.ServiceHelper;
import com.hst.osa.serviceinterfaces.IServiceListener;
import com.hst.osa.utils.OSAValidator;
import com.hst.osa.utils.PreferenceStorage;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderHistoryListAdapter extends RecyclerView.Adapter<OrderHistoryListAdapter.MyViewHolder> {

    private ArrayList<OrderHistory> orderHistoryArrayList;
    Context mContext;
    private OnItemClickListener onItemClickListener;
    String resFor = "";
    int pos;
    String cartID = "";
    String UserID = "";
    String quantity = "";
    OrderHistory orderHistory;
    MyViewHolder myViewHolder;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtProductName, txtProductPrice, txtProductMRP, txtProductStock, productQuantity;
        public LinearLayout productLayout;
        public ImageView productBanner, productDelete;
        public ImageView btnPlus, btnMinus;

        public MyViewHolder(View view) {
            super(view);
            productLayout = (LinearLayout) view.findViewById(R.id.product_layout);
            productBanner = (ImageView) view.findViewById(R.id.product_img);
            productLayout.setOnClickListener(this);
            txtProductName = (TextView) view.findViewById(R.id.product_name);
            txtProductPrice = (TextView) view.findViewById(R.id.product_price);
            txtProductMRP = (TextView) view.findViewById(R.id.product_mrp);
            productQuantity = (TextView) view.findViewById(R.id.product_quantity);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClickHistory(v, getAdapterPosition());
            }
//            else {
//                onClickListener.onClick(Selecttick);
//            }
        }
    }

    public OrderHistoryListAdapter(Context context, ArrayList<OrderHistory> orderHistoryArrayList, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.orderHistoryArrayList = orderHistoryArrayList;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClickHistory(View view, int position);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order_history, parent, false);

        UserID = PreferenceStorage.getUserId(itemView.getContext());
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        orderHistory = orderHistoryArrayList.get(position);
        myViewHolder = holder;
        holder.txtProductName.setText(orderHistory.getorder_id());
        holder.txtProductMRP.setVisibility(View.GONE);
        holder.productQuantity.setVisibility(View.GONE);
        holder.txtProductPrice.setText("₹" + orderHistory.getTotal_amount());
//        String prodQty = "Quantity: ".concat(orderHistory.getquantity());
//        holder.productQuantity.setText(prodQty);

        if (OSAValidator.checkNullString(orderHistory.getorder_cover_img())) {
            Picasso.get().load(orderHistory.getorder_cover_img()).into(holder.productBanner);
        } else {
//            newsImage.setImageResource(R.drawable.news_banner);
        }
    }

    @Override
    public int getItemCount() {
        return orderHistoryArrayList.size();
    }
}
