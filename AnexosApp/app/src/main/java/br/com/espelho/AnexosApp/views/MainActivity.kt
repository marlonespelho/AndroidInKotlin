package br.com.espelho.AnexosApp.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
        mSecurityPreferences = SecurityPreferences(this)
        iniciarFragment()
        textViewMsgBoasVindas.text = "Bem vindo(a), ${mSecurityPreferences.getStoreString(AnexosConstants.KEY.USER_NAME)}"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.loggout,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item?.itemId == R.id.loggout){
            handleLoggout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleLoggout(){
        mSecurityPreferences.removeStoreString(AnexosConstants.KEY.MANTER_LOGADO)
        mSecurityPreferences.removeStoreString(AnexosConstants.KEY.USER_EMAIL)
        mSecurityPreferences.removeStoreString(AnexosConstants.KEY.USER_NAME)
        mSecurityPreferences.removeStoreString(AnexosConstants.KEY.USER_ID)
        mSecurityPreferences.removeStoreString(AnexosConstants.KEY.USER_TELEFONE)
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun iniciarFragment(){
        val fragment: Fragment = AnexosListFragment.newInstance()
        supportFragmentManager.beginTransaction().replace(R.id.frameAnexos, fragment).commit()
    }

}
