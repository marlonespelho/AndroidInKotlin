package br.grupointegrado.tads.finallistgoalsapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.grupointegrado.tads.finallistgoalsapp.Models.modGoal
import br.grupointegrado.tads.finallistgoalsapp.Utils.DateUtils
import br.grupointegrado.tads.finallistgoalsapp.Utils.FileUtils
import br.grupointegrado.tads.finallistgoalsapp.Utils.JsonUtils
import kotlinx.android.synthetic.main.activity_add_new_goal.*
import java.text.SimpleDateFormat
import java.util.*


class addNewGoalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_goal)
        DateUtils(et_dt_limit,this).edtWithDateDialog()

        btn_save.setOnClickListener {
            if (saveNewGoal()){
                finish()
            }
        }
    }

    fun saveNewGoal():Boolean{
        if (verifyFields()){
            val format = SimpleDateFormat("dd/MM/yyyy")
            val dtLimit = format.parse(et_dt_limit.text.toString())
            val newGoal = modGoal(et_title.text.toString(),
                    et_description.text.toString(),
                    dtLimit)
            var arrayJson = FileUtils().loadFile(this)
            var arrayGoals = ArrayList<modGoal>()
            if (arrayJson!=null){
                arrayGoals = JsonUtils().arrayJSONToArrayGoals(arrayJson)!!
            }
            arrayGoals.add(newGoal)
            arrayJson = JsonUtils().arrayGoalsToArrayJSON(arrayGoals)
            FileUtils().saveFile(this,arrayJson)
            return true
        }
        return false
    }
    fun verifyFields():Boolean{
        try {
            if (et_title.text.toString().equals("")){
                Toast.makeText(this,"Título não informado", Toast.LENGTH_LONG).show()
                return false
            }
            if (et_description.text.toString().equals("")){
                Toast.makeText(this,"Descrição não informada", Toast.LENGTH_LONG).show()
                return false
            }
            if (et_dt_limit.text.toString().length != 10){
                Toast.makeText(this,"Data informada invalida", Toast.LENGTH_LONG).show()
                return false
            }
            return true

        }catch (ex:Exception){
            ex.printStackTrace()
            Toast.makeText(this,"Data informada invalida", Toast.LENGTH_LONG).show()
            return false
        }
    }

}
