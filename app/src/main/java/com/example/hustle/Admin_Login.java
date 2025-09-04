package com.example.hustle;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Admin_Login extends Fragment {

    View view;
    ImageView imageView;
    TextView move;
    Button go;
    EditText name,password;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin__login, container, false);

        preferences = getActivity().getSharedPreferences("Admin_share",MODE_PRIVATE);
        editor = preferences.edit();

        if (preferences.contains("ADMINNAME")) {
            Intent intent = new Intent(getActivity(), AdminMainPage.class);
            intent.putExtra("data", preferences.getString("ADMINNAME", null));
            startActivity(intent);
        }
        else
        {
            imageView = view.findViewById(R.id.back);
            move = view.findViewById(R.id.move);
            name = view.findViewById(R.id.uname);
            password = view.findViewById(R.id.pass);
            go = view.findViewById(R.id.go);
            go.setOnClickListener(view1 -> {
                String fullname = name.getText().toString();
                String passwrd = password.getText().toString();

                if (fullname.isEmpty() || passwrd.isEmpty()) {
                    Toast.makeText(getActivity(), "Fill all the fields", Toast.LENGTH_LONG).show();
                } else {

                    Query check = databaseReference.orderByChild("User Name").equalTo(fullname);
                    check.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                if (snapshot.hasChild(fullname)) {
                                    String role = snapshot.child(fullname).child("ROLE").getValue(String.class);
                                    String data_pass = snapshot.child(fullname).child("Password").getValue(String.class);
                                    assert data_pass != null;
                                    if (role != null || !role.equals("ADMIN")) {
                                        if (data_pass.equals(passwrd)) {
                                            Toast.makeText(getActivity(), "Welcome ADMIN" + fullname, Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getActivity(), AdminMainPage.class);
                                            intent.putExtra("Admin_Data", fullname);
                                            editor.putString("ADMINNAME", fullname);
                                            editor.commit();
                                            startActivity(intent);
                                        } else {
                                            password.requestFocus();
                                            password.setError("Wrong Password");
                                        }
                                    } else {
                                        name.requestFocus();
                                        name.setError("Enter ADMIN details");
                                    }
                                }
                            } else {
                                name.requestFocus();
                                name.setError("No such user exits!!");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            });
            move.setOnClickListener(view -> {
                Intent intent = new Intent(getActivity(), SignUp.class);
                startActivity(intent);
            });
            imageView.setOnClickListener(view -> {
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);

            });
        }
        return view;
    }
}