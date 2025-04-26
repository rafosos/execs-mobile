package com.example.ac2exercicios;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Handler handler = new Handler();
    EditText inputNome;
    EditText inputTempo;
    Button btnAdd;
    Button btnIniciar;
    LinearLayout layoutExercicios;
    List<Exercicio> exercicios = new ArrayList<>();
    List<LinearLayout> viewsExercicios = new ArrayList<>();

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

        inputNome = findViewById(R.id.inputNome);
        inputTempo = findViewById(R.id.inputTempo);
        btnAdd = findViewById(R.id.btnAdd);
        btnIniciar = findViewById(R.id.btnIniciar);

        btnAdd.setOnClickListener(v -> {
            String nome = inputNome.getText().toString();
            int tempo = Integer.parseInt(inputTempo.getText().toString());

            if(nome.isEmpty() || tempo == 0){
                Toast.makeText(this, "Verifique os campos e tente novamente.", Toast.LENGTH_SHORT).show();
                return;
            }

            Exercicio exec = new Exercicio(nome, tempo);
            exercicios.add(exec);

            //adicionar no layout
            //criacao layout pai
            LinearLayout container = new LinearLayout(this);
            container.setOrientation(LinearLayout.HORIZONTAL);

            //nome exec
            TextView nomeExercicio = new TextView(this);
            nomeExercicio.setText(nome);
            nomeExercicio.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, //width
                    ViewGroup.LayoutParams.WRAP_CONTENT, //height
                    3.0f
            ));

            container.addView(nomeExercicio);

            //tempo
            TextView tempoExercicio = new TextView(this);
            tempoExercicio.setText(String.valueOf(tempo));
            tempoExercicio.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, //width
                    ViewGroup.LayoutParams.WRAP_CONTENT, //height
                    2.0f
            ));
            container.addView(tempoExercicio);

            //icone delete
            ImageButton iconeApagar = new ImageButton(this);
            iconeApagar.setImageResource(R.drawable.delete_24px);
            iconeApagar.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, //width
                    ViewGroup.LayoutParams.WRAP_CONTENT, //height
                    1.0f
            ));
            iconeApagar.setOnClickListener(j -> {
                exercicios.remove(exec);
                layoutExercicios.removeView(container);
            });

            layoutExercicios.addView(container);
        });

        btnIniciar.setOnClickListener(v -> {
            for (Exercicio exercicio : exercicios) {
                handler.post()
            }
            Intent c = new Intent(this, Contador.class);
            c.putExtra("nome", "teste");
            c.putExtra("segundos", 10);
            this.startService(c);
        });
    }

    public static class Contador extends Service {

        private Handler handler = new Handler();
        private int segundos = 0;

        @Override
        public int onStartCommand(Intent intent, int flags, int startId){
            String nome = intent.getStringExtra("nome");
            segundos = intent.getIntExtra("segundos", 0);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("debugggg", String.valueOf(segundos));
                    if(segundos <= 0){
                        stopSelf();
                    }else{
                        segundos--;
                        handler.postDelayed(this, 1000);
                    }
                }
            });
            return Service.START_NOT_STICKY;
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }

}