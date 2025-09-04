package com.example.hustle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainScreen extends AppCompatActivity {
    Animation top_anim,bottom_anim;
    ImageView imageView;
    TextView logo,tag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_screen);
        //Animations
        top_anim = AnimationUtils.loadAnimation(this,R.anim.top);
        bottom_anim = AnimationUtils.loadAnimation(this,R.anim.bottom);

        imageView = findViewById(R.id.imageView);
        logo = findViewById(R.id.textView7);
        tag = findViewById(R.id.textView8);
        imageView.setAnimation(top_anim);
        logo.setAnimation(bottom_anim);
        tag.setAnimation(bottom_anim);

        int SPLASH_SCREEN = 3000;
        new Handler().postDelayed(() -> {
            Intent i = new Intent(MainScreen.this, Login.class);
            startActivity(i);
            finish();
        }, SPLASH_SCREEN);
    }
}