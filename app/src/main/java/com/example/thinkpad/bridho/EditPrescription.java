package com.example.thinkpad.bridho;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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


public class EditPrescription extends AppCompatActivity {

    private EditText etDAddr, etDName;
    private Button btnDelete, btnAppoint;
    private static final String TAG = "EditPrescription";
    AppointmentDataAdd helper;
    private String sName, sAddr, sDate, sTime;
    private int selectedID;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextView tvTime;
    private TextView mDisplayDate;
    private Appointment appoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_prescription);

        getSupportActionBar().setTitle("Edit Prescription");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        etDAddr = (EditText)findViewById(R.id.etDAddr);
        etDName = (EditText)findViewById(R.id.etDName);
        btnAppoint = (Button)findViewById(R.id.btnAppoint);
        btnDelete =(Button)findViewById(R.id.btnDelete);
        helper = new AppointmentDataAdd(this);



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
                        EditPrescription.this,
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

        //get the intent extra from the ListDataActivity
        Intent receivedIntent = getIntent();

        //now get the itemID we passed as an extra
        selectedID = receivedIntent.getIntExtra("id",-1); //NOTE: -1 is just the default value

        Cursor cursor = helper.fetch(selectedID);
        cursor.moveToFirst();
        etDName = findViewById(R.id.etDName);
        etDName.setText(cursor.getString(0));
        etDAddr = findViewById(R.id.etDAddr);
        etDAddr.setText(cursor.getString(1));
        mDisplayDate = findViewById(R.id.tvdate);
        mDisplayDate.setText(cursor.getString(2));
        tvTime = findViewById(R.id.tvtime);
        tvTime.setText(cursor.getString(3));

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //toastMessage(selectedID + sName);
                boolean del = helper.deleteName(selectedID,sName);
                //etDName.setText("");
                if(del) {
                    toastMessage("removed from database");
                    Intent intent = new Intent(EditPrescription.this, ViewAppointment.class);
                    startActivity(intent);
                    //cancelAlarm();
                }else {
                    toastMessage("Not removed from DB");
                }
            }
        });

        btnAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllInputData();
                createAppointment();
                updateAppointment(appoint, selectedID);
            }
        });

    }
    public void setTime(View view) {
        Calendar calender = Calendar.getInstance();
        int hour = calender.get(Calendar.HOUR);
        int minute = calender.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(
                EditPrescription.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tvTime.setText(hourOfDay+":"+minute);
            }
        },hour,minute,false);
        timePickerDialog.show();

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

    public void updateAppointment(Appointment appoint, int selectedID) {
        boolean insertData = helper.updateAppoint(appoint, selectedID);

        if (insertData) {
            toastMessage("Data Updated Inserted!");
            Intent intent = new Intent(EditPrescription.this, ViewAppointment.class);
            startActivity(intent);
        } else {
            toastMessage("Something went wrong");
        }
    }



    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id= item.getItemId();
        /*if (id==android.R.id.home){
            this.finish();
        }*/
        Intent intent = new Intent(EditPrescription.this, ViewAppointment.class);
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }
}
