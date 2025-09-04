    package com.example.hustle;

    import androidx.annotation.NonNull;
    import androidx.annotation.RequiresApi;
    import androidx.appcompat.app.AlertDialog;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.fragment.app.FragmentTransaction;

    import android.annotation.SuppressLint;
    import android.app.DatePickerDialog;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.graphics.Color;
    import android.graphics.drawable.ColorDrawable;
    import android.icu.util.Calendar;
    import android.os.Build;
    import android.os.Bundle;
    import android.view.View;
    import android.view.Window;
    import android.view.WindowManager;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.ImageView;
    import android.widget.PopupMenu;
    import android.widget.Spinner;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    import java.util.ArrayList;
    import java.util.Objects;

    public class DashBoard extends AppCompatActivity {
        Button search_buses;
        Button date;
        DatePickerDialog.OnDateSetListener onDateSetListener;
        TextView name,s_date,head,logo;
        ImageView menu;
        String src_bid,dest_bid;
        int flag =0;
        String data,d;
        SharedPreferences sharedPreferences,sharedPreferences1;
        SharedPreferences.Editor editor,editor1;
        Spinner src,dest;
        ValueEventListener listener;
        ArrayList<String> src_list,dest_list = new ArrayList<>();
        ArrayAdapter<String> src_arrayAdapter, dest_arrayAdapter;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Buses");
        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            sharedPreferences = getSharedPreferences("Uid_share",MODE_PRIVATE);
            editor = sharedPreferences.edit();
            sharedPreferences1 = getSharedPreferences("Email_share",MODE_PRIVATE);
            editor1 = sharedPreferences1.edit();


            final Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_dash_board);

            logo = findViewById(R.id.image_view_logo);
            src= findViewById(R.id.Source);
            dest = findViewById(R.id.Destination);
            name = findViewById(R.id.uname);
            head = findViewById(R.id.textView2);

            menu = findViewById(R.id.menu);
            s_date = findViewById(R.id.s_date);
            src_list = new ArrayList<>();
            src_arrayAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,src_list);
            dest_arrayAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,dest_list);
            src.setAdapter(src_arrayAdapter);
            dest.setAdapter(dest_arrayAdapter);

            Intent intent = getIntent();
            data = intent.getStringExtra("data");

            databaseReference.child(data).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    String Username = String.valueOf(dataSnapshot.child("User Name").getValue());
                    name.setText("Hey " + Username + "! ");
                } else {
                    Toast.makeText(DashBoard.this, "Fail", Toast.LENGTH_SHORT).show();
                }
            });
            fetch_data();

            logo.setOnClickListener(view -> {
                Intent in = new Intent(getApplicationContext(),DashBoard.class);
                in.putExtra("data",data);
                startActivity(in);
            });

            menu.setOnClickListener(view->{
                PopupMenu popupMenu = new PopupMenu(DashBoard.this,view);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    if(menuItem.getItemId() == R.id.profile) {
                        Intent intent1 = new Intent(DashBoard.this, Profile.class);
                        if(data.matches("^\\d{10}$")) {
                            intent1.putExtra("mobile", "way1");
                            intent1.putExtra("Phone", data);
                        }else{
                            intent1.putExtra("mail", "way2");
                            intent1.putExtra("Email", data);
                        }
                        startActivity(intent1);
                    }
                    if(menuItem.getItemId() == R.id.change_date) {
                        head.setText("Change\t Travel\t Date");
                        frag_Reschdule reschdule = new frag_Reschdule();
                        Bundle args = new Bundle();
                        args.putString("data", data);
                        reschdule.setArguments(args);
                        search_buses.setVisibility(View.GONE);
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_layout, reschdule).addToBackStack(null).commit();

                    }
                    if(menuItem.getItemId() == R.id.download) {
                        head.setText("GET \tYOUR \tTICKET ");
                        DownloadFragment downloadFragment = new DownloadFragment();
                        Bundle args = new Bundle();
                        args.putString("data", data);
                        downloadFragment.setArguments(args);
                        search_buses.setVisibility(View.GONE);
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_layout, downloadFragment).addToBackStack(null).commit();

                    }

                    if(menuItem.getItemId() == R.id.exit) {
                        editor.clear();
                        editor.commit();
                        editor1.clear();
                        editor1.commit();
                        startActivity(new Intent(DashBoard.this,Login.class));
                    }
                    return true;
                });
                popupMenu.show();
            });

            search_buses = findViewById(R.id.search_buses);
            date = findViewById(R.id.date_pick);

            date.setOnClickListener(view -> {
                @SuppressLint("ResourceType") DatePickerDialog datePickerDialog = new DatePickerDialog(DashBoard.this,
                        android.R.color.white, onDateSetListener, year, month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                datePickerDialog.show();
            });
            onDateSetListener = (datePicker, year1, month1, day1) -> {
                month1 = month1 + 1;
                d = makeDateString(day1, month1, year1);
                date.setText("Selected Date is:-");
                s_date.setText(d);
            };

            search_buses.setOnClickListener(view -> {
                if(!s_date.getText().toString().equals("Selected Date will be shown here") ) {
                    validate_info();
                }
                else
                    s_date.setError("Select a date");
            });
        }

        private  void validate_info() {
            listener = databaseReference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        String busId = (String) snapshot1.child("Bus_Id").getValue();
                        assert busId != null;
                        databaseReference1.child(busId).get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DataSnapshot dataSnapshot = task.getResult();
                                src_bid = Objects.requireNonNull(dataSnapshot.child("Source").getValue()).toString();
                                dest_bid = Objects.requireNonNull(dataSnapshot.child("Destination").getValue()).toString();
                                if(src_bid.equals(src.getSelectedItem().toString()) && dest_bid.equals(dest.getSelectedItem().toString())) {
                                    flag=0;
                                    Intent intent1 = new Intent(getApplicationContext(), Available_buses.class);
                                    intent1.putExtra("Src",src.getSelectedItem().toString());
                                    intent1.putExtra("Dest",dest.getSelectedItem().toString());
                                    intent1.putExtra("Travel_date",s_date.getText().toString());
                                    intent1.putExtra("data",data);
                                    startActivity(intent1);
                                }
                                else{flag=1;}
                            }
                        });
                    }
                    if(flag==1){

                        Toast.makeText(DashBoard.this, "No Bus on this Route!!", Toast.LENGTH_SHORT).show();

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }


        private void fetch_data() {
            listener = databaseReference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            String val = Objects.requireNonNull(snapshot1.child("Source").getValue()).toString();
                            String val2 = Objects.requireNonNull(snapshot1.child("Destination").getValue()).toString();
                            src_list.add(val);
                            dest_list.add(val2);
                            }
                        delete_duplicate(src_list);
                        delete_duplicate(dest_list);
                        src_arrayAdapter.notifyDataSetChanged();
                        dest_arrayAdapter.notifyDataSetChanged();
                }

                private void delete_duplicate(ArrayList<String> list) {
                    for (int i =0 ;i<list.size();i++) {
                        String check = list.get(i);
                        for (int j = i+1; j < list.size(); j++) {
                            if (check.equals(list.get(j))) {
                                list.remove(j);
                            }
                        }
                    }
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

        @Override
        public void onBackPressed() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you Sure you want to Exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialogInterface, i) -> finish())
                    .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }