package br.grupointegrado.tads.finallistgoalsapp.Models

import java.text.SimpleDateFormat
import java.util.*

class modGoal {
    var titleGoal : String
    var descGoal : String
    var dtLimit : Date
    var dtConcluded : Date?
    var complete : Boolean

    constructor(title:String,description:String,dtLimit: Date) {
        this.titleGoal = title
        this.descGoal = description
        this.dtLimit = dtLimit
        this.dtConcluded = null
        complete = false
    }

    constructor(titleGoal: String, descGoal: String, dtLimit: Date, dtConcluded: Date?, complete: Boolean) {
        this.titleGoal = titleGoal
        this.descGoal = descGoal
        this.dtLimit = dtLimit
        this.dtConcluded = dtConcluded
        this.complete = complete
    }

    override fun toString(): String {
        var strBuilder = StringBuilder()
        val daysToCompleted = ((dtLimit.time- Date().time)/(1000*60*60*24))
        //val hoursToCompleted = (dtLimit.time-Date().time)%(1000*60*60*24)
        strBuilder.setLength(0)
        strBuilder.append("Titulo: $titleGoal \n")
        if (daysToCompleted<0){
            strBuilder.append("Limite excedido a: ").append(daysToCompleted*-1).append(" dias ")
        }else if (complete){
            strBuilder.append("Meta concluída em: ").append(SimpleDateFormat("dd/MM/yyyy").format(dtConcluded))
        }else{
            strBuilder.append("$daysToCompleted dias para concluir a meta ")
        }
        return strBuilder.toString()
    }

}