package com.example.hustle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
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
import com.squareup.picasso.Picasso;

import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    FloatingActionButton fab;
    CircleImageView cir;
    String data , fullname,email, mobile_num;
    TextView user;
    TextInputEditText mobile,name,eemail;
    Button update,logout,ok;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference("images");
    String photo_url;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private Uri uri;
    SharedPreferences sharedPreferences,sharedPreferences1;
    SharedPreferences.Editor editor,editor1;
    private Uri old;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences = getSharedPreferences("Uid_share",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        sharedPreferences1 = getSharedPreferences("Email_share",MODE_PRIVATE);
        editor1 = sharedPreferences1.edit();
        mobile= findViewById(R.id.phone);
        eemail = findViewById(R.id.email);
        name = findViewById(R.id.uname);

        update = findViewById(R.id.update);
        logout = findViewById(R.id.sign_out);
        ok = findViewById(R.id.okay);

        user = findViewById(R.id.username);
        fab = findViewById(R.id.floatingActionButton);
        cir = findViewById(R.id.profile);

        fab.setOnClickListener(view -> ImagePicker.with(Profile.this)

            .crop()	    //Crop image(Optional), Check Customization for more option
            .compress(1024)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
            .start()

        );

        Intent intent = getIntent();
        String way1 = intent.getStringExtra("mobile");
        String way2 = intent.getStringExtra("mail");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(way1!=null && way1.equals("way1")) {
            data = intent.getStringExtra("Phone");
            mobile.setText(data);
            mobile.setEnabled(false);
        }
        else if(way2!=null && way2.equals("way2")) {
            data = intent.getStringExtra("Email");
            eemail.setText(data);
            eemail.setEnabled(false);
            photo_url = intent.getStringExtra("photo_url");
        }
        hun_kar_kam(data);

        ok.setOnClickListener(view -> {
            Intent intent1 = new Intent(Profile.this,DashBoard.class);
            intent1.putExtra("data",data);
            startActivity(intent1);
        });
        logout.setOnClickListener(view -> {
            editor.clear();
            editor.commit();
            editor1.clear();
            editor1.commit();
            startActivity(new Intent(Profile.this,Login.class));
        });
        update.setOnClickListener(view-> {
            fullname = Objects.requireNonNull(name.getText()).toString();
            email = Objects.requireNonNull(eemail.getText()).toString();
            mobile_num = Objects.requireNonNull(mobile.getText()).toString();
            if (fullname.isEmpty() || email.isEmpty() || data.isEmpty()) {
                Toast.makeText(Profile.this, "Fill all the fields", Toast.LENGTH_LONG).show();
            } else {
                if (valid_info()) {
                    databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (uri != null) {
                                StorageReference reference = storageReference.child(firebaseAuth.getCurrentUser().getUid() + "." +getFileExtension(uri));

                                databaseReference.child(data).child("PhotoName").setValue(firebaseAuth.getCurrentUser().getUid() + "." + getFileExtension(uri));
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

                            databaseReference.child(data).child("User Name").setValue(fullname);
                            databaseReference.child(data).child("Email").setValue(email);
                            databaseReference.child(data).child("Phone Number").setValue(mobile_num);

                            Toast.makeText(Profile.this, "Data Saved!", Toast.LENGTH_SHORT).show();
                            Intent intent2 = new Intent(Profile.this, DashBoard.class);
                            intent2.putExtra("data", data);
                            startActivity(intent2);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
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

    private boolean valid_info() {
        if (!fullname.matches("^[a-zA-Z\\s]+")) {
            name.requestFocus();
            name.setError("Enter only Alphabets");
        } else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            eemail.requestFocus();
            eemail.setError("Enter a Valid email");
        } else if (!mobile_num.matches("^\\d{10}$")) {
            mobile.requestFocus();
            mobile.setError("Not a Valid Number");
        }  else {
            return true;
        }
        return false;
    }

    private void hun_kar_kam(String data) {
        if (data != null) {
            databaseReference.child(data).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.hasChild("User Name")) {
                        String uname = String.valueOf(dataSnapshot.child("User Name").getValue());
                        user.setText(uname);
                        name.setText(uname);
                    }

                    if (dataSnapshot.hasChild("Email")) {
                        String mail = String.valueOf(dataSnapshot.child("Email").getValue());
                        mail.replace('1', '.');
                        if (data.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                            eemail.setText(data);
                            eemail.setEnabled(false);
                        }
                        if(mail.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
                            eemail.setText(mail);
                            eemail.setEnabled(false);
                        }
                    }
                    if (dataSnapshot.hasChild("Phone Number")) {
                        String mob = String.valueOf(dataSnapshot.child("Phone Number").getValue());
                        if (data.matches("^\\d{10}$")) {
                            mobile.setText(data);
                            mobile.setEnabled(false);
                        }
                        if (mob.matches("^\\d{10}$")) {
                            mobile.setText(mob);
                            mobile.setEnabled(false);
                        }
                    }
                    if (dataSnapshot.hasChild("PhotoName")) {
                        String photo_url = String.valueOf(dataSnapshot.child("PhotoName").getValue());
                        if(URLUtil.isDataUrl(photo_url))
                            Glide.with(this).load(photo_url).into(cir);
                        else{
                            Uri yuri = firebaseUser.getPhotoUrl();
                            Picasso.with(Profile.this).load(yuri).into(cir);
                            
                        }
                    }

                } else {
                    Toast.makeText(Profile.this, "Failed to Retrieve Data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    @Override
    protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        uri = data.getData();
        cir.setImageURI(uri);
    }
}
