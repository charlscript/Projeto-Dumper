package com.example.dumper2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_cadastro_de_ponto.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class CadastroDePontoActivity : AppCompatActivity(), OnMapReadyCallback, MapFragment.OnFragmentInteractionListener, GoogleMap.OnMarkerClickListener {

    private var Localidade : Location? = null
    private lateinit var user: String
    private var  mfrag: SupportMapFragment? = null
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        user = auth.currentUser?.email.toString()
        setContentView(R.layout.activity_cadastro_de_ponto)
        setSupportActionBar(toolbar)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Cadastro de ponto"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        (supportFragmentManager.findFragmentById(R.id.mapFrag) as SupportMapFragment?)?.let{
            mfrag = it
            it.getMapAsync(this)
        }


        /*BUTTON DO CHAT*/
        fab.setOnClickListener {
            val intent = Intent(this, ChatBotActivity::class.java)
            startActivity(intent)
        }

        btn_submit.setOnClickListener{

            val retrofitClient = DumperAPI.getRetrofitInstance("https://dumper-app.herokuapp.com")
            val endpoint = retrofitClient.create(Endpoint::class.java)


            val point = RegisterPoint(
                nome = et_cadastro_user_name.text.trim().toString(),
                descricao = editText2.text.toString(),
                userEmail = if (!user.isNullOrBlank()) user else "",
                validado = false,
                latitude = if (Localidade != null) Localidade?.latitude.toString() else "",
                longitude = if (Localidade != null) Localidade?.longitude.toString() else ""
            )
            endpoint.savePoint(point).enqueue(object : Callback<RegisterPointResponse>{
                override fun onResponse(call: Call<RegisterPointResponse>, response: Response<RegisterPointResponse>) {
                    if (response.body()!!.nome == et_cadastro_user_name.text.trim().toString()){
                        Toast.makeText(this@CadastroDePontoActivity, "Ponto " + response.body()!!.nome + " cadastrado com sucesso!", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@CadastroDePontoActivity, MapaActivity::class.java)
                        startActivity(intent)
                    }
                }

                override fun onFailure(call: Call<RegisterPointResponse>, t: Throwable) {
                    Toast.makeText(this@CadastroDePontoActivity, "Não foi possível cadastrar o ponto!",Toast.LENGTH_LONG).show()
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

    override fun onFragmentInteraction(local: Location){
        Localidade = local
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setUpMap()
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

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }else if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true

            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    Localidade = location


                    mMap.uiSettings.isZoomControlsEnabled = true
                    mMap.setOnMarkerClickListener(this)
                    val currentLatLng = LatLng(Localidade!!.latitude.toDouble(), Localidade!!.longitude.toDouble())
                    placeMarkerOnMap(currentLatLng)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12.0f))

                }
            }
        }

    }

    //override  fun OnFragmentInteractionListener(){
    //
    //}

    override fun onMarkerClick(p0: Marker?) = false

    private fun placeMarkerOnMap(location: LatLng) {
        val markerOptions = MarkerOptions().position(location)

        val titleStr = getAddress(location)
        markerOptions.title(titleStr)

        mMap.addMarker(markerOptions)
    }

    private fun getAddress(latLng: LatLng): String {
        // 1
        val geocoder = Geocoder(this)
        val addresses: List<Address>?
        val address: Address?
        var addressText = ""

        try {
            // 2
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            // 3
            if (null != addresses && !addresses.isEmpty()) {
                address = addresses[0]
                for (i in 0 until address.maxAddressLineIndex+1) {
                    addressText += if (i == 0) address.getAddressLine(i) else "\n" + address.getAddressLine(i)
                }
            }
        } catch (e: IOException) {
            Log.e("MapsActivity", e.localizedMessage as String)
        }

        return addressText
    }

    fun msgShow(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }


}
