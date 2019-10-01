package br.grupointegrado.tads.finallistgoalsapp.Utils

import android.app.DatePickerDialog
import android.content.Context
import android.text.Selection
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.*

class DateUtils:View.OnKeyListener {
    val myCalendar = Calendar.getInstance()
    val et_date : EditText
    val context : Context

    constructor(et_date: EditText, context: Context) {
        this.et_date = et_date
        this.context = context
    }

    val date = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, month)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        updateLabel()
    }

    fun edtWithDateDialog(){
        this.et_date.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                DatePickerDialog(this.context,date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show()
                v.clearFocus()
            }
        }
    }

    private fun updateLabel() {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
        et_date.setText(sdf.format(myCalendar.getTime()))
    }
    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        val ed = v as EditText
        if (event?.action == KeyEvent.ACTION_UP
                && keyCode != KeyEvent.KEYCODE_DEL) {
            val length = ed.text.toString().length
            when (length) {
                2 -> {
                    ed.setTextKeepState(ed.text.toString() + "/")
                }
                5 -> {
                    ed.setTextKeepState(ed.text.toString() + "/")
                }
                else -> {
                }
            }
        }
        Selection.setSelection(ed.getText(), ed.getText().toString()
                .length)
        return false
    }
    }

