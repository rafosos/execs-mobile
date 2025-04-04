package com.example.aulasmobile;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class DbTeste extends AppCompatActivity {

    private BancoHelper dbHelper;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listaUsuarios;
    private ArrayList<Integer> listaIds;
    private ListView listViewUsuarios;
    private EditText inputNome;
    private EditText inputEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_db_teste);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new BancoHelper(this);

        TextView btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { finish(); }
        });

        inputNome = findViewById(R.id.inputNome);
        inputEmail = findViewById(R.id.inputEmail);
        Button btnSalvar = findViewById(R.id.btnSalvar);
        listViewUsuarios = findViewById(R.id.listUsuarios);

        btnSalvar.setOnClickListener(this::onClickSalvar);

        listViewUsuarios.setOnItemClickListener((parent, view, position, id) -> {
            int userId = listaIds.get(position);
            String nome = listaUsuarios.get(position).split(" - ")[1];
            String email = listaUsuarios.get(position).split(" - ")[2];

            inputNome.setText(nome);
            inputEmail.setText(email);

            btnSalvar.setText("ATUALIZAR");

            btnSalvar.setOnClickListener(v -> {


            String novoNome = inputNome.getText().toString();
            String novoEmail = inputEmail.getText().toString();

            if(!novoNome.isEmpty() || !novoEmail.isEmpty()){
                int resultado = dbHelper.atualizarUsuario(userId, novoNome, novoEmail);
                if (resultado > 0){
                    Toast.makeText(this, "usuario atualizado 2.0", Toast.LENGTH_SHORT).show();
                    carregarUsuarios();
                    inputNome.setText("");
                    inputEmail.setText("");
                    btnSalvar.setText("SALVAR");
                    btnSalvar.setOnClickListener(this::onClickSalvar);
                } else {
                    Toast.makeText(this, "erro ao atualizar :(", Toast.LENGTH_SHORT).show();
                }
            }
            });

        });

        listViewUsuarios.setOnItemLongClickListener((adapterView, view1, pos, l) -> {
            int idUsuario = listaIds.get(pos);
            int deletado = dbHelper.excluirUsuario(idUsuario);

            if (deletado > 0) {
                Toast.makeText(this, "usuario excluido já era fi", Toast.LENGTH_SHORT).show();
                carregarUsuarios();
            }
            return true;
        });

    }

    private void carregarUsuarios(){
        Cursor cursor = dbHelper.listarUsuarios();
        listaUsuarios = new ArrayList<>();
        listaIds = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String nome = cursor.getString(1);
                String email = cursor.getString(2);

                listaUsuarios.add(id + " - " + nome + " - " + email);
                listaIds.add(id);
            } while (cursor.moveToNext());
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaUsuarios);
        listViewUsuarios.setAdapter(adapter);
    }

    private void onClickSalvar(View v){
        String nome = inputNome.getText().toString();
        String email = inputEmail.getText().toString();

        if(!nome.isEmpty() && !email.isEmpty()){
            long resultado = dbHelper.inserirUsuario(nome, email);
            if (resultado != -1){
                Toast.makeText(this, "deu bom UwU", Toast.LENGTH_SHORT).show();
                inputNome.setText("");
                inputEmail.setText("");
                carregarUsuarios();
            }else{
                Toast.makeText(this, "erro ao salvar", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "preenche nome e email direito ai po", Toast.LENGTH_SHORT).show();
        }
    }
}