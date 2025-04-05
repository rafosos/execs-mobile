package com.example.ac1filmes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "filmesac1.db";
    private static int DATABASE_VERSION = 1;
    private static String TABLE_NAME = "filmes";
    private static String COLUMN_ID = "id";
    private static String COLUMN_TITULO = "titulo";
    private static String COLUMN_DIRETOR = "diretor";
    private static String COLUMN_LANCAMENTO = "ano_lancamento";
    private static String COLUMN_NOTA = "nota";
    private static String COLUMN_GENERO = "genero";
    private static String COLUMN_CINEMA = "viu_cinema";

    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITULO + " TEXT NOT NULL, " +
                COLUMN_DIRETOR + " TEXT, " +
                COLUMN_LANCAMENTO + " INTEGER, " +
                COLUMN_NOTA + " INTEGER CHECK(" + COLUMN_NOTA +" > 0 and " + COLUMN_NOTA + " < 6) NOT NULL, " +
                COLUMN_GENERO + " TEXT DEFAULT 'não definido', " +
                COLUMN_CINEMA + " BOOLEAN )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long add(String titulo, String diretor, int ano_lancamento, int nota, String genero, boolean cinema){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITULO, titulo);
        values.put(COLUMN_DIRETOR, diretor);
        values.put(COLUMN_LANCAMENTO, ano_lancamento);
        values.put(COLUMN_NOTA, nota);
        values.put(COLUMN_GENERO, genero);
        values.put(COLUMN_CINEMA, cinema);
        return db.insert(TABLE_NAME, null, values);
    }

    public Cursor getAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public Cursor getByGenero(String genero){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE genero like ?", new String[]{genero});
    }

    public int update(int id, String titulo, String diretor, int ano_lancamento, int nota, String genero, boolean cinema){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITULO, titulo);
        values.put(COLUMN_DIRETOR, diretor);
        values.put(COLUMN_LANCAMENTO, ano_lancamento);
        values.put(COLUMN_NOTA, nota);
        values.put(COLUMN_GENERO, genero);
        values.put(COLUMN_CINEMA, cinema);
        return db.update(TABLE_NAME, values, COLUMN_ID+"=?", new String[]{String.valueOf(id)});
    }

    public int delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_ID +"=?", new String[]{String.valueOf(id)});
    }
}
