package br.grupointegrado.tads.clima

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView

class PrevisaoAdapter : RecyclerView.Adapter<PrevisaoAdapter.PrevisaoAdapterViewHolder>{

    private var dadosClima : Array<String>?
    private var itemClickListener : PrevisaoItemClickListener

    constructor(dadosClima: Array<String>?, itemClickListener: PrevisaoItemClickListener) : super() {
        this.dadosClima = dadosClima
        this.itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrevisaoAdapterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.previsao_lista_item,parent,false)
        return PrevisaoAdapterViewHolder(itemView)
    }
    override fun getItemCount(): Int {
        val dados = this.dadosClima
        if (dados != null){
            return dados.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: PrevisaoAdapterViewHolder, position: Int) {
        val linhaPrevisao = this.dadosClima?.get(position)
        holder.tvDadosPrevisao.text = linhaPrevisao
    }

    fun setDadosClima(dadosClima: Array<String>?){
        this.dadosClima = dadosClima
        notifyDataSetChanged()
    }

    fun getDadosClima(): Array<String>?{
        return this.dadosClima
    }

    interface PrevisaoItemClickListener{
        fun onItemClick(index : Int)
    }

    inner class PrevisaoAdapterViewHolder : RecyclerView.ViewHolder{
        val tvDadosPrevisao : TextView

        constructor(itemView: View?) : super(itemView){
            tvDadosPrevisao = itemView!!.findViewById(R.id.tv_dados_previsao)

            itemView.setOnClickListener({
                itemClickListener.onItemClick(adapterPosition)
            })
        }
    }
}