package com.example.roveapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.roveapp.databinding.ActivityHeatMapBinding
import com.google.android.gms.maps.model.TileOverlayOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.maps.android.heatmaps.HeatmapTileProvider
import com.google.maps.android.heatmaps.WeightedLatLng
import org.json.JSONArray

class HeatMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityHeatMapBinding
    private lateinit var btn: FloatingActionButton
    private lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHeatMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        btn = binding.addButton
        btn.setOnClickListener{
            val intent = Intent(this, ReportCrimeActivity::class.java)
            startActivity(intent)
        }
        val drawerLayout: DrawerLayout= findViewById(R.id.drawerLayout)
        val navView:NavigationView=findViewById(R.id.nav_view)
        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> Toast.makeText(applicationContext,"Clicked Home",Toast.LENGTH_SHORT).show()
                R.id.duty_sch -> Toast.makeText(applicationContext,"Duty Schedule",Toast.LENGTH_SHORT).show()
                R.id.crime_identify -> Toast.makeText(applicationContext,"Crime Identification ",Toast.LENGTH_SHORT).show()
                R.id.crime_list -> Toast.makeText(applicationContext,"Crime List",Toast.LENGTH_SHORT).show()
                R.id.nav_share -> Toast.makeText(applicationContext,"Clicked Share",Toast.LENGTH_SHORT).show()
                R.id.nav_logout -> Toast.makeText(applicationContext,"Clicked Logout",Toast.LENGTH_SHORT).show()
            }

            true
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
    private fun getJsonDataFromAsset(fileName: String): JSONArray? {
        try {
            val jsonString = assets.open(fileName).bufferedReader().use { it.readText() }
            return JSONArray(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
    private fun generateHeatMapData(): ArrayList<WeightedLatLng> {
        val data = ArrayList<WeightedLatLng>()

        // call our function which gets json data from our asset file
        val jsonData = getJsonDataFromAsset("myv54-l7rfd.json")

        // ensure null safety with let call
        jsonData?.let {
            // loop over each json object
            for (i in 0 until it.length()) {
                // parse each json object
                val entry = it.getJSONObject(i)
                val lat = entry.getDouble("latitude1")
                val lon = entry.getDouble("longitude1")
                //val density = entry.getDouble("density")
                val weightedLatLng = WeightedLatLng(LatLng(lat, lon))
                data.add(weightedLatLng)
                // optional: remove edge cases like 0 population density values
                /*if (density != 0.0) {
                    val weightedLatLng = WeightedLatLng(LatLng(lat, lon), density)
                    data.add(weightedLatLng)
                }*/
            }
        }

        return data
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val data = generateHeatMapData()

        val heatMapProvider = HeatmapTileProvider.Builder()
            .weightedData(data) // load our weighted data
            .radius(50) // optional, in pixels, can be anything between 20 and 50
            .build()

        googleMap.addTileOverlay(TileOverlayOptions().tileProvider(heatMapProvider))

        val indiaLatLng = LatLng(26.8467, 80.9462)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(indiaLatLng, 15f))
    }
}