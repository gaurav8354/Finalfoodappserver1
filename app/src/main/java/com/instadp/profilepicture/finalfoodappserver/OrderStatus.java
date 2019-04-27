package com.instadp.profilepicture.finalfoodappserver;

import android.app.DownloadManager;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.instadp.profilepicture.finalfoodappserver.Common.Common;
import com.instadp.profilepicture.finalfoodappserver.Interface.ItemOnClickListner;
import com.instadp.profilepicture.finalfoodappserver.Model.Request;
import com.instadp.profilepicture.finalfoodappserver.ViewHolder.OrderViewHolder;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.Arrays;

public class OrderStatus extends AppCompatActivity {
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager  layoutManager;
    FirebaseRecyclerAdapter<Request,OrderViewHolder> adapter;
    FirebaseDatabase db;
    DatabaseReference requests;
    MaterialSpinner spinner;
    String subname="";
    PubNub pubnub1;
    PNStatus status1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        db=FirebaseDatabase.getInstance();
        requests=db.getReference("Requests");
        recyclerView= (RecyclerView) findViewById(R.id.listOrder);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        loadOrder();
        Toast.makeText(this,subname, Toast.LENGTH_SHORT).show();
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey("sub-c-6c09116c-26f7-11e8-ae0d-3a0cea6ae1c4");
        pnConfiguration.setPublishKey("pub-c-622112ad-f5c5-4d3e-9592-ced57cb89ee1");
        PubNub pubnub = new PubNub(pnConfiguration);
        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
                pubnub1=pubnub;
                status1=status;

                if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
                    // This event happens when radio / connectivity is lost
                    Toast.makeText(OrderStatus.this, "not connected sucessfully", Toast.LENGTH_SHORT).show();
                }

                else if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {

                    Toast.makeText(OrderStatus.this, "sucessfully connected", Toast.LENGTH_SHORT).show();

                }
                else if (status.getCategory() == PNStatusCategory.PNReconnectedCategory) {

                    // Happens as part of our regular operation. This event happens when
                    // radio / connectivity is lost, then regained.
                }
                else if (status.getCategory() == PNStatusCategory.PNDecryptionErrorCategory) {

                    // Handle messsage decryption error. Probably client configured to
                    // encrypt messages and on live data feed it received plain text.
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                // Handle new message stored in message.message
                if (message.getChannel() != null) {
                    // Message has been received on channel group stored in
                    // message.getChannel()
                }
                else {
                    // Message has been received on channel stored in
                    // message.getSubscription()
                }

            /*
                log the following items with your favorite logger
                    - message.getMessage()
                    - message.getSubscription()
                    - message.getTimetoken()
            */
            }
            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });
        pubnub.subscribe().channels(Arrays.asList("Msg")).execute();
    }

    private void loadOrder() {
        adapter=new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {
                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderAddress.setText(model.getAddress());
                viewHolder.txtOrderPhone.setText(model.getPhone());
                viewHolder.setItemOnClickListner(new ItemOnClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                    }
                });

            }

        };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle().equals(Common.UPDATE)){
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
    }
    else if(item.getTitle().equals(Common.DELETE))
        {
            deleteOrder(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog(String key, final Request item) {
       AlertDialog.Builder alertDialog=new AlertDialog.Builder(OrderStatus.this);
        alertDialog.setTitle("Update Order");
        alertDialog.setMessage("Please  choose status");
        LayoutInflater inflater=this.getLayoutInflater();
        final View view=inflater.inflate(R.layout.update_order_layout,null);
        spinner= (MaterialSpinner) view.findViewById(R.id.statusSpinner);
        spinner.setItems("Placed","Accepted","Ready");
        alertDialog.setView(view);
        final String localkey=key;
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.setStatus(String.valueOf(spinner.getSelectedIndex()));
            requests.child(localkey).setValue(item);
         //my logic clg
            if   (String.valueOf(spinner.getSelectedIndex()).equals("2"))
                {
                    int splash_display=2000;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (status1.getCategory() == PNStatusCategory.PNConnectedCategory){
                                pubnub1.publish().channel("Msg").message(localkey).async(new PNCallback<PNPublishResult>() {
                                    @Override
                                    public void onResponse(PNPublishResult result, PNStatus status) {
                                        // Check whether request successfully completed or not.
                                        if (!status.isError()) {
                                            // Message successfully published to specified channel.
                                            Toast.makeText(OrderStatus.this, "message published sucessfully", Toast.LENGTH_SHORT).show();
                                        }
                                        // Request processing failed.
                                        else {
                                            Toast.makeText(OrderStatus.this, "not published", Toast.LENGTH_SHORT).show();
                                            // Handle message publish error. Check 'category' property to find out possible issue
                                            // because of which request did fail.
                                            //
                                            // Request can be resent using: [status retry];
                                        }
                                    }
                                });}

                        }
                    },splash_display);

                }
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
alertDialog.show();
    }
    private void deleteOrder(String key) {

    requests.child(key).removeValue();

    }
}

