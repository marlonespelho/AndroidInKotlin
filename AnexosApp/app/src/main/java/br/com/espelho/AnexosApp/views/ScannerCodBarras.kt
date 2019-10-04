package br.com.espelho.AnexosApp.views

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import br.com.espelho.AnexosApp.R
import br.com.espelho.AnexosApp.util.*
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import kotlinx.android.synthetic.main.activity_scanner_cod_barras.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import pub.devrel.easypermissions.EasyPermissions

class ScannerCodBarras : AppCompatActivity() , ZXingScannerView.ResultHandler{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner_cod_barras)
        if(!PermissionUtil().hasCameraPermission(this)){
            PermissionUtil().asksCameraPermission(this)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onResume() {
        super.onResume()
        z_xing_scanner.setResultHandler(this)
        restartCameraIfInactive()
    }
    private fun restartCameraIfInactive(){
        if( !z_xing_scanner.isCameraStarted()
            && PermissionUtil().hasCameraPermission(this)){
            startCamera()
        }
    }

    override fun onPause() {
        super.onPause()
        z_xing_scanner.stopCameraForAllDevices()
    }

    override fun onDestroy() {
        super.onDestroy()
        clearContent()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionUtil.CAMERA_PERMISSION -> {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                startCamera()
            } else if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED){
                Toast.makeText(this, getString(R.string.erro_permissao), Toast.LENGTH_LONG).show()
                finish()
            }
            return
        }
        }
    }
    private fun startCamera(){
        z_xing_scanner.startCameraForAllDevices(this)
    }

    override fun handleResult(result: Result?) {
        if( result == null ){
            unrecognizedCode(this, {clearContent()})
            return
        }
        processBarcodeResult(result.text, result.barcodeFormat.name)
    }

    private fun processBarcodeResult(text: String, barcodeFormatName: String ){
        val resultSaved = BarCodeSharedPreferences.getSavedResult(this)
        if( resultSaved == null || !resultSaved.text.equals(text, true) ){
            notification(this)
            val result = Result(text, text.toByteArray(), arrayOf(),
                BarcodeFormat.valueOf(barcodeFormatName))

            BarCodeSharedPreferences.saveResult(this, result)
            editTextBarCode.setText(result.text)
        }
        z_xing_scanner.resumeCameraPreview(this)
    }

    fun clearContent(view: View? = null){
        editTextBarCode.setText("Nenhum código lido")
        BarCodeSharedPreferences.saveResult(this)
    }


}
