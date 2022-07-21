package com.example.hustle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
public class frag_Reschdule extends Fragment {

    boolean init = true;
    View view;
    String data,d,book;
    TextView sdate,date;
    Spinner bookId;
    Button pick, reschedule;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    ImageView back;
    ArrayList<String> ticket_list = new ArrayList<>();
    ArrayAdapter ticket_arrayAdapter;
    ValueEventListener listener;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Booking");

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_reschdule, container, false);

        final Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);


        data = getArguments().getString("data");


        bookId = view.findViewById(R.id.book);
        date = view.findViewById(R.id.travel_date);
        back = view.findViewById(R.id.back);
        pick = view.findViewById(R.id.date_pick);
        reschedule = view.findViewById(R.id.reschdule);
        sdate = view.findViewById(R.id.s_date);
        ticket_arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, ticket_list);
        bookId.setAdapter(ticket_arrayAdapter);
        fetch_data();

        pick.setOnClickListener(view1 -> {
            reschedule.setEnabled(true);
            @SuppressLint("ResourceType") DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    android.R.color.white, onDateSetListener, year, month,day);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
            datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
            datePickerDialog.show();
        });
        onDateSetListener = (datePicker, year1, month1, day1) -> {
            month1 = month1 + 1;
            d = makeDateString(day1, month1, year1);
            pick.setText("Selected Date is:-");
            sdate.setText(d);
        };

        reschedule.setOnClickListener(view1 -> {
            if(sdate.getText().toString().equals("Selected Date is:-") )
                sdate.setError("Select a date");
            else{
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        databaseReference.child(bookId.getSelectedItem().toString()).child("Travel_Date").setValue(sdate.getText().toString());
                        Toast.makeText(getActivity(), "!!TRAVEL DATE UPDATED!!", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("\tBOOKING ID:-"+book+"\nClick OK to Download the TICKET!");
                        builder.setTitle("Alert! NOT DOWN BOOKING ID TO DOWNLOAD TICKET!!");
                        builder.setCancelable(false);
                        builder
                                .setPositiveButton(
                                        "OK",
                                        new DialogInterface
                                                .OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int which)
                                            {
                                                Intent intent = new Intent(getActivity(),ticket.class);
                                                intent.putExtra("bookID",book);
                                                startActivity(intent);
                                            }
                                        });

                        // Create the Alert dialog
                        AlertDialog alertDialog = builder.create();

                        // Show the Alert Dialog box
                        alertDialog.show();

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        bookId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                reschedule.setEnabled(true);
                    book = bookId.getSelectedItem().toString();
                    databaseReference.child(book).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DataSnapshot dataSnapshot = task.getResult();
                            String ttdate = String.valueOf(dataSnapshot.child("Travel_Date").getValue());
                            date.setText(ttdate);
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

                            if(yyyy<year) {
                                Toast.makeText(getActivity(), "Can't Change Past Date", Toast.LENGTH_SHORT).show();
                                reschedule.setEnabled(false);
                            }
                            else {
                                if (yyyy == year) {
                                    if(mm<month+1){
                                        Toast.makeText(getActivity(), "Can't Change Past Date", Toast.LENGTH_SHORT).show();
                                        reschedule.setEnabled(false);
                                    }else{
                                        if(mm==month+1){
                                            if(dd<day){
                                                reschedule.setEnabled(false);
                                                Toast.makeText(getActivity(), "Can't Change Past Date", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                if(dd<day){
                                                    reschedule.setEnabled(false);
                                                    Toast.makeText(getActivity(), "Can't Change Past Date", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        }
                                }
                            }


                        }
                    });

            }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
        });

        back.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(),DashBoard.class);
            intent.putExtra("data",data);
            startActivity(intent);

        });
        return view;
    }

    private void fetch_data() {

        listener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if(snapshot1.hasChild("UserName")) {
                        String user = Objects.requireNonNull(snapshot1.child("UserName").getValue()).toString();

                        if (user.equals(data)) {
                            String val = Objects.requireNonNull(snapshot1.child("BookID").getValue()).toString();
                            ticket_list.add(val);
                        }
                    }
                }
                ticket_arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private String makeDateString(int day, int month, int year) {
        return get_monthFormat(month)+" "+ day+" "+" "+year;
    }

    private String get_monthFormat(int month) {
        switch (month){
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
            default:
                throw new IllegalStateException("Unexpected value: " + month);
        }
    }
    private int get_month(String month) {
        switch (month){
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

}