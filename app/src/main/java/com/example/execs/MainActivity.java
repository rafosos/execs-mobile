package com.example.execs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnExec1 = findViewById(R.id.btnExec1);
        btnExec1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { navigate(Exec1.class); }
        });

        Button btnExec2 = findViewById(R.id.btnExec2);
        btnExec2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { navigate(Exec2.class); }
        });
    }

    private void navigate(Class goal){
        Intent intent = new Intent(MainActivity.this, goal);
        startActivity(intent);
    }
}