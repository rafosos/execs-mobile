package com.example.aulasmobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Webb extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_webb);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText input = findViewById(R.id.inputUrl);
        Button btnNavegar = findViewById(R.id.btnNavegar);
        btnNavegar.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){
                String endereco =  input.getText().toString();
                Uri uri = Uri.parse(endereco);

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        Button btnLigar = findViewById(R.id.btnLigar);
        btnLigar.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String telefone = input.getText().toString();
                Uri uri = Uri.parse("tel:" + telefone);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    //            Intent intent = new Intent(Intent.ACTION_CALL, uri); //action call crasha td
                startActivity(intent);
            }
        });

        Button btnContatos = findViewById(R.id.btnContatos);
        btnContatos.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String index = input.getText().toString();
                Uri uri = Uri.parse("content://com.android.contacts/contacts/" + index);
                Log.d("debugggg", uri.toString());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        Button btnMapa = findViewById(R.id.btnMapa);
        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String endereco = input.getText().toString();
                endereco = String.join("+", endereco.split(" "));
                Uri uri = Uri.parse("geo://0,0?q=" + endereco);
                Log.d("debugggg", uri.toString());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

    }
}