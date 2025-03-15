package com.example.lista2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

        Button exec1 = findViewById(R.id.btnExec1);
        exec1.setOnClickListener(new View.OnClickListener(){
            @Override public  void onClick(View v){ navigate(Exec1.class);}
        });

        Button exec2 = findViewById(R.id.btnExec2);
        exec2.setOnClickListener(new View.OnClickListener(){
            @Override public  void onClick(View v){ navigate(Exec2.class);}
        });

        Button exec3 = findViewById(R.id.btnExec3);
        exec3.setOnClickListener(new View.OnClickListener(){
            @Override public  void onClick(View v){ navigate(Exec3.class);}
        });

        Button exec4 = findViewById(R.id.btnExec4);
        exec4.setOnClickListener(new View.OnClickListener(){
            @Override public  void onClick(View v){ navigate(Exec4.class);}
        });
    }

    private void navigate(Class goal){
        Intent intent = new Intent(MainActivity.this, goal);
        startActivity(intent);
    }
}