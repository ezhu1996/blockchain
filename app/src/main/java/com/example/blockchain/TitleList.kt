package com.example.blockchain

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class TitleList(private val context: Activity, internal var titles: List<Title>) : ArrayAdapter<Title>(context, R.layout.layout_author_list, titles) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.layout_author_list, null, true)

        val textViewName = listViewItem.findViewById<View>(R.id.textViewName) as TextView
        val textViewRating = listViewItem.findViewById<View>(R.id.textViewCountry) as TextView

        val title = titles[position]
        textViewName.text = title.titleName
        textViewRating.text = title.rating.toString()

        return listViewItem
    }
}
