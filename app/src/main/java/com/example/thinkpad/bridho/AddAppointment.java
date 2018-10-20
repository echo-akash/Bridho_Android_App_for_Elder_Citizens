package com.example.thinkpad.bridho;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AddAppointment extends AppCompatActivity {

    private static final String TAG = "AddAppointment";
    private TextView mDisplayDate;
    private EditText etDName, etDAddr;
    private Button btnAppoint;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextView tvTime;
    AppointmentDataAdd helper;
    private Appointment appoint;
    String sName, sAddr, sDate, sTime;

    public static AddAppointment inst;

    public static AddAppointment instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        getSupportActionBar().setTitle("Add Appointment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        helper = new AppointmentDataAdd(this);


        btnAppoint = (Button) findViewById(R.id.btnAppoint);
        etDName = (EditText) findViewById(R.id.etDName);
        etDAddr = (EditText) findViewById(R.id.etDAddr);
        tvTime = (TextView) findViewById(R.id.tvtime);
        mDisplayDate = (TextView) findViewById(R.id.tvdate);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);



                DatePickerDialog dialog = new DatePickerDialog(
                        AddAppointment.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                Log.d(TAG, "onDateSet: mm/dd/yy: "+ month +"/" + day + "/"+year);
                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);

            }
        };

        btnAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllInputData();
                createAppointment();
                makeAppointment(appoint);
                setAppointRemind();
            }
        });





    }



    public void setTime(View view) {
        Calendar calender = Calendar.getInstance();
        int hour = calender.get(Calendar.HOUR);
        int minute = calender.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(
                AddAppointment.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tvTime.setText(hourOfDay+":"+minute);
            }
        },hour,minute,false);
        timePickerDialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id= item.getItemId();
        if (id==android.R.id.home){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    void getAllInputData(){
        sName = etDName.getText().toString();
        sAddr = etDAddr.getText().toString();
        sDate = mDisplayDate.getText().toString();
        sTime = tvTime.getText().toString();
    }

    void createAppointment(){
        appoint= new Appointment(sName, sAddr, sDate, sTime);
    }

    public void makeAppointment(Appointment appoint) {
        boolean insertData = helper.addAppoint(appoint);

        if (insertData) {
            toastMessage("Data Successfully Inserted!");
            Intent intent = new Intent(AddAppointment.this, Reminder.class);
            startActivity(intent);
        } else {
            toastMessage("Something went wrong");
        }
    }

    private void setAppointRemind(){

        Log.d(TAG, "Alarm Receiver set");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.clear();
        String date = mDisplayDate.getText().toString();
        String[] dateList = date.split("/");
        String time = tvTime.getText().toString();
        String[] timelist = time.split(":");

        String month = dateList[0];
        String day = dateList[1];
        String year = dateList[2];
        String hour = timelist[0];
        String minute = timelist[1];


        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        calendar.set(Calendar.MONTH, Integer.parseInt(month)-1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));

        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
        calendar.set(Calendar.MINUTE, Integer.parseInt(minute));

        toastMessage("Alarm set at" + " " +year +" "+ month +" "+ day +" "+ hour +" "+ minute);

        Intent intent = new Intent(AddAppointment.this,MedicineAlarmReceiver.class);
        toastMessage("1");
        intent.setAction("MY_APPOINT_NOTIFICATION_MESSAGE");
        toastMessage("2");
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),101,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        toastMessage("3");
        AlarmManager alarmManager=(AlarmManager) getSystemService(ALARM_SERVICE);
        toastMessage("4");
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

    }





    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

}
