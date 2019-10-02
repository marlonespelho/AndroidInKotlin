package br.com.espelho.AnexosApp.business

import android.content.Context
import br.com.espelho.AnexosApp.Entities.AnexoEntity
import br.com.espelho.AnexosApp.constants.AnexosConstants
import br.com.espelho.AnexosApp.repository.AnexoRepository
import br.com.espelho.AnexosApp.util.SecurityPreferences
import java.lang.Exception

class AnexoBusiness (val context: Context){

    private val mAnexoRepository : AnexoRepository = AnexoRepository.getInstance(context)
    private val mSecurityPreferences: SecurityPreferences = SecurityPreferences(context)

    fun insert(usarioId: Int){
        try {
            AnexoRepository.getInstance(context).insert(usarioId)
        }catch (e: Exception){
            throw e
        }
    }

    fun getList(): MutableList<AnexoEntity>{
        val idUsuario = mSecurityPreferences.getStoreString(AnexosConstants.KEY.USER_ID)!!.toInt()
        return mAnexoRepository.getList(idUsuario)
    }

    fun  get(anexoId: Int) : AnexoEntity?{
        return mAnexoRepository.get(anexoId)
    }

    fun update(anexo: AnexoEntity) : AnexoEntity{
        mAnexoRepository.update(anexo)
        return mAnexoRepository.get(anexo.id)!!
    }

}