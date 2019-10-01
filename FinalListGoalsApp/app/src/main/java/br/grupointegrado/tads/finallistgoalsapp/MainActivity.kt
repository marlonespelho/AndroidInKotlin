package br.grupointegrado.tads.finallistgoalsapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import br.grupointegrado.tads.finallistgoalsapp.Models.modGoal
import br.grupointegrado.tads.finallistgoalsapp.Utils.FileUtils
import br.grupointegrado.tads.finallistgoalsapp.Utils.JsonUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var listGoals : ArrayList<modGoal>? = null

    companion object {
        val EXTRA_POSITION_GOAL = "br.grupointegrado.tads.finallistgoalsapp.mainactivity"
    }
       override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fillListView()
       }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.new_goal_menu){
            val intent = Intent(this, addNewGoalActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        fillListView()
        super.onResume()
    }

    fun fillListView(){
        val arrayJson = FileUtils().loadFile(this)
        if (arrayJson==null||arrayJson.length() == 0){
            tv_null_list.visibility = View.VISIBLE
            rcview_listGoals.visibility = View.INVISIBLE
            FileUtils().saveFile(this,null)
        }else{
            this.listGoals = JsonUtils().arrayJSONToArrayGoals(arrayJson!!)
            tv_null_list.visibility = View.INVISIBLE
            rcview_listGoals.visibility = View.VISIBLE
            val layoutManager = LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL,false)
            val adapter = AdapterListGoals( this.listGoals!!, this)
            rcview_listGoals.adapter = adapter
            rcview_listGoals.layoutManager = layoutManager
        }
    }
}
