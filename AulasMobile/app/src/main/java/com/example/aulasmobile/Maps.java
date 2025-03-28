package com.example.aulasmobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Maps extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_maps);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { finish(); }
        });

        EditText inputPartida = findViewById(R.id.inputPartida);
        EditText inputChegada = findViewById(R.id.inputChegada);

        Button btnEndereco = findViewById(R.id.btnEndereco);
        Button btnCoordenada = findViewById(R.id.btnCoordenada);
        Button btnRota = findViewById(R.id.btnRota);
        Button btnEmail = findViewById(R.id.btnEmail);
        Button btnSMS = findViewById(R.id.btnSMS);

//        1) Faca uma aplicação que de 3 opções ao usuário de busca no
//        Google Maps. A primeira baseada no endereço digitado, a
//        segunda baseada na coordenada especifica e a terceira para
//        gerar a rota entre dois endereços.

        btnEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String endereco = inputPartida.getText().toString();
                endereco = String.join("+", endereco.split(" "));
                Uri uri = Uri.parse("geo://0,0?q=" + endereco);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        btnCoordenada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lat = inputPartida.getText().toString();
                String lon = inputChegada.getText().toString();
                Uri uriGeo = Uri.parse("geo:"+lat+","+lon);
                Intent it = new Intent(Intent.ACTION_VIEW, uriGeo);
                startActivity(it);
            }
        });

        btnRota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String partida = inputPartida.getText().toString();
                String destino = inputChegada.getText().toString();
                Uri uriGeo = Uri.parse("https://maps.google.com/maps?f=d&saddr="+partida+"&daddr="+destino+"&hl=pt");
                Intent it = new Intent(Intent.ACTION_VIEW, uriGeo);
                startActivity(it);
            }
        });

//        2) Faça uma pesquisa e descubra como utilizar uma Intent para
//        enviar um SMS e para enviar um email através de uma aplicação
//        para Android.

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputPartida.getText().toString();
                String corpo = inputChegada.getText().toString();
                Uri uri = Uri.parse("mailto:" + email);
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(uri);
                intent.putExtra(Intent.EXTRA_SUBJECT, corpo);
                startActivity(intent);
            }
        });

        btnSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numero = inputPartida.getText().toString();
                String mensagem = inputChegada.getText().toString();
                Uri uri = Uri.parse("smsto:" + numero);
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", mensagem);
                startActivity(intent);
            }
        });

    }
}
