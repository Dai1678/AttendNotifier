package com.example.dai.attendnotifier.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dai.attendnotifier.R
import com.example.dai.attendnotifier.model.ClassworkModel
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.daily_class_list_item.view.*

class DailyClassListAdapter(
    classworkList: Array<ClassworkModel?>,
    private val dailyClassListAdapterListener: DailyClassListListener
) :
    RecyclerView.Adapter<DailyClassListAdapter.ViewHolder>() {

    private var classworkList: Array<ClassworkModel?> = classworkList
        set(classworkList) {
            field = classworkList
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView = LayoutInflater.from(parent.context).inflate(R.layout.daily_class_list_item, parent, false)
        return ViewHolder(
            view = rowView,
            dailyClassListAdapter = dailyClassListAdapterListener
        )
    }

    override fun getItemCount(): Int {
        return classworkList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindListItem(classworkList[position])
    }

    interface DailyClassListListener {
        fun onClickRow(classworkName: String, id: Int)
        fun onClickCheckBox(id: Int)
    }

    class ViewHolder(view: View, val dailyClassListAdapter: DailyClassListListener) :
        RecyclerView.ViewHolder(view) {

        private val icons = arrayOf(
            R.mipmap.classwork_1_image,
            R.mipmap.classwork_2_image,
            R.mipmap.classwork_3_image,
            R.mipmap.classwork_4_image,
            R.mipmap.classwork_5_image,
            R.mipmap.classwork_6_image
        )

        private var realm: Realm

        @SuppressLint("SetTextI18n")
        fun bindListItem(model: ClassworkModel?) {
            if (model != null) {
                itemView.classwork_number_image.setImageResource(icons[adapterPosition])
                itemView.classwork_name_text.text = model.classworkName
                itemView.notify_checkbox.isChecked = model.isNotify
                itemView.attend_time_text.text = "出席数: ${model.attendedRecord}"
                itemView.late_time_text.text = "遅刻数: ${model.lateRecord}"
                itemView.absent_time_text.text = "欠席数: ${model.absentRecord}"
                itemView.classwork_time_range_text.text = model.timeRange

                itemView.setOnClickListener {
                    dailyClassListAdapter.onClickRow(
                        itemView.classwork_name_text.text.toString(),
                        model.id
                    )
                }

                itemView.notify_checkbox.setOnClickListener {
                    //TODO 通知on/offの切り替え


                    //DB登録
                    dailyClassListAdapter.onClickCheckBox(model.id)
                }

            } else {
                itemView.classwork_name_text.text = "授業はありません"
                itemView.notify_checkbox.visibility = View.INVISIBLE
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