package com.example.inventario

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.editPass
import org.json.JSONException
import org.json.JSONObject

class Register : AppCompatActivity() {
    //fuel es para enviar datos entre servidores
    internal var RegisterURL = "http://192.168.0.27/servicio/Registrar.php"

    private var LoginTaskR = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnRegister?.setOnClickListener {
            var user = editUser.text.toString()
            var pass = editPass.text.toString()
            Fuel.post(
                RegisterURL, listOf(
                    "nick" to user
                    , "pass" to pass
                )
            ).responseJson { request, response, result ->
                Log.d("plzzzzz", result.get().content)
                onTaskCompleted(result.get().content,LoginTaskR,user)
            }
            Toast.makeText(this,"Se resgistro exitosamente a "+user, Toast.LENGTH_SHORT).show()
        }
    }
    private fun onTaskCompleted(response: String, task: Int, user: String) {
        Log.d("responsejson", response)
        //Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
        when (task) {
            LoginTaskR -> if (isSuccess(response,user)) {
                Toast.makeText(this, "Register Successfully!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                this.finish()
            } else {
                Toast.makeText(this, "No registrado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun isSuccess(response: String, user: String): Boolean {
        try {
            val jsonObject = JSONObject(response)
            return if(jsonObject.optString("nick") == user ){
                true
            }
            else {
                false
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return false
    }

}
