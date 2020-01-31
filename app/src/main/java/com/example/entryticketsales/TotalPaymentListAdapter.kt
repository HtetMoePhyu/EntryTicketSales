package com.example.entryticketsales

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.entryticketsales.vos.Ticket
import kotlinx.android.synthetic.main.activity_total_payment.view.*
import kotlinx.android.synthetic.main.row_payment_item_view.view.*

class TotalPaymentListAdapter(private val list : List<Ticket>) :RecyclerView.Adapter<TotalPaymentListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_payment_item_view,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        if(!list.isEmpty())
            return  list.size
        else
            return 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item,position)
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(obj : Ticket,position: Int){
            itemView.tvNo.text       = (position+1).toString()
            itemView.tvLocation.text = obj.location
            itemView.tvCost.text     = obj.price
        }
    }
}