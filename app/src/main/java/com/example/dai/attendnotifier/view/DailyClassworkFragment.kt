package com.example.dai.attendnotifier.view

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dai.attendnotifier.R
import com.example.dai.attendnotifier.adapter.DailyClassListAdapter
import com.example.dai.attendnotifier.model.ClassworkModel
import com.example.dai.attendnotifier.model.RecordRealmModel
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.daily_class_list_item.*
import kotlinx.android.synthetic.main.fragment_daily_classwork.*
import kotlinx.android.synthetic.main.fragment_daily_classwork.view.*
import java.util.*

class DailyClassworkFragment : Fragment(), View.OnClickListener {

    private lateinit var realm: Realm
    private lateinit var clickListener: DailyClassworkFragmentClickListener
    private val classworkDataArray = arrayListOf<ClassworkModel?>()
    private lateinit var dailyClassListAdapter: DailyClassListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_daily_classwork, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRealm()

        setHeaderText()

        val itemTouchHelper =
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.ACTION_STATE_IDLE) {

                override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                    if (viewHolder is DailyClassListAdapter.ViewHolder) {
                        return ItemTouchHelper.Callback.makeMovementFlags(
                            0,
                            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                        )
                    }
                    return super.getMovementFlags(recyclerView, viewHolder)
                }

                override fun onMove(
                    view: RecyclerView,
                    holder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(view: RecyclerView.ViewHolder, direction: Int) {
                    if (view is DailyClassListAdapter.ViewHolder) {
                        val swipedPosition = view.adapterPosition
                        removeClasswork(swipedPosition)
                    }
                }
            })

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
            itemTouchHelper.attachToRecyclerView(daily_classwork_list_view)
            daily_classwork_list_view.apply {
                adapter = dailyClassListAdapter
                layoutManager = LinearLayoutManager(context)

                val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
                addItemDecoration(dividerItemDecoration)

                addItemDecoration(itemTouchHelper)
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

        val dateNumber = arguments!!.getInt(DATE_NUMBER, Calendar.MONDAY)

        classworkDataArray.clear()
        for (i in 0 until getClassworkDataSize(dateNumber)) {
            classworkDataArray.add(getClassworkData(dateNumber, i + 1))
        }

        dailyClassListAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    private fun intentClassworkEdit(classworkName: String, id: Int) {
        if (!isRecordDataCreated(id)) {
            createRecordData(id)
        }

        val intent = Intent(activity, ClassworkEditActivity::class.java)
        intent.putExtra(CLASSWORK_NAME, classworkName)
        intent.putExtra(CLASSWORK_ID, id)
        startActivity(intent)
        //startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity, daily_classwork_list_item, "list_item").toBundle())
    }

    private fun setHeaderText() {
        val dateNumber = arguments!!.getInt(DATE_NUMBER, Calendar.MONDAY)

        fragment_daily_classwork_header_text.apply {
            when (dateNumber) {
                Calendar.SUNDAY -> text = resources.getText(R.string.common_sunday)
                Calendar.MONDAY -> text = resources.getText(R.string.common_monday)
                Calendar.TUESDAY -> text = resources.getText(R.string.common_tuesday)
                Calendar.WEDNESDAY -> text = resources.getText(R.string.common_wednesday)
                Calendar.THURSDAY -> text = resources.getText(R.string.common_thursday)
                Calendar.FRIDAY -> text = resources.getText(R.string.common_friday)
                Calendar.SATURDAY -> text = resources.getText(R.string.common_saturday)
            }
        }
    }

    private fun setSubText() {
        //TODO 表示中のセメスターをテキスト表示
//        fragment_daily_classwork_sub_text.apply {
//            text
//        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.fragment_daily_classwork_header_text -> {
                clickListener.headerTextClick()
            }

            R.id.fragment_daily_classwork_sub_text -> {
                clickListener.subTextClick()
            }
        }
    }

    fun insertClasswork(name: String) {
        if (classworkDataArray.size < MAX_CLASSWORK_SIZE) {
            val model = createClassworkData()
            classworkDataArray.add(model)
            dailyClassListAdapter.notifyItemInserted(classworkDataArray.size)
            daily_classwork_list_view.smoothScrollToPosition(classworkDataArray.size)
        } else {
            showWarning()
        }
    }

    private fun removeClasswork(position: Int) {
        val targetModel = classworkDataArray[position]
        targetModel?.let {
            deleteClassworkData(it)
            classworkDataArray.removeAt(position)
            updateClassworkNumber()
            dailyClassListAdapter.notifyDataSetChanged()
        }
    }

    private fun showWarning() {
        val marginSide = 24
        val marginBottom = 230  //FIXME 端末依存ありそう
        val snackBar = Snackbar.make(
            activity!!.bottom_app_bar_layout,
            "これ以上作成できません",
            Snackbar.LENGTH_LONG
        ).setAction("OK") { }

        val snackBarView = snackBar.view
        val params = snackBarView.layoutParams as CoordinatorLayout.LayoutParams

        params.setMargins(
            params.leftMargin + marginSide,
            params.topMargin,
            params.rightMargin + marginSide,
            params.bottomMargin + marginBottom
        )

        snackBarView.layoutParams = params
        snackBar.show()
    }

    interface DailyClassworkFragmentClickListener {
        fun headerTextClick()
        fun subTextClick()
    }

    /*--------- Realm -------------*/
    private fun initRealm() {
        val realmConfiguration = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(0)
            .build()
        realm = Realm.getInstance(realmConfiguration)
    }

    private fun createClassworkData(): ClassworkModel {
        val dateNumber = arguments!!.getInt(DATE_NUMBER, Calendar.MONDAY)
        var model = ClassworkModel()

        realm.executeTransaction {
            model = it.createObject(ClassworkModel::class.java, UUID.randomUUID().mostSignificantBits.toInt())
            model.apply {
                dayOfWeekNumber = dateNumber
                classworkNumber = classworkDataArray.size + 1
                //classworkName = name //FIXME
                //timeRange = ""
            }
        }
        return model
    }

    //曜日あたりの授業取得
    private fun getClassworkData(dateNumber: Int, classworkNumber: Int): ClassworkModel? {
        return realm.where(ClassworkModel::class.java).equalTo("dayOfWeekNumber", dateNumber)
            .equalTo("classworkNumber", classworkNumber).findFirst()
    }

    //曜日あたりの授業数取得
    private fun getClassworkDataSize(dateNumber: Int): Int {
        return realm.where(ClassworkModel::class.java).equalTo("dayOfWeekNumber", dateNumber).findAll().size
    }

    //授業データの削除
    private fun deleteClassworkData(targetModel: ClassworkModel) {
        val targetId = targetModel.id

        realm.executeTransaction {
            val result = realm.where(ClassworkModel::class.java).equalTo("id", targetId).findFirst()
            result?.deleteFromRealm()
        }
    }

    //授業時限の更新
    private fun updateClassworkNumber() {
        realm.executeTransaction {
            for (i in 0 until classworkDataArray.size) {
                val result =
                    realm.where(ClassworkModel::class.java).equalTo("id", classworkDataArray[i]!!.id).findFirst()
                result?.classworkNumber = i + 1
            }
        }
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

    private fun setNotifyStatusChanged(id: Int) {
        realm.executeTransaction {
            val model = it.where(ClassworkModel::class.java).equalTo("id", id).findFirst()
            model?.let { model.isNotify = !(model.isNotify) }
        }
    }
    /*--------- Realm -------------*/

    companion object {
        private const val DATE_NUMBER = "DATE_NUMBER"
        private const val MAX_CLASSWORK_SIZE = 10
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
