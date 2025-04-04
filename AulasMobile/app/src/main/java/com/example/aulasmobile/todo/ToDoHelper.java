package com.example.aulasmobile.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ToDoHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todo.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "tarefas";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITULO = "titulo";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_DESCRICAO = "descricao";

    public ToDoHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITULO + " TEXT NOT NULL, " +
                COLUMN_DESCRICAO + " TEXT, " +
                COLUMN_STATUS + " TEXT CHECK( " + COLUMN_STATUS + " IN ('pendente', 'concluida')) NOT NULL DEFAULT 'pendente');";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long add(String titulo, String descricao){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Log.d("debugggg", "oieeeeee, salvando aq $titulo $descricao");
        values.put(COLUMN_TITULO, titulo);
        values.put(COLUMN_DESCRICAO, descricao);
        values.put(COLUMN_STATUS, EStatus.pendente.toString());
        return db.insert(TABLE_NAME, null, values);
    }

    public Cursor getAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public int updateTarefa(int id, String titulo, String descricao, EStatus status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITULO, titulo);
        values.put(COLUMN_DESCRICAO, descricao);
        values.put(COLUMN_STATUS, status.toString());
        return db.update(TABLE_NAME, values, COLUMN_ID+"=?", new String[]{String.valueOf(id)});
    }

    public int deleteTarefa(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_ID +"=?", new String[]{String.valueOf(id)});
    }
}
