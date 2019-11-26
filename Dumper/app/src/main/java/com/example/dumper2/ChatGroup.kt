package com.example.dumper2

import android.content.Context
import android.content.Intent
import android.graphics.Insets.add
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.format.DateUtils
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
import androidx.core.content.ContextCompat.startActivity
import kotlinx.android.synthetic.main.message_received.view.*

private const val VIEW_TYPE_MY_MESSAGE = 1
private const val VIEW_TYPE_OTHER_MESSAGE = 2
class ChatGroup : AppCompatActivity() {
    private lateinit var adapter: MyAdapter
    private lateinit var auth: FirebaseAuth


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
        adapter = MyAdapter(this)
        chatGroup.adapter = adapter

        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                val retrofitClient = DumperAPI.getRetrofitInstance("https://dumper-app.herokuapp.com")
                val endpoint = retrofitClient.create(Endpoint::class.java)
                val callback = endpoint
                 callback.getGroup(id.toString()).enqueue(object : Callback<GrupoAdminResponse>{
                    override fun onResponse(call: Call<GrupoAdminResponse>, response: Response<GrupoAdminResponse>) {
                        adapter.clear()
                        response.body()?.messages?.forEach{
                            val respMessage = Message(
                                it._id,
                                it.mensagem,
                                it.nome,
                                it.email
                            )
                            adapter.addMessage(respMessage)
                        }
                        adapter.notifyDataSetChanged()
                    }

                    override fun onFailure(call: Call<GrupoAdminResponse>, t: Throwable) {
                        Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
                    }
                })

                mainHandler.postDelayed(this, 3000)
            }
        })

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
                    adapter.clear()
                    response.body()?.messages?.forEach{
                        val respMessage = Message(
                            it._id,
                            it.mensagem,
                            it.nome,
                            it.email
                        )
                        adapter.addMessage(respMessage)
                    }
                    adapter.notifyDataSetChanged()

                    chatGroup.smoothScrollToPosition(chatGroup.adapter!!.getItemCount())
                    et_message.setText("")
                }
            })

        }
    }


    class MyAdapter (val context: Context) : RecyclerView.Adapter<MessageViewHolder>() {
        var messages = mutableListOf<Message>()
        fun clear(){
            messages.clear()
        }
        fun addMessage(message: Message){
            messages.add(message)
        }

        override fun getItemCount(): Int {
            return messages.size
        }

        override fun getItemViewType(position: Int): Int {
            val message = messages.get(position)
            lateinit var auth: FirebaseAuth
            auth = FirebaseAuth.getInstance()
            val user = auth.currentUser
            val email = user!!.email
            return if(email == message.email) {
                VIEW_TYPE_MY_MESSAGE
            }
            else {
                VIEW_TYPE_OTHER_MESSAGE
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
            return if(viewType == VIEW_TYPE_MY_MESSAGE) {
                MyMessageViewHolder(LayoutInflater.from(context).inflate(R.layout.messages_group, parent, false))
            } else {
                OtherMessageViewHolder(LayoutInflater.from(context).inflate(R.layout.message_received, parent, false))
            }
        }

        override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
            val message = messages.get(position)

            holder?.bind(message)
        }

        inner class MyMessageViewHolder (view: View) : MessageViewHolder(view) {
            private var messageText: TextView = view.message_sent
            private var my_name: TextView = view.my_name

            override fun bind(message: Message) {
                messageText.text = message.mensagem
                my_name.text = message.nome
            }
        }

        inner class OtherMessageViewHolder (view: View) : MessageViewHolder(view) {
            private var messageText: TextView = view.message_received
            private var other_name: TextView = view.other_name

            override fun bind(message: Message) {
                messageText.text = message.mensagem
                other_name.text = message.nome

            }
        }
    }

    open class MessageViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        open fun bind(message:Message) {}
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
