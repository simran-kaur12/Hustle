package com.example.hustle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Available_buses extends AppCompatActivity implements RecyclerViewInterface {
    RecyclerView recyclerView;
    BusAdapter busAdapter;
    String user,source,destination,t_date;
    ImageView imageViewBack,interchange;
    TextView src,dest,travel,ar_time,com,category,price,busNumber,totalTime;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://hustle-6af47-default-rtdb.firebaseio.com/");
    MainModel mainModel;

    ArrayList<String> al_source, al_destination;
    ArrayList<MainModel> al_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_buses);

        recyclerView = findViewById(R.id.bus_info);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        al_source=new ArrayList<>();
        al_destination=new ArrayList<>();
        al_main=new ArrayList<>();

        src = findViewById(R.id.Source);
        dest = findViewById(R.id.Destination);
        travel = findViewById(R.id.travel_date);
        interchange = findViewById(R.id.change);

        ar_time = findViewById(R.id.arrival_time);
        com = findViewById(R.id.travel_com);
        category = findViewById(R.id.ac_nonAC);
        totalTime = findViewById(R.id.duration);
        price = findViewById(R.id.fare);
        busNumber = findViewById(R.id.busNo);

        Intent intent = getIntent();
        user = intent.getStringExtra("data");
        source = intent.getStringExtra("Src");
        destination = intent.getStringExtra("Dest");
        t_date = intent.getStringExtra("Travel_date");

        src.setText(source);
        dest.setText(destination);
        travel.setText(t_date);

        imageViewBack = findViewById(R.id.back);
        imageViewBack.setOnClickListener(view -> {
            Intent intent1 = new Intent(Available_buses.this, DashBoard.class);
            intent1.putExtra("data", user);
            startActivity(intent1);
        });

        interchange.setOnClickListener(view -> {
            String temp = source;
            source = destination;
            destination = temp;
            src.setText(source);
            dest.setText(destination);

            fetch_data();

        });

        fetch_data();

    }

    private void fetch_data() {

        al_main.clear();
        databaseReference.child("Buses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds : snapshot.getChildren())
                {
                     mainModel = ds.getValue(MainModel.class);

                    assert mainModel != null;
                    if(mainModel.getSource().equalsIgnoreCase(source) && mainModel.getDestination().equalsIgnoreCase(destination))
                    {
                        al_main.add(mainModel);
                    }
                }

                setAdapter();
                recyclerView.setAdapter(busAdapter);
                if(al_main.isEmpty()){
                    Toast.makeText(Available_buses.this, "NO BUSES ONN THIS ROUTE!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e("error", error.getMessage());

            }
        });
    }

    private void setAdapter() {
        busAdapter = new BusAdapter(this, Available_buses.this, al_main);
    }

    @Override
    public void onItemClick(int pos) {
        Intent intent = new Intent(Available_buses.this,Booking.class);
        mainModel = al_main.get(pos);
        intent.putExtra("Bus Id",mainModel.getBus_Id());
        intent.putExtra("data",user);
        intent.putExtra("Travel_date",t_date);
        startActivity(intent);
    }
}