package com.example.aulasmobile.todo;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aulasmobile.R;

import java.util.ArrayList;
import java.util.List;

public class ToDo extends AppCompatActivity {
    private ToDoHelper dbHelper;
    private ListView listView;
    private LinearLayout containerCheckboxes;
    private ArrayList<String> listaTarefas;
    private ArrayList<Integer> listaId;
    private List<CheckBox> checkBoxList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private EditText inputTitle;
    private EditText inputDescricao;
    private Button btnAdicionar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_to_do);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.dbHelper = new ToDoHelper(this);

        TextView btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { finish(); }
        });

        this.inputTitle = findViewById(R.id.inputTitle);
        this.inputDescricao = findViewById(R.id.inputDescricao);
//        this.listView = findViewById(R.id.listTarefas);
        this.btnAdicionar = findViewById(R.id.btnAdd);
        this.containerCheckboxes = findViewById(R.id.listTarefas);

        this.btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = inputTitle.getText().toString();
                String descricao = inputDescricao.getText().toString();

                if(!title.isEmpty()){
                    long res = dbHelper.add(title, descricao);
                    if(res != -1){
                        Toast.makeText(ToDo.this, "Tarefa adicionada com sucesso!", Toast.LENGTH_SHORT).show();
                        inputDescricao.setText("");
                        inputTitle.setText("");
                        carregarTarefas();
                    }else {
                        Toast.makeText(ToDo.this, "O título não pode ser nulo!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        this.carregarTarefas();

    }

    private void carregarTarefas(){
        Cursor cursor = dbHelper.getAll();
        listaId = new ArrayList<>();
        listaTarefas = new ArrayList<>();

        this.containerCheckboxes.removeAllViews();

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String titulo = cursor.getString(1);
                String descricao = cursor.getString(2);
                EStatus status = EStatus.valueOf(cursor.getString(3));

                listaId.add(id);
                listaTarefas.add(titulo + " - " + descricao);

                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(titulo + " - " + descricao);
                checkBox.setChecked(status == EStatus.concluida);
                checkBox.setOnCheckedChangeListener((v, i) -> {
                    dbHelper.updateTarefa(id, titulo, descricao, i ? EStatus.concluida : EStatus.pendente);
                    carregarTarefas();
                });
                this.containerCheckboxes.addView(checkBox);
                this.checkBoxList.add(checkBox);
            } while (cursor.moveToNext());
        }

//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, listaTarefas);
//        listView.setAdapter(adapter);

    }
}