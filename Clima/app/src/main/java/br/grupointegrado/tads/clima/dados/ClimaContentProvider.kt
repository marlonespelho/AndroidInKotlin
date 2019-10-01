package br.grupointegrado.tads.clima.dados

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import br.grupointegrado.tads.clima.util.DataUtils

class ClimaContentProvider : ContentProvider() {

    var bdHelper : ClimaBDHelper? = null

    companion object {
        var COD_CLIMA = 100
        var CODE_CLIMA_POR_DATA = 101
        val uriMatcher : UriMatcher
        init {
            uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
            uriMatcher.addURI(ClimaContrato.AUTORIDADE, ClimaContrato.URI_CLIMA, COD_CLIMA)
            uriMatcher.addURI(ClimaContrato.AUTORIDADE, "${ClimaContrato.URI_CLIMA}/#", CODE_CLIMA_POR_DATA)
        }
    }

    override fun onCreate(): Boolean {
        this.bdHelper = ClimaBDHelper(this.context)
        return true
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        val db = bdHelper!!.readableDatabase
        val cursor : Cursor
        when(uriMatcher.match(uri)){
            COD_CLIMA ->{
                cursor = db.query(ClimaContrato.Climas.TABELA,
                        null, null, null, null, null, sortOrder)
            }
            CODE_CLIMA_POR_DATA ->{
                val dataClima = uri.lastPathSegment
                val where = "${ClimaContrato.Climas.COLUNA_DATA_HORA} = ?"
                val whereArgs = arrayOf(dataClima.toString())
                cursor = db.query(ClimaContrato.Climas.TABELA,
                        null,
                        where, whereArgs,
                        null, null, sortOrder)

            }
            else -> throw UnsupportedOperationException("Uri Desconhecida: $uri")
        }
        cursor.setNotificationUri(context.contentResolver, uri)
        return cursor
    }

    override fun bulkInsert(uri: Uri, values: Array<out ContentValues>): Int {
        val db = bdHelper!!.writableDatabase

        when(uriMatcher.match(uri)){
            COD_CLIMA ->{
                var registrosInseridos = 0
                db.beginTransaction()
                try {
                    for (value in values){
                        val dataClima = value.getAsLong(ClimaContrato.Climas.COLUNA_DATA_HORA)
                        if (!DataUtils.dataEstaNormalizada(dataClima)){
                            throw IllegalArgumentException("A data deve estar normalizada")
                        }
                        val _id = db.insert(ClimaContrato.Climas.TABELA, null, value)
                        if (_id != -1L){
                            registrosInseridos++
                        }
                    }
                    db.setTransactionSuccessful()
                }finally {
                    db.endTransaction()
                }
                if (registrosInseridos > 0){
                    context.contentResolver.notifyChange(uri, null)
                }
                return registrosInseridos
            }
        }

        return super.bulkInsert(uri, values)
    }

    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun shutdown() {
        this.bdHelper?.close()
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        var registrosDeletados: Int
        val selecao = if (selection != null) selection else ""
        when (uriMatcher.match(uri)) {
            COD_CLIMA -> {
                registrosDeletados = bdHelper!!.getWritableDatabase().
                        delete(ClimaContrato.Climas.TABELA,
                        selecao, selectionArgs)
            }
            else -> throw UnsupportedOperationException("Unknown uri: $uri")
        }
        if (registrosDeletados != 0) {
            context.contentResolver.notifyChange(uri, null)
        }
       return registrosDeletados
    }

    override fun getType(uri: Uri?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}