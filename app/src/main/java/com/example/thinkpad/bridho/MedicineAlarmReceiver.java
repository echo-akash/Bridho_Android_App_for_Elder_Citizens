package com.example.thinkpad.bridho;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class MedicineAlarmReceiver extends BroadcastReceiver {

    String docname = "";
    @Override
    public void onReceive(Context arg0, Intent intent) {

        Toast.makeText(arg0, "11", Toast.LENGTH_SHORT).show();

        if (intent.getAction().equals("MY_NOTIFICATION_MESSAGE")) {
            Toast.makeText(arg0, "Receiver", Toast.LENGTH_SHORT).show();
            NotificationManager notificationManager=(NotificationManager) arg0.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder=new NotificationCompat.Builder(arg0)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Take your Medicine!!")
                    .setContentText("Visit "+docname+".")

                    .setAutoCancel(true);
            notificationManager.notify(100,builder.build());
        }else if (intent.getAction().equals("MY_APPOINT_NOTIFICATION_MESSAGE")){
            Toast.makeText(arg0, "Receiver", Toast.LENGTH_SHORT).show();
            NotificationManager notificationManager=(NotificationManager) arg0.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder=new NotificationCompat.Builder(arg0)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Go to Doctor")
                    .setContentText("Visit "+docname+".")

                    .setAutoCancel(true);
            notificationManager.notify(101,builder.build());
        }


    }


}









