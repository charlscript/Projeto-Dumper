package com.example.dumper2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_chat_bot.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatBotActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var listView: ListView
    var messages = mutableListOf<String>()
    var adapter: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_bot)
        setSupportActionBar(toolbar)
        listView = findViewById(R.id.chat_view)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Chat"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        val retrofitClient = DumperAPI.getRetrofitInstance("https://dumper-app.herokuapp.com")
        val endpoint = retrofitClient.create(Endpoint::class.java)


        btn_send_message.setOnClickListener{
            val message = et_message.text.toString().trim()
            val user = auth.currentUser
            var email = user?.email
            val dialogBody = DialogFlowPost(message, email, email)
            val callback = endpoint
            adapter = ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1, messages)
            listView.adapter = adapter

            callback.dialog(dialogBody).enqueue(object : Callback<DialogFlowResponse> {
                override fun onFailure(call: Call<DialogFlowResponse>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(call: Call<DialogFlowResponse>, response: Response<DialogFlowResponse>) {
                    println(response.body())
                    messages.add(response.body()!!.query)
                    messages.add(response.body()!!.response)
                    adapter?.notifyDataSetChanged()
                    }
                })

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /*===============MENU===============*/
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_mapa -> {
            msgShow("Mapa")
            val intent = Intent(this, MapaActivity::class.java)
            startActivity(intent)
            true
        }
        R.id.action_add_novos_pontos -> {
            msgShow("Adicionar novos ponto")
            val intent = Intent(this, CadastroDePontoActivity::class.java)
            startActivity(intent)
            true
        }
        R.id.action_lista_pontos -> {
            msgShow("Lista de pontos")
            val intent = Intent(this, ListaPontoActivity2::class.java)
            startActivity(intent)
            true
        }
        R.id.action_fale_conosco -> {
            msgShow("Fale conosco")
            val intent = Intent(this, FaleConoscoActivity::class.java)
            startActivity(intent)
            true
        }
        R.id.action_logout -> {
            msgShow("Saindo")
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    fun msgShow(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

}
