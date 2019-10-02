package br.com.espelho.AnexosApp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.espelho.AnexosApp.Entities.AnexoEntity
import br.com.espelho.AnexosApp.Entities.OnAnexoListFragmentInteractionListener
import br.com.espelho.AnexosApp.R
import br.com.espelho.AnexosApp.viewholder.AnexoViewHolder

class AnexoListAdapter(val anexos: List<AnexoEntity>,val listener: OnAnexoListFragmentInteractionListener) : RecyclerView.Adapter<AnexoViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnexoViewHolder {
        val context = parent?.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.row_anexo_list, parent, false)
        return AnexoViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return anexos.size
    }

    override fun onBindViewHolder(holder: AnexoViewHolder, position: Int) {
        if (anexos != null){
            val anexo = anexos[position]
            holder.bindData(anexo)
        }
    }

}