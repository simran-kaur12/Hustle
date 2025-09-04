package com.example.hustle.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hustle.R;
import com.example.hustle.databinding.ActivitySignUpBinding;
import com.example.hustle.fragments.Admin_Login;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {
    ActivitySignUpBinding binding;
    Button btn;
    TextView user;
    ImageView slide;
    int flag =0;
    EditText uname,email,mobile,pass,c_pass;
    FloatingActionButton fab;
    String number;
    CircleImageView cir;
    Uri uri;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    Button more_info;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://hustle-6af47-default-rtdb.firebaseio.com/");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference("images_admin");
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferences = getSharedPreferences("Admin_share",MODE_PRIVATE);
        editor = preferences.edit();
        Intent intent = getIntent();
        fab = findViewById(R.id.floatingActionButton);
        cir = findViewById(R.id.profile);
        uname = findViewById(R.id.name);
        email = findViewById(R.id.Email);
        mobile = findViewById(R.id.phone);
        pass = findViewById(R.id.pass);
        c_pass = findViewById(R.id.con_pass);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        more_info = findViewById(R.id.register);

        fab.setOnClickListener(view -> ImagePicker.with(SignUp.this)
                .crop()	    //Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start());

        changeStatusBarColor();
        btn = findViewById(R.id.register);
        user = findViewById(R.id.old_user);
        slide = findViewById(R.id.slide_butt);
        slide.setOnClickListener(view -> {
            startActivity(new Intent(SignUp.this, Login.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
        });
        user.setOnClickListener(view -> {
            startActivity(new Intent(SignUp.this, Login.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
        });
        btn.setOnClickListener(view -> {
            startActivity(new Intent(SignUp.this, Login.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
        });
        more_info.setOnClickListener(view ->{
            String fullname = uname.getText().toString();
            String mail = email.getText().toString();
            String p_num = mobile.getText().toString();
            String password = pass.getText().toString();
            String confirm_pass = c_pass.getText().toString();

            if( fullname.isEmpty() || mail.isEmpty() || p_num.isEmpty() || password.isEmpty() || confirm_pass.isEmpty()){
                Toast.makeText(SignUp.this, "Fill all the fields", Toast.LENGTH_LONG).show();
            }
            else if(!mail.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
                email.requestFocus();
                email.setError("Enter a Valid email");
            }
            else if(!p_num.matches("^\\d{10}$")){
                mobile.requestFocus();
                mobile.setError("Not a Valid Number");
            }
            else if(password.length()<=5){
                pass.requestFocus();
                pass.setError("Minimum 6 Characters Required");
            }
            else if(!password.equals(confirm_pass)){
                c_pass.requestFocus();
                c_pass.setError("Passwords don't Match");
            }
            else if(flag==0) {
                databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(fullname)) {
                            uname.requestFocus();
                            uname.setError("UserName is Already in Use!");
                        }
                        else
                        {
                            flag=1;
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            else
            {
                databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                            databaseReference.child("Users").child(fullname).child("User Name").setValue(fullname);
                            databaseReference.child("Users").child(fullname).child("Email").setValue(mail);
                            databaseReference.child("Users").child(fullname).child("Phone Number").setValue(p_num);
                            databaseReference.child("Users").child(fullname).child("Password").setValue(password);
                            databaseReference.child("Users").child(fullname).child("ROLE").setValue("ADMIN");
                            editor.putString("ADMINNAME",fullname);
                            editor.commit();
                        Toast.makeText(SignUp.this, "Data Saved!", Toast.LENGTH_SHORT).show();
                        if (uri != null) {
                            StorageReference reference = storageReference.child(firebaseAuth.getCurrentUser().getUid() + "." +getFileExtension(uri));

                            databaseReference.child("Users").child(fullname).child("PhotoName").setValue(firebaseAuth.getCurrentUser().getUid() + "." + getFileExtension(uri));
                            reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    reference.getDownloadUrl().addOnSuccessListener((new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            firebaseUser = firebaseAuth.getCurrentUser();
                                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                                    .setPhotoUri(uri).build();
                                            firebaseUser.updateProfile(profileChangeRequest);
                                        }
                                    }));
                                }
                            });
                        }
                        startActivity(new Intent(SignUp.this, Admin_Login.class));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private String getFileExtension(Uri uri) {
        String type = null;

        final String url = uri.toString();

        final String extension = MimeTypeMap.getFileExtensionFromUrl(url);

        if(extension!=null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
            for(int i =0 ;i< type.length();i++)
                if(type.charAt(i)=='/') {
                    type = type.substring(i + 1, type.length());
                    break;
                }
        }
        if(type ==null)
            type = "";
        return type;
    }


    public void changeStatusBarColor(){
        Window w = getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
        w.setStatusBarColor(getResources().getColor(R.color.textforecolor));
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        uri = data.getData();
        cir.setImageURI(uri);
    }
    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(SignUp.this,Login.class));
        super.onBackPressed();
    }
}