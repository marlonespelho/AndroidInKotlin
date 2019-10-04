package br.com.espelho.AnexosApp.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import br.com.espelho.AnexosApp.R
import br.com.espelho.AnexosApp.business.AnexoBusiness
import br.com.espelho.AnexosApp.business.UsuarioBusiness
import br.com.espelho.AnexosApp.util.ValidationException
import kotlinx.android.synthetic.main.activity_cadastro.*
import java.lang.Exception

class CadastroActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mUsuarioBusiness: UsuarioBusiness
    private lateinit var mAnexoBusiness: AnexoBusiness

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)
        setListeners()
        mUsuarioBusiness = UsuarioBusiness(this)
        mAnexoBusiness = AnexoBusiness(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.buttonSalvar -> {
                handleSave()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

   private fun setListeners(){
        buttonSalvar.setOnClickListener(this)
    }

    private fun handleSave(){
        try {
            val nome = editTextNome.text.toString()
            val email = editTextEmail.text.toString()
            val senha = editTextSenha.text.toString()
            val telefone = editTextTelefone.text.toString()
            val idUsuario = mUsuarioBusiness.insert(nome, telefone, email, senha)
            mAnexoBusiness.insert(idUsuario)
            Toast.makeText(this,"cadastrado com sucesso", Toast.LENGTH_LONG).show()
            finish()
        }catch (e: ValidationException){
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }catch (e: Exception){
            Toast.makeText(this, getString(R.string.erro_generico), Toast.LENGTH_LONG).show()
        }

    }


}
