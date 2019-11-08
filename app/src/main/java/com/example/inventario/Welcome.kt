package com.example.inventario

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

import kotlinx.android.synthetic.main.activity_welcome.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class Welcome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        setSupportActionBar(toolbar)

        /*fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/
        fab?.setOnClickListener {
            val intent: Intent = Intent(this, addInventario::class.java)
            startActivity(intent)
            //this.finish()
        }
    }

}
/*
btnLogin?.setOnClickListener {

    var reqParam = URLEncoder.encode("nick", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8")
    reqParam += "&" + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8")
    val mURL = URL(LoginURL)
    Toast.makeText(this@MainActivity, "antes de conectar", Toast.LENGTH_SHORT).show()
    with(mURL.openConnection() as HttpURLConnection) {
        // optional default is GET
        requestMethod = "POST"

        val wr = OutputStreamWriter(getOutputStream());
        wr.write(reqParam);
        wr.flush();
        Toast.makeText(this@MainActivity, "conecto", Toast.LENGTH_SHORT).show()
        //println("URL : $url")
        //println("Response Code : $responseCode")

        BufferedReader(InputStreamReader(inputStream)).use {
            val response = StringBuffer()

            var inputLine = it.readLine()
            while (inputLine != null) {
                response.append(inputLine)
                inputLine = it.readLine()
            }
            it.close()
            Toast.makeText(this@MainActivity, response, Toast.LENGTH_SHORT).show()
            if(response.substring(0) != "[]"){
                abrir()
            }
            else{
                Toast.makeText(this@MainActivity, "No registrado", Toast.LENGTH_SHORT).show()
            }
            //println("Response : $response")
            //respuesta = response.toString()
            //es(respuesta.append(response))
        }
    }
}
}
fun es(respuesta: String){
    val res = 0
    if(respuesta.length <  2){
        val intent = Intent(this@MainActivity, Welcome::class.java)
        startActivity(intent)
        this.finish()
    }
    else{
        Toast.makeText(this@MainActivity, "No registrado", Toast.LENGTH_SHORT).show()
    }
}
fun abrir(){
    val intent:Intent = Intent(this, Welcome::class.java)
    startActivity(intent)
}*/
