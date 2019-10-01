package br.grupointegrado.tads.finallistgoalsapp.Utils

import br.grupointegrado.tads.finallistgoalsapp.Models.modGoal
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat

class JsonUtils {

    fun goalToJSON(goal: modGoal): JSONObject?{
        try {
            var jsonGoal = JSONObject()
            val formatDate = SimpleDateFormat("dd/MM/yyyy")

            jsonGoal.put("title",goal.titleGoal)
            jsonGoal.put("description",goal.descGoal)
            jsonGoal.put("dtlimit",formatDate.parse(goal.dtLimit.toString()))
            jsonGoal.put("dtcompleted",if (goal.dtConcluded==null)""
            else formatDate.parse(goal.dtConcluded.toString()))
            jsonGoal.put("completed",goal.complete)

            return jsonGoal
        }catch (ex: Exception){
            ex.printStackTrace()
            return null
        }
    }

    fun JSONToGoal(json: JSONObject):modGoal?{
        try {
            var formtDate = SimpleDateFormat("dd/MM/yyyy")
            var dateCompleted = if (json.getString("dtcompleted").equals("")) null
            else formtDate.parse(json.getString("dtcompleted"))
            var  goal = modGoal(json.getString("title"),
                    json.getString("description"),
                    formtDate.parse(json.getString("dtlimit")),
                    dateCompleted,
                    json.getBoolean("completed"))

            return goal
        }catch (ex:Exception){
            ex.printStackTrace()
            return null
        }
    }

    fun arrayJSONToArrayGoals(jsonArray: JSONArray):ArrayList<modGoal>?{
        try {
            var listGoals = ArrayList<modGoal>()
            val formtDate = SimpleDateFormat("dd/MM/yyyy")
            for (i in 0..(jsonArray.length()-1)){
                var json = jsonArray.getJSONObject(i)
                var dateCompleted = if (json.getString("dtcompleted").equals("")) null
                else formtDate.parse(json.getString("dtcompleted"))
                var goal = modGoal(json.getString("title"),
                        json.getString("description"),
                        formtDate.parse(json.getString("dtlimit")),
                        dateCompleted,
                        json.getBoolean("completed"))
                listGoals.add(goal)
            }
            return  listGoals
        }catch (ex: Exception){
            ex.printStackTrace()
            return null
        }
    }

    fun arrayGoalsToArrayJSON(goalsArray: ArrayList<modGoal>): JSONArray?{
        try {
            var jsonArray = JSONArray()
            val formatDate = SimpleDateFormat("dd/MM/yyyy")

            for (i in 0..(goalsArray.size-1)){
                val goal = goalsArray[i]
                var jsonGoal = JSONObject()

                jsonGoal.put("title",goal.titleGoal)
                jsonGoal.put("description",goal.descGoal)
                jsonGoal.put("dtlimit",formatDate.format(goal.dtLimit))
                jsonGoal.put("dtcompleted",if (goal.dtConcluded==null)""
                else formatDate.format(goal.dtConcluded))
                jsonGoal.put("completed",goal.complete)

                jsonArray.put(jsonGoal)
            }
            return jsonArray
        }catch (ex:Exception){
            ex.printStackTrace()
            return null
        }
    }

}