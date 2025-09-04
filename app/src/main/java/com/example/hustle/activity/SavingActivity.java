package com.example.hustle.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.hustle.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SavingActivity extends AppCompatActivity {
    String Admin,bnumber,paisa,source,destination, boarding,departing,agency, ttime,attime,cat,ddtime,busID;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Buses");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving);
        Intent intent = getIntent();
        attime = intent.getStringExtra("Atime");
        boarding = intent.getStringExtra("board");
        busID = intent.getStringExtra("bid");
        bnumber = intent.getStringExtra("bno");
        cat = intent.getStringExtra("cat");
        ddtime = intent.getStringExtra("dtime");
        departing = intent.getStringExtra("depart");
        destination = intent.getStringExtra("dest");

        ttime = intent.getStringExtra("time");
        paisa = intent.getStringExtra("paisa");
        source = intent.getStringExtra("src");
        agency = intent.getStringExtra("agency");
        Admin = intent.getStringExtra("Admin_Data");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!busID.equals("")|| !busID.equals(null)) {
                    databaseReference.child(busID).child("Arrival_Time").setValue(attime);
                    databaseReference.child(busID).child("Boarding_Loc").setValue(boarding);
                    databaseReference.child(busID).child("Bus_Id").setValue(busID);
                    databaseReference.child(busID).child("Bus_No").setValue(bnumber);
                    databaseReference.child(busID).child("Category").setValue(cat);
                    databaseReference.child(busID).child("Departing_Loc").setValue(departing);
                    databaseReference.child(busID).child("Departure_Time").setValue(ddtime);
                    databaseReference.child(busID).child("Destination").setValue(destination);
                    databaseReference.child(busID).child("Duration").setValue(ttime.concat(" hrs"));
                    databaseReference.child(busID).child("Fare").setValue(paisa);
                    databaseReference.child(busID).child("Source").setValue(source);
                    databaseReference.child(busID).child("Travel_Agency").setValue(agency);
                    Toast.makeText(SavingActivity.this, "DATA SAVED!!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SavingActivity.this, AdminMainPage.class);
                    intent.putExtra("Admin_Data", Admin);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("error", error.getMessage());

            }

        });

    }
}