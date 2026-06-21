package com.example.youtubevideosearchapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class video extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // تأكد أن هذا الملف XML يحتوي على الـ RecyclerView
        setContentView(R.layout.activity_video);
    }
}