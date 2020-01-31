package com.example.entryticketsales

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.entryticketsales.utils.makeJsonParsing
import com.example.entryticketsales.viewmodels.EntryTicketsInfoViewModel
import com.example.entryticketsales.vos.Item
import com.example.entryticketsales.vos.Ticket
import kotlinx.android.synthetic.main.activity_total_payment.*
import kotlinx.android.synthetic.main.submission_success_dialog.view.*

class TotalPaymentActivity : AppCompatActivity() {

    companion object {
        fun getIntent(context: Context) = Intent(context, TotalPaymentActivity::class.java)
    }

    private var viewModel   : EntryTicketsInfoViewModel? = null
    lateinit var item       : Item
    private var itemList    : MutableList<Ticket>? = null
    var mapSelectedList : MutableList<Ticket> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_total_payment)

        setSupportActionBar(toolbarPayment)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar.apply {
            title = ""
        }

        val from : String = intent.getStringExtra("selectedFrom")

        if(from == "map"){
            var totalCost = intent.getIntExtra("totalAmt",0)
            val idList = intent.getSerializableExtra("idList") as List<Int>

            //observe api data
            viewModel = ViewModelProviders.of(this).get(EntryTicketsInfoViewModel::class.java)

            viewModel!!.getTicketInfo()
            viewModel!!.result.observe(this, Observer {
                if (!it.items.isNullOrEmpty()) {
                    item = it.items[0]
                    val rawData = Html.fromHtml(item.content).toString()
                    val data = rawData.replace("￼", "")
                    itemList = makeJsonParsing(data, this)

                    for(i in 0 until idList.size){
                        for(j in 0 until itemList!!.size){
                            if(idList[i] == itemList!![j].id.toInt())
                                mapSelectedList.add(itemList!![j])

                        }
                    }

                    if(mapSelectedList.isEmpty()){
                        tvNoData.isVisible = true
                        rvPayment.isVisible = false
                    }

                    for(i in 0 until mapSelectedList.size)
                        totalCost += mapSelectedList[i].price.toInt()

                    tvTotalCost.text = "$totalCost mmk"

                    val adapter = TotalPaymentListAdapter(mapSelectedList)
                    rvPayment.layoutManager = LinearLayoutManager(this)
                    rvPayment.adapter = adapter
                }
            })
        }

        else if(from == "list"){
            val selectedList = intent.getSerializableExtra("List") as List<Ticket>
            val totalCost = intent.getIntExtra("totalAmt",0)

            val adapter = TotalPaymentListAdapter(selectedList)
            rvPayment.layoutManager = LinearLayoutManager(this)
            rvPayment.adapter = adapter

            tvTotalCost.text = "$totalCost  mmk"

        }

        val preferences = getSharedPreferences("PREFS", 0)
        val userName = preferences.getString("userName", "")
        val email = preferences.getString("email","")

        btnSubmit.setOnClickListener {
            showSuccessDialog(userName!!,email!!)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
                return true
            } else ->
            return super.onOptionsItemSelected(item)
        }
    }

    //for success dialog
    private fun showSuccessDialog(name : String, email : String) {

        val alertDialogView = LayoutInflater.from(this).inflate(R.layout.submission_success_dialog, null)
        val alertDialogBuilder = AlertDialog.Builder(this).setView(alertDialogView)

        alertDialogView.tvUserNamePayment.text = name

        val mDialog = alertDialogBuilder.show()
        val anni: Animation = AnimationUtils.loadAnimation(this, R.anim.img_float)
        alertDialogView.imgThankU.startAnimation(anni)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(mDialog.window?.attributes)

        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

//        lp.width = 650
//        lp.height = 570

        lp.gravity = Gravity.CENTER
        lp.windowAnimations = R.style.MyDialogAnimation
        mDialog.window?.attributes = lp

        alertDialogView.closePaymentSuccessDialog.setOnClickListener {
            mDialog.dismiss()
            startActivity(LoginActivity.getIntent(applicationContext))
            finish()
        }
    }
}
