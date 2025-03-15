package com.example.execs;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class Exec3 extends AppCompatActivity {
    private TextView txtVoltar;
    private EditText inputNome;
    private EditText inputUF;
    private EditText inputCidade;
    private EditText inputIdade;
    private EditText inputTelefone;
    private EditText inputEmail;
    private RadioGroup radioGroupSize;
    private List<CheckBox> checkBoxList = new ArrayList<>();
    private LinearLayout checkboxContainer;
    private Button btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exec3);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.txtVoltar = findViewById(R.id.btnVoltar);
        this.inputNome = findViewById(R.id.inputNome);
        this.inputUF = findViewById(R.id.inputUF);
        this.inputCidade = findViewById(R.id.inputCidade);
        this.inputIdade = findViewById(R.id.inputIdade);
        this.inputTelefone = findViewById(R.id.inputTelefone);
        this.inputEmail = findViewById(R.id.inputEmail);
        this.radioGroupSize = findViewById(R.id.radioGroupSize);
        this.btnEnviar = findViewById(R.id.btnEnviar);

        this.txtVoltar.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){ finish(); }
        });

        this.btnEnviar.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){ send(); }
        });

        this.checkboxContainer = findViewById(R.id.layoutCheckbox);
        String[] cores = new String[]{"Azul", "Verde", "Vermelho", "Laranja", "Amarelo"};
        for (String cor : cores){
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(cor);
            this.checkboxContainer.addView(checkBox);
            this.checkBoxList.add(checkBox);
        }
    }

    private void send(){
        this.inputNome.setText("");
        this.inputUF.setText("");
        this.inputCidade.setText("");
        this.inputIdade.setText("");
        this.inputTelefone.setText("");
        this.inputEmail.setText("");
        this.radioGroupSize.clearCheck();
        for (CheckBox box : checkBoxList){
            box.setChecked(false);
        }
    }
}