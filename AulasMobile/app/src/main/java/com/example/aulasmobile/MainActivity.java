package com.example.aulasmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        TextView txt = findViewById(R.id.abrirView);
        txt.setOnClickListener(new View.OnClickListener(){
            @Override public  void onClick(View v){ navigate(Aula1403.class);}
        });

        TextView grid = findViewById(R.id.gridLayout);
        grid.setOnClickListener(new View.OnClickListener(){
            @Override public  void onClick(View v){ navigate(GridLayout.class);}
        });

        TextView scroll = findViewById(R.id.scrollView);
        scroll.setOnClickListener(new View.OnClickListener(){
            @Override public  void onClick(View v){ navigate(ScrollVieww.class);}
        });

        TextView web = findViewById(R.id.web);
        web.setOnClickListener(new View.OnClickListener(){
            @Override public  void onClick(View v){ navigate(Webb.class);}
        });

        TextView mapa = findViewById(R.id.mapa);
        mapa.setOnClickListener(new View.OnClickListener(){
            @Override public  void onClick(View v){ navigate(Maps.class);}
        });
    }

    private void navigate(Class goal){
        Intent intent = new Intent(MainActivity.this, goal);
        startActivity(intent);
    }
}