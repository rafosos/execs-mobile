package com.example.execs;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class exec1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exec1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btn = findViewById(R.id.botao);
        EditText inputIdade = findViewById(R.id.inputIdade);
        EditText inputNome = findViewById(R.id.inputNome);

//        mostrar se é maior de idade ou n

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int idade = Integer.parseInt(String.valueOf(inputIdade.getText()));
                String msg = inputNome.getText() + ", você é ";
                if (idade >= 18){
                    msg = msg + "maior de idade!";
                } else {
                    msg = msg + "menor de idade!";
                }
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}