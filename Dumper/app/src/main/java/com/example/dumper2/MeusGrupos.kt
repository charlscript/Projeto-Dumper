package com.example.dumper2

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.activity_meus_grupos.*


class MeusGrupos : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    var grupos = mutableListOf<GrupoAdminResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grupos_gerenciados)
        setSupportActionBar(toolbar)
        val actionbar = supportActionBar
        actionbar!!.title = "Todos os grupos"
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        auth = FirebaseAuth.getInstance()
        val email = auth.currentUser!!.email

        val mLinearLayoutManager = LinearLayoutManager(this);
        groups.setLayoutManager(mLinearLayoutManager)
        groups.itemAnimator = DefaultItemAnimator()
        groups.adapter = MyAdapter(this,grupos)

        getGrupos()
        fab.setOnClickListener { _ ->
            val intent = Intent(this, ChatBotActivity::class.java)
            println(this)
            startActivity(intent)
        }
    }



    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nome: TextView = view.findViewById(R.id.nome)
        val descricao: TextView = view.findViewById(R.id.descricao)
        var group: GrupoAdminResponse? = null
        var context: Context = view.context


        init {
            //
        }
    }

    class MyAdapter(val parent:Context, val list: List<GrupoAdminResponse>) : RecyclerView.Adapter<MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.admin_groups, parent, false)
            return MyViewHolder(view)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
            val u = list[position]
            viewHolder.nome.setText(u.nome)
            viewHolder.descricao.setText(u.descricao)
            viewHolder.group = u
            viewHolder.itemView.setOnClickListener{

                val intent = Intent(viewHolder.context,ChatGroup::class.java)
                intent.putExtra("ID",u._id)
                intent.putExtra("Grupo", u.nome)
                viewHolder.context.startActivity(intent)

            }

        }

    }

    fun getGrupos() {

        val retrofitClient = DumperAPI.getRetrofitInstance("https://dumper-app.herokuapp.com")
        val endpoint = retrofitClient.create(Endpoint::class.java)
        val callback = endpoint
        val user = Admin(auth.currentUser?.email)

        callback.findUserGroups(user).enqueue(object: Callback<List<GrupoAdminResponse>> {
            override fun onFailure(call: Call<List<GrupoAdminResponse>>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<List<GrupoAdminResponse>>, response: Response<List<GrupoAdminResponse>>) {
                response.body()?.forEach {
                    val group = GrupoAdminResponse(
                        it._id,
                        it.nome,
                        it.EmailAdmin,
                        it.messages,
                        it.descricao,
                        it.createdAt,
                        it.updatedAt,
                        it.__v
                    )
                    grupos.add(group)
                    groups.adapter?.notifyDataSetChanged()
                }
            }
        })
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
