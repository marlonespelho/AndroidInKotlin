package br.com.espelho.AnexosApp.business

import android.content.Context
import br.com.espelho.AnexosApp.Entities.UsuarioEntity
import br.com.espelho.AnexosApp.R
import br.com.espelho.AnexosApp.constants.AnexosConstants
import br.com.espelho.AnexosApp.repository.UsuarioRepository
import br.com.espelho.AnexosApp.util.SecurityPreferences
import br.com.espelho.AnexosApp.util.ValidationException
import java.lang.Exception

class UsuarioBusiness (val context:Context){

    private val mUsuarioRepository : UsuarioRepository = UsuarioRepository.getInstance(context)
    private val mSecurityPreferences: SecurityPreferences = SecurityPreferences(context)


    fun insert(nome: String, telefone: String?, email: String, senha:String):Int{
        try {
            validarCampos(nome, email, senha)
            return UsuarioRepository.getInstance(context).insert(nome, telefone, email, senha)
        }catch (e:Exception){
            throw e
        }
    }

    fun login(email: String, senha: String, manterLogado: Boolean) :Boolean{
        val usuario : UsuarioEntity? = mUsuarioRepository.get(email, senha)
        if (usuario != null){
            manterUsuarioLogado(manterLogado)
            mSecurityPreferences.storeSting(AnexosConstants.KEY.USER_ID, usuario.id.toString())
            mSecurityPreferences.storeSting(AnexosConstants.KEY.USER_NAME, usuario.nome)
            mSecurityPreferences.storeSting(AnexosConstants.KEY.USER_EMAIL, usuario.email)
            mSecurityPreferences.storeSting(AnexosConstants.KEY.USER_TELEFONE, usuario.telefone!!)
            return true
        }
        return false
    }

    private fun manterUsuarioLogado(manterLogado: Boolean){
        if (manterLogado){
            mSecurityPreferences.storeSting(AnexosConstants.KEY.MANTER_LOGADO, true.toString())
        } else{
            mSecurityPreferences.storeSting(AnexosConstants.KEY.MANTER_LOGADO, false.toString())
        }
    }

    private fun validarCampos(nome: String, email: String, senha:String){
        if (nome.length < 2){
            throw ValidationException(context.getString(R.string.erro_nome))
        }
        if (senha.length < 6){
            throw ValidationException(context.getString(R.string.erro_senha))
        }
        if (!validarEmail(email) || mUsuarioRepository.emailExistente(email)){
            throw ValidationException(context.getString(R.string.erro_email))
        }
    }

    private fun validarEmail(email: String): Boolean {
        val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return EMAIL_REGEX.toRegex().matches(email)
    }

}