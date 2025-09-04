package com.example.hustle;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.N)
public class DeleteBusFragment extends Fragment {

    View view;
    String Admin,check,date;
    Spinner bid;
    Button delete;
    ArrayList<String> ids= new ArrayList<String>();
    ArrayList<String> bidd = new ArrayList<String>();
    ArrayAdapter id_Adapter;
    ValueEventListener listener,listener1;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Buses");
    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Booking");

    final Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DAY_OF_MONTH);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_delete_bus, container, false);

        Admin = getArguments().getString("Admin_Data");
        bid = view.findViewById(R.id.bid);
        this.delete = view.findViewById(R.id.delete);
        id_Adapter = new ArrayAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item,ids);

        delete.setOnClickListener(view1 -> {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String del =bid.getSelectedItem().toString();
                    for (DataSnapshot Snapshot: snapshot.getChildren()) {
                        if(Snapshot.hasChild("Bus_Id")){
                            databaseReference.child(del).removeValue();
                            Toast.makeText(getActivity(), "Bus Deleted", Toast.LENGTH_SHORT).show();
                            ids.remove(del);
                            id_Adapter.notifyDataSetChanged();
                            break;
                        }
                    }
                 /*   Intent intent = new Intent(getActivity(),AdminMainPage.class);
                    intent.putExtra("Admin_Data",Admin);
                    startActivity(intent);*/
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });

        return view;
    }
    private int get_month(String toLowerCase) {
        switch (toLowerCase){
            case "jan":
                return 1;
            case "feb":
                return 2;
            case "mar":
                return 3;
            case "apr":
                return 4;
            case "may":
                return 5;
            case "jun":
                return 6;
            case "jul":
                return 7;
            case "aug":
                return 8;
            case "sep":
                return 9;
            case "oct":
                return 10;
            case "nov":
                return 11;
            case "dec":
                return 12;
            default:
                return 0;
        }
    }

    @Override
    public void onStart() {
        ids.clear();
        bidd.clear();
        listener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String val = Objects.requireNonNull(snapshot1.child("Bus_Id").getValue()).toString();
                    bidd.add(val);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        listener1 = databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (ds.hasChild("BusId")) {
                        check = Objects.requireNonNull(ds.child("BusId").getValue()).toString();
                        for (int i = bidd.size() - 1; i >= 0; i--) {
                            if (bidd.get(i).equals(check)) {
                                bidd.remove(i);
                                break;
                            }
                        }
                    }
                }
                for(int i =0;i<bidd.size();i++){
                    ids.add(i,bidd.get(i));
                }
                bid.setAdapter(id_Adapter);
                id_Adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e("error", error.getMessage());

            }
        });

        super.onStart();
    }
}