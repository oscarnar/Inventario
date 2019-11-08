package com.example.inventario

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.createBitmap
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import kotlinx.android.synthetic.main.activity_add_inventario.*
import okhttp3.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

class addInventario : AppCompatActivity() {

    internal var client = OkHttpClient()
    lateinit var context: Context
    private var enviarURL = "http://192.168.0.27/servicio/Upload.php"
    private lateinit var bitmap:Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_inventario)

        context = this
        btnPhoto.setOnClickListener{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_SELECT_IMAGE_IN_ALBUM)

                } else {
                    chooseImage()
                }
            }
            else
                chooseImage()
        }
        btnAdd.setOnClickListener {
            var deno = this.denominacion.text.toString()
            var id_ = this.id.text.toString()
            var cod = this.codigo.text.toString()
            if(deno ==""){
                Toast.makeText(this, "Falta llenar el campo denominacion", Toast.LENGTH_SHORT).show()
            }
            if(id_ ==""){
                Toast.makeText(this, "Falta llenar el campo ID", Toast.LENGTH_SHORT).show()
            }
            if(cod ==""){
                Toast.makeText(this, "Falta llenar el campo codigo", Toast.LENGTH_SHORT).show()
            }
            if(deno != "" && id_ != "" && cod != ""){
                enviar()
            }
        }
    }

    fun enviar(){
        var deno = this.denominacion.text.toString()
        var codi = this.codigo.text.toString()
        var marc = this.marca.text.toString()
        var mode = this.modelo.text.toString()
        var colo = this.color.text.toString()
        var seri = this.serie.text.toString()
        var id_ = this.id.text.toString()
        var des = this.descripcion.text.toString()

        archivo(id_)

        Fuel.post(
            enviarURL, listOf(
                "id" to id_,
                "denominacion" to deno
                , "modelo" to mode, "codigo" to codi, "marca" to marc,
                "color" to colo, "serie" to seri, "descripcion" to des
            )
        ).responseJson { request, response, result ->
            Log.d("plzzzzz", result.get().content)
        }
    }

    fun archivo(id_ :String) {
        //val drawable = ContextCompat.getDrawable(applicationContext,R.drawable.)
        //val bitmap = (drawable as BitmapDrawable).bitmap
        println("subiendo")
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("uploaded_file", id_+".jpg",
                RequestBody.create(MediaType.parse("image/*"), byteArray))
            .build()

        val request = Request.Builder()
            .url(enviarURL)
            //.header("Accept", "application/json")
            //.header("Content-Type", "multipart/form-data")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("OKHTTP:", e.message)
                println("failur")
            }
            override fun onResponse(call: Call, response: Response) {
                Log.e("OKHTTP:", response.body()?.string())
                println("respon")
            }
        })
    }

    //Funciones para elegir fotos y tomarlas
    fun selectImageInAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
        }
    }
    fun takePhoto() {
        val intent1 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent1.resolveActivity(packageManager) != null) {
            startActivityForResult(intent1, REQUEST_TAKE_PHOTO)
        }

        /*val imageFile = createImageFile()
        val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(callCameraIntent.resolveActivity(packageManager) != null) {
            val authorities = packageName + ".fileprovider"
            val imageUri = FileProvider.getUriForFile(this@addInventario, authorities, imageFile)
            callCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(callCameraIntent, REQUEST_TAKE_PHOTO)
        }*/
    }
    /*lateinit var imageFilePath: String
    @Throws(IOException::class)
    fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName: String = "JPEG_" + timeStamp + "_"
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if(!storageDir.exists()) storageDir.mkdirs()
        val imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
        imageFilePath = imageFile.absolutePath
        return imageFile
    }*/

    private lateinit var fileName: String

    private lateinit var captureFile: File

    private lateinit var outputFileUri: Uri
    fun chooseImage(){
        // Determine Uri of camera image to save.
        println("context init")
        val root = File(Environment.getExternalStorageDirectory().toString() + File.separator + context.resources.getString(R.string.app_name) + File.separator)
        root.mkdirs()
        fileName = "IMG_" + System.currentTimeMillis() + ".png"
        captureFile = File(root, fileName)

        outputFileUri = Uri.fromFile(captureFile)

        // Camera.
        val cameraIntents = ArrayList<Intent>()
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val packageManager = context.packageManager
        val listCam = packageManager.queryIntentActivities(captureIntent, 0)
        for (res in listCam) {
            val packageName = res.activityInfo.packageName
            val intent = Intent(captureIntent)
            intent.putExtra("return-data", true)
            intent.component = ComponentName(packageName, res.activityInfo.name)
            intent.setPackage(packageName)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
            cameraIntents.add(intent)
            break
        }

        // Filesystem.
        val galleryIntent = Intent()
        galleryIntent.type = "image/*"
        galleryIntent.action = Intent.ACTION_PICK

        // Chooser of filesystem options.
        val chooserIntent = Intent.createChooser(galleryIntent, "Elija origen")

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toTypedArray<Parcelable>())

        startActivityForResult(chooserIntent, REQUEST_SELECT_IMAGE_IN_ALBUM)
    }
    //recibe la imagen y la muestra en la actividad
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM){

            var uri : Uri? = null
            if (data != null && data.data != null) {
                uri = data.data
            } else {
                var url = MediaStore.Images.Media.insertImage(contentResolver, captureFile.absolutePath, captureFile.name, captureFile.name)
                uri = Uri.parse(url)
            }
            //imageView.setImageURI(uri)
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            //val baos = ByteArrayOutputStream()
            //bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /* Ignored for PNGs */, baos)
            //archivo(bitmap)
        }
    }
    companion object {
        private val REQUEST_TAKE_PHOTO = 0
        private val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
        //val MEDIA_TYPE_MARKDOWN = "text/x-markdown; charset=utf-8".toMediaType()
        private val IMGUR_CLIENT_ID = "9199fdef135c122"
        //private val MEDIA_TYPE_PNG = "image/png".toMediaType()
    }
}
