package com.example.hustle.activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hustle.R;
import com.example.hustle.databinding.ActivityBookingBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;

public class Booking extends AppCompatActivity implements PaymentResultListener {

    ActivityBookingBinding activityBookingBinding;
    String bid;
    String data;
    String t_date;
    int total_fare;

    ArrayList<Integer> seat,al_booked,value;
    ImageView back;
    TextView atime,tagency,rupee,totalt,bnum,type,src,dest,go;

    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Booking");
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://hustle-6af47-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBookingBinding = ActivityBookingBinding.inflate(getLayoutInflater());
        setContentView(activityBookingBinding.getRoot());

        //<!---razorpay word-->
        Checkout.preload(Booking.this);
        //<!--Here it ends-->
        Intent intent = getIntent();
        bid = intent.getStringExtra("Bus Id");
        data = intent.getStringExtra("data");
        t_date = intent.getStringExtra("Travel_date");

        seat = new ArrayList<>();
        al_booked = new ArrayList<>();
        value = new ArrayList<>();
        for(int i=0;i<36;i++)
            value.add(0);

        atime = findViewById(R.id.arrivalT);
        tagency = findViewById(R.id.agency);
        type = findViewById(R.id.vary);
        totalt = findViewById(R.id.duration);
        rupee = findViewById(R.id.price);
        bnum = findViewById(R.id.bus_Num);
        src = findViewById(R.id.src);
        dest = findViewById(R.id.dest);
        go = findViewById(R.id.go_date);
        back = findViewById(R.id.back);
        back.setOnClickListener(view -> {
            onBackPressed();
        });
        activityBookingBinding.book.setOnClickListener(view -> makePayment());


        activityBookingBinding.s1.setOnClickListener(view -> {
            if(value.get(0) % 2 == 0) {
                if(seat.size() >4){
                    Toast.makeText(Booking.this,"You Cannot book more than 5 seats!!",Toast.LENGTH_LONG).show();
                }else {
                    activityBookingBinding.s1.setImageResource(R.drawable.your_seat_img);
                    seat.add(1);
                }
            }
            else{
                activityBookingBinding.s1.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(1));
            }
            value.set(0, value.get(0)+1);
        });
        activityBookingBinding.s2.setOnClickListener(view -> {
            if(value.get(1) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s2.setImageResource(R.drawable.your_seat_img);
                    seat.add(2);
                }
            }
            else{
                activityBookingBinding.s2.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(2));
            }
            value.set(1,value.get(1)+1);
        });
        activityBookingBinding.s3.setOnClickListener(view -> {
            if(value.get(2) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s3.setImageResource(R.drawable.your_seat_img);
                    seat.add(3);
                }
            }
            else{
                activityBookingBinding.s3.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(3));
            }
            value.set(2, value.get(2)+1);
        });
        activityBookingBinding.s4.setOnClickListener(view -> {
            if(value.get(3) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s4.setImageResource(R.drawable.your_seat_img);
                    seat.add(4);
                }
            }
            else{
                activityBookingBinding.s4.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(4));
            }
            value.set(3, value.get(3)+1);
        });
        activityBookingBinding.s5.setOnClickListener(view -> {
            if(value.get(4) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s5.setImageResource(R.drawable.your_seat_img);
                    seat.add(5);
                }
            }
            else{
                activityBookingBinding.s5.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(5));
            }
            value.set(4,value.get(4)+1);
        });
        activityBookingBinding.s6.setOnClickListener(view -> {

            if(value.get(5) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s6.setImageResource(R.drawable.your_seat_img);
                    seat.add(6);
                }
            }
            else{
                activityBookingBinding.s6.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(6));
            }
            value.set(5,value.get(5)+1);
        });
        activityBookingBinding.s7.setOnClickListener(view -> {

            if(value.get(6) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                     activityBookingBinding.s7.setImageResource(R.drawable.your_seat_img);
                     seat.add(7);
                }
            }
            else{
                activityBookingBinding.s7.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(7));
            }
            value.set(6,value.get(6)+1);
        });
        activityBookingBinding.s8.setOnClickListener(view -> {

            if(value.get(7) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s8.setImageResource(R.drawable.your_seat_img);
                    seat.add(8);
                }
            }
            else{
                activityBookingBinding.s8.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(8));
            }
            value.set(7, value.get(7)+1);
        });
        activityBookingBinding.s9.setOnClickListener(view -> {

            if(value.get(8) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s9.setImageResource(R.drawable.your_seat_img);
                    seat.add(9);
                }
            }
            else{
                activityBookingBinding.s9.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(9));
            }
            value.set(8, value.get(8)+1);
        });
        activityBookingBinding.s10.setOnClickListener(view -> {

            if(value.get(9) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s10.setImageResource(R.drawable.your_seat_img);
                    seat.add(10);
                }
            }
            else{
                activityBookingBinding.s10.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(10));
            }
            value.set(9, value.get(9)+1);
        });
        activityBookingBinding.s11.setOnClickListener(view -> {

            if(value.get(10) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                        activityBookingBinding.s11.setImageResource(R.drawable.your_seat_img);
                        seat.add(11);
                }
            }
            else{
                activityBookingBinding.s11.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(11));
            }
            value.set(10, value.get(10)+1);
        });
        activityBookingBinding.s12.setOnClickListener(view -> {

            if(value.get(11) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s12.setImageResource(R.drawable.your_seat_img);
                    seat.add(12);
                }
            }
            else{
                activityBookingBinding.s12.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(12));
            }
            value.set(11, value.get(11)+1);
        });
        activityBookingBinding.s13.setOnClickListener(view -> {

            if(value.get(12) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s13.setImageResource(R.drawable.your_seat_img);
                    seat.add(13);
                }
            }
            else{
                activityBookingBinding.s13.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(13));
            }
            value.set(12, value.get(12)+1);
        });
        activityBookingBinding.s14.setOnClickListener(view -> {

            if(value.get(13) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s14.setImageResource(R.drawable.your_seat_img);
                    seat.add(14);
                }
            }
            else{
                activityBookingBinding.s14.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(14));
            }
            value.set(13, value.get(13)+1);
        });
        activityBookingBinding.s15.setOnClickListener(view -> {

            if(value.get(14) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s15.setImageResource(R.drawable.your_seat_img);
                    seat.add(15);
                }
            }
            else{
                activityBookingBinding.s15.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(15));
            }
            value.set(14, value.get(14)+1);
        });
        activityBookingBinding.s16.setOnClickListener(view -> {

            if(value.get(15) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s16.setImageResource(R.drawable.your_seat_img);
                    seat.add(16);
                }
            }
            else{
                activityBookingBinding.s16.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(16));
            }
           value.set(15,value.get(15)+1);
        });
        activityBookingBinding.s17.setOnClickListener(view -> {

            if(value.get(16) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s17.setImageResource(R.drawable.your_seat_img);
                    seat.add(17);
                }
            }
            else{
                activityBookingBinding.s17.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(17));
            }
            value.set(16,value.get(16)+1);
        });
        activityBookingBinding.s18.setOnClickListener(view -> {

            if(value.get(17) % 2 == 0)
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s18.setImageResource(R.drawable.your_seat_img);
                    seat.add(18);
                }
            else{
                activityBookingBinding.s18.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(18));
            }
            value.set(17,value.get(17)+1);
        });
        activityBookingBinding.s19.setOnClickListener(view -> {

            if(value.get(18) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s19.setImageResource(R.drawable.your_seat_img);
                    seat.add(19);
                }
            }
            else{
                activityBookingBinding.s19.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(19));
            }
            value.set(18,value.get(18)+1);
        });
        activityBookingBinding.s20.setOnClickListener(view -> {

            if(value.get(19) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s20.setImageResource(R.drawable.your_seat_img);
                    seat.add(20);
                }
            }
            else{
                activityBookingBinding.s20.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(20));
            }
            value.set(19,value.get(19)+1);
        });
        activityBookingBinding.s21.setOnClickListener(view -> {

            if(value.get(20) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s21.setImageResource(R.drawable.your_seat_img);
                    seat.add(21);
                }
            }
            else{
                activityBookingBinding.s21.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(21));
            }
            value.set(20,value.get(20)+1);
        });
        activityBookingBinding.s22.setOnClickListener(view -> {

            if(value.get(21) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s22.setImageResource(R.drawable.your_seat_img);
                    seat.add(22);
                }
            }
            else{
                activityBookingBinding.s22.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(22));
            }
            value.set(21,value.get(21)+1);
        });
        activityBookingBinding.s23.setOnClickListener(view -> {

            if(value.get(22) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s23.setImageResource(R.drawable.your_seat_img);
                    seat.add(23);
                }
            }
            else{
                activityBookingBinding.s23.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(23));
            }
            value.set(22,value.get(22)+1);
        });
        activityBookingBinding.s24.setOnClickListener(view -> {

            if(value.get(23) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s24.setImageResource(R.drawable.your_seat_img);
                    seat.add(24);
                }
            }
            else{
                activityBookingBinding.s24.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(24));
            }
            value.set(23,value.get(23)+1);
        });
        activityBookingBinding.s25.setOnClickListener(view -> {

            if(value.get(24) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s25.setImageResource(R.drawable.your_seat_img);
                    seat.add(25);
                }
            }
            else{
                activityBookingBinding.s25.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(25));
            }
            value.set(24,value.get(24)+1);
        });
        activityBookingBinding.s26.setOnClickListener(view -> {

            if(value.get(25) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s26.setImageResource(R.drawable.your_seat_img);
                    seat.add(26);
                }
            }
            else{
                activityBookingBinding.s26.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(26));
            }
            value.set(25,value.get(25)+1);
        });
        activityBookingBinding.s27.setOnClickListener(view -> {

            if(value.get(26) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s27.setImageResource(R.drawable.your_seat_img);
                    seat.add(27);
                }
            }
            else{
                activityBookingBinding.s27.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(27));
            }
            value.set(26,value.get(26)+1);
        });
        activityBookingBinding.s28.setOnClickListener(view -> {

            if(value.get(27) % 2 == 0) {
                activityBookingBinding.s28.setImageResource(R.drawable.your_seat_img);
                seat.add(28);
            }
            else{
                activityBookingBinding.s28.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(28));
            }
            value.set(27,value.get(27)+1);
        });
        activityBookingBinding.s29.setOnClickListener(view -> {

            if(value.get(28) % 2 == 0) {
                activityBookingBinding.s29.setImageResource(R.drawable.your_seat_img);
                seat.add(29);
            }
            else{
                activityBookingBinding.s29.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(29));
            }
            value.set(28,value.get(28)+1);
        });
        activityBookingBinding.s30.setOnClickListener(view -> {

            if(value.get(29) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s30.setImageResource(R.drawable.your_seat_img);
                    seat.add(30);
                }
            }
            else{
                activityBookingBinding.s30.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(30));
            }
            value.set(29,value.get(29)+1);
        });
        activityBookingBinding.s31.setOnClickListener(view -> {

            if(value.get(30) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s31.setImageResource(R.drawable.your_seat_img);
                    seat.add(31);
                }
            }
            else{
                activityBookingBinding.s31.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(31));
            }
            value.set(30,value.get(30)+1);
        });
        activityBookingBinding.s32.setOnClickListener(view -> {

            if(value.get(31) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s32.setImageResource(R.drawable.your_seat_img);
                    seat.add(32);
                }
            }
            else{
                activityBookingBinding.s32.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(32));
            }
            value.set(31,value.get(31)+1);
        });
        activityBookingBinding.s33.setOnClickListener(view -> {

            if(value.get(32) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s33.setImageResource(R.drawable.your_seat_img);
                    seat.add(33);
                }
            }
            else{
                activityBookingBinding.s33.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(33));
            }
            value.set(32,value.get(32)+1);
        });
        activityBookingBinding.s34.setOnClickListener(view -> {

            if(value.get(33) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s34.setImageResource(R.drawable.your_seat_img);
                    seat.add(34);
                }
            }
            else{
                activityBookingBinding.s34.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(34));
            }
            value.set(33,value.get(33)+1);
        });
        activityBookingBinding.s35.setOnClickListener(view -> {

            if(value.get(34) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {

                    activityBookingBinding.s35.setImageResource(R.drawable.your_seat_img);
                    seat.add(35);
                }
            }
            else{
                activityBookingBinding.s35.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(35));
            }
            value.set(34,value.get(34)+1);
        });
        activityBookingBinding.s36.setOnClickListener(view -> {

            if(value.get(35) % 2 == 0) {
                if (seat.size() > 4) {
                    Toast.makeText(Booking.this, "You Cannot book more than 5 seats!!", Toast.LENGTH_LONG).show();
                } else {
                    activityBookingBinding.s36.setImageResource(R.drawable.your_seat_img);
                    seat.add(36);
                }
            }
            else{
                activityBookingBinding.s36.setImageResource(R.drawable.available_img);
                seat.remove(Integer.valueOf(36));
            }
            value.set(35,value.get(35)+1);
        });
        fetch_data(bid);
        fetch_booked();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Booking.this, Available_buses.class);
        intent.putExtra("data",data);
        intent.putExtra("Bus Id",bid);
        intent.putExtra("Travel_date",t_date);
        super.onBackPressed();
    }

    private void makePayment() {

        total_fare = totalFare(seat);

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_yr0VD4MnUwTyg8");

        checkout.setImage(R.drawable.bus_booking_round_logo);

        final Activity activity = this;


        try {
            JSONObject options = new JSONObject();

            options.put("name", "HUSTLE LTD.");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", bookID);//from response of step 3.
            options.put("theme.color", R.color.themeColor);
            options.put("currency", "INR");
            options.put("amount", total_fare*100);//pass amount in currency subunits
            if(data.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
                options.put("prefill.email", data);
            if(data.matches("^\\d{10}$"))
                options.put("prefill.contact",data);

            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }
    }

    private void setColor(ArrayList<Integer> al_booked) {
        int i=0;
        while(i<al_booked.size()) {
            switch (al_booked.get(i)) {
                case 1:
                    activityBookingBinding.s1.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s1.setEnabled(false);
                    break;
                case 2:
                    activityBookingBinding.s2.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s2.setEnabled(false);
                    break;
                case 3:
                    activityBookingBinding.s3.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s3.setEnabled(false);
                    break;
                case 4:
                    activityBookingBinding.s4.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s4.setEnabled(false);
                    break;
                case 5:
                    activityBookingBinding.s5.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s5.setEnabled(false);
                    break;
                case 6:
                    activityBookingBinding.s6.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s6.setEnabled(false);
                    break;
                case 7:
                    activityBookingBinding.s7.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s7.setEnabled(false);
                    break;
                case 8:
                    activityBookingBinding.s8.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s8.setEnabled(false);
                    break;
                case 9:
                    activityBookingBinding.s9.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s9.setEnabled(false);
                    break;
                case 10:
                    activityBookingBinding.s10.setImageResource(R.drawable.available_img);
                    activityBookingBinding.s10.setEnabled(false);
                    break;
                case 11:
                    activityBookingBinding.s11.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s11.setEnabled(false);
                    break;
                case 12:
                    activityBookingBinding.s12.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s12.setEnabled(false);
                    break;
                case 13:
                    activityBookingBinding.s13.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s13.setEnabled(false);
                    break;
                case 14:
                    activityBookingBinding.s14.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s14.setEnabled(false);
                    break;
                case 15:
                    activityBookingBinding.s15.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s15.setEnabled(false);
                    break;
                case 16:
                    activityBookingBinding.s16.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s16.setEnabled(false);
                    break;
                case 17:
                    activityBookingBinding.s17.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s17.setEnabled(false);
                    break;
                case 18:
                    activityBookingBinding.s18.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s18.setEnabled(false);
                    break;
                case 19:
                    activityBookingBinding.s19.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s19.setEnabled(false);
                    break;
                case 20:
                    activityBookingBinding.s20.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s20.setEnabled(false);
                    break;
                case 21:
                    activityBookingBinding.s21.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s21.setEnabled(false);
                    break;
                case 22:
                    activityBookingBinding.s22.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s22.setEnabled(false);
                    break;
                case 23:
                    activityBookingBinding.s23.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s23.setEnabled(false);
                    break;
                case 24:
                    activityBookingBinding.s24.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s24.setEnabled(false);
                    break;
                case 25:
                    activityBookingBinding.s25.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s25.setEnabled(false);
                    break;
                case 26:
                    activityBookingBinding.s26.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s26.setEnabled(false);
                    break;
                case 27:
                    activityBookingBinding.s27.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s27.setEnabled(false);
                    break;
                case 28:
                    activityBookingBinding.s28.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s28.setEnabled(false);
                    break;
                case 29:
                    activityBookingBinding.s29.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s29.setEnabled(false);
                    break;
                case 30:
                    activityBookingBinding.s30.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s30.setEnabled(false);
                    break;
                case 31:
                    activityBookingBinding.s31.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s31.setEnabled(false);
                    break;
                case 32:
                    activityBookingBinding.s32.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s32.setEnabled(false);
                    break;
                case 33:
                    activityBookingBinding.s33.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s33.setEnabled(false);
                    break;
                case 34:
                    activityBookingBinding.s34.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s34.setEnabled(false);
                    break;
                case 35:
                    activityBookingBinding.s35.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s35.setEnabled(false);
                    break;
                case 36:
                    activityBookingBinding.s36.setImageResource(R.drawable.booked_img);
                    activityBookingBinding.s36.setEnabled(false);
                    break;
                default:
                    break;
            }
            i++;
        }
    }

    private String getSeatNo(ArrayList<Integer> seat) {
        String seats = null;
        for (int i = 0; i < seat.size(); i++) {
           if (seat.size() == 1)
                seats = String.valueOf(seat.get(i));
            else{
                if(i==0)
                    seats = String.valueOf(seat.get(i));
                else
                    seats = seats + "," + seat.get(i);
                }
                 }
         return seats;
    }

    private int totalFare(ArrayList<Integer> seat) {
        String paisa = rupee.getText().toString().replace("/-","");
        return Integer.parseInt(paisa.trim()) * seat.size();
    }

    private void fetch_booked() {

        dbref.orderByChild("BusId").equalTo(bid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot snapshot1 : snapshot.getChildren()) {

                    if (snapshot1.child("Travel_Date").exists()) {

                        boolean check = snapshot1.child("Travel_Date").getValue().toString().equals(t_date);

                        String c = snapshot1.child("Travel_Date").getValue().toString();

                        if (check && c.equals(t_date)) {

                            String val = String.valueOf(snapshot1.child("SeatNos").getValue());

                            String[] strarray = val.split(",");

                            for (String s : strarray) {

                                if (!s.trim().equals("null")) {

                                    int newy = Integer.parseInt(s.trim());

                                    al_booked.add(newy);

                                }
                            }
                        }
                    }
                }
                setColor(al_booked);
            }
            @Override

            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetch_data(String bid) {

        databaseReference.child("Buses").child(bid).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    atime.setText(snapshot.child("Arrival_Time").getValue().toString());

                    tagency.setText(snapshot.child("Travel_Agency").getValue().toString());

                    type.setText(snapshot.child("Category").getValue().toString());

                    totalt.setText(snapshot.child("Duration").getValue().toString());

                    rupee.setText(snapshot.child("Fare").getValue().toString().concat("/-"));

                    bnum.setText(snapshot.child("Bus_No").getValue().toString());

                    src.setText(snapshot.child("Source").getValue().toString());

                    dest.setText(snapshot.child("Destination").getValue().toString());

                    go.setText(t_date);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "!! SUCCESSFULLY PAID !!", Toast.LENGTH_SHORT).show();
        String seatno = getSeatNo(seat);
            dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String bookID = dbref.push().getKey();
                     if(bookID!=null) {
                         dbref.child(bookID).child("BookID").setValue(bookID);
                        dbref.child(bookID).child("BusId").setValue(bid);
                        dbref.child(bookID).child("UserName").setValue(data);
                        dbref.child(bookID).child("TotalFare").setValue(total_fare);
                        dbref.child(bookID).child("TotalSeats").setValue(seat.size());
                        dbref.child(bookID).child("SeatNos").setValue(seatno);
                        dbref.child(bookID).child("Travel_Date").setValue(t_date);
                        dbref.child(bookID).child("STATUS").setValue("BOOKED");

                        AlertDialog.Builder builder = new AlertDialog.Builder(Booking.this);
                        builder.setMessage("\tBOOKING ID:-"+bookID+"\nClick YES to Download the TICKET!");
                        builder.setTitle("Alert! NOT DOWN BOOKING ID TO DOWNLOAD TICKET!!");
                        builder.setCancelable(false);
                         builder
                         .setPositiveButton(
                         "Yes",
                         new DialogInterface
                                 .OnClickListener() {

                             @Override
                             public void onClick(DialogInterface dialog,
                                                 int which)
                             {
                                 Intent intent = new Intent(Booking.this, ticket.class);
                                 intent.putExtra("bookID",bookID);
                                 startActivity(intent);

                             }
                         });

                         builder
                             .setNegativeButton(
                                     "No",
                                     new DialogInterface
                                             .OnClickListener() {

                                         @Override
                                         public void onClick(DialogInterface dialog,
                                                             int which)
                                         {

                                             dialog.cancel();
                                         }
                                 });

                         // Create the Alert dialog
                         AlertDialog alertDialog = builder.create();

                         // Show the Alert Dialog box
                         alertDialog.show();
                     }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }


    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "FAILED PAYMENT:- "+s, Toast.LENGTH_SHORT).show();

    }
}