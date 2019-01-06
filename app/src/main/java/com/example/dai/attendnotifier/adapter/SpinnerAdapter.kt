package com.example.dai.attendnotifier.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.dai.attendnotifier.R
import kotlinx.android.synthetic.main.spinner_drop_down_item.view.*
import kotlinx.android.synthetic.main.spinner_selected_item.view.*

class SpinnerAdapter(context: Context) : BaseAdapter() {

    private val spinnerItemsList = context.resources.getStringArray(R.array.attend_status_spinner_item)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View =
            convertView ?: LayoutInflater.from(parent.context).inflate(R.layout.spinner_selected_item, parent, false)

        view.spinner_selected_text.text = spinnerItemsList[position]

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View =
            convertView ?: LayoutInflater.from(parent.context).inflate(R.layout.spinner_drop_down_item, parent, false)

        view.spinner_drop_down_text.text = spinnerItemsList[position]

        when (position) {
            0 -> view.spinner_attend_status_image.setImageResource(R.drawable.gray_circle)
            1 -> view.spinner_attend_status_image.setImageResource(R.drawable.light_green_circle)
            2 -> view.spinner_attend_status_image.setImageResource(R.drawable.light_yellow_circle)
            3 -> view.spinner_attend_status_image.setImageResource(R.drawable.red_circle)
            4 -> view.spinner_attend_status_image.setImageResource(R.drawable.black_circle)
        }

        return view
    }

    override fun getItem(position: Int): Any {
        return spinnerItemsList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return spinnerItemsList.size
    }
}