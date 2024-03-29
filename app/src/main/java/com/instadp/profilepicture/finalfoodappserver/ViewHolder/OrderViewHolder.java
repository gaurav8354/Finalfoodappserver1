package com.instadp.profilepicture.finalfoodappserver.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.instadp.profilepicture.finalfoodappserver.Interface.ItemOnClickListner;
import com.instadp.profilepicture.finalfoodappserver.R;

/**
 * Created by gaurav on 3/18/2018.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener{

    public TextView txtOrderId,txtOrderStatus,txtOrderPhone,txtOrderAddress;

    private ItemOnClickListner itemOnClickListner;
    public OrderViewHolder(View itemView) {
        super(itemView);
        txtOrderAddress= (TextView) itemView.findViewById(R.id.order_address);
        txtOrderId= (TextView) itemView.findViewById(R.id.order_id);
        txtOrderStatus= (TextView) itemView.findViewById(R.id.order_status);
        txtOrderPhone= (TextView) itemView.findViewById(R.id.order_phone);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }

    public  void setItemOnClickListner(ItemOnClickListner itemOnClickListner)
    {
        this.itemOnClickListner=itemOnClickListner;
    }

    @Override
    public void onClick(View v) { itemOnClickListner.onClick(itemView,getAdapterPosition(),false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    menu.setHeaderTitle("Select The Action");
        menu.add(0,0,getAdapterPosition(),"Update");
        menu.add(0,1,getAdapterPosition(),"Delete");
        menu.add(0,2,getAdapterPosition(),"View Order Detail");
    }
}
