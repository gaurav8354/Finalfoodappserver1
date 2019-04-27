package com.instadp.profilepicture.finalfoodappserver.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.instadp.profilepicture.finalfoodappserver.Common.Common;
import com.instadp.profilepicture.finalfoodappserver.Interface.ItemOnClickListner;
import com.instadp.profilepicture.finalfoodappserver.R;

/**
 * Created by gaurav on 3/11/2018.
 */

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener
{

    public TextView foodName;
    public ImageView foodView;
    private ItemOnClickListner itemClickLisner;

    public FoodViewHolder(View itemView) {
        super(itemView);
        foodName= (TextView) itemView.findViewById(R.id.food_name);
        foodView= (ImageView) itemView.findViewById(R.id.food_image);

        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);
    }
    public  void  setItemClickListner(ItemOnClickListner itemClickListner){
        this.itemClickLisner=itemClickListner;
    }

    @Override
    public void onClick(View v) {
        itemClickLisner.onClick(v,getAdapterPosition(),false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select the action");
        menu.add(0,0,getAdapterPosition(), Common.UPDATE);
        menu.add(0,1,getAdapterPosition(), Common.DELETE);


    }
}
