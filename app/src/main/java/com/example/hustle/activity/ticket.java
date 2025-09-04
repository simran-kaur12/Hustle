package com.example.hustle.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hustle.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ticket extends AppCompatActivity {
    Display display;
    String path,imageURI;
    Bitmap bitmap;
    int tot_height,tot_width;
    public static final int READ_PHONE =110;
    String file_name ="Screenshot";
    File myPath;
    String data,bookID,bus_ID;
    Button get,mainPage;
    TextView a_time,source,destination,boarding,busNo,seatNo,Category,travel_time,uname,book;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://hustle-6af47-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PHONE);
        }
        get = findViewById(R.id.download);
        a_time = findViewById(R.id.arrival_time);
        source = findViewById(R.id.source);
        destination = findViewById(R.id.dest);
        boarding = findViewById(R.id.board);
        mainPage = findViewById(R.id.MAIN);
        busNo = findViewById(R.id.busNo);
        book = findViewById(R.id.bookId);
        seatNo = findViewById(R.id.seat_no);
        Category = findViewById(R.id.Category);
        travel_time = findViewById(R.id.travel_date);
        uname = findViewById(R.id.pass_name);

        Intent intent = getIntent();
        bookID = intent.getStringExtra("bookID");

        fetch_data();
        mainPage.setOnClickListener(view -> {
            Intent intent1 = new Intent(ticket.this, DashBoard.class);
            intent1.putExtra("data",data);
            startActivity(intent1);
        });
        get.setOnClickListener(view -> {
            get.setVisibility(View.GONE);
            mainPage.setVisibility(View.GONE);
            takeScreenshot();
            get.setVisibility(View.VISIBLE);
            mainPage.setVisibility(View.VISIBLE);
        });

    }

    private void fetch_data() {
        databaseReference.child("Booking").child(bookID).get().addOnCompleteListener(task -> {
            DataSnapshot dataSnapshot = task.getResult();
            if(dataSnapshot.exists()){
                String bID = String.valueOf(dataSnapshot.child("BookID").getValue());
                book.setText(bID);
                String seats = String.valueOf(dataSnapshot.child("SeatNos").getValue());
                seatNo.setText(seats);
                String t_dat = String.valueOf(dataSnapshot.child("Travel_Date").getValue());
                travel_time.setText(seats);
                data = String.valueOf(dataSnapshot.child("UserName").getValue());

                databaseReference.child("Users").child(data).get().addOnCompleteListener(task12 -> {
                    DataSnapshot dataSnapshot1 = task12.getResult();
                    if(dataSnapshot1.exists()) {
                        String name = String.valueOf(dataSnapshot1.child("User Name").getValue());
                        uname.setText(name);
                    }
                });
                bus_ID = String.valueOf(dataSnapshot.child("BusId").getValue());
                databaseReference.child("Buses").child(bus_ID).get().addOnCompleteListener(task1 -> {
                    DataSnapshot dataSnapshot2 = task1.getResult();
                    if(dataSnapshot2.exists()){

                        String src = dataSnapshot2.child("Source").getValue().toString();
                        source.setText(src);
                        String des = String.valueOf(dataSnapshot2.child("Destination").getValue());
                        destination.setText(des);
                        String type = String.valueOf(dataSnapshot2.child("Category").getValue());
                        Category.setText(type);
                        String arrivalTime = String.valueOf(dataSnapshot2.child("Arrival_Time").getValue());
                        a_time.setText(arrivalTime);
                        String boarding_loc = String.valueOf(dataSnapshot2.child("Boarding_Loc").getValue());
                        boarding.setText(boarding_loc);
                        String bno = String.valueOf(dataSnapshot2.child("Bus_No").getValue());
                        busNo.setText(bno);
                    }
                });
            }

        });

    }
    public Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth){
        Bitmap returnBitmap = Bitmap.createBitmap(totalWidth,totalHeight,Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnBitmap);
        Drawable bgDrawable = view.getBackground();
        if(bgDrawable !=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);

        view.draw(canvas);
        return returnBitmap;
    }
    private void takeScreenshot(){

        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshot/");
        if(!folder.exists())
            folder.mkdir();

        path = folder.getAbsolutePath();
        path = path + "/" + file_name + System.currentTimeMillis() + ".pdf";

        @SuppressLint("CutPasteId") View u = findViewById(R.id.ticket);

        @SuppressLint("CutPasteId") NestedScrollView z = findViewById(R.id.ticket);
        tot_height = z.getChildAt(0).getHeight();
        tot_width = z.getChildAt(0).getWidth();

        String str = Environment.getExternalStorageDirectory() + "/BUS TICKET/";
        File file = new File(str);
        if(!file.exists()){
            file.mkdir();
        }
        String filename = file_name +".jpg";
        myPath = new File(str,filename);
        imageURI = myPath.getPath();
        bitmap = getBitmapFromView(u,tot_height,tot_width);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(myPath);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        createPDF();
    }

    private void createPDF() {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(),bitmap.getHeight(),1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPaint(paint);
        Bitmap bitmap = Bitmap.createScaledBitmap(this.bitmap,this.bitmap.getWidth(),this.bitmap.getHeight(),true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap,0,0,null);
        document.finishPage(page);
        File mfilePath = new File(path);
        try{
            document.writeTo(new FileOutputStream(mfilePath));
        }
        catch (IOException e){
            e.printStackTrace();
            Toast.makeText(this, "Something Wrong", Toast.LENGTH_SHORT).show();
        }
        document.close();
        if(!mfilePath.exists())
            mfilePath.delete();

        openPdf(path);
    }

    private void openPdf(String path) {
        File file = new File(path);
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file),"application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target,"Open File");
        try {
            startActivity(intent);
        }catch (ActivityNotFoundException e){
            Toast.makeText(this, "No Apps To read PDF file", Toast.LENGTH_SHORT).show();
        }
    }
}