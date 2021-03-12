package com.example.mycamera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var button = findViewById<Button>(R.id.button)
        var imageView = findViewById<ImageView>(R.id.imageView)

        button.isEnabled = false

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),111)

        }
        else
            button.isEnabled = true
         button.setOnClickListener{
                var i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(i,101)
         }}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==101)
        {
            var pic = data?.getParcelableExtra<Bitmap>("data")
            var imageView = findViewById<ImageView>(R.id.imageView)
            imageView.setImageBitmap(pic)
        }
    }
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray )
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            var button = findViewById<Button>(R.id.button)
            button.isEnabled = true
        }
    }
}