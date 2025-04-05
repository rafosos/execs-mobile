package com.example.ac1filmes;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String[] generos = new String[]{"drama", "suspense", "comédia", "terror", "ação", "ficção"};
    private DbHelper dbHelper;
    private Spinner generoSpinner;
    private Spinner spinnerFiltro;
    private Button btnSalvar;
    private ImageButton btnFiltro;
    private EditText inputTitulo;
    private EditText inputDiretor;
    private EditText inputLancamento;
    private CheckBox cinemaCheckbox;
    private LinearLayout containerFilmes;

    private ArrayList<ImageButton> estrelas = new ArrayList<>();
    private ArrayList<Boolean> estrelasValues = new ArrayList<>();

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

        this.zerarEstrelas();

        dbHelper = new DbHelper(this);
        containerFilmes = findViewById(R.id.containerFilmes);
        btnSalvar = findViewById(R.id.btnSalvar);
        inputTitulo = findViewById(R.id.inputTitulo);
        inputDiretor = findViewById(R.id.inputDiretor);
        inputLancamento = findViewById(R.id.inputLancamento);
        cinemaCheckbox = findViewById(R.id.cinema);
        generoSpinner = findViewById(R.id.spinnerGenero);
        generoSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, generos));
        btnSalvar.setOnClickListener(this::onClickSalvar);
        btnFiltro = findViewById(R.id.btnFiltro);

        spinnerFiltro = findViewById(R.id.spinnerFiltro);
        spinnerFiltro.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, generos));
        btnFiltro.setOnClickListener(v -> {
            Log.d("debugggg", "chamou");
            carregarResultados(dbHelper.getByGenero(spinnerFiltro.getSelectedItem().toString()));
        });

        carregarFilmes();
    }

    private void onClickSalvar(View v){
        long res = dbHelper.add(
                inputTitulo.getText().toString(),
                inputDiretor.getText().toString(),
                Integer.parseInt(inputLancamento.getText().toString()),
                getNota(),
                generoSpinner.getSelectedItem().toString(),
                cinemaCheckbox.isChecked()
        );
        if(res != -1){
            Toast.makeText(MainActivity.this, "Filme adicionado com sucesso!", Toast.LENGTH_SHORT).show();
            zerarInputs();
            carregarFilmes();
        }else {
            Toast.makeText(MainActivity.this, "Erro ao adicionar filme :(", Toast.LENGTH_SHORT).show();
        }
    }

    private void carregarFilmes(){
        Cursor cursor = dbHelper.getAll();
        carregarResultados(cursor);
    }

    private void carregarResultados(Cursor cursor){
        containerFilmes.removeAllViews();
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String titulo = cursor.getString(1);
                Log.d("debugggg", titulo);
                String diretor = cursor.getString(2);
                int lancamento = cursor.getInt(3);
                int estrelas = cursor.getInt(4);
                String genero = cursor.getString(5);
                boolean cinema = cursor.getInt(6) > 0;

                LinearLayout filme = new LinearLayout(this);
                filme.setOrientation(LinearLayout.VERTICAL);

                TextView viewTitulo = new TextView(this);
                viewTitulo.setText(titulo);
                viewTitulo.setTextSize(20);
                filme.addView(viewTitulo);

                LinearLayout containerInfos = new LinearLayout(this);
                containerInfos.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout containerEsquerda = new LinearLayout(this);
                containerEsquerda.setOrientation(LinearLayout.VERTICAL);
                containerEsquerda.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        1.0f
                    ));

                TextView viewDiretor = new TextView(this);
                String txtDiretor = "Diretor: " + diretor;
                viewDiretor.setText(txtDiretor);
                containerEsquerda.addView(viewDiretor);

                TextView viewAno = new TextView(this);
                viewAno.setText(String.valueOf(lancamento));
                containerEsquerda.addView(viewAno);

                containerInfos.addView(containerEsquerda);

                LinearLayout containerDireita = new LinearLayout(this);
                containerDireita.setOrientation(LinearLayout.VERTICAL);
                containerDireita.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        1.0f
                ));


                TextView viewGenero = new TextView(this);
                viewGenero.setText(genero);
                containerDireita.addView(viewGenero);

                TextView viewCinema = new TextView(this);
                viewCinema.setText(cinema ? "Vi" : "Não vi" +" no cinema");
                containerDireita.addView(viewCinema);

                containerInfos.addView(containerDireita);
                filme.addView(containerInfos);

                TextView viewNota = new TextView(this);
                viewNota.setText(String.valueOf(estrelas));
                filme.addView(viewNota);


                filme.setOnLongClickListener(v -> {
                    int deletado = dbHelper.delete(id);
                    if (deletado > 0) {
                        Toast.makeText(this, "Filme excluido", Toast.LENGTH_SHORT).show();
                        carregarFilmes();
                    }
                    return true;
                });

                filme.setOnClickListener(v -> {
                    inputTitulo.setText(titulo);
                    inputDiretor.setText(diretor);
                    inputLancamento.setText(String.valueOf(lancamento));
                    setEstrelas(estrelas);
                    generoSpinner.setSelection(Arrays.asList(generos).indexOf(genero));
                    cinemaCheckbox.setChecked(cinema);

                    btnSalvar.setText("ATUALIZAR");
                    btnSalvar.setOnClickListener(b -> {
                        String tituloNovo = inputTitulo.getText().toString();
                        String diretorNovo = inputDiretor.getText().toString();
                        int anoNovo = Integer.parseInt(inputLancamento.getText().toString());
                        int notaNova = getNota();
                        String generoNovo = generoSpinner.getSelectedItem().toString();
                        boolean cinemaNovo = cinemaCheckbox.isChecked();
                        int resUpdate = dbHelper.update(id, tituloNovo, diretorNovo, anoNovo, notaNova, generoNovo, cinemaNovo);
                        if (resUpdate > 0){
                            Toast.makeText(this, "Filme atualizado com sucesso 2.0", Toast.LENGTH_SHORT).show();
                            carregarFilmes();
                            zerarInputs();
                            btnSalvar.setText("SALVAR FILME");
                            btnSalvar.setOnClickListener(this::onClickSalvar);
                        } else {
                            Toast.makeText(this, "Erro ao atualizar filme :(", Toast.LENGTH_SHORT).show();
                        }
                    });
                });

                containerFilmes.addView(filme);
            } while (cursor.moveToNext());
        }
    }

    private void setEstrelas(int index){
        zerarEstrelas();
        estrelasValues = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            estrelas.get(i).setImageResource(i < index ? R.drawable.estrela_cheia : R.drawable.estrela_vazia);
            estrelasValues.add(i < index);
        }
    }

    private void zerarInputs(){
        inputTitulo.setText("");
        inputDiretor.setText("");
        inputLancamento.setText("");
        zerarEstrelas();
        generoSpinner.setSelected(false);
        cinemaCheckbox.setChecked(false);
    }

    private int getNota(){
        int nota = 0;
        for (Boolean value : estrelasValues){
            if(value) nota ++;
        }
        return nota;
    }

    private void zerarEstrelas(){
        estrelas = new ArrayList<>();
        estrelasValues = new ArrayList<>();

        ImageButton estrela1 = findViewById(R.id.estrela1);
        estrela1.setImageResource(R.drawable.estrela_vazia);
        estrela1.setOnClickListener(v -> {setEstrelas(1);});
        estrelas.add(estrela1);
        estrelasValues.add(false);

        ImageButton estrela2 = findViewById(R.id.estrela2);
        estrela2.setImageResource(R.drawable.estrela_vazia);
        estrela2.setOnClickListener(v -> {
            setEstrelas(2);
        });
        estrelas.add(estrela2);
        estrelasValues.add(false);

        ImageButton estrela3 = findViewById(R.id.estrela3);
        estrela3.setImageResource(R.drawable.estrela_vazia);
        estrela3.setOnClickListener(v -> {
            setEstrelas(3);
        });
        estrelas.add(estrela3);
        estrelasValues.add(false);

        ImageButton estrela4 = findViewById(R.id.estrela4);
        estrela4.setImageResource(R.drawable.estrela_vazia);
        estrela4.setOnClickListener(v -> {
            setEstrelas(4);
        });
        estrelas.add(estrela4);
        estrelasValues.add(false);

        ImageButton estrela5 = findViewById(R.id.estrela5);
        estrela5.setImageResource(R.drawable.estrela_vazia);
        estrela5.setOnClickListener(v -> {
            setEstrelas(5);
        });
        estrelas.add(estrela5);
        estrelasValues.add(false);
    }
}