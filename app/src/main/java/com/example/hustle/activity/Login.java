package com.example.hustle.activity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hustle.R;
import com.example.hustle.fragments.Admin_Login;
import com.example.hustle.fragments.fragment_VerifyOTP;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import de.hdodenhof.circleimageview.CircleImageView;

public class Login extends AppCompatActivity {

    SignInButton email;
    TextView change;
    CircleImageView logo;
    Button btn, log_admin;
    String num,photo_url;
    EditText phone;
    CountryCodePicker cCode;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://hustle-6af47-default-rtdb.firebaseio.com/");
    FirebaseAuth mAuth;
    GoogleSignInClient googleSignInClient;
    Uri pic_uri;
    SharedPreferences preferences,preferences1,preferences2;
    SharedPreferences.Editor editor,editor1,editor2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        preferences = getSharedPreferences("Uid_share",MODE_PRIVATE);
        editor = preferences.edit();
        preferences1 = getSharedPreferences("Email_share", Context.MODE_PRIVATE);
        editor1 = preferences1.edit();
        preferences2 = getSharedPreferences("Admin_share",MODE_PRIVATE);
        editor2 = preferences2.edit();
        if (preferences.contains("UID")) {
            Intent intent = new Intent(Login.this, DashBoard.class);
            intent.putExtra("data",preferences.getString("UID",null));
            startActivity(intent);
        } else if (preferences1.contains("Email")) {
                Intent intent = new Intent(Login.this,DashBoard.class);
                intent.putExtra("data",preferences1.getString("Email",null));
                startActivity(intent);
            }
        else if(preferences2.contains("ADMINNAME")) {
            Intent intent = new Intent(Login.this, AdminMainPage.class);
            intent.putExtra("Admin_Data",preferences2.getString("ADMINNAME",null));
            startActivity(intent);
            }
        else{
            phone = findViewById(R.id.phone_num);
            cCode = findViewById(R.id.c_code);
            logo = findViewById(R.id.logo);
            btn = findViewById(R.id.otp);
            log_admin = findViewById(R.id.admin);
            change = findViewById(R.id.step);
            logo.setOnClickListener(view -> {
                startActivity(new Intent(Login.this, SignUp.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
            });
            log_admin.setOnClickListener(view -> {
                Admin_Login fragment = new Admin_Login();
                btn.setVisibility(View.GONE);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment).addToBackStack(null).commit();
            });
            btn.setOnClickListener(view -> {
                num = phone.getText().toString();
                if (num.length() != 10) {
                    phone.requestFocus();
                    phone.setError("Enter a Valid Phone Number");
                } else {
                    change.setText("Verify\t\n Your\nOTP");
                    logo.setImageResource(R.drawable.send);
                    fragment_VerifyOTP verifyOTP = new fragment_VerifyOTP();
                    Bundle args = new Bundle();
                    args.putString("Phone", cCode.getSelectedCountryCodeWithPlus() + phone.getText().toString());
                    verifyOTP.setArguments(args);
                    btn.setVisibility(View.GONE);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, verifyOTP).addToBackStack(null).commit();
                }
            });

            email = findViewById(R.id.mail);
            email.setOnClickListener(view -> google_sign_in());
            mAuth = FirebaseAuth.getInstance();
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    //.requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            googleSignInClient = GoogleSignIn.getClient(Login.this, gso);

        }
    }


    private void google_sign_in() {
        googleSignInClient.signOut();
        Intent signInIntent = googleSignInClient.getSignInIntent();
        activityResultLauncher.launch(signInIntent);
    }
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new
                    ActivityResultContracts.StartActivityForResult(),
            result -> {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                handleSignInResult(task);
            });

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (account != null) {
                String name = account.getDisplayName();
                String e_Email = account.getEmail();
                pic_uri = account.getPhotoUrl();
                databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        assert e_Email != null;
                        String e_Email2 = e_Email.replace('.','1');
                        Log.e("email", e_Email);
                        Log.e("email2", e_Email2);
                        Log.e("name", name);
                        databaseReference.child("Users").child(e_Email2).child("User Name").setValue(name);
                        databaseReference.child("Users").child(e_Email2).child("Email").setValue(e_Email2);
                        if(pic_uri!=null)
                        {
                            photo_url=pic_uri.toString();
                            databaseReference.child("Users").child(e_Email2).child("PhotoName").setValue(photo_url);
                        }
                        Toast.makeText(Login.this, "Data Saved!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, Profile.class);
                        editor1.putString("Email",e_Email2);
                        editor1.commit();
                        intent.putExtra("mail","way2");
                        intent.putExtra("Email",e_Email2);
                        if(photo_url!=null)
                            intent.putExtra("photo_url", photo_url);
                        startActivity(intent);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }catch (ApiException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}