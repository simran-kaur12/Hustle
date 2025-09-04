package com.example.hustle.activity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hustle.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Objects;

public class add_bus extends AppCompatActivity {
    Spinner src, dest, category;
    TextView bid, srce, desti;
    int hour, min,hour1,min1;
    Button sub;
    int id, bids;
    String AM_PM,AM_PM1;
    String busID;
    String Admin;
    EditText atime, duration, dtime, bno, board, depart, fare, tagency;
    ArrayList<String> ids = new ArrayList<>();
    ArrayAdapter<CharSequence> route_Adapter, cat_Adapter;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Buses");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus);
        Intent intent = getIntent();
        Admin = intent.getStringExtra("Admin_Data");

        bid = findViewById(R.id.bid);
        src = findViewById(R.id.Source);
        dest = findViewById(R.id.Destination);

        atime = findViewById(R.id.arrival_time);
        dtime = findViewById(R.id.dtime);
        board = findViewById(R.id.board);
        depart = findViewById(R.id.depart);
        bno =findViewById(R.id.busNO);
        fare = findViewById(R.id.fare);
        tagency = findViewById(R.id.travel_com);
        sub = findViewById(R.id.submit);
        duration = findViewById(R.id.duration);
        atime.setInputType(InputType.TYPE_NULL);
        dtime.setInputType(InputType.TYPE_NULL);
        category = findViewById(R.id.Category);
        srce = findViewById(R.id.srce);
        desti = findViewById(R.id.desti);
        route_Adapter = ArrayAdapter.createFromResource(this, R.array.cities, R.layout.simple_spinner);
        cat_Adapter = ArrayAdapter.createFromResource(this, R.array.category, R.layout.simple_spinner);
        category.setAdapter(cat_Adapter);
        src.setAdapter(route_Adapter);
        dest.setAdapter(route_Adapter);

        sub.setOnClickListener(view1 -> {
            int i = validate();
            if(i==1 && !bid.getText().toString().equals("BUS ID WILL BE SHOWN HERE")){
                Toast.makeText(add_bus.this,"yes ",Toast.LENGTH_SHORT).show();
                String bnumber = bno.getText().toString();
                String paisa = fare.getText().toString();
                String source,destination;
                source = src.getSelectedItem().toString();
                destination = dest.getSelectedItem().toString();
                String boarding = board.getText().toString();
                String departing = depart.getText().toString();
                String agency = tagency.getText().toString();
                String ttime = duration.getText().toString();
                String attime = atime.getText().toString();
                String cat = category.getSelectedItem().toString();
                String ddtime = dtime.getText().toString();
                busID = bid.getText().toString();
                Intent intent1 = new Intent(add_bus.this, SavingActivity.class);
                intent1.putExtra("Atime",attime);
                intent1.putExtra("board",boarding);
                intent1.putExtra("bid",busID);
                intent1.putExtra("bno",bnumber);
                intent1.putExtra("cat",cat);
                intent1.putExtra("depart",departing);
                intent1.putExtra("dtime",ddtime);
                intent1.putExtra("dest",destination);
                intent1.putExtra("time",ttime);
                intent1.putExtra("paisa",paisa);
                intent1.putExtra("src",source);
                intent1.putExtra("agency",agency);
                intent1.putExtra("Admin_Data",Admin);
                startActivity(intent1);
            }
        });

        atime.setOnClickListener(view1 -> {
            @SuppressLint("SetTextI18n") TimePickerDialog timePickerDialog = new TimePickerDialog(add_bus.this, (timePicker, hourofday, minute) -> {
                if (hourofday > 12) {
                    hourofday = hourofday - 12;
                    AM_PM = "PM";
                } else if(hourofday==0) {
                    hourofday = 12;
                    AM_PM = "AM";
                }
                else if(hourofday==12){
                    AM_PM = "PM";
                }
                else{
                    AM_PM = "AM";
                }
                hour = hourofday;
                min = minute;
                atime.setText(hourofday + ":" + minute + " " + AM_PM);
            }, 12, 0, false
            );
            timePickerDialog.updateTime(hour, min);
            timePickerDialog.show();
        });
        dtime.setOnClickListener(view1 -> {
            @SuppressLint("SetTextI18n") TimePickerDialog timePickerDialog = new TimePickerDialog(add_bus.this, (timePicker, hourofday, minute) -> {
                if (hourofday > 12) {
                    hourofday = hourofday - 12;
                    AM_PM1 = "PM";
                } else if(hourofday==0) {
                    hourofday = 12;
                    AM_PM1 = "AM";
                }
                else if(hourofday==12){
                    AM_PM1 = "PM";
                }
                else{
                    AM_PM1 = "AM";
                }
                hour1 = hourofday;
                min1 = minute;
                dtime.setText(hourofday + ":" + minute + " " + AM_PM);
            }, 12, 0, false
            );
            timePickerDialog.updateTime(hour1, min1);
            timePickerDialog.show();
        });

    }

    @Override
    public void onStart() {
        ArrayList<Integer> maxid = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if(snapshot1.hasChild("Bus_Id")) {
                        ids.add(Objects.requireNonNull(snapshot1.child("Bus_Id").getValue()).toString());
                        id = ids.size();
                        String buusID = ids.get(id-1);
                        buusID = buusID.substring(2);
                        bids = Integer.parseInt(buusID);
                        maxid.add(bids);
                    }
                }
                int big = maxid.get(0);
                for(int i= 0;i<maxid.size();i++) {
                    if(big<maxid.get(i))
                        big = maxid.get(i);
                }
                big  = big + 1 ;
                busID = "B-";
                busID = busID.concat(String.valueOf(big));
                bid.setText(busID);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("error", error.getMessage());

            }

        });
        super.onStart();
    }

    private int validate() {
        String bnumber = bno.getText().toString();
        String paisa = fare.getText().toString();
        String source,destination;
        source = src.getSelectedItem().toString();
        destination = dest.getSelectedItem().toString();
        String boarding = board.getText().toString();
        String departing = depart.getText().toString();
        String agency = tagency.getText().toString();
        String ttime = duration.getText().toString();
        String attime = atime.getText().toString();
        String ddtime = dtime.getText().toString();
        if (source.equals(destination)) {
            srce.setError("SAME SOURCE AND DESTINATION");
            desti.setError("SAME SOURCE AND DESTINATION");
            return 0;
        } else if (attime.isEmpty() || ddtime.isEmpty()
                || paisa.isEmpty() ||boarding.isEmpty() ||
                departing.isEmpty() || agency.isEmpty() ||
                bnumber.isEmpty() || ttime.isEmpty()) {
            Toast.makeText(add_bus.this, "Fill all the Fields", Toast.LENGTH_SHORT).show();
            return 0;
        }
        else if (attime.equals(ddtime)) {
            dtime.setError("Arrival and Departure time can't be same");
            atime.setText("");
            dtime.setText("");
            return 0;
        }else if(!tcheck()){
            dtime.setError("Departure time should be more!");
        }
        else if (!bnumber.matches("^[A-Z]{2}\\s[0-9]{2}\\s[A-Z]{2}\\s[0-9]{4}$")) {
            bno.setError("Not a Valid Bus number");
            return 0;
        } else {
            if (paisa.length() == 3) {
                paisa.length();
            }
        }
        return 1;
    }

    private boolean tcheck() {
        if(AM_PM.equals(AM_PM1)){
            if(hour1<hour){
                return hour == 12;
            }else return min1 >= min;

        }else return !AM_PM.equals("PM") || !AM_PM1.equals("AM");
    }

}
