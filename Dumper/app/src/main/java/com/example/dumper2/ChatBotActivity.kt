package com.example.dumper2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_chat_bot.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.messages.view.*

class ChatBotActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var messages = mutableListOf<Messages>()

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_bot)
        setSupportActionBar(toolbar)
        val actionbar = supportActionBar
        actionbar!!.title = "Chat"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        val mLinearLayoutManager = LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true)
        mLinearLayoutManager.isSmoothScrollbarEnabled()
        chatBot.setLayoutManager(mLinearLayoutManager)
        chatBot.itemAnimator = DefaultItemAnimator()
        chatBot.adapter = MyAdapter(messages)

        val retrofitClient = DumperAPI.getRetrofitInstance("https://dumper-app.herokuapp.com")
        val endpoint = retrofitClient.create(Endpoint::class.java)


        btn_send_message.setOnClickListener{
            val message = et_message.text.toString().trim()
            val user = auth.currentUser
            var email = user?.email
            val dialogBody = DialogFlowPost(message, email, email)
            val callback = endpoint

            callback.dialog(dialogBody).enqueue(object : Callback<DialogFlowResponse> {
                override fun onFailure(call: Call<DialogFlowResponse>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(call: Call<DialogFlowResponse>, response: Response<DialogFlowResponse>) {

                    val message = Messages(response.body()!!.query, response.body()!!.response)
                    messages.add(message)
                    chatBot.adapter?.notifyDataSetChanged()
                    chatBot.smoothScrollToPosition(chatBot.adapter!!.getItemCount())
                    et_message.setText("")
                    }
                })

        }
    }
    
    /**
     * CLASSE VIEW HOLDER (QUASE O EQUIVALENTE AO TABLEVIEW CELL
     */
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val message_received: TextView = view.findViewById(R.id.message_received)
        val message_sent: TextView = view.findViewById(R.id.message_sent)
        var message: Messages? = null

        init {
            view.setOnClickListener {
                Toast.makeText(view.context, "teste" , Toast.LENGTH_LONG).show()
            }
        }

    }

    /**
     * CLASSE ADAPATER RESPONSÁVEL PELOS DADOS E "FORMA" DE APRESENTAÇÃO
     */
    class MyAdapter(val list: List<Messages>) : RecyclerView.Adapter<MyViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.messages, parent, false)
            return MyViewHolder(view)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
            val u = list[position]
            viewHolder.message_sent.setText(u.message_sent)
            viewHolder.message_received.setText(u.message_received)
            viewHolder.message = u
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
