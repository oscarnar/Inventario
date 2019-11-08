package com.example.inventario

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.URL
import java.net.HttpURLConnection
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    //sudo systemctl start httpd ---- encender el servidor
    //sudo su adb kill-server adb start-server ---- arreglar problemas con la Adnroid Studio y el celular
    //mysql -u admin -p  ---- Entrar como usuario a las base de datos de la compu
    //comandos: show databases; --- Vemos las base de datos que tengo
    //          use name_db; --- name_db se remplaza por la base de datos que queremos entrar

    /*$fullflepath = 'C:\temp\test.jpg';
    $upload_url = 'http://www.example.com/uploadtarget.php';
    $params = array( 'photo'=>"@$fullfilepath", 'title'=>$title );
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_VERBOSE, 1);
    curl_setopt($ch, CURLOPT_URL, $upload_url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $params);
    $response = curl_exec($ch);
    curl_close($ch);*/

    /*
     < ?php $path="aa/";
     // Set your path to image upload
     if(!is_dir($path)){ mkdir($path); }
     $roomPhotoList = $_POST['image'];
     $random_digit=date('Y_m_d_h_i_s');
     $filename=$random_digit.'.jpg';
     $decoded=base64_decode($roomPhotoList);
     file_put_contents($path.$filename,$decoded);
     ?>
     */

    internal var LoginURL = "http://192.168.0.27/servicio/Validar.php"
    private val LoginTask = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnReg?.setOnClickListener {
            val intent:Intent = Intent(this, Register::class.java)
            startActivity(intent)
            //this.finish()
        }

        btnLogin?.setOnClickListener {
            var user = this.editUsuario.text.toString()
            var pass = this.editPass.text.toString()
            Fuel.post(
                LoginURL, listOf(
                    "nick" to user
                    , "pass" to pass
                )
            ).responseJson { request, response, result ->
                Log.d("plzzzzz", result.get().content)
                onTaskCompleted(result.get().content,LoginTask,user)
            }
        }
    }

    private fun onTaskCompleted(response: String, task: Int, user: String) {
        Log.d("responsejson", response)
        //Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
        //val jsonObject = JSONObject(response)
        //val temp = jsonObject.getString("nick")
        //Toast.makeText(this,temp.toString(), Toast.LENGTH_SHORT).show()
        //when (task) {
            //LoginTask ->
        if (isSuccess(response,user) == true) {
                Toast.makeText(this, "Bienvenido "+user, Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Welcome::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                this.finish()
            } else {
                Toast.makeText(this, "No registrado", Toast.LENGTH_SHORT).show()
            }
    }


    fun isSuccess(response: String, user: String): Boolean {
        try {
            val jsonObject = JSONObject(response)
            //val temp = jsonObject.optString("nick")
            //Toast.makeText(this,temp, Toast.LENGTH_SHORT).show()
            return if (jsonObject.optString("nick") == user ){
                true
            } else {
                false
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return false
    }
}
