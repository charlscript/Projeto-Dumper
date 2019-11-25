package com.example.dumper2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_cadastrar_grupo.*


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CadastrarGrupo : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar_grupo)
        setSupportActionBar(toolbar)
        val retrofitClient = DumperAPI.getRetrofitInstance("https://dumper-app.herokuapp.com")
        val endpoint = retrofitClient.create(Endpoint::class.java)
        val callback = endpoint

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Cadastro de grupos"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)




        var btnEnviar = findViewById<Button>(R.id.btn_cadastrar_grupo)

        btnEnviar.setOnClickListener{
            val nomeGrupo = et_nomeGrupo.text.toString().trim()
            val descricaoGrupo = et_descricaoGrupo.text.toString().trim()
            var EmailAdmin = auth.currentUser!!.email
            var grupo = Grupo(nomeGrupo,EmailAdmin,descricaoGrupo)
            callback.createGrupo(grupo).enqueue(object: Callback<GrupoResponse> {

                override fun onResponse(call: Call<GrupoResponse>, response: Response<GrupoResponse>) {
                    if (response.body()!!._id != "") {
                        Toast.makeText(this@CadastrarGrupo, "Grupo criado com sucesso", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@CadastrarGrupo, MapaActivity::class.java)
                        startActivity(intent)
                    } else {
                        val builder = AlertDialog.Builder(this@CadastrarGrupo)
                        builder.setTitle("Erro")
                        builder.setMessage("Erro ao criar grupo")
                    }
                }
                override fun onFailure(call: Call<GrupoResponse>, t: Throwable) {

                }
            })
        }

        fab.setOnClickListener { _ ->
            val intent = Intent(this, ChatBotActivity::class.java)
            startActivity(intent)
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
            msgShow("Map")
            val intent = Intent(this, MapaActivity::class.java)
            startActivity(intent)
            true
        }
        R.id.action_add_novos_pontos -> {
            msgShow("Adicionar novos pontos")
            val intent = Intent(this, CadastroDePontoActivity::class.java)
            intent.putExtra("User", auth.currentUser?.email)
            startActivity(intent)
            true
        }
        R.id.action_admin_grupos -> {
            msgShow("Grupos que eu gerencio")
            val intent = Intent(this, GruposGerenciados::class.java)
            startActivity(intent)
            true
        }
        R.id.action_grupos -> {
            msgShow("Grupos")
            val intent = Intent(this, TodosGrupos::class.java)
            startActivity(intent)
            true
        }
        R.id.action_meus_grupos -> {
            msgShow("Grupos que eu participo")
            val intent = Intent(this, MeusGrupos::class.java)
            startActivity(intent)
            true
        }
        R.id.action_cadastro_grupo -> {
            msgShow("Cadastrar um grupo")
            val intent = Intent(this, CadastrarGrupo::class.java)
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
