package com.example.dumper2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_cadastro_usuario.*


class CadastroUsuarioActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_usuario)
        setSupportActionBar(toolbar)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Cadastro de Usuário"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        auth = FirebaseAuth.getInstance()

        var et_cadastro_user_name = findViewById<EditText>(R.id.et_cadastro_user_name)
        var et_cadastro_email = findViewById<EditText>(R.id.et_cadastro_email)
        var et_cadastro_password = findViewById<TextView>(R.id.et_cadastro_password)
        var btn_submit = findViewById<Button>(R.id.btn_submit)
        val retrofitClient = DumperAPI.getRetrofitInstance("https://dumper-app.herokuapp.com")
        val endpoint = retrofitClient.create(Endpoint::class.java)
        val callback = endpoint



        btn_submit.setOnClickListener {
            var nomeUser = et_cadastro_user_name.text.toString();
            var emailUser = et_cadastro_email.text.toString().trim().toLowerCase();
            var passwordUser = et_cadastro_password.text.toString().trim();
            val user = RegisterUser(nomeUser, emailUser, passwordUser)

            if(nomeUser == "") {
                val builder = AlertDialog.Builder(this@CadastroUsuarioActivity)
                builder.setMessage("Por favor, Preencha o nome de usuário")
                builder.setPositiveButton("OK") { _, _ ->
                    //code:TODO
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            } else if( emailUser == ""){
                val builder = AlertDialog.Builder(this@CadastroUsuarioActivity)
                builder.setMessage("Por favor, Preencha o e-mail")
                builder.setPositiveButton("OK") { _, _ ->
                    //code:TODO
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            } else if(passwordUser == "") {
                val builder = AlertDialog.Builder(this@CadastroUsuarioActivity)
                builder.setMessage("Por favor, Preencha a senha")
                builder.setPositiveButton("OK") { _, _ ->
                    //code:TODO
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
            if(!nomeUser.isEmpty() && !emailUser.isEmpty() && !passwordUser.isEmpty()){

                auth.createUserWithEmailAndPassword(emailUser, passwordUser)

                    .addOnCompleteListener(this) { task ->

                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(nomeUser)
                                .build()
                            user?.updateProfile(profileUpdates)
                                ?.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(applicationContext, "Usuário cadastrado com sucesso.", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this@CadastroUsuarioActivity, LoginActivity::class.java)
                                        startActivity(intent)
                                    }
                                }
                            //val user = auth.currentUser
                            //updateUI(user)
                        } else {
                            Toast.makeText(applicationContext, "Falha no registro", Toast.LENGTH_SHORT).show()
                        }

                        // ...
                    }


                /*callback!!.saveUser(user).enqueue(object : Callback<RegisterUserResponse> {

                    override fun onResponse(call: Call<RegisterUserResponse>, response: Response<RegisterUserResponse>) {

                        if (response.body()!!.email == emailUser) {
                            Toast.makeText(applicationContext, "Usuário cadastrado com sucesso.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@CadastroUsuarioActivity, LoginActivity::class.java)
                            startActivity(intent)
                        } else if (response.body()!!.erro == "Usuário já existe") {
                            Toast.makeText(applicationContext, "Email já cadastrado no sistema.", Toast.LENGTH_SHORT).show()
                        } else {
                            val builder = AlertDialog.Builder(this@CadastroUsuarioActivity)
                            builder.setTitle("Erro de conexão")
                            builder.setMessage("Por favor, Tente novamente mais tarde!")
                            builder.setPositiveButton("OK") { _, _ ->
                                //code:TODO
                            }
                            val dialog: AlertDialog = builder.create()
                            dialog.show()
                        }
                    }
                    override fun onFailure(call: Call<RegisterUserResponse>, t: Throwable) {

                    }
                })*/
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
