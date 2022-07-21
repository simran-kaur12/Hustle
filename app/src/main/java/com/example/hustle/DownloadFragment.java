package com.example.hustle;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class DownloadFragment extends Fragment {

    View view;
    String data;
    ImageView back;
    Spinner bookId;
    Button ticket;
    ArrayList<String> ticket_list = new ArrayList<>();
    ArrayAdapter ticket_arrayAdapter;
    ValueEventListener listener;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Booking");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_download, container, false);
        data = getArguments().getString("data");

        back = view.findViewById(R.id.back);
        bookId = view.findViewById(R.id.book);
        ticket = view.findViewById(R.id.GET_YOUR_TICKET);

        ticket_arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, ticket_list);
        bookId.setAdapter(ticket_arrayAdapter);

        ticket.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(),ticket.class);
            intent.putExtra("bookID",bookId.getSelectedItem().toString());
            startActivity(intent);
        });

        back.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(),DashBoard.class);
            intent.putExtra("data",data);
            startActivity(intent);

        });
        fetch_data();
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

}