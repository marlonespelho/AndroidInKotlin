package br.com.espelho.AnexosApp.util

import android.os.Build
import android.content.pm.PackageManager
import android.Manifest.permission
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import androidx.core.app.ActivityCompat
import br.com.espelho.AnexosApp.R

class PermissionUtil{

    companion object{
        const val CAMERA_PERMISSION = 1
    }

    private fun needToAskPermission(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
    }

    fun hasCameraPermission(context: Context): Boolean {
        return if (needToAskPermission()) ActivityCompat.checkSelfPermission(
            context, permission.CAMERA) == PackageManager.PERMISSION_GRANTED else true}

    fun asksCameraPermission(activity:Activity){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission.CAMERA)){
            AlertDialog.Builder(activity).setMessage(R.string.erro_permissao_camera).
                    setPositiveButton(R.string.confirmar){dialog, which ->
                        ActivityCompat.requestPermissions(activity, arrayOf(permission.CAMERA), CAMERA_PERMISSION)
                    }.show()
        }else{
            ActivityCompat.requestPermissions(activity, arrayOf(permission.CAMERA), CAMERA_PERMISSION)
        }
    }

}