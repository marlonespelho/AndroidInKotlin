package br.grupointegrado.tads.clima

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.LoaderManager
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.content.Loader
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import br.grupointegrado.tads.buscadorgithub.NetworkUtils
import br.grupointegrado.tads.clima.dados.ClimaPreferencias
import br.grupointegrado.tads.clima.util.JsonUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), PrevisaoAdapter.PrevisaoItemClickListener, LoaderManager.LoaderCallbacks<String>,
    OnSharedPreferenceChangeListener{

    lateinit var previsaoAdapter : PrevisaoAdapter
    var cacheResultado : String? = null

    companion object {
        val ID_LOADER_CLIMA = 1000
        val OWN_LOCALIZACAO = "LOCAL_BUSCA"
        var PREFERENCIAS_ALTERADAS = false
    }

    override fun onResume() {
        super.onResume()
        carregarDadosClima()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this)
        previsaoAdapter = PrevisaoAdapter(null,this)
        val layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        rv_clima.adapter = previsaoAdapter
        rv_clima.layoutManager = layoutManager

        supportLoaderManager.initLoader(ID_LOADER_CLIMA,null,this)
        carregarDadosClima()

    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onStart() {
        super.onStart()
        if (PREFERENCIAS_ALTERADAS){
            supportLoaderManager.restartLoader(ID_LOADER_CLIMA, null, this)
            PREFERENCIAS_ALTERADAS = false
        }
    }

    override fun onItemClick(index: Int) {
        val intent = Intent(this,DetalhesActivity::class.java)
        val previsao = previsaoAdapter?.getDadosClima()?.get(index)
        intent.putExtra("detalhesPrevisao", previsao)
        startActivity(intent)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.clima,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.acao_atualizar){
            carregarDadosClima()
        }
        if (item?.itemId == R.id.acao_exibir_mapa){
            abrirMapa()
            return true
        }
        if (item?.itemId == R.id.acao_configurar){
            abrirConfiguracao()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<String> {
        val loader = object : AsyncTaskLoader<String>(this){
            override fun onStartLoading() {
                super.onStartLoading()
                if (args == null){
                    return
                }
                exibirProgressbar()
                if (cacheResultado!=null){
                    deliverResult(cacheResultado)
                }else{
                    forceLoad()
                }
            }
            override fun loadInBackground(): String? {
                try {
                    val localizacao = args?.getString(OWN_LOCALIZACAO)
                    val url = NetworkUtils.construirUrl(localizacao!!)
                    if (url!=null){
                        return NetworkUtils.obterRespostaDaUrlHttp(url)
                    }
                    return null
                }catch (ex:Exception){
                    ex.printStackTrace()
                    return null
                }
            }

            override fun deliverResult(data: String?) {
                super.deliverResult(data)
                cacheResultado = data
            }

        }
        return loader
    }

    override fun onLoadFinished(loader: Loader<String>, data: String?) {
        if (data!=null){
            exibirResultado()
            val clima = JsonUtils.getSimplesStringsDeClimaDoJson(this@MainActivity,data)
            for (i in 0 until clima!!.size){
                previsaoAdapter.setDadosClima(clima)
            }
        }else{
            exibirMensagemErro()
        }
    }

    override fun onLoaderReset(loader: Loader<String>) {

    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        PREFERENCIAS_ALTERADAS = true
    }

    fun carregarDadosClima(){
        val params = Bundle()
        params.putString(OWN_LOCALIZACAO,ClimaPreferencias.getLocalizacaoSalva(this))
        supportLoaderManager.restartLoader(ID_LOADER_CLIMA,params,this)
    }

    fun abrirMapa(){
        val endereco = "Campo Mourão, Paraná, Brasil"
        val builder = Uri.Builder().scheme("geo").path("0,0").appendQueryParameter("q",endereco)
        val uriEndereco = builder.build()
        val intent = Intent(Intent.ACTION_VIEW,uriEndereco)
        if (intent.resolveActivity(packageManager)!=null){
            startActivity(intent)
        }
    }

    fun abrirConfiguracao(){
        val intent = Intent(this, ConfiguracaoActivity::class.java)
        startActivity(intent)
    }

    fun exibirMensagemErro(){
        rv_clima.visibility = View.INVISIBLE
        tv_mensagem_erro.visibility = View.VISIBLE
        pg_aguarde.visibility = View.INVISIBLE
    }
    fun exibirResultado(){
        rv_clima.visibility = View.VISIBLE
        tv_mensagem_erro.visibility = View.INVISIBLE
        pg_aguarde.visibility = View.INVISIBLE
    }
    fun exibirProgressbar(){
        rv_clima.visibility = View.INVISIBLE
        tv_mensagem_erro.visibility = View.INVISIBLE
        pg_aguarde.visibility = View.VISIBLE
    }




}
