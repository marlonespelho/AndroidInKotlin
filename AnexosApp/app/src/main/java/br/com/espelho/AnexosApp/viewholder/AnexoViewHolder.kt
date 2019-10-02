package br.com.espelho.AnexosApp.viewholder

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import br.com.espelho.AnexosApp.Entities.AnexoEntity
import br.com.espelho.AnexosApp.Entities.OnAnexoListFragmentInteractionListener
import br.com.espelho.AnexosApp.R

class AnexoViewHolder (itemView: View, val listener: OnAnexoListFragmentInteractionListener): RecyclerView.ViewHolder(itemView){


    fun bindData(anexo : AnexoEntity){
        val mTextDescription: TextView = itemView.findViewById(R.id.textViewTipoAnexo)
        val mCheckBoxAnexoExistente : CheckBox = itemView.findViewById(R.id.checkBoxAnexoExistente)
        val mRowAnexo : (ConstraintLayout) = itemView.findViewById(R.id.rowAnexo)

        mTextDescription.text = anexo.tipoAnexo.name
        mCheckBoxAnexoExistente.isChecked = anexo.anexo != null
        mRowAnexo.setOnClickListener({
            listener.onListClick(anexo.id)
        })
    }
}