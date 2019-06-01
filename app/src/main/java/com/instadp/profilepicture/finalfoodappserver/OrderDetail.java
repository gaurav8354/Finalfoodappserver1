package com.instadp.profilepicture.finalfoodappserver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.instadp.profilepicture.finalfoodappserver.Common.Common;
import com.instadp.profilepicture.finalfoodappserver.Interface.ItemOnClickListner;
import com.instadp.profilepicture.finalfoodappserver.Model.Food;
import com.instadp.profilepicture.finalfoodappserver.Model.Order;
import com.instadp.profilepicture.finalfoodappserver.Model.Request;
import com.instadp.profilepicture.finalfoodappserver.ViewHolder.OrderDetailViewHolder;
import com.instadp.profilepicture.finalfoodappserver.ViewHolder.OrderViewHolder;

public class OrderDetail extends AppCompatActivity {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager  layoutManager;
    FirebaseRecyclerAdapter<Order,OrderDetailViewHolder> adapter;
    FirebaseDatabase db;
    DatabaseReference requests;
Intent i;
String orderid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        i=getIntent();
        setContentView(R.layout.activity_order_detail);
        db=FirebaseDatabase.getInstance();
        requests=db.getReference("Requests").child(i.getStringExtra("ordid")).child("food");
        recyclerView= (RecyclerView) findViewById(R.id.order_detail_recycleview);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
         loadOrder();


    }
    private void loadOrder() {
        adapter=new FirebaseRecyclerAdapter<Order, OrderDetailViewHolder>(
                Order.class,
                R.layout.order_detail_layout,
                OrderDetailViewHolder.class,
                requests
        ) {


            @Override
            protected void populateViewHolder(OrderDetailViewHolder viewHolder, Order model, int position) {
                viewHolder.foodprice.setText(model.getPrice());
                viewHolder.foodname.setText(model.getProduceName());
                viewHolder.foodquantity.setText(model.getQuantity());
                String total=Integer.parseInt(model.getQuantity())*Integer.parseInt(model.getPrice())+"";
                viewHolder.foodtotal.setText(total);


            }

        };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}
