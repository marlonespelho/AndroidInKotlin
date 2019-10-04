package br.com.espelho.AnexosApp.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import br.com.espelho.AnexosApp.R
import br.com.espelho.AnexosApp.business.UsuarioBusiness
import br.com.espelho.AnexosApp.constants.AnexosConstants
import br.com.espelho.AnexosApp.util.SecurityPreferences
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mUsuarioBusiness: UsuarioBusiness
    private lateinit var mSecurityPreferences :SecurityPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mUsuarioBusiness = UsuarioBusiness(this)
        mSecurityPreferences = SecurityPreferences(this)
        setListeners()
    }

    override fun onBackPressed() {
    }

    private fun setListeners(){
        buttonCadastrar.setOnClickListener(this)
        buttonEntrar.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id){
            R.id.buttonCadastrar ->{
                handleCadastro()
            }
            R.id.buttonEntrar -> {
                handleLogin()
            }
        }

    }

    private fun handleLogin(){
        val email = editTextEmail.text.toString()
        val senha = editTextSenha.text.toString()
        if (mUsuarioBusiness.login(email, senha, switchConectado.isChecked)){
            finish()
            setResult(0)
        }
        else{
            Toast.makeText(this, getString(R.string.usuario_senha_incorretos), Toast.LENGTH_LONG).show()
        }
    }

    private fun handleCadastro(){
        startActivity(Intent(this, CadastroActivity::class.java))
    }


}
