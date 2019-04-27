package com.instadp.profilepicture.finalfoodappserver.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.instadp.profilepicture.finalfoodappserver.Model.Request;
import com.instadp.profilepicture.finalfoodappserver.OrderStatus;
import com.instadp.profilepicture.finalfoodappserver.R;

import java.util.Random;

public class ListenOrder extends Service implements ChildEventListener {

    FirebaseDatabase db;
    DatabaseReference orders;

    @Override
    public void onCreate() {
        super.onCreate();
        db=FirebaseDatabase.getInstance();
        orders=db.getReference("Requests");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        orders.addChildEventListener(this);
        return super.onStartCommand(intent, flags, startId);

    }

    public ListenOrder() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        return null;

    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

        Request request=dataSnapshot.getValue(Request.class);
        if(request.getStatus().equals("0"))
        {
            showNotification(dataSnapshot.getKey(),Request.class);
        }
    }

    private void showNotification(String key, Class<Request> requestClass) {
    Intent intent=new Intent(getBaseContext(), OrderStatus.class);
        PendingIntent contentintent=PendingIntent.getActivity(getBaseContext(),0,intent,0);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext());
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setTicker("Food now")
                .setContentInfo("New Order")
                .setContentText("You have recived new Order")
                .setSmallIcon(R.drawable.iconmy);
        NotificationManager manager= (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                int randomint=new Random().nextInt(0000-1)+1;

        manager.notify(randomint,builder.build());
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
