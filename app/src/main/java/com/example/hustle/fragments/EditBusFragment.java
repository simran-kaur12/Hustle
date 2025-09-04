package com.example.hustle.fragments;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.hustle.R;
import com.example.hustle.activity.SavingActivity;
import com.example.hustle.activity.AdminMainPage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.N)
public class EditBusFragment extends Fragment {

    View view;
    Spinner src, dest, category,bid;
    TextView srce, desti,asrc,adest,acat;
    int hour, min, hour1, min1;
    ValueEventListener listener,listener1;
    Button update,ok;
    String check,date;
    String AM_PM, AM_PM1,busID,Admin;
    EditText atime, duration, dtime, bno, board, depart, fare, tagency;
    ArrayList<String> ids= new ArrayList<>();
    ArrayList<String> bidd = new ArrayList<>();
    ArrayAdapter<CharSequence> route_Adapter;
    ArrayAdapter<CharSequence> cat_Adapter;
    ArrayAdapter id_Adapter;
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
        view = inflater.inflate(R.layout.fragment_edit_bus, container, false);

        Admin = getArguments().getString("Admin_Data");

        bid = view.findViewById(R.id.bid);
        src = view.findViewById(R.id.Source);
        dest = view.findViewById(R.id.Destination);
        acat = view.findViewById(R.id.ans_cat);
        asrc = view.findViewById(R.id.ans_src);
        adest = view.findViewById(R.id.ans_dest);
        atime = view.findViewById(R.id.arrival_time);
        dtime = view.findViewById(R.id.dtime);
        board = view.findViewById(R.id.board);
        depart = view.findViewById(R.id.depart);
        bno = view.findViewById(R.id.busNO);
        fare = view.findViewById(R.id.fare);
        tagency = view.findViewById(R.id.travel_com);
        update = view.findViewById(R.id.update);
        duration = view.findViewById(R.id.duration);
        ok = view.findViewById(R.id.okay);

        category = view.findViewById(R.id.Category);
        srce = view.findViewById(R.id.srce);
        desti = view.findViewById(R.id.desti);
        route_Adapter = ArrayAdapter.createFromResource(getActivity(), R.array.cities, R.layout.simple_spinner);
        cat_Adapter = ArrayAdapter.createFromResource(getActivity(), R.array.category, R.layout.simple_spinner);
        id_Adapter = new ArrayAdapter(getActivity(),R.layout.simple_spinner,ids);

        category.setAdapter(cat_Adapter);
        src.setAdapter(route_Adapter);
        dest.setAdapter(route_Adapter);

        ok.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), AdminMainPage.class);
            intent.putExtra("Admin_Data",Admin);
            startActivity(intent);
        });

        atime.setInputType(InputType.TYPE_NULL);
        dtime.setInputType(InputType.TYPE_NULL);

        bid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                  String busid = bid.getSelectedItem().toString();
                  databaseReference.child(busid).get().addOnCompleteListener(task -> {
                      if (task.isSuccessful()) {
                          DataSnapshot dataSnapshot = task.getResult();
                          if (dataSnapshot.hasChild("Source")) {
                              String srce = String.valueOf(dataSnapshot.child("Source").getValue());
                              asrc.setText(srce);
                          }
                          if (dataSnapshot.hasChild("Destination")) {
                              String destination = String.valueOf(dataSnapshot.child("Destination").getValue());
                              adest.setText(destination);
                          }

                          if (dataSnapshot.hasChild("Arrival_Time")) {
                              String Atime = String.valueOf(dataSnapshot.child("Arrival_Time").getValue());
                              atime.setText(Atime);
                          }
                          if (dataSnapshot.hasChild("Departure_Time")) {
                              String Dtime = String.valueOf(dataSnapshot.child("Departure_Time").getValue());
                              dtime.setText(Dtime);
                          }
                          if (dataSnapshot.hasChild("Boarding_Loc")) {
                              String Board_l = String.valueOf(dataSnapshot.child("Boarding_Loc").getValue());
                              board.setText(Board_l);
                          }
                          if (dataSnapshot.hasChild("Departing_Loc")) {
                              String Depart_l = String.valueOf(dataSnapshot.child("Departing_Loc").getValue());
                              depart.setText(Depart_l);
                          }
                          if (dataSnapshot.hasChild("Bus_No")) {
                              String BNO = String.valueOf(dataSnapshot.child("Bus_No").getValue());
                              bno.setText(BNO);
                          }
                          if (dataSnapshot.hasChild("Duration")) {
                              String dur = String.valueOf(dataSnapshot.child("Duration").getValue());
                              duration.setText(dur);
                          }
                          if (dataSnapshot.hasChild("Category")) {
                              String cat = String.valueOf(dataSnapshot.child("Category").getValue());
                              acat.setText(cat);
                          }
                          if (dataSnapshot.hasChild("Travel_Agency")) {
                              String agency = String.valueOf(dataSnapshot.child("Travel_Agency").getValue());
                              tagency.setText(agency);
                          }
                          if (dataSnapshot.hasChild("Fare")) {
                              String paisa = String.valueOf(dataSnapshot.child("Fare").getValue());
                              fare.setText(paisa);
                          }

                      } else {
                          Toast.makeText(getActivity(), "Failed to Retrieve Data", Toast.LENGTH_SHORT).show();
                      }
                  });
              }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        update.setOnClickListener(view1 -> {
            int i = validate();
            if (i == 1) {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
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
                        busID = bid.getSelectedItem().toString();
                        Intent intent1 = new Intent(getActivity(), SavingActivity.class);
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

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("error", error.getMessage());

                    }

                });
            }
        });

        atime.setOnClickListener(view1 -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTimeSet(TimePicker timePicker, int hourofday, int minute) {
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
                }
            }, 12, 0, false
            );
            timePickerDialog.updateTime(hour, min);
            timePickerDialog.show();
        });
        dtime.setOnClickListener(view1 -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTimeSet(TimePicker timePicker, int hourofday, int minute) {
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
                    dtime.setText(hourofday + ":" + minute + " " + AM_PM1);
                }
            }, 12, 0, false
            );
            timePickerDialog.updateTime(hour1, min1);
            timePickerDialog.show();
        });

        return view;
    }

    @Override
    public void onStart() {
        ids.clear();
        bidd.clear();
        listener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if(snapshot1.child("Bus_Id").exists()) {
                        String val = Objects.requireNonNull(snapshot1.child("Bus_Id").getValue()).toString();
                        bidd.add(val);
                    }
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
                        date = Objects.requireNonNull(ds.child("Travel_Date").getValue()).toString();
                        if(!tcheck(date)) {
                            for (int i = bidd.size() - 1; i >= 0; i--) {
                                if (bidd.get(i).equals(check)) {
                                    bidd.remove(i);
                                    break;
                                }
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

            private boolean tcheck(String ttdate) {
                String datee,monthh,yearr;
                int dd,mm,yyyy;
                String tdate = ttdate.replace(" ","");
                monthh = tdate.substring(0, 3);
                mm = get_month(monthh.toLowerCase(Locale.ROOT));
                if(tdate.length()==9) {
                    datee = tdate.substring(3, 5);
                    yearr = tdate.substring(5, tdate.length());
                }
                else{
                    datee = tdate.substring(3);
                    yearr =tdate.substring(4,tdate.length());
                }
                dd = Integer.parseInt(datee);
                yyyy = Integer.parseInt(yearr);

                if(yyyy<year) {return true;}
                else {
                    if (yyyy == year) {
                        if(mm<month+1){
                            return true;  }
                        else{
                            if(mm==month+1){
                                return dd < day;
                            }

                        }
                    }
                }return false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e("error", error.getMessage());

            }
        });

        super.onStart();
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

    private int validate() {

        if (src.getSelectedItem().toString().equals(dest.getSelectedItem().toString())) {
            srce.setError("SAME SOURCE AND DESTINATION");
            desti.setError("SAME SOURCE AND DESTINATION");
            return 0;
        } else if (atime.getText().toString().isEmpty() || dtime.getText().toString().isEmpty()
                || fare.getText().toString().isEmpty() || board.getText().toString().isEmpty() ||
                depart.getText().toString().isEmpty() || tagency.getText().toString().isEmpty() ||
                bno.getText().toString().isEmpty() || duration.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Fill all the Fields", Toast.LENGTH_SHORT).show();
            return 0;
        } else if (atime.getText().toString().equals(dtime.getText().toString())) {
            dtime.setError("Arrival and Departure time can't be same");
            atime.setText("");
            dtime.setText("");
            return 0;
        } else if(AM_PM!=null && AM_PM1!=null){
            if (AM_PM.equals(AM_PM1)) {
                if (hour1 < hour) {
                    dtime.setError("Departure time should be more!!");
                    return 0;
                } else if (min1 < min) {
                    dtime.setError("Departure time should be more!!");
                    return 0;
                }
            }
        } else if(AM_PM!=null && AM_PM1!=null){
            if (AM_PM.equals("PM") && AM_PM1.equals("AM")) {
                dtime.setError("Departure time should be more!!");
                return 0;
            }
        } else if (!bno.getText().toString().matches("^[A-Z]{2}\\s[0-9]{2}\\s[A-Z]{2}\\s[0-9]{4}$")) {
            bno.setError("Not a Valid Bus number");
            return 0;
        } else if (!(fare.getText().toString().length() != 3 || fare.getText().toString().length() != 4)) {
            fare.setError("NOT A VALID FORMAT LIKE '300 OR 4000'");
            return 0;
        }
        return 1;
    }

}