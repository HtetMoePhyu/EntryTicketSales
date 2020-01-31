package com.example.entryticketsales

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.entryticketsales.vos.Ticket
import kotlinx.android.synthetic.main.row_ticket_item_view.view.*

class ItemListAdapter(private val ticketInfoList : MutableList<Ticket>,
                      private var listener: OnItemClickListener): RecyclerView.Adapter<ItemListAdapter.ViewHolder>(){


    private var selectedPosition = -1
    //private var deselectPosition = -1
   // private var deselectCondi = true

    private var isSelected = false

    interface OnItemClickListener {
        fun onItemClick(item : Ticket,position: Int,isSelected : Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.row_ticket_item_view,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ticketInfoList.size
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //val condition = selectedPosition == position
        val context = holder.itemView.context

        val ticketInfo = ticketInfoList[position]
        val id = ticketInfoList[position].id.toInt()

        holder.bind(ticketInfo)

        holder.itemView.setOnClickListener {
            listener.onItemClick(ticketInfo, position, isSelected)
            holder.itemView.isSelected = true
        }
    }

    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        fun bind(obj : Ticket){
            itemView.tvLocationName.text = obj.location
            itemView.tvTicketPrice.text  = obj.price
        }
    }

//    fun setSelectedIds(selectedIdList: List<Int>) {
//        this.selectedIdList = selectedIdList
//        notifyDataSetChanged()
//    }

}

//            holder.itemView.layoutTicketRow.setBackgroundColor(context.resources.getColor(R.color.selected_color))

//            if (selectedPosition >= 0) {
//notifyItemChanged(selectedPosition)
//notifyItemChanged(deselectPosition)
//            }

//            deselectPosition = selectedPosition
//selectedPosition = holder.adapterPosition
// notifyItemChanged(selectedPosition)
//            notifyItemChanged(deselectPosition)
//}

//        holder.itemView.isSelected = condition
//        if(condition){
//            holder.itemView.layoutTicketRow.setBackgroundColor(context.resources.getColor(R.color.selected_color))
//        }
//else{
//   holder.itemView.layoutTicketRow.setBackgroundColor(context.resources.getColor(android.R.color.white))
//}

//        if ((selectedPosition == position) && (selectedPosition == deselectPosition) && (deselectPosition == position)) {
//            if (deselectCondi) {
//                deselectCondi = false
//                holder.itemView.layoutTicketRow.isSelected = false
//                holder.itemView.layoutTicketRow.setBackgroundColor(context.resources.getColor(R.color.white))
//
//            }
//            else {
//                deselectCondi = true
//                holder.itemView.layoutTicketRow.isSelected = true
//                holder.itemView.layoutTicketRow.setBackgroundColor(context.resources.getColor(R.color.selected_color))
//            }
//        }
