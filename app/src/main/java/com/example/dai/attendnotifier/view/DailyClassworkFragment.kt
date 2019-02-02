package com.example.dai.attendnotifier.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dai.attendnotifier.R
import com.example.dai.attendnotifier.adapter.DailyClassListAdapter
import com.example.dai.attendnotifier.model.ClassworkModel
import com.example.dai.attendnotifier.model.RecordRealmModel
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.fragment_daily_classwork.*
import kotlinx.android.synthetic.main.fragment_daily_classwork.view.*
import java.util.*

class DailyClassworkFragment : Fragment(), View.OnClickListener {

    private lateinit var realm: Realm
    private lateinit var clickListener: DailyClassworkFragmentClickListener
    private val classworkDataArray = arrayOfNulls<ClassworkModel?>(CLASSWORK_NUMBER_SIZE)
    private lateinit var dailyClassListAdapter: DailyClassListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_daily_classwork, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRealm()

        setHeaderText()

        dailyClassListAdapter = DailyClassListAdapter(
            classworkDataArray,
            object : DailyClassListAdapter.DailyClassListListener {
                override fun onClickCheckBox(id: Int) {
                    this@DailyClassworkFragment.setNotifyStatusChanged(id)
                }

                override fun onClickRow(classworkName: String, id: Int) {
                    this@DailyClassworkFragment.intentClassworkEdit(classworkName, id)
                }
            })

        with(view) {
            daily_class_list_view.apply {
                adapter = dailyClassListAdapter
                layoutManager = LinearLayoutManager(context)

                val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
                addItemDecoration(dividerItemDecoration)
            }
        }

        fragment_daily_classwork_header_text.setOnClickListener(this)
        fragment_daily_classwork_sub_text.setOnClickListener(this)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            clickListener = context as DailyClassworkFragmentClickListener
        } catch (e: java.lang.ClassCastException) {
            throw ClassCastException(activity!!.toString() + "must implement DailyClassworkFragmentClickListener.")
        }

    }

    override fun onResume() {
        super.onResume()
        Log.d("onResume", "onResume")

        val pageNumber = arguments!!.getInt(DATE_NUMBER, 0)

        for (i in 0 until classworkDataArray.size) {
            classworkDataArray[i] = getClassworkData(pageNumber, i)
        }

        dailyClassListAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    private fun getClassworkData(pageNumber: Int, classworkNumber: Int): ClassworkModel? {
        //曜日あたりの授業数取得
        return realm.where(ClassworkModel::class.java).equalTo("dayOfWeekId", pageNumber)
            .equalTo("classworkNumberId", classworkNumber).findFirst()
    }

    private fun isRecordDataCreated(id: Int): Boolean {
        val result = realm.where(RecordRealmModel::class.java).equalTo("recordId", id).findFirst()
        return result != null
    }

    private fun createRecordData(recordId: Int) {
        for (i in 0 until ClassworkEditActivity.CLASSWORK_TIME_SIZE) {
            realm.executeTransaction {
                val model = it.createObject(RecordRealmModel::class.java, UUID.randomUUID().mostSignificantBits.toInt())
                model.recordId = recordId
                model.classworkTimeId = i
            }
        }
    }

    private fun intentClassworkEdit(classworkName: String, id: Int) {
        if (!isRecordDataCreated(id)) {
            createRecordData(id)
        }

        val intent = Intent(activity, ClassworkEditActivity::class.java)
        intent.putExtra(CLASSWORK_NAME, classworkName)
        intent.putExtra(CLASSWORK_ID, id)
        startActivity(intent)
    }

    private fun setNotifyStatusChanged(id: Int) {
        realm.executeTransaction {
            val model = it.where(ClassworkModel::class.java).equalTo("id", id).findFirst()
            if (model != null) {
                model.isNotify = !model.isNotify
            }
        }
    }

    private fun initRealm() {
        val realmConfiguration = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(0)
            .build()
        realm = Realm.getInstance(realmConfiguration)
    }

    private fun setHeaderText() {
        val pageNumber = arguments!!.getInt(DATE_NUMBER, 0)

        fragment_daily_classwork_header_text.apply {
            when(pageNumber){
                0 -> text = resources.getText(R.string.common_monday)
                1 -> text = resources.getText(R.string.common_tuesday)
                2 -> text = resources.getText(R.string.common_wednesday)
                3 -> text = resources.getText(R.string.common_thursday)
                4 -> text = resources.getText(R.string.common_friday)
                5 -> text = resources.getText(R.string.common_saturday)
                6 -> text = resources.getText(R.string.common_sunday)
            }
        }
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.fragment_daily_classwork_header_text -> {
                clickListener.headerTextClick()
            }

            R.id.fragment_daily_classwork_sub_text -> {
                clickListener.subTextClick()
            }
        }
    }

    interface DailyClassworkFragmentClickListener {
        fun headerTextClick()
        fun subTextClick()
    }

    companion object {
        private const val DATE_NUMBER = "DATE_NUMBER"
        private const val CLASSWORK_NUMBER_SIZE = 6
        const val CLASSWORK_NAME = "CLASSWORK_NAME"
        const val CLASSWORK_ID = "CLASSWORK_ID"

        fun newInstance(dateNumber: Int): DailyClassworkFragment {
            val dailyClassWorkFragment = DailyClassworkFragment()
            val bundle = Bundle()
            bundle.putInt(DATE_NUMBER, dateNumber)
            dailyClassWorkFragment.arguments = bundle
            return dailyClassWorkFragment
        }
    }

}