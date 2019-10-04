package br.com.espelho.AnexosApp.views

import android.content.Intent
import android.graphics.MaskFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import br.com.espelho.AnexosApp.Entities.UsuarioEntity
import br.com.espelho.AnexosApp.R
import br.com.espelho.AnexosApp.business.UsuarioBusiness
import br.com.espelho.AnexosApp.constants.AnexosConstants
import br.com.espelho.AnexosApp.util.SecurityPreferences
import br.com.espelho.AnexosApp.util.ValidationException
import kotlinx.android.synthetic.main.activity_perfil.*
import java.lang.Exception

class PerfilActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mUsuarioBusiness: UsuarioBusiness
    private lateinit var mSecurityPreferences : SecurityPreferences
    private lateinit var mUsuarioEntity: UsuarioEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        mUsuarioBusiness = UsuarioBusiness(this)
        mSecurityPreferences = SecurityPreferences(this)
        atualizarDados()
        preencherCampos()
        setListeners()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.perfil,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId == R.id.alterarSenha){
            startActivity(Intent(this, AlterarSenhaActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.imageButtonEditar ->{
                habilitarCamposParaEdicao(true)
            }
            R.id.imageButtonCancelar ->{
                habilitarCamposParaEdicao(false)
                preencherCampos()
            }
            R.id.imageButtonSalvar ->{
                habilitarCamposParaEdicao(false)
                handleSave()
                atualizarDados()
                preencherCampos()
            }
        }
    }

    private fun habilitarCamposParaEdicao(habilitar: Boolean) {
        editTextTelefone.isEnabled = habilitar
        editTextNome.isEnabled = habilitar
        imageButtonSalvar.isVisible = habilitar
        imageButtonCancelar.isVisible = habilitar
        imageButtonEditar.isVisible = !habilitar
    }

    private fun preencherCampos(){
        editTextTelefone.setText(mUsuarioEntity.telefone)
        editTextNome.setText(mUsuarioEntity.nome)
        textViewEmail.text = mUsuarioEntity.email
    }

    private fun atualizarDados(){
        mUsuarioEntity = mUsuarioBusiness.get(mSecurityPreferences.getStoreString(AnexosConstants.KEY.USER_ID)!!.toInt())
    }

    private fun setListeners(){
        imageButtonEditar.setOnClickListener(this)
        imageButtonCancelar.setOnClickListener(this)
        imageButtonSalvar.setOnClickListener(this)
    }

    private fun handleSave(){
        try {
            val telefone = editTextTelefone.text.toString().replace("(","").replace(")","").replace("-","")
            mUsuarioBusiness.update(editTextNome.text.toString(), telefone)
        }catch (e: ValidationException){
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }catch (e: Exception){
            Toast.makeText(this, getString(R.string.erro_generico), Toast.LENGTH_LONG).show()
        }
    }
}
