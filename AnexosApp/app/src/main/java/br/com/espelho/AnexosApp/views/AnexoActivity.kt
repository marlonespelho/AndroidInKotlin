package br.com.espelho.AnexosApp.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import br.com.espelho.AnexosApp.R
import br.com.espelho.AnexosApp.business.AnexoBusiness
import br.com.espelho.AnexosApp.constants.AnexosConstants
import kotlinx.android.synthetic.main.activity_anexo.*
import android.provider.MediaStore
import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.Toast
import br.com.espelho.AnexosApp.Entities.AnexoEntity
import br.com.espelho.AnexosApp.util.BitMapUtil
import br.com.espelho.AnexosApp.util.FileUtil





class AnexoActivity : AppCompatActivity(), View.OnClickListener{

    private var mAnexoId : Int? = null
    private lateinit var  mAnexoBusiness : AnexoBusiness
    private lateinit var mAnexo: AnexoEntity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anexo)
        mAnexoBusiness = AnexoBusiness(this)
        loadDataFromActivity()
        setListeners()
    }

    private fun loadDataFromActivity(){
        val bundle = intent.extras
        if (bundle != null){
            mAnexoId = bundle.getInt(AnexosConstants.KEY.ANEXO_ID)
            mAnexo = mAnexoBusiness.get(mAnexoId!!)!!
            textViewTpAnexo.text = mAnexo.tipoAnexo.name
            if (mAnexo.anexo == null){
                imageViewAnexo.setImageResource(R.drawable.ic_clear_black_24dp)
            } else{
                carregarImagem()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.imageButtonAnexar ->{
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, 0)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == 0){
            if (data != null){
                val bundle : Bundle = data.extras!!
                val bitmap = bundle.get("data") as Bitmap
                mAnexo.anexo = BitMapUtil().converterBitMapParaByteArray(bitmap)
                mAnexo = mAnexoBusiness.update(mAnexo)
                carregarImagem()
            }
        }  else{
            Toast.makeText(this, "erro na captura", Toast.LENGTH_LONG).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun carregarImagem(){
        val imageViewAnexo : ImageView = findViewById(R.id.imageViewAnexo)
        imageViewAnexo.setImageBitmap(BitMapUtil().converterByteArrayParaBitMap(mAnexo.anexo!!))
    }

    private fun setListeners(){
        imageButtonAnexar.setOnClickListener(this)
    }
}
