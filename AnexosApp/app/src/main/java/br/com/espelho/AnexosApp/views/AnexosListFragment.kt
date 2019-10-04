package br.com.espelho.AnexosApp.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.espelho.AnexosApp.Entities.OnAnexoListFragmentInteractionListener
import br.com.espelho.AnexosApp.R
import br.com.espelho.AnexosApp.adapter.AnexoListAdapter
import br.com.espelho.AnexosApp.business.AnexoBusiness
import br.com.espelho.AnexosApp.constants.AnexosConstants


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AnexosListFragment : Fragment() {

    private lateinit var mContext : Context
    private lateinit var  mReclycerAnexo : RecyclerView
    private lateinit var  mAnexoBusiness: AnexoBusiness
    private lateinit var  mListInteraction : OnAnexoListFragmentInteractionListener

    companion object {
        @JvmStatic
        fun newInstance(): AnexosListFragment{
            return AnexosListFragment()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_anexos_list, container, false)
        mContext = rootView.context
        mAnexoBusiness = AnexoBusiness(context!!)
        mListInteraction = object: OnAnexoListFragmentInteractionListener{
            override fun onListClick(anexoId: Int) {
                val bundle = Bundle()
                bundle.putInt(AnexosConstants.KEY.ANEXO_ID, anexoId)
                val intent = (Intent(mContext, AnexoActivity::class.java))
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
        mReclycerAnexo = rootView.findViewById(R.id.recyclerViewAnexos)
        mReclycerAnexo.adapter = AnexoListAdapter(mutableListOf(), mListInteraction)
        mReclycerAnexo.layoutManager = LinearLayoutManager(mContext)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        loadAnexos()
    }

    private fun loadAnexos(){
        mReclycerAnexo.adapter = AnexoListAdapter(mAnexoBusiness.getList(), mListInteraction)
    }
}
