package com.example.dumper2


import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    @RequiresApi(Build.VERSION_CODES.O)

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Toast.makeText(this@LoginActivity, "Bem-vindo", Toast.LENGTH_LONG).show()
            val intent = Intent(this@LoginActivity, MapaActivity::class.java)
            startActivity(intent)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        val deepColor = Color.parseColor("#0097A7")

        this.window.statusBarColor = deepColor

        setContentView(R.layout.activity_login)
        var et_user_name = findViewById<EditText>(R.id.et_user_name)
        var et_password = findViewById<EditText>(R.id.et_password)
        var btn_cadastrar = findViewById<TextView>(R.id.btn_reset)
        var btn_logar = findViewById<Button>(R.id.btn_submit)
        var cadastrar_texto = findViewById<LinearLayout>(R.id.cadastrar_texto)

        var mProgressBar =  findViewById<ProgressBar>(R.id.indeterminateBar);
        mProgressBar.visibility = View.GONE

        et_password.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                btn_logar.performClick()
                true
            } else {
                false
            }
        }

        btn_cadastrar.setOnClickListener {
            val intent = Intent(this, CadastroUsuarioActivity::class.java)
            startActivity(intent)
        }

        btn_logar.setOnClickListener {

            val email = et_user_name.text.toString().trim().toLowerCase()
            val password = et_password.text.toString().trim()
            //val auth = Auth(email,password)
            val retrofitClient = DumperAPI.getRetrofitInstance("https://dumper-app.herokuapp.com")
            val endpoint = retrofitClient.create(Endpoint::class.java)
            val callback = endpoint

            if(email== ""){
                val builder = AlertDialog.Builder(this@LoginActivity)
                builder.setMessage("Por favor, Preencha o email")
                builder.setPositiveButton("OK") { _, _ ->
                   //code: TODO
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            } else if(password==""){
                val builder = AlertDialog.Builder(this@LoginActivity)
                builder.setMessage("Por favor, Preencha a senha")
                builder.setPositiveButton("OK") { _, _ ->
                    //code: TODO
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
            if(email != "" && password != ""){
                mProgressBar.visibility = View.VISIBLE;
                et_user_name.visibility = View.GONE;
                et_password.visibility = View.GONE;
                btn_logar.visibility = View.GONE;
                cadastrar_texto.visibility = View.GONE;

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            Toast.makeText(this@LoginActivity, "Bem-vindo, "+user?.displayName, Toast.LENGTH_LONG).show()
                            val intent = Intent(this@LoginActivity, MapaActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@LoginActivity, "Falha no login ", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@LoginActivity, LoginActivity::class.java)
                            startActivity(intent)
                        }

                        // ...
                    }
                /*callback!!.login(auth).enqueue(object : Callback<Login> {

                    override fun onResponse(call: Call<Login>, response: Response<Login>) {
                        if (response.body()!!.isMatch == "true") {
                            Toast.makeText(this@LoginActivity, "Bem-vindo, " + response.body()!!.nome, Toast.LENGTH_LONG).show()
                            val intent = Intent(this@LoginActivity, MapaActivity::class.java)
                            intent.putExtra("User", auth.email)
                            startActivity(intent)
                            mProgressBar.visibility = View.GONE;
                            et_user_name.visibility = View.VISIBLE;
                            et_password.visibility = View.VISIBLE;
                            btn_logar.visibility = View.VISIBLE;
                            cadastrar_texto.visibility = View.VISIBLE;
                        } else if (response.body()!!.isMatch == "Usuário não encontrado") {
                            val builder = AlertDialog.Builder(this@LoginActivity)
                            builder.setTitle("Erro")
                            builder.setMessage("Usuário não encontrado")
                            builder.setPositiveButton("OK") { _, _ ->
                               //code: TODO
                            }
                            val dialog: AlertDialog = builder.create()
                            mProgressBar.visibility = View.GONE;
                            et_user_name.visibility = View.VISIBLE;
                            et_password.visibility = View.VISIBLE;
                            btn_logar.visibility = View.VISIBLE;
                            cadastrar_texto.visibility = View.VISIBLE;
                            dialog.show()
                        } else {
                            val builder = AlertDialog.Builder(this@LoginActivity)
                            builder.setTitle("Erro")
                            builder.setMessage("Senha incorreta")
                            builder.setPositiveButton("OK") { _, _ ->
                                Toast.makeText(applicationContext, "Ok, we change the app background.", Toast.LENGTH_SHORT).show()
                            }
                            val dialog: AlertDialog = builder.create()
                            dialog.show()
                        }
                    }
                    override fun onFailure(call: Call<Login>, t: Throwable) {

                    }
                })*/
            }
        }
        btn_passwrd.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.reset_frag, null)

            val close = view.findViewById<ImageView>(R.id.iv_close)
            close.setOnClickListener {
                dialog.dismiss()
            }
            val sendMail = view.findViewById<Button>(R.id.btn_reset_pass)
            sendMail.setOnClickListener{

                var emailAddress = view.findViewById<EditText>(R.id.et_reset_password_email).text
                auth.sendPasswordResetEmail(emailAddress.toString().trim().toLowerCase())
                Toast.makeText(applicationContext,"Verifique sua caixa de entrada!",Toast.LENGTH_LONG).show()
                Handler().postDelayed({
                    dialog.dismiss()
                },2000)

            }
            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
        }

    }

}
