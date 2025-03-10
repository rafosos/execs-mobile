package com.example.execs;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.math.BigDecimal;

public class Exec2 extends AppCompatActivity {

    private String operacao;

    TextView visorHist;
    EditText visorAtual;
    TextView btnVoltar;
    Button btnMais;
    Button btnMenos;
    Button btnVezes;
    Button btnDividir;
    Button btnApagar;
    Button btnIgual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exec2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.btnVoltar = findViewById(R.id.btnVoltar);
        this.visorHist = findViewById(R.id.visorHist);
        this.visorAtual = findViewById(R.id.visorAtual);
        this.btnMais = findViewById(R.id.btnMais);
        this.btnMenos = findViewById(R.id.btnMenos);
        this.btnVezes = findViewById(R.id.btnVezes);
        this.btnDividir = findViewById(R.id.btnDividir);
        this.btnApagar = findViewById(R.id.btnApagar);
        this.btnIgual = findViewById(R.id.btnIgual);

        btnMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOperadorPress(" + ");
            }
        });
        btnMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOperadorPress(" - ");
            }
        });
        btnVezes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOperadorPress(" x ");
            }
        });
        btnDividir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOperadorPress(" : ");
            }
        });
        btnIgual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String valor = String.valueOf(visorAtual.getText());
                operacao = String.format("%s%s", visorHist.getText(), valor);
                visorHist.setText(operacao);
                calculate();
                visorAtual.setSelection(visorAtual.getText().length());
            }
        });
        btnApagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operacao = "";
                visorAtual.setHint("0");
                visorAtual.setText("");
                visorHist.setText("");
            }
        });
        btnVoltar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){ finish(); }
        });

    }

    private void calculate(){
        int indexV = operacao.indexOf("x");
        int indexD = operacao.indexOf(":");

        if(indexV != -1 || indexD != -1){ //tem divisão ou multiplicação
            int splitIndex = 0;
            if (indexV == -1)
                splitIndex = indexD;
            else
                splitIndex = indexV;
            String bef = operacao.substring(0, splitIndex);
            String aft = operacao.substring(splitIndex+1);

            String[] befArray = bef.split(" ");
            String[] aftArray = aft.trim().split(" ");

            BigDecimal termo1 = new BigDecimal(befArray[befArray.length-1]);
            BigDecimal termo2 = new BigDecimal(aftArray[0]);

            BigDecimal res;
            if (indexV == -1) //só divisão
                res = termo1.divide(termo2);
            else
                res = termo1.multiply(termo2);

            befArray[befArray.length-1] = "";
            aftArray[0] = "";

            operacao = TextUtils.join(" ", befArray) + res.toString() + TextUtils.join(" ", aftArray);
            calculate();
        }
        else {
            String[] funcao = operacao.split(" ");
            if(funcao.length > 1){
                BigDecimal res = calcularResultado(funcao[1], new BigDecimal(funcao[0]), new BigDecimal(funcao[2]));
                funcao[0] = funcao[1] = "";
                funcao[2] = res.toString();
                operacao = TextUtils.join(" ", funcao).trim();
                calculate();
            }
        }
        visorAtual.setText(operacao);
        visorHist.setText(operacao);
    }

    private void onOperadorPress(String operator){
        String valor = String.valueOf(this.visorAtual.getText());
        if (!valor.isEmpty()){
            this.visorAtual.setHint("0");
            this.visorAtual.setText("");
            this.visorHist.setText(String.format("%s%s%s", this.visorHist.getText(), valor, operator));
        }
    }

    private BigDecimal calcularResultado(String operator, BigDecimal val1, BigDecimal val2){
        BigDecimal res = new BigDecimal(0);
        switch (operator){
            case "+":
                res = val1.add(val2);
                break;
            case "-":
                res = val1.subtract(val2);
                break;
            case "x":
                res = val1.multiply(val2);
                break;
            case ":":
                res = val1.divide(val2);
                break;
        }
        return res;
    }
}