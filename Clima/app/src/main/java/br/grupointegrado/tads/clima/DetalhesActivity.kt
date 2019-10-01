package br.grupointegrado.tads.clima

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ShareCompat
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_detalhes.*

class DetalhesActivity : AppCompatActivity() {
    var detalhes : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes)
        if (intent.hasExtra("detalhesPrevisao")){
            this.detalhes = intent.getStringExtra("detalhesPrevisao")
            tv_exibir_detalhes.text = this.detalhes
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detalhes,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.acao_compartilhar){
            compartilhar()
            return true
        }
        if (item?.itemId == R.id.acao_configurar){
            val intent = Intent(this, ConfiguracaoActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    fun compartilhar(){
        val titulo = "Detalhes clima"
        val intent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain").setChooserTitle(titulo)
                .setText(this.detalhes).intent
        if (intent.resolveActivity(packageManager)!=null) {
            startActivity(intent)
        }
    }

}
