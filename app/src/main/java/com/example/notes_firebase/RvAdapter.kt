package com.example.notes_firebase

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

class RvAdapter(var mList:ArrayList<Model>,var context:Context):
    RecyclerView.Adapter<RvAdapter.ViewHolder>() {

    fun searchListFun(searchList: ArrayList<Model>) {

       mList = searchList
        notifyDataSetChanged()

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var raw_name_tv = itemView.findViewById<TextView>(R.id.raw_name_tv)
        var raw__email_tv = itemView.findViewById<TextView>(R.id.raw_email_tv)
        var raw_date_tv = itemView.findViewById<TextView>(R.id.raw_date_tv)
        var card_raw = itemView.findViewById<CardView>(R.id.card_raw)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var v = LayoutInflater.from(context).inflate(R.layout.rv_raw,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {

        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var mData = mList.get(position)

      //  val rnd = Random
      //  val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))

        holder.raw_name_tv.text = mData.name
        holder.raw__email_tv.text = mData.email
        holder.raw_date_tv.text= mData.password.toString()

        var color:Int = colorCode()
        holder.card_raw.setCardBackgroundColor(holder.itemView.resources.getColor(colorCode(),null))

       holder.card_raw.setOnClickListener {

           var i = Intent(context,UpdateNotesActivity::class.java)
           i.putExtra("name",mData.name.toString())
           i.putExtra("notes",mData.email.toString())
           i.putExtra("id",mData.id.toString())
           context.startActivity(i)

       }


    }

    fun colorCode(): Int {

        var arr = arrayOf(
            R.color.color1,
            R.color.color2,
            R.color.color3,
            R.color.color4,
            R.color.color5
        )

        var ran = Random.nextInt(arr.size)
        return arr[ran]

//        var colorList = ArrayList<Int>()
//        colorList.add(R.color.color1)
//        colorList.add(R.color.color2)
//        colorList.add(R.color.color3)
//        colorList.add(R.color.color4)
//        colorList.add(R.color.color5)
//
//        var ran = Random
//        var ranColor = ran.nextInt(colorList.size)
//        return colorList[ranColor]


    }


}