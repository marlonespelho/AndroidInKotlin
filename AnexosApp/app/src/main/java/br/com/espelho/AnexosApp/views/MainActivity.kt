package br.com.espelho.AnexosApp.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import br.com.espelho.AnexosApp.R
import br.com.espelho.AnexosApp.constants.AnexosConstants
import br.com.espelho.AnexosApp.util.SecurityPreferences
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mSecurityPreferences : SecurityPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageViewInicio.setImageResource(R.mipmap.ic_solpac_round)
        mSecurityPreferences = SecurityPreferences(this)

    }

    override fun onResume() {
        super.onResume()
        if (!verificarLogado()){
            startActivityForResult(Intent(this, LoginActivity::class.java) ,0)
        }else{
            iniciarFragment()
            textViewMsgBoasVindas.text = "Bem vindo(a), ${mensagemBoasVidas()}"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 0 && !verificarLogado()){
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main,menu)
        return true
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this).setTitle(R.string.loggout)
            .setMessage(R.string.msg_sair)
            .setPositiveButton(R.string.confirmar){dialog, which ->
                if (!verificarManterLogado()){
                    handleLoggout()
                }
                finish()}
            .setNegativeButton(R.string.cancelar){dialog, which ->}
            .create().show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.loggout ->{
                AlertDialog.Builder(this).setTitle(R.string.loggout)
                    .setMessage(R.string.msg_loggout)
                    .setPositiveButton(R.string.confirmar){dialog, which ->
                        handleLoggout()
                        startActivity(Intent(this, MainActivity::class.java))}
                    .setNegativeButton(R.string.cancelar){dialog, which ->}
                    .create().show()
            }
            R.id.perfil -> {
                startActivity(Intent(this, PerfilActivity::class.java))
            }
            R.id.qrcode -> {
                AlertDialog.Builder(this).setTitle(R.string.qrcode)
                    .setMessage(R.string.abrir_qrcode)
                    .setPositiveButton(R.string.confirmar){dialog, which ->
                        startActivity(Intent(this, ScannerCodBarras::class.java))
                    }
                    .setNegativeButton(R.string.cancelar){dialog, which ->}.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleLoggout(){
        mSecurityPreferences.removeStoreString(AnexosConstants.KEY.MANTER_LOGADO)
        mSecurityPreferences.removeStoreString(AnexosConstants.KEY.USER_EMAIL)
        mSecurityPreferences.removeStoreString(AnexosConstants.KEY.USER_NAME)
        mSecurityPreferences.removeStoreString(AnexosConstants.KEY.USER_ID)
        finish()
    }

    private fun iniciarFragment(){
        val fragment: Fragment = AnexosListFragment.newInstance()
        supportFragmentManager.beginTransaction().replace(R.id.frameAnexos, fragment).commit()
    }

    private fun mensagemBoasVidas() :String{
        val nome = mSecurityPreferences.getStoreString(AnexosConstants.KEY.USER_NAME)

        if (nome!!.indexOf(" ") < 0 ){
            return nome
        }
        else{
            return nome.substring(0,nome.indexOf(" "))
        }
    }

    private fun verificarLogado():Boolean{
        val logado = mSecurityPreferences.getStoreString(AnexosConstants.KEY.USER_ID)
        if (logado == null || logado.isEmpty() ){
            return false
        }
        return true
    }
    private fun verificarManterLogado():Boolean{
        val manterLogado = mSecurityPreferences.getStoreString(AnexosConstants.KEY.MANTER_LOGADO)?.toBoolean()
        if (manterLogado == null){
            return false
        }
        return manterLogado
    }

}
