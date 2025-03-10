package com.example.execs;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Exec4 extends AppCompatActivity {
    private LinearLayout checkboxContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exec4);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView txtVoltar = findViewById(R.id.btnVoltar);
        txtVoltar.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){ finish(); }
        });

        EditText inputNome = findViewById(R.id.inputNome);
        this.checkboxContainer = findViewById(R.id.layoutCheckbox);

        inputNome.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
                Log.d("debugggg", s.toString());
                addCheckbox(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void addCheckbox(String nome){
        this.checkboxContainer.removeAllViews();
        for (char letra : nome.toCharArray()){
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(String.valueOf(letra));
            this.checkboxContainer.addView(checkBox);
        }
    }
}