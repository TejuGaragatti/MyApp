package com.example.mycamera

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity()
{
    private val CAPTURE_IMAGE_REQUEST = 1
    private var photoFile: File? = null
    private lateinit var mCurrentPhotoPath: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        var imageView = findViewById<ImageView>(R.id.imageView)

        button.setOnClickListener {
            captureImage()
        }
    }


        @SuppressLint("SimpleDateFormat")
        @Throws(IOException::class)
        private fun createImageFile(): File? {

            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val imageFileName = "JPEG_" + timeStamp + "_"
            val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val image = File.createTempFile(
                    imageFileName, ".jpg", storageDir)

            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = image.absolutePath
            return image
        }

    private fun displayMessage(baseContext: Context?, absolutePath: String) {
        val message:String=""
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }

        @SuppressLint("QueryPermissionsNeeded")
    private fun captureImage() {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
            } else {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (takePictureIntent.resolveActivity(packageManager) != null) {
                    // Create the File where the photo should go
                    try {
                        photoFile = createImageFile()
                        displayMessage(baseContext, photoFile!!.getAbsolutePath())
                        Log.i("Teju", photoFile!!.getAbsolutePath())

                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            val photoURI = FileProvider.getUriForFile(this,
                                    "com.example.mycamera.fileprovider",
                                    photoFile!!)

                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

                            startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST)
                        }
                    } catch (ex: Exception) {
                        // Error occurred while creating the File
                        displayMessage(baseContext, "Capture Image Bug: " + ex.message.toString())
                    }
                } else {
                    displayMessage(baseContext, "Null")
                }
            }
        }
     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) //Bundle extras = data.getExtras();
     {
         super.onActivityResult(requestCode, resultCode, data)
         if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
             val myBitmap = BitmapFactory.decodeFile(photoFile!!.absolutePath)
             val imageView = findViewById<ImageView>(R.id.imageView)
             imageView.setImageBitmap(myBitmap)
         } else {
             displayMessage(baseContext, "Request cancelled or something went wrong.")
         }
     }



     override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    if (requestCode == 0) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            captureImage()
        }
        else{

            displayMessage(baseContext, "This app is not going to work without camera permission")
        }
    }
}

}
