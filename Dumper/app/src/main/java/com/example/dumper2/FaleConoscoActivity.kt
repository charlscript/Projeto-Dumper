package com.example.dumper2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_fale_conosco.*

class FaleConoscoActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fale_conosco)
        setSupportActionBar(toolbar)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Fale Conosco"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        var et_faleConosco_name_ponto = findViewById<EditText>(R.id.et_faleConosco_user_name)
        var et_faleConosco_email = findViewById<EditText>(R.id.et_faleConosco_email)
        var faleConosco_msg = findViewById<TextView>(R.id.faleConosco_msg)
        var faleConosco_btn_submit = findViewById<Button>(R.id.faleConosco_btn_submit)

        fab.setOnClickListener { _ ->
            val intent = Intent(this, ChatBotActivity::class.java)
            startActivity(intent)
        }

        faleConosco_btn_submit.setOnClickListener {
            var nome = et_faleConosco_name_ponto.text.toString();
            var email = et_faleConosco_email.text.toString();
            var msg = faleConosco_msg.text.toString();

            if(nome == ""){
                val builder = AlertDialog.Builder(this@FaleConoscoActivity)
                builder.setMessage("Por favor, Preencha o nome de usuário")
                builder.setPositiveButton("OK") { _, _ ->
                    //code:TODO
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            } else if(email == ""){
                val builder = AlertDialog.Builder(this@FaleConoscoActivity)
                builder.setMessage("Por favor, Preencha o email")
                builder.setPositiveButton("OK") { _, _ ->
                    //code:TODO
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            } else if(msg == ""){
                val builder = AlertDialog.Builder(this@FaleConoscoActivity)
                builder.setMessage("Por favor, Preencha o conteudo da mensagem")
                builder.setPositiveButton("OK") { _, _ ->
                    //code:TODO
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
            
            if(nome != "" && email != "" && msg != ""){
                if(enviar()){
                    Toast.makeText(applicationContext, "Mensagem enviada com sucesso!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MapaActivity::class.java)
                    startActivity(intent)
                }else{
                    val builder = AlertDialog.Builder(this@FaleConoscoActivity)
                    builder.setTitle("Erro de conexão")
                    builder.setMessage("Por favor, Tente novamente mais tarde!")
                    builder.setPositiveButton("OK") { _, _ ->
                        //code:TODO
                    }
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /*FUNÇÃO ENVIAR PARA API*/
    fun enviar(): Boolean{
        return true;
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
        R.id.action_admin_grupos -> {
            msgShow("Grupos que eu gerencio")
            val intent = Intent(this, ListaPontoActivity2::class.java)
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
