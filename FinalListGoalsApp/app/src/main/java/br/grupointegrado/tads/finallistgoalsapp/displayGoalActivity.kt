package br.grupointegrado.tads.finallistgoalsapp

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import br.grupointegrado.tads.finallistgoalsapp.Models.modGoal
import br.grupointegrado.tads.finallistgoalsapp.Utils.DateUtils
import br.grupointegrado.tads.finallistgoalsapp.Utils.FileUtils
import br.grupointegrado.tads.finallistgoalsapp.Utils.JsonUtils
import kotlinx.android.synthetic.main.activity_display_goal.*
import java.text.SimpleDateFormat
import java.util.*

class displayGoalActivity : AppCompatActivity() {

    var listGoals : ArrayList<modGoal>? = null
    var goalSelected : modGoal? = null
    var editState = 0 // 0 disable - 1 enable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_goal)
        val pos = this.intent.getIntExtra(MainActivity.EXTRA_POSITION_GOAL,0)

        DateUtils(et_dt_limit,this).edtWithDateDialog()

        fillFields(pos)

        btn_delete.setOnClickListener {
            val alertBuilder = AlertDialog.Builder(this)
            alertBuilder.setTitle("Exclusão de registro")
            alertBuilder.setMessage("Deseja mesmo excluir esta meta?")
            alertBuilder.setPositiveButton("SIM", DialogInterface.OnClickListener{
                dialog, whichButton ->
                deleteGoal()
                finish()
            })
            alertBuilder.setNegativeButton("NÃO", DialogInterface.OnClickListener{
                dialog, whichButton ->
                Toast.makeText(this,"Cancelado",Toast.LENGTH_LONG).show()
            })
            val AlertDialog = alertBuilder
            AlertDialog.show()
        }
        btn_edit.setOnClickListener{

            enableFields(true)
        }
        btn_cancel.setOnClickListener {
            if (this.editState == 1){
                enableFields(false)
                fillFields(pos)
            }
        }
        btn_save.setOnClickListener {
            if (this.editState == 0){
                Toast.makeText(this,"Edição desabilitada",Toast.LENGTH_SHORT).show()
            } else if (verifyFields()){
                updateGoal(pos)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.complete,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.complete_goal){
            if (this.goalSelected!!.complete){
                Toast.makeText(this,"Meta já está concluida",Toast.LENGTH_SHORT).show()
            }
            else{
                val alertBuilder = AlertDialog.Builder(this)
                alertBuilder.setTitle("Concluir Meta")
                alertBuilder.setMessage("Deseja mesmo marcar esta meta como concluida?")
                alertBuilder.setPositiveButton("SIM", DialogInterface.OnClickListener{
                    dialog, whichButton ->
                    completeGoal()

                })
                alertBuilder.setNegativeButton("NÃO", DialogInterface.OnClickListener{
                    dialog, whichButton ->
                    Toast.makeText(this,"Cancelado",Toast.LENGTH_LONG).show()
                })
                val AlertDialog = alertBuilder
                AlertDialog.show()
            }
            }
            return super.onOptionsItemSelected(item)
        }

    fun fillFields(pos : Int){
        val format = SimpleDateFormat("dd/MM/yyyy")
        this.listGoals = JsonUtils().arrayJSONToArrayGoals(FileUtils().loadFile(this)!!)
        this.goalSelected = this.listGoals?.get(pos)
        et_title.setText(this.goalSelected?.titleGoal)
        et_description.setText(this.goalSelected?.descGoal)
        et_dt_limit.setText(format.format(this.goalSelected?.dtLimit))
        if (this.goalSelected?.dtConcluded != null){
            et_dt_completed.setText(format.format(this.goalSelected?.dtConcluded))
        }
        if (this.goalSelected?.complete!!){
            check_completed.isChecked = true
        }
    }
    fun deleteGoal(){
        this.listGoals?.remove(this.goalSelected)
        FileUtils().saveFile(this,JsonUtils().arrayGoalsToArrayJSON(this.listGoals!!))
    }
    fun completeGoal(){
        val pos = this.intent.getIntExtra(MainActivity.EXTRA_POSITION_GOAL,0)
        this.goalSelected?.dtConcluded = Date()
        this.goalSelected?.complete = true
        this.listGoals!![pos] = this.goalSelected!!
        FileUtils().saveFile(this,JsonUtils().arrayGoalsToArrayJSON(this.listGoals!!))
        fillFields(pos)
        Toast.makeText(this,"Meta Concluida",Toast.LENGTH_SHORT).show()
    }
    fun enableFields(boolean: Boolean){
        et_dt_limit.isEnabled = boolean
        et_title.isEnabled = boolean
        et_description.isEnabled = boolean
        if (boolean) this.editState = 1 else this.editState = 0
    }
    fun updateGoal(pos: Int){
        val dtLimit = SimpleDateFormat("dd/MM/yyyy").parse(et_dt_limit.text.toString())
        this.goalSelected?.titleGoal = et_title.text.toString()
        this.goalSelected?.descGoal = et_description.text.toString()
        this.goalSelected?.dtLimit = dtLimit
        this.listGoals!![pos] = this.goalSelected!!
        FileUtils().saveFile(this,JsonUtils().arrayGoalsToArrayJSON(this.listGoals!!))
        fillFields(pos)
        enableFields(false)
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
