package com.example.aulasmobile;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Threadss extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_threadss);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        threadd t = new threadd();
        t.start();

        Button btnIniciar = findViewById(R.id.btnIniciar);
        btnIniciar.setOnClickListener(v -> {
            Intent intent = new Intent(this, ServicoTop.class);
            startService(intent);
        });
    }

    class threadd extends Thread{
        public void run(){
            Log.i("debugggg", "oi sou uma thread");
            try{
                Thread.sleep(3000);
                TextView txt = findViewById(R.id.txt);
                txt.setText("oi troquei o texto");
            }catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }
    }

    public static class ServicoTop extends Service {

        private Handler handler = new Handler();
        private int segundos = 0;

        @Override public void onCreate(){}

        @Override
        public int onStartCommand(Intent intent, int flags, int startId){
            segundos = 0;

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i("debugggg", "service run");
                    segundos++;
                    if (segundos < 10){
                        handler.postDelayed(this,1000);
                    } else{
                        Log.i("debugggg", "10 segundos passaram");
                        Toast.makeText(getApplicationContext(), "10 segundos se passaram 'O'", Toast.LENGTH_SHORT).show();
                        stopSelf();
                    }
                }
            }, 1000);

            return Service.START_NOT_STICKY;
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent){
            return null;
        }
    }
}