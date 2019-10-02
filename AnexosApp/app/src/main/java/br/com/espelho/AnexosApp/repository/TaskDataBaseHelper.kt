package br.com.espelho.AnexosApp.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.com.espelho.AnexosApp.constants.DataBaseConstants

class TaskDataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    companion object {
        private val DATABASE_VERSION: Int = 1
        private val DATABASE_NAME: String = "bd_anexos"
    }

    private val createTableUser = """
        CREATE TABLE ${DataBaseConstants.USER.TABLE_NAME}(
            ${DataBaseConstants.USER.COLUMNS.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${DataBaseConstants.USER.COLUMNS.NOME} TEXT NOT NULL,
            ${DataBaseConstants.USER.COLUMNS.TELEFONE} TEXT,
            ${DataBaseConstants.USER.COLUMNS.EMAIL} TEXT NOT NULL UNIQUE,
            ${DataBaseConstants.USER.COLUMNS.SENHA} TEXT NOT NULL
        );
        """

    private val createTableAnexo = """
        CREATE TABLE ${DataBaseConstants.ANEXO.TABLE_NAME} (
            ${DataBaseConstants.ANEXO.COLUMNS.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${DataBaseConstants.ANEXO.COLUMNS.USER} INTEGER NOT NULL 
                CONSTRAINT fk_user REFERENCES ${DataBaseConstants.USER.TABLE_NAME} 
                (${DataBaseConstants.USER.COLUMNS.ID}) ON DELETE CASCADE,
            ${DataBaseConstants.ANEXO.COLUMNS.TIPOANEXO} TEXT,
            ${DataBaseConstants.ANEXO.COLUMNS.ANEXO} BLOB
        );
    """

    private val deleteTableUser = "drop table if exists ${DataBaseConstants.USER.TABLE_NAME}"
    private val deleteTableAnexo = "drop table if exists ${DataBaseConstants.ANEXO.TABLE_NAME}"

    override fun onCreate(sqLite: SQLiteDatabase) {
        sqLite.execSQL(createTableUser)
        sqLite.execSQL(createTableAnexo)
    }

    override fun onUpgrade(sqLite: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        sqLite.execSQL(deleteTableUser)
        sqLite.execSQL(deleteTableAnexo)
        sqLite.execSQL(createTableUser)
        sqLite.execSQL(createTableAnexo)
    }
}