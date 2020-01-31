package com.example.entryticketsales.utils

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.entryticketsales.R
import com.example.entryticketsales.vos.Ticket
import kotlinx.android.synthetic.main.custom_toast_layout.view.*
import org.json.JSONException
import org.json.JSONObject

fun showToast(context: Context, message: String){
    val view : View = LayoutInflater.from(context).inflate(R.layout.custom_toast_layout, null)
    view.tvMessage.text = message
    val toast = Toast(context)
    toast.duration = Toast.LENGTH_SHORT
    toast.view = view
    toast.show()
}

//email format is valid or not
fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

//json data parsing
fun makeJsonParsing(jsonText: String,context : Context): MutableList<Ticket> {
    val ticketDataList = mutableListOf<Ticket>()

    if(!jsonText.isNullOrEmpty()) {
        try {
            val jsonObject = JSONObject(jsonText)
            val ticket = jsonObject.getJSONArray("tickets")

            for(i in (0 until ticket.length())) {
                val tc: JSONObject = ticket.getJSONObject(i)
                val id = tc.getString("id")
                val name = tc.getString("name")
                val price = tc.getString("price")
                val location = tc.getString("location")

                ticketDataList.add(Ticket(id,location,name,price))
            }
        }
        catch (je: JSONException)
        {
            Log.e("Json Error","Json parsing error!")
            Toast.makeText(context,"Json parsing error!",Toast.LENGTH_SHORT).show()
        }
    }
    return ticketDataList
}