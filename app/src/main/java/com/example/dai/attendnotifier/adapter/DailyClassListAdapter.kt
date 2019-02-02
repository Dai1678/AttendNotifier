package com.example.dai.attendnotifier.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dai.attendnotifier.R
import com.example.dai.attendnotifier.model.ClassworkModel
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.daily_class_list_item.view.*

class DailyClassListAdapter(
    classworkList: ArrayList<ClassworkModel?>,
    private val dailyClassListAdapterListener: DailyClassListListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val EMPTY_VIEW = 10
    }

    private var classworkList: ArrayList<ClassworkModel?> = classworkList
        set(classworkList) {
            field = classworkList
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        Log.d("empty", itemCount.toString())
        if (viewType == EMPTY_VIEW) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.empty_recycler_view, parent, false)
            parent.isFocusable = false
            return EmptyViewHolder(view)
        }

        view = LayoutInflater.from(parent.context).inflate(R.layout.daily_class_list_item, parent, false)
        return ViewHolder(
            view = view,
            dailyClassListAdapter = dailyClassListAdapterListener
        )
    }

    override fun getItemCount(): Int {
        return if (classworkList.size > 0) classworkList.size else 1
    }

    override fun getItemViewType(position: Int): Int {
        if (classworkList.size == 0) {
            Log.d("empty", "this is empty")
            return EMPTY_VIEW
        }
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bindListItem(classworkList[position])
        } else if (holder is EmptyViewHolder) {
            holder.bindItem()
        }
    }

    interface DailyClassListListener {
        fun onClickRow(classworkName: String, id: Int)
        fun onClickCheckBox(id: Int)
    }

    class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItem() {
            super.itemView
        }
    }

    class ViewHolder(view: View, val dailyClassListAdapter: DailyClassListListener) :
        RecyclerView.ViewHolder(view) {

        //TODO 10個くらい用意しとく
        private val icons = arrayOf(
            R.mipmap.classwork_number1_image,
            R.mipmap.classwork_number2_image,
            R.mipmap.classwork_number3_image,
            R.mipmap.classwork_number4_image,
            R.mipmap.classwork_number5_image,
            R.mipmap.classwork_number6_image,
            R.mipmap.classwork_number6_image
        )

        private var realm: Realm

        @SuppressLint("SetTextI18n")
        fun bindListItem(model: ClassworkModel?) {
            if (model != null) {
                itemView.apply {
                    classwork_number_image.setImageResource(icons[adapterPosition])
                    notify_checkbox.isChecked = model.isNotify
                    classwork_time_range_text.text = model.timeRange
                    attend_time_text.text = "出席数: ${model.attendedRecord}"
                    late_time_text.text = "遅刻数: ${model.lateRecord}"
                    absent_time_text.text = "欠席数: ${model.absentRecord}"
                }

                setLayoutParam(itemView, model.classworkName)

                itemView.setOnClickListener {
                    dailyClassListAdapter.onClickRow(
                        itemView.classwork_name_text.text.toString(),
                        model.id
                    )
                }

                itemView.notify_checkbox.setOnClickListener {
                    //授業なしレイアウトとの切り替え
                    setLayoutParam(itemView, model.classworkName)

                    //TODO 通知on/offの切り替え


                    //DB登録
                    dailyClassListAdapter.onClickCheckBox(model.id)
                }

            } else {
                itemView.apply {
                    classwork_name_text.text = "授業はありません"
                    notify_checkbox.visibility = View.INVISIBLE
                }
            }
        }

        private fun setLayoutParam(itemView: View, classworkName: String){
            if (itemView.notify_checkbox.isChecked) {
                itemView.apply {
                    classwork_name_text.text = classworkName
                    attend_time_text.visibility = View.VISIBLE
                    late_time_text.visibility = View.VISIBLE
                    absent_time_text.visibility = View.VISIBLE
                }
            } else {
                itemView.apply {
                    classwork_name_text.text = "授業はありません"
                    attend_time_text.visibility = View.INVISIBLE
                    late_time_text.visibility = View.INVISIBLE
                    absent_time_text.visibility = View.INVISIBLE
                }
            }
        }

        init {
            val realmConfiguration = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(0)
                .build()
            realm = Realm.getInstance(realmConfiguration)


        }
    }
}