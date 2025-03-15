package com.example.execs;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class Exec5 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exec5);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView txtVoltar = findViewById(R.id.btnVoltar);
        txtVoltar.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){ finish(); }
        });

        List<CheckBox> checkboxes = new ArrayList<>();
        checkboxes.add(findViewById(R.id.checkEscuro));
        checkboxes.add(findViewById(R.id.checkLogin));
        checkboxes.add(findViewById(R.id.checkNotificacao));

        Button btnSalvar = findViewById(R.id.btnSalvar);
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> selecionados = new ArrayList<>();
                for (CheckBox box : checkboxes){
                    if (box.isChecked())
                        selecionados.add(String.valueOf(box.getText()));

                    box.setChecked(false);
                }

                if (selecionados.isEmpty())
                    Toast.makeText(getApplicationContext(), "Nenhuma preferência foi escolhida", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Selecionados: " + String.join(", ", selecionados), Toast.LENGTH_SHORT).show();
            }
        });
    }
}