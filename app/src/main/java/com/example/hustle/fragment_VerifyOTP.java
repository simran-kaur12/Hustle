package com.example.hustle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class fragment_VerifyOTP extends Fragment {
    View view;
    ImageView image;
    EditText o1,o2,o3,o4,o5,o6;
    TextView mobile,timer,resend;
    Button verify_otp,btn;
    String phone;
    String otpid;
    FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor  editor;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://hustle-6af47-default-rtdb.firebaseio.com/");
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_verify_otp, container, false);
        sharedPreferences = getActivity().getSharedPreferences("Uid_share", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        phone = getArguments().getString("Phone");
        mobile = view.findViewById(R.id.mobile_no);
        mobile.setText("+91"+phone);
        image = view.findViewById(R.id.back);
        o1 = view.findViewById(R.id.op1);
        o2 = view.findViewById(R.id.op2);
        o3 = view.findViewById(R.id.op3);
        o4 = view.findViewById(R.id.op4);
        o5 = view.findViewById(R.id.op5);
        o6 = view.findViewById(R.id.op6);
        resend = view.findViewById(R.id.resend);
        timer = view.findViewById(R.id.timer);
        verify_otp = view.findViewById(R.id.v_otp);
        firebaseAuth = FirebaseAuth.getInstance();
        resend.setOnClickListener(view-> {
            set_timer();
            generate_otp();
        });
        set_timer();
        generate_otp();

        verify_otp.setOnClickListener(view -> {

            if ((o1.getText().toString().isEmpty() || o2.getText().toString().isEmpty()
                    || o3.getText().toString().isEmpty() || o4.getText().toString().isEmpty() ||
                    o5.getText().toString().isEmpty() || o6.getText().toString().isEmpty())) {
                Toast.makeText(getActivity(), "BLANK FIELD CANNOT BE PROCESSED", Toast.LENGTH_SHORT).show();
            } else {
                String otp = o1.getText().toString() + o2.getText().toString() + o3.getText().toString() + o4.getText().toString() + o5.getText().toString() + o6.getText().toString();
                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(otpid, otp);
                signInWithPhoneAuthCredential(phoneAuthCredential);
                databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(phone)) {
                            Toast.makeText(getActivity(), "Phone Number is Already Registered", Toast.LENGTH_SHORT).show();
                        } else {
                            databaseReference.child(phone);
                            Toast.makeText(getActivity(), "Welcome Logged In", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        numberOtpMove();

        image.setOnClickListener(view ->{
            Intent intent = new Intent(getActivity(),Login.class);
            startActivity(intent);
        });
        return view;
    }

    private void numberOtpMove() {
        o1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    o2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        o2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    o3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        o3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    o4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        o4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    o5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        o5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    o6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void set_timer() {
        long duration = TimeUnit.MINUTES.toMillis(1);
        new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long l) {
                String seconds = String.format(Locale.ENGLISH, "%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(l),
                        TimeUnit.MILLISECONDS.toSeconds(l),
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));

                timer.setText(seconds);
            }
            @Override
            public void onFinish() {
                timer.setVisibility(View.GONE);
            }
        }.start();
    }

    private void generate_otp() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(mobile.getText().toString())       // Phone data to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())          // Activity (for callback binding)
                        .setCallbacks(callbacks)        // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
           public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getActivity(), "OTP ERROR", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            otpid = s;
        }
    };
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()) {
                           Intent intent = new Intent(getActivity(), Profile.class);
                           intent.putExtra("mobile","way1");
                           intent.putExtra("Phone", phone);
                           editor.putString("UID",phone);
                           editor.commit();
                           startActivity(intent);
                       } else {
                           Toast.makeText(getActivity(), "SIGN IN FAILED", Toast.LENGTH_SHORT).show();
                       }
                   }
               });
            }

}