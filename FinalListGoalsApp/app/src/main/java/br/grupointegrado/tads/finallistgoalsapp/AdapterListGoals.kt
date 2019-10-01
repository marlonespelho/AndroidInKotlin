package br.grupointegrado.tads.finallistgoalsapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import br.grupointegrado.tads.finallistgoalsapp.AdapterListGoals.ViewHolder
import br.grupointegrado.tads.finallistgoalsapp.Models.modGoal
import java.text.SimpleDateFormat
import java.util.*

class AdapterListGoals : RecyclerView.Adapter<ViewHolder> {

    val listGoals : ArrayList<modGoal>
    val context : Context

    companion object {
        val EXTRA_POSITION_GOAL = "br.grupointegrado.tads.finallistgoalsapp.mainactivity"
    }

    constructor(listGoals: ArrayList<modGoal>, context: Context) : super() {
        this.listGoals = listGoals
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder { // Onde se deve inflar o layout
        val item = LayoutInflater.from(this.context).inflate(R.layout.item_recycler_view_layout,parent,false)
        val viewHolder = ViewHolder(item)
        return  viewHolder
    }

    override fun getItemCount(): Int { // Tamanho da lista
        return this.listGoals.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { // Onde trabalha com as views trazidas pela ViewHolder
        val goal = this.listGoals[position]

        var daysToGo = (goal.dtLimit.time - Date().time) / (1000*60*60*24)

        var strStatus = StringBuilder()
        strStatus.setLength(0) // #FFD700 amarelo -- #FF0000 Vermelho
        holder.tv_status_goal!!.setTextColor(Color.parseColor("#000000"))
        if (goal.complete){
            holder.tv_status_goal!!.setTextColor(Color.parseColor("#228B22"))
            strStatus.append("Meta Concluída!")
        }
        else if(daysToGo.toInt() == 0){
           holder.tv_status_goal!!.setTextColor(Color.parseColor("#FFD700"))
           strStatus.append("Ultimo dia para concluir a meta!")
        }
        else if(daysToGo < 0){
           holder.tv_status_goal!!.setTextColor(Color.parseColor("#FF0000"))
           var year = 0
           daysToGo = daysToGo * (-1)
           while((daysToGo-365) > 0)   {
              year++
              daysToGo = daysToGo - 365
           }
           strStatus.append("Data limite excedida a ")
           if (year > 0){
               strStatus.append(year).append(" ano(s) e ")
           }
           strStatus.append(daysToGo).append(" dia(s).")
        }
        else{
            var year = 0
            while((daysToGo-365) > 0)   {
               year++
               daysToGo = daysToGo - 365
            }
            strStatus.append("Resta(m) ")
            if (year > 0){
                strStatus.append(year).append(" ano(s) e ")
            }
            strStatus.append(daysToGo).append(" dia(s).")
        }

        holder.tv_title!!.text = goal.titleGoal
        holder.tv_status_goal!!.text = strStatus.toString()
        holder.btn_display_goal!!.setOnClickListener {
            val intent = Intent(context, displayGoalActivity::class.java)
            intent.putExtra(EXTRA_POSITION_GOAL,position)
            context.startActivity(intent)
        }

    }

    inner class ViewHolder : RecyclerView.ViewHolder{
        val tv_title : TextView?
        val tv_status_goal : TextView?
        val btn_display_goal : ImageButton?
        constructor(itemView : View?):super(itemView){
            this.tv_title = itemView?.findViewById(R.id.tv_title_goal)
            this.tv_status_goal = itemView?.findViewById(R.id.tv_status_goal)
            this.btn_display_goal = itemView?.findViewById(R.id.btn_display_goal)
        }
    }
}