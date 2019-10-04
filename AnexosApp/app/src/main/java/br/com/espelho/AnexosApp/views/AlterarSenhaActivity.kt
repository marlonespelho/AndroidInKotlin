package br.com.espelho.AnexosApp.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import br.com.espelho.AnexosApp.R
import br.com.espelho.AnexosApp.business.UsuarioBusiness
import br.com.espelho.AnexosApp.util.ValidationException
import kotlinx.android.synthetic.main.activity_alterar_senha.*
import kotlinx.android.synthetic.main.activity_perfil.*
import java.lang.Exception

class AlterarSenhaActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mUsuarioBusiness: UsuarioBusiness

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alterar_senha)
        mUsuarioBusiness = UsuarioBusiness(this)
        setListeners()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.imageButton ->{
                handleUpdate()
            }
        }
    }

    private fun setListeners(){
        imageButton.setOnClickListener(this)
    }
    private fun handleUpdate(){
        try {
            mUsuarioBusiness.updateSenha(editTextSenhaAtual.text.toString(), editTextSenhaNova.text.toString())
            finish()
        }catch (e: ValidationException){
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }catch (e: Exception){
            Toast.makeText(this, getString(R.string.erro_generico), Toast.LENGTH_LONG).show()
        }
    }
}
