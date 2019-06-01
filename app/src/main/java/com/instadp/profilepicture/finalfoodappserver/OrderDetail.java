package com.instadp.profilepicture.finalfoodappserver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.instadp.profilepicture.finalfoodappserver.Common.Common;
import com.instadp.profilepicture.finalfoodappserver.Interface.ItemOnClickListner;
import com.instadp.profilepicture.finalfoodappserver.Model.Food;
import com.instadp.profilepicture.finalfoodappserver.Model.Order;
import com.instadp.profilepicture.finalfoodappserver.Model.Request;
import com.instadp.profilepicture.finalfoodappserver.Model.Request1;
import com.instadp.profilepicture.finalfoodappserver.ViewHolder.OrderDetailViewHolder;
import com.instadp.profilepicture.finalfoodappserver.ViewHolder.OrderViewHolder;
import com.squareup.picasso.Picasso;

public class OrderDetail extends AppCompatActivity {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager  layoutManager;
    TextView prttl,prname,prnum,praltnum;
    FirebaseRecyclerAdapter<Order,OrderDetailViewHolder> adapter;
    FirebaseDatabase db;
    DatabaseReference requests,rq1;
Intent i;
String orderid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        i=getIntent();
        setContentView(R.layout.activity_order_detail);
//        getActionBar().setTitle(i.getStringExtra("ordid"));
        getSupportActionBar().setTitle(i.getStringExtra("ordid"));
        db=FirebaseDatabase.getInstance();
        requests=db.getReference("Requests").child(i.getStringExtra("ordid")).child("food");
        rq1=db.getReference("Request").child(i.getStringExtra("ordid"));
        recyclerView= (RecyclerView) findViewById(R.id.order_detail_recycleview);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
         loadOrder();
         loadDetails();
    }

    private void loadDetails() {
        praltnum= (TextView) findViewById(R.id.order_detail_main_altnumber);
        prname= (TextView) findViewById(R.id.order_detail_main_name);
        prttl= (TextView) findViewById(R.id.order_detail_main_total);
        prnum= (TextView) findViewById(R.id.order_detail_main_number);
        final DatabaseReference zonesRef1 = FirebaseDatabase.getInstance().getReference("Requests").child(i.getStringExtra("ordid")).child("address");
        DatabaseReference zonesRef2 = FirebaseDatabase.getInstance().getReference("Requests").child(i.getStringExtra("ordid")).child("name");
        DatabaseReference zonesRef3 = FirebaseDatabase.getInstance().getReference("Requests").child(i.getStringExtra("ordid")).child("phone");
        DatabaseReference zonesRef4 = FirebaseDatabase.getInstance().getReference("Requests").child(i.getStringExtra("ordid")).child("total");

        zonesRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //viewHolder.foodtotal.setText(dataSnapshot.getValue().toString());
                praltnum.setText("Alt-num : "+dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });zonesRef4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //viewHolder.foodtotal.setText(dataSnapshot.getValue().toString());
                prttl.setText("Total : ₹"+dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });zonesRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //viewHolder.foodtotal.setText(dataSnapshot.getValue().toString());
                prname.setText("Name : "+dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });zonesRef3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //viewHolder.foodtotal.setText(dataSnapshot.getValue().toString());
             prnum.setText("Number: "+dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadOrder() {
        adapter=new FirebaseRecyclerAdapter<Order, OrderDetailViewHolder>(
                Order.class,
                R.layout.order_detail_layout,
                OrderDetailViewHolder.class,
                requests
        ) {


            @Override
            protected void populateViewHolder(final OrderDetailViewHolder viewHolder, Order model, int position) {
                viewHolder.foodprice.setText("Price: ₹"+model.getPrice());
                viewHolder.foodname.setText(model.getProduceName());
                viewHolder.foodquantity.setText("Quantity : "+model.getQuantity());
                String total=Integer.parseInt(model.getQuantity())*Integer.parseInt(model.getPrice())+"";
                viewHolder.foodtotal.setText("Total Price: ₹"+total);
                DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("Food").child(model.getProductId()).child("image");
                zonesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //viewHolder.foodtotal.setText(dataSnapshot.getValue().toString());

                        Picasso.with(getBaseContext()).load(dataSnapshot.getValue().toString()).into(viewHolder.fooddetailimage);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

        };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

}
