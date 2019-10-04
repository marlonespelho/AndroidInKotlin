package br.com.espelho.AnexosApp.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import br.com.espelho.AnexosApp.Entities.UsuarioEntity
import br.com.espelho.AnexosApp.constants.DataBaseConstants
import br.com.espelho.AnexosApp.util.SenhaUtils
import java.lang.Exception

class UsuarioRepository private constructor(context : Context){

    private var mTaskDataBaseHelper: TaskDataBaseHelper = TaskDataBaseHelper(context)

    companion object{
        private var INSTANCE : UsuarioRepository? = null
        fun getInstance(context : Context) : UsuarioRepository{
            if (INSTANCE == null){
                INSTANCE = UsuarioRepository(context)
            }
            return INSTANCE as UsuarioRepository
        }
    }

    fun insert(nome: String, telefone: String?, email: String, senha:String): Int{
        val db = mTaskDataBaseHelper.writableDatabase
        val insertValues = ContentValues()
        insertValues.put(DataBaseConstants.USER.COLUMNS.NOME, nome)
        insertValues.put(DataBaseConstants.USER.COLUMNS.TELEFONE, telefone)
        insertValues.put(DataBaseConstants.USER.COLUMNS.EMAIL, email)
        insertValues.put(DataBaseConstants.USER.COLUMNS.SENHA, SenhaUtils().criptografarSenha(senha))
        return db.insert(DataBaseConstants.USER.TABLE_NAME,null, insertValues).toInt()
    }

    fun emailExistente(email: String): Boolean {
        var exist: Boolean
        try {
            val db = mTaskDataBaseHelper.readableDatabase
            val projection = arrayOf(DataBaseConstants.USER.COLUMNS.ID)
            val selection = "${DataBaseConstants.USER.COLUMNS.EMAIL} = ?"
            val selectionArgs = arrayOf(email)
            val cursor : Cursor = db.query(DataBaseConstants.USER.TABLE_NAME, projection, selection, selectionArgs, null, null, null)
            exist = cursor.count > 0
            cursor.close()
        }catch (e: Exception){
            throw  e
        }
        return exist
    }

    fun get(email: String, senha: String) : UsuarioEntity?{
        var usuarioEntity: UsuarioEntity? = null
        try {
            val db = mTaskDataBaseHelper.readableDatabase
            val projection = arrayOf(DataBaseConstants.USER.COLUMNS.ID,
                DataBaseConstants.USER.COLUMNS.NOME,
                DataBaseConstants.USER.COLUMNS.EMAIL,
                DataBaseConstants.USER.COLUMNS.SENHA,
                DataBaseConstants.USER.COLUMNS.TELEFONE
                )
            val selection = "${DataBaseConstants.USER.COLUMNS.EMAIL} = ?"
            val selectionArgs = arrayOf(email)
            val cursor : Cursor = db.query(DataBaseConstants.USER.TABLE_NAME, projection, selection, selectionArgs, null, null, null)
            if (cursor.count > 0){
                cursor.moveToFirst()
                usuarioEntity = UsuarioEntity(
                    cursor.getInt(cursor.getColumnIndex(DataBaseConstants.USER.COLUMNS.ID)),
                    cursor.getString(cursor.getColumnIndex(DataBaseConstants.USER.COLUMNS.NOME)),
                    cursor.getString(cursor.getColumnIndex(DataBaseConstants.USER.COLUMNS.EMAIL)),
                    cursor.getString(cursor.getColumnIndex(DataBaseConstants.USER.COLUMNS.SENHA)),
                    cursor.getString(cursor.getColumnIndex(DataBaseConstants.USER.COLUMNS.TELEFONE))
                )
                if (!SenhaUtils().validarSenha(usuarioEntity.senha, senha)){
                    usuarioEntity = null
                }
            }
            cursor.close()
        }catch (e: Exception) {
            return null
        }

        return usuarioEntity
    }

    fun getById(idUsuario: Int): UsuarioEntity? {
        val db = mTaskDataBaseHelper.readableDatabase
        var usuarioEntity: UsuarioEntity? = null
        val projection = arrayOf(DataBaseConstants.USER.COLUMNS.ID,
            DataBaseConstants.USER.COLUMNS.NOME,
            DataBaseConstants.USER.COLUMNS.EMAIL,
            DataBaseConstants.USER.COLUMNS.SENHA,
            DataBaseConstants.USER.COLUMNS.TELEFONE
        )
        val selection = "${DataBaseConstants.USER.COLUMNS.ID} = ?"
        val selectionArgs = arrayOf(idUsuario.toString())
        val cursor : Cursor = db.query(DataBaseConstants.USER.TABLE_NAME, projection, selection, selectionArgs, null, null, null)
        if (cursor.count > 0){
            cursor.moveToFirst()
            usuarioEntity = UsuarioEntity(
                cursor.getInt(cursor.getColumnIndex(DataBaseConstants.USER.COLUMNS.ID)),
                cursor.getString(cursor.getColumnIndex(DataBaseConstants.USER.COLUMNS.NOME)),
                cursor.getString(cursor.getColumnIndex(DataBaseConstants.USER.COLUMNS.EMAIL)),
                cursor.getString(cursor.getColumnIndex(DataBaseConstants.USER.COLUMNS.SENHA)),
                cursor.getString(cursor.getColumnIndex(DataBaseConstants.USER.COLUMNS.TELEFONE))
            )
        }
        cursor.close()
        return  usuarioEntity
    }

    fun update(idUsuario: Int, nome: String, telefone: String) {
        try {
            val db = mTaskDataBaseHelper.writableDatabase
            val updataValues = ContentValues()
            updataValues.put(DataBaseConstants.USER.COLUMNS.NOME, nome)
            updataValues.put(DataBaseConstants.USER.COLUMNS.TELEFONE, telefone)
            val selection = "${DataBaseConstants.USER.COLUMNS.ID} = ?"
            val selectionArgs = arrayOf(idUsuario.toString())
            db.update(DataBaseConstants.USER.TABLE_NAME, updataValues, selection, selectionArgs)
        }catch (e:Exception){
            throw e
        }
    }

    fun update(idUsuario: Int, senha: String) {
        try {
            val db = mTaskDataBaseHelper.writableDatabase
            val updataValues = ContentValues()
            updataValues.put(DataBaseConstants.USER.COLUMNS.SENHA, SenhaUtils().criptografarSenha(senha))
            val selection = "${DataBaseConstants.USER.COLUMNS.ID} = ?"
            val selectionArgs = arrayOf(idUsuario.toString())
            db.update(DataBaseConstants.USER.TABLE_NAME, updataValues, selection, selectionArgs)
        }catch (e:Exception){
            throw e
        }
    }

}