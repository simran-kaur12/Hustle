package com.example.hustle.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.hustle.fragments.DeleteBusFragment;
import com.example.hustle.fragments.EditBusFragment;
import com.example.hustle.R;

public class AdminMainPage extends AppCompatActivity {
    String Admin;
    TextView uname,head;
    ImageView menu;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ScrollView scrollView;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_page);
        preferences = getSharedPreferences("Admin_share",MODE_PRIVATE);
        editor = preferences.edit();

        Intent intent = getIntent();
        Admin = intent.getStringExtra("Admin_Data");
        uname = findViewById(R.id.uname);
        uname.setText(Admin);
        menu = findViewById(R.id.menu);
        head = findViewById(R.id.work);
        scrollView = findViewById(R.id.scroll);

        menu.setOnClickListener(view->{
            PopupMenu popupMenu = new PopupMenu(AdminMainPage.this,view);
            popupMenu.getMenuInflater().inflate(R.menu.admin_popup_menu,popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if(menuItem.getItemId() == R.id.add) {
                    Intent intent1 = new Intent(AdminMainPage.this, add_bus.class);
                    intent1.putExtra("Admin_Data",Admin);
                    startActivity(intent1);
                }
                if(menuItem.getItemId() == R.id.edit) {
                    head.setText("EDIT BUS DETAILS WHO HAVE NO BOOKINGS");
                    EditBusFragment editBusFragment = new EditBusFragment();
                    Bundle args = new Bundle();
                    args.putString("Admin_Data", Admin);
                    editBusFragment.setArguments(args);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, editBusFragment).addToBackStack(null).commit();
                }
                if(menuItem.getItemId() == R.id.delete){
                    head.setText("DELETE BUS DETAILS WHO HAVE NO FUTURE BOOKINGS");
                    DeleteBusFragment deleteBusFragment = new DeleteBusFragment();
                    Bundle args = new Bundle();
                    args.putString("Admin_Data", Admin);
                    deleteBusFragment.setArguments(args);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, deleteBusFragment).addToBackStack(null).commit();

                }
                if(menuItem.getItemId() == R.id.exit) {
                    editor.clear();
                    editor.commit();
                    startActivity(new Intent(AdminMainPage.this, Login.class));
                }
                return true;
            });
            popupMenu.show();
        });

    }
}