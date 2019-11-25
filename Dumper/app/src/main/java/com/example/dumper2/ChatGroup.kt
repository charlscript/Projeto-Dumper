package com.example.dumper2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_chat_group.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_chat_group.btn_send_message
import kotlinx.android.synthetic.main.activity_chat_group.et_message
import kotlinx.android.synthetic.main.activity_chat_group.toolbar
import kotlinx.android.synthetic.main.messages_group.view.*
import androidx.core.app.NotificationCompat.getExtras



class ChatGroup : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    var messages = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_group)
        setSupportActionBar(toolbar)
        val actionbar = supportActionBar
        val extras = intent.extras
        actionbar!!.title = extras!!.getString("Grupo")
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
        val id = extras!!.getString("ID")
        val user = auth.currentUser
        val email = user?.email

        val mLinearLayoutManager = LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true)
        mLinearLayoutManager.isSmoothScrollbarEnabled()
        chatGroup.setLayoutManager(mLinearLayoutManager)
        chatGroup.itemAnimator = DefaultItemAnimator()
        chatGroup.adapter = MyAdapter(messages)

        val retrofitClient = DumperAPI.getRetrofitInstance("https://dumper-app.herokuapp.com")
        val endpoint = retrofitClient.create(Endpoint::class.java)


        btn_send_message.setOnClickListener{
            val message = et_message.text.toString().trim()

            var nome = user?.displayName
            val messageSent = MessageSent(message,nome,email)
            val messageBody = MessageBody(id, messageSent)

            val callback = endpoint

            callback.sendMessage(messageBody).enqueue(object : Callback<GrupoAdminResponse> {
                override fun onFailure(call: Call<GrupoAdminResponse>, t: Throwable) {
                    Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(call: Call<GrupoAdminResponse>, response: Response<GrupoAdminResponse>) {
                    messages.clear()
                    response.body()?.messages?.forEach{
                        val respMessage = Message(
                            it._id,
                            it.mensagem,
                            it.nome,
                            it.email
                        )
                        messages.add(respMessage)
                    }

                    chatGroup.adapter?.notifyDataSetChanged()
                    chatGroup.smoothScrollToPosition(chatGroup.adapter!!.getItemCount())
                    et_message.setText("")
                }
            })

        }
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val message_received: TextView = view.findViewById(R.id.message_received)
        val my_name: TextView = view.findViewById(R.id.my_name)
        val other_name: TextView = view.findViewById(R.id.other_name)
        val message_sent: TextView = view.findViewById(R.id.message_sent)
        var message: Message? = null

        init {
            view.setOnClickListener {
                //Toast.makeText(view.context, "teste" , Toast.LENGTH_LONG).show()
            }
        }

    }

    class MyAdapter(val list: List<Message>) : RecyclerView.Adapter<MyViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.messages_group, parent, false)
            return MyViewHolder(view)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
            val u = list[position]
            lateinit var auth: FirebaseAuth
            auth = FirebaseAuth.getInstance()
            val user = auth.currentUser
            val email = user!!.email
            if (u.email == email){
                viewHolder.message_sent.setText(u.mensagem)
                viewHolder.my_name.setText(u.nome)
                viewHolder.message = u
            }else{
                viewHolder.message_received.setText(u.mensagem)
                viewHolder.other_name.setText(u.nome)
                viewHolder.message = u
            }
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
