package com.instadp.profilepicture.finalfoodappserver.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.instadp.profilepicture.finalfoodappserver.R;

public class OrderDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {

    public TextView foodname,foodprice,foodquantity,foodtotal;
    public ImageView fooddetailimage;
    public OrderDetailViewHolder(View itemView) {
        super(itemView);
        foodname= (TextView) itemView.findViewById(R.id.order_detail_product_name);
        foodprice= (TextView) itemView.findViewById(R.id.order_detail_product_price);
        foodquantity= (TextView) itemView.findViewById(R.id.order_detail_product_quantity);
        foodtotal= (TextView) itemView.findViewById(R.id.order_detail_product_total);
        fooddetailimage= (ImageView) itemView.findViewById(R.id.order_detail_image);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

    }
}
