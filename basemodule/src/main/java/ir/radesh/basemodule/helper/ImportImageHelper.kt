package ir.radesh.basemodule.helper

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import ir.radesh.basemodule.commons.showToast
import timber.log.Timber

class ImportImageHelper(val fragment: Fragment,val listener: ImportImageListener) {
    private val STORAGE_PERMISSION = 60
    private val CAMERA_PERMISSION = 70
    private var tempImageUri: Uri? = null
    private var selectedImageUri: Uri? = null

    private fun checkReadExternalPermission(): Boolean {
        return fragment.requireContext().checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkWriteExternalPermission(): Boolean {
        return fragment.requireContext().checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkCameraPermission(): Boolean {
        return fragment.requireContext().checkCallingOrSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }


    fun addImageGalley(code: Int){
        if (checkReadExternalPermission()){
            showFileChooser(code)
        }else {
            fragment.requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION)
        }
    }

    private fun addImageFromCamera(code: Int){
        if (checkCameraPermission() && checkWriteExternalPermission()){
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "upload_image.jpg")
            values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera")
            tempImageUri = fragment.context?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, tempImageUri)
            fragment.startActivityForResult(intent, code)
        }else {
            fragment.requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), CAMERA_PERMISSION)
        }
    }

    private fun showFileChooser(code: Int) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        fragment.startActivityForResult(Intent.createChooser(intent, "انتخاب عکس"), code)
    }



    fun getOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var imageUri: Uri? = null
        if (data != null && data.data != null) {
            imageUri = data.data!!
        }else if (tempImageUri != null){
            imageUri = tempImageUri
        }
        if (imageUri == null){
            Timber.e("no data to add")
            return
        }
        selectedImageUri = imageUri
        listener.onImageReady(imageUri, requestCode)
        tempImageUri = null
    }

    fun getOnRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Timber.e("PERMISSION_GRANTED")
                } else {
                    Timber.e("PERMISSION_NOT GRANTED")
                    fragment.showToast("برای اضافه کردن تصویر دسترسی به حافظه داخلی لازم است")
                }
            }

            CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Timber.e("PERMISSION_GRANTED")
                } else {
                    Timber.e("PERMISSION_NOT GRANTED")
                    fragment.showToast("برای اضافه کردن تصویر از طریق دوربین دسترسی به دوربین و حافظه دستگاه لازم است")
                }
            }
        }
    }

    fun getImageUri(): Uri? {
        return selectedImageUri
    }

    fun isImageSelected(): Boolean {
        return selectedImageUri != null
    }

    interface ImportImageListener{
        fun onImageReady(image: Uri, code: Int)
    }

}