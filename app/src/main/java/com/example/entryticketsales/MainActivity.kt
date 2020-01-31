package com.example.entryticketsales

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.text.Html
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.entryticketsales.utils.makeJsonParsing
import com.example.entryticketsales.utils.showToast
import com.example.entryticketsales.viewmodels.EntryTicketsInfoViewModel
import com.example.entryticketsales.vos.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.main_layout.*
import kotlinx.android.synthetic.main.marker_tap_dialog.view.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    val selectedTicketList: ArrayList<Ticket> = arrayListOf()
    var totalCost: Int = 0

    private var viewModel : EntryTicketsInfoViewModel? = null
    lateinit var item     : Item
    private var itemList    : MutableList<Ticket>? = null

    private lateinit var mMap: GoogleMap
    val locationList: MutableList<LatLng> = mutableListOf()

    val inseinPark = LatLng(16.888231, 96.108353)
    val kanDawGyiLake = LatLng(16.794838, 96.163713)
    val nationalMusium = LatLng(16.788494, 96.142236)
    val pyithuyinpyinPark = LatLng(16.799018, 96.138394)
    val seinlansopyay = LatLng(16.855080, 96.118488)

    var mapSelectedIdList : ArrayList<Int> = arrayListOf()

    companion object {
        fun getIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        //observe api data
        viewModel = ViewModelProviders.of(this).get(EntryTicketsInfoViewModel::class.java)

        viewModel!!.getTicketInfo()
        viewModel!!.result.observe(this, Observer {
            if (!it.items.isNullOrEmpty()) {
                item = it.items[0]
                val rawData = Html.fromHtml(item.content).toString()
                val data = rawData.replace("￼", "")
                itemList = makeJsonParsing(data,this)

                //list to adapter
                val adapter = ItemListAdapter(itemList!!, listener)
                rvItemList.layoutManager = LinearLayoutManager(this)
                rvItemList.adapter = adapter
            }
        })

        //done -> map selected
        tvDoneMain.setOnClickListener {
            val intent: Intent = TotalPaymentActivity.getIntent(this)
            intent.putExtra("idList",mapSelectedIdList)
            intent.putExtra("selectedFrom","map")
            startActivity(intent)
        }

        //done -> list selected
        btnDoneList.setOnClickListener {
            val intent: Intent = TotalPaymentActivity.getIntent(this)
            intent.putExtra("List", selectedTicketList)
            intent.putExtra("totalAmt", totalCost)
            intent.putExtra("selectedFrom","list")
            startActivity(intent)
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //fixed location to map
        locationList.add(inseinPark)
        locationList.add(kanDawGyiLake)
        locationList.add(nationalMusium)
        locationList.add(pyithuyinpyinPark)
        locationList.add(seinlansopyay)
    }

    private var listener = object : ItemListAdapter.OnItemClickListener {
        override fun onItemClick(item: Ticket, position: Int, isSelected: Boolean) {
            addToList(item, position, isSelected)
        }
    }

    private fun addToList(ticketInfo: Ticket, position: Int, isSelected: Boolean): MutableList<Ticket> {

        //showToast(this,selectedTicketList.size.toString())
        //showToast(this,totalCost.toString())

            selectedTicketList.add(ticketInfo)
            totalCost += ticketInfo.price.toInt()

        return selectedTicketList
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        for (i in 0 until locationList.size) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(locationList[i]))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(locationList[i]))
            val zoomLevel = 11.8f
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationList[i], zoomLevel))

            when (i) {
                0 -> {
                    mMap.addMarker(MarkerOptions().position(locationList[0]).title("Insein Park")
                        .icon(bitmapDescriptorFromVector(this,R.drawable.ic_location_on_black_24dp)))
                }
                1 -> {
                    mMap.addMarker(MarkerOptions().position(locationList[1]).title("Kan Daw Gyi Lake")
                        .icon(bitmapDescriptorFromVector(this,R.drawable.ic_location_on_black_24dp)))
                }
                2 -> {
                    mMap.addMarker(MarkerOptions().position(locationList[2]).title("National Musium")
                        .icon(bitmapDescriptorFromVector(this,R.drawable.ic_location_on_black_24dp)))
                }
                3 -> {
                    mMap.addMarker(MarkerOptions().position(locationList[3]).title("Pyi Thu Yin Pyin Park")
                        .icon(bitmapDescriptorFromVector(this,R.drawable.ic_location_on_black_24dp)))
                }
                4 -> {
                    mMap.addMarker(MarkerOptions().position(locationList[4]).title("Sein Lan So Pyay Park")
                        .icon(bitmapDescriptorFromVector(this,R.drawable.ic_location_on_black_24dp)))
                }
            }

            mMap.setOnMarkerClickListener(this)
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {

        var location = ""
        var price    = 0

        val title = marker.title
        when(title){
            "Insein Park" -> {
                location = title
                price    = 500
                showAlertDialog(location,price,marker,1)
            }

            "Kan Daw Gyi Lake" -> {
                location = title
                price    = 300
                showAlertDialog(location,price,marker,2)
            }

            "National Musium" -> {
                location = title
                price    = 400
                showAlertDialog(location,price,marker,3)

            }

            "Pyi Thu Yin Pyin Park" -> {
                location = title
                price    = 200
                showAlertDialog(location,price,marker,4)
            }

            "Sein Lan So Pyay Park" -> {
                location = title
                price    = 600
                showAlertDialog(location,price,marker,5)
            }
        }
        return true
    }

    //tap on map
    private fun showAlertDialog(locationName : String, ticketPrice : Int,marker: Marker,id : Int) {

        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.marker_tap_dialog, null)

        view.itemName.text   = locationName
        view.itemAmount.text = "$ticketPrice mmk"

        builder.setView(view)

        val dialog = builder.create()
        view.btnPickUp.setOnClickListener {
            dialog.dismiss()
            marker.setIcon(bitmapDescriptorFromVector(this,R.drawable.ic_check))
            mapSelectedIdList.add(id)
            mapSelectedIdList.distinct()

            //showToast(this,mapSelectedIdList.distinct().size.toString())

        }
        view.btnRemove.setOnClickListener {

            if (mapSelectedIdList.isNullOrEmpty()){
                showToast(this, "There is no selected ticket!")
                dialog.dismiss()
             }
            else{
              if(mapSelectedIdList.contains(id)) {
                  mapSelectedIdList.distinct()
                  mapSelectedIdList.remove(id)
                  dialog.dismiss()
              }
            }
            //showToast(this,mapSelectedIdList.size.toString())
            marker.setIcon(bitmapDescriptorFromVector(this,R.drawable.ic_location_on_black_24dp))
        }

        view.closeDialog.setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.attributes?.windowAnimations = R.style.MyDialogAnimation
        dialog.show()

        //set width and height for dialog
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)

        lp.width = 650
        lp.height = 500

        dialog.window?.setBackgroundDrawableResource(R.color.transparent)
        dialog.window?.attributes = lp
    }

    private fun bitmapDescriptorFromVector(context : Context,vectorID : Int): BitmapDescriptor{

        val vectorDrawable = ContextCompat.getDrawable(context,vectorID)
        vectorDrawable!!.setBounds (0,0,vectorDrawable.intrinsicWidth,vectorDrawable.intrinsicHeight)

        val bitmap : Bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth,vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888)
        val canvas  = Canvas(bitmap)
        vectorDrawable.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}

