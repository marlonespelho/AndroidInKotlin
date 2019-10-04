package br.com.espelho.AnexosApp.util

import org.mindrot.jbcrypt.BCrypt
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and

class SenhaUtils {

    fun criptografarSenha(senha: String) : String{
        return BCrypt.hashpw(senha, BCrypt.gensalt())
    }

    fun validarSenha(senhaSalva: String, senhaInformada: String): Boolean {
        return BCrypt.checkpw(senhaInformada, senhaSalva)
    }

}