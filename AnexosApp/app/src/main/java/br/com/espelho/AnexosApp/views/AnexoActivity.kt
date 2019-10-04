package br.com.espelho.AnexosApp.views


import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import br.com.espelho.AnexosApp.business.AnexoBusiness
import br.com.espelho.AnexosApp.constants.AnexosConstants
import kotlinx.android.synthetic.main.activity_anexo.*
import android.provider.MediaStore
import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.Toast
import br.com.espelho.AnexosApp.Entities.AnexoEntity
import br.com.espelho.AnexosApp.R
import br.com.espelho.AnexosApp.util.BitMapUtil
import androidx.appcompat.app.AlertDialog
import br.com.espelho.AnexosApp.util.PermissionUtil

class AnexoActivity : AppCompatActivity(), View.OnClickListener{

    private var mAnexoId : Int? = null
    private lateinit var mAnexoBusiness : AnexoBusiness
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
                if (PermissionUtil().hasCameraPermission(this)) {
                    capturarImagem()
                }else{
                    PermissionUtil().asksCameraPermission(this)
                }
             }
            R.id.imageViewAnexo ->{
                if (mAnexo.anexo != null){
                    val bundle = Bundle()
                    bundle.putInt(AnexosConstants.KEY.ANEXO_ID, mAnexoId!!)
                    val intent = (Intent(this, FullscreenAnexoActivity::class.java))
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, getString(R.string.anexo_vazio),Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onRequestPermissionsResult( requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionUtil.CAMERA_PERMISSION -> {
                    if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                        capturarImagem()
                    } else if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED){
                        Toast.makeText(this, getString(R.string.erro_permissao),Toast.LENGTH_LONG).show()
                    }
                    return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                if (data != null){
                    val bundle : Bundle = data.extras!!
                    val bitmapData = bundle.get("data") as Bitmap
                    mAnexo.anexo =  BitMapUtil().converterBitMapParaByteArray(bitmapData)
                    mAnexoBusiness.update(mAnexo)
                    mAnexo = mAnexoBusiness.get(mAnexoId!!)!!
                    carregarImagem()
                }
            }
            else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this, "captura cancelada", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this, "erro na captura", Toast.LENGTH_LONG).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun carregarImagem(){
        val imageViewAnexo : ImageView = findViewById(R.id.imageViewAnexo)
        imageViewAnexo.setImageBitmap(BitMapUtil().converterByteArrayParaBitMap(mAnexo.anexo!!))
    }

    private fun setListeners(){
        imageButtonAnexar.setOnClickListener(this)
        imageViewAnexo.setOnClickListener(this)
    }

    private fun capturarImagem(){
        if (mAnexo.anexo != null){
            AlertDialog.Builder(this).setTitle(R.string.anexar)
                .setMessage(R.string.alerta_anexo)
                .setPositiveButton(R.string.confirmar)
                {dialog, which ->
                    startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), 1)
                }.setNegativeButton(R.string.cancelar){dialog, which -> }.create().show()
        }
        else{
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 1)
        }
    }

}
