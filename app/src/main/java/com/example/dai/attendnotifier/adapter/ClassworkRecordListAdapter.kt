package com.example.dai.attendnotifier.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dai.attendnotifier.R
import com.example.dai.attendnotifier.model.AttendStatusEnum
import com.example.dai.attendnotifier.model.RecordRealmModel
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.classwork_record_list_item.view.*

class ClassworkRecordListAdapter(
    recordList: Array<RecordRealmModel?>,
    private val classworkRecordListListener: ClassworkRecordListListener
) :
    RecyclerView.Adapter<ClassworkRecordListAdapter.ViewHolder>() {

    private var recordList: Array<RecordRealmModel?> = recordList
        set(recordList) {
            field = recordList
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rowView = LayoutInflater.from(parent.context).inflate(R.layout.classwork_record_list_item, parent, false)
        return ViewHolder(
            view = rowView,
            listListener = classworkRecordListListener
        )
    }

    override fun getItemCount(): Int {
        return recordList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindListItem(recordList[position])
    }

    fun getAttendRecord(status: Int): Int {
        var attendRecord = 0
        for (i in 0 until itemCount) {
            if (recordList[i]!!.attendStatus == status) {
                attendRecord++
            }
        }
        return attendRecord
    }


    interface ClassworkRecordListListener {
        fun onClickRow(id: Int, text: TextView)
    }

    class ViewHolder(
        view: View,
        val listListener: ClassworkRecordListListener
    ) :
        RecyclerView.ViewHolder(view) {

        private var realm: Realm

        @SuppressLint("SetTextI18n")
        fun bindListItem(model: RecordRealmModel?) {
            if (model != null) {
                itemView.classwork_number_text.text = "第${adapterPosition + 1}回"
                itemView.classwork_date_text.text = model.dateStr
                itemView.attend_status_spinner.adapter = SpinnerAdapter(context = itemView.context)
                itemView.attend_status_spinner.onItemSelectedListener = null
                itemView.attend_status_spinner.setSelection(model.attendStatus, false)
                setAttendStatusImage(model.attendStatus)

                itemView.setOnClickListener {
                    listListener.onClickRow(model.id, itemView.classwork_date_text)
                }

                itemView.attend_status_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>) {}

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        setAttendStatusImage(position)

                        realm.executeTransaction {
                            val result = realm.where(RecordRealmModel::class.java).equalTo("id", model.id).findFirst()
                            result?.let { result.attendStatus = position }
                        }
                    }
                }
            }
        }

        private fun setAttendStatusImage(status: Int) {
            when (status) {
                AttendStatusEnum.NOT_ATTEND.num -> { //未受講
                    itemView.attend_status_image.setImageResource(R.drawable.gray_circle)
                }
                AttendStatusEnum.ATTENDED.num -> { //出席
                    itemView.attend_status_image.setImageResource(R.drawable.light_green_circle)
                }
                AttendStatusEnum.LATE.num -> { //遅刻
                    itemView.attend_status_image.setImageResource(R.drawable.light_yellow_circle)
                }
                AttendStatusEnum.ABSENT.num -> { //欠席
                    itemView.attend_status_image.setImageResource(R.drawable.red_circle)
                }
                AttendStatusEnum.OTHER.num -> { //その他
                    itemView.attend_status_image.setImageResource(R.drawable.black_circle)
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
