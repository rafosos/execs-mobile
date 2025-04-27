package com.example.ac2exercicios;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final String CANAL_NOTIFICACAO = "Contador";
    final String BROADCAST_NAME = "contador";
    final String ID_CANAL_NOTIFICACAO = "treino";
    final String DESC_CANAL_NOTIFICACAO = "Contador do treino";
    final int PERMISSION_REQUEST_NOTIFICATION = 101;
    NotificationManagerCompat manager;
    int notificationId = 123;
    int exercicioAtivo = 0;
    EditText inputNome;
    EditText inputTempo;
    TextView execAtivo;
    Button btnAdd;
    Button btnIniciar;
    LinearLayout layoutExercicios;
    List<Exercicio> exercicios = new ArrayList<>();

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CANAL_NOTIFICACAO;
            String description = DESC_CANAL_NOTIFICACAO;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(ID_CANAL_NOTIFICACAO, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.createNotificationChannel();
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
        execAtivo = findViewById(R.id.execAtivo);
        btnIniciar = findViewById(R.id.btnIniciar);
        layoutExercicios = findViewById(R.id.layoutExercicios);
        manager = NotificationManagerCompat.from(this);


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
            container.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, //width
                    ViewGroup.LayoutParams.WRAP_CONTENT //height
            ));

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
            container.addView(iconeApagar);

            layoutExercicios.addView(container);
            inputNome.setText("");
            inputTempo.setText("");
        });
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (BROADCAST_NAME.equals(intent.getAction())){
                    String exercicio = intent.getStringExtra("nome");
                    long segundos = intent.getLongExtra("segundos",0);
                    boolean acabou = intent.getBooleanExtra("acabou", false);


                    if(acabou){
                        exercicioAtivo++;
                        iniciarServico();
                    }else{
                        String mensagem = exercicio + " - " + segundos + "s";
                        lancarNotificacao(mensagem);
                        execAtivo.setText(mensagem);
                    }
                }

            }
        };
        IntentFilter filter = new IntentFilter("contador");
        registerReceiver(broadcastReceiver, filter, RECEIVER_NOT_EXPORTED);

        btnIniciar.setOnClickListener(v -> {
             iniciarServico();
        });
    }

    public void iniciarServico(){
        btnIniciar.setEnabled(false);
        if(exercicioAtivo < (long) exercicios.size()){
            Intent c = new Intent(this, Contador.class);
            c.putExtra("nome", exercicios.get(exercicioAtivo).nome);
            c.putExtra("segundos", exercicios.get(exercicioAtivo).tempo);
            this.startService(c);
        }else{
            btnIniciar.setEnabled(true);
            execAtivo.setText("Nenhum exercicio ativo...");
            exercicioAtivo = 0;
            manager.cancel(notificationId);
        }
    }

    public void lancarNotificacao(String texto){
        Intent intent = new Intent(this, this.getClass());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setAction(Intent.ACTION_MAIN);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ID_CANAL_NOTIFICACAO)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Treino")
                .setContentText(texto)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_NOTIFICATION);
            return;
        }
        manager.notify(notificationId, builder.build());
    }


    public static class Contador extends Service {

        private int segundos = 0;

        @Override
        public int onStartCommand(Intent intent, int flags, int startId){
            String nome = intent.getStringExtra("nome");
            segundos = intent.getIntExtra("segundos", 0);

            CountDownTimer timer = new CountDownTimer(segundos*1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Intent intent = new Intent("contador");
                    intent.setPackage(getPackageName());
                    intent.putExtra("nome",nome);
                    intent.putExtra("acabou",false);
                    intent.putExtra("segundos", millisUntilFinished/1000);
                    sendBroadcast(intent);
                }

                @Override
                public void onFinish() {
                    Intent intent = new Intent("contador");
                    intent.putExtra("acabou",true);
                    intent.setPackage(getPackageName());
                    intent.putExtra("nome", "Nenhum exercicio ativo...");
                    intent.putExtra("segundos", 0);
                    sendBroadcast(intent);
                }
            };
            timer.start();

            return Service.START_NOT_STICKY;
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }

}