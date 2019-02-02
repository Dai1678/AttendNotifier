package com.example.dai.attendnotifier.view


import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.TextView
import com.example.dai.attendnotifier.R
import com.example.dai.attendnotifier.adapter.ClassworkRecordListAdapter
import com.example.dai.attendnotifier.model.AttendStatusEnum
import com.example.dai.attendnotifier.model.ClassworkModel
import com.example.dai.attendnotifier.model.RecordRealmModel
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_classwork_edit.*
import java.util.*

class ClassworkEditActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var realm: Realm
    private lateinit var classworkRecordListAdapter: ClassworkRecordListAdapter
    private var clickRowId = 0
    private lateinit var clickRowText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classwork_edit)

        initRealm()

        setSupportActionBar(activity_classwork_edit_toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = intent.getStringExtra(DailyClassworkFragment.CLASSWORK_NAME)
        }


        val classworkName = intent.getStringExtra(DailyClassworkFragment.CLASSWORK_NAME)
        classwork_name_edit.apply {
            setText(classworkName)
            setSelection(classwork_name_edit.text.length)
        }

        val recordId = intent.getIntExtra(DailyClassworkFragment.CLASSWORK_ID, 0)
        val recordDataArray = arrayOfNulls<RecordRealmModel?>(CLASSWORK_TIME_SIZE)
        for (i in 0 until recordDataArray.size) {
            recordDataArray[i] = getRecordData(recordId, i)
        }

        classworkRecordListAdapter = ClassworkRecordListAdapter(
            recordDataArray,
            object : ClassworkRecordListAdapter.ClassworkRecordListListener {
                override fun onClickRow(id: Int, text: TextView) {
                    this@ClassworkEditActivity.onClickRow(id, text)
                }
            }
        )

        classwork_record_list_view.apply {
            adapter = classworkRecordListAdapter

            layoutManager = LinearLayoutManager(context)

            val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            addItemDecoration(dividerItemDecoration)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    private fun getRecordData(recordId: Int, classworkTimeNumber: Int): RecordRealmModel? {
        //授業あたりの出席記録取得
        return realm.where(RecordRealmModel::class.java).equalTo("recordId", recordId)
            .equalTo("classworkTimeId", classworkTimeNumber).findFirst()
    }

    private fun onClickRow(id: Int, text: TextView) {
        clickRowId = id
        clickRowText = text
        val datePickDialog = DatePickDialog()
        datePickDialog.show(supportFragmentManager, DATE_PICKER_DIALOG_TAG)
    }

    private fun saveClassworkInfo() {
        val classworkId = intent.getIntExtra(DailyClassworkFragment.CLASSWORK_ID, 0)

        val classworkName = classwork_name_edit.text.toString()

        //TODO 変更ない場合は更新処理しない
        realm.executeTransaction {
            val classworkModel = it.where(ClassworkModel::class.java).equalTo("id", classworkId).findFirst()

            if (classworkModel != null) {
                classworkModel.classworkName = classworkName
                classworkModel.notAttendRecord =
                        classworkRecordListAdapter.getAttendRecord(AttendStatusEnum.NOT_ATTEND.num)
                classworkModel.attendedRecord =
                        classworkRecordListAdapter.getAttendRecord(AttendStatusEnum.ATTENDED.num)
                classworkModel.lateRecord = classworkRecordListAdapter.getAttendRecord(AttendStatusEnum.LATE.num)
                classworkModel.absentRecord = classworkRecordListAdapter.getAttendRecord(AttendStatusEnum.ABSENT.num)
                classworkModel.otherRecord = classworkRecordListAdapter.getAttendRecord(AttendStatusEnum.OTHER.num)

                Log.d("classworkName", classworkName)
                Log.d(
                    "attendRecord",
                    "出席数: ${classworkModel.attendedRecord} 遅刻数: ${classworkModel.lateRecord} 欠席数: ${classworkModel.absentRecord}"
                )
            }
        }

    }

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val selectDateString = String.format(Locale.JAPAN, "%d年%d月%d日", year, monthOfYear + 1, dayOfMonth)
        clickRowText.text = selectDateString

        realm.executeTransaction {
            val result = realm.where(RecordRealmModel::class.java).equalTo("id", clickRowId).findFirst()
            if (result != null) {
                result.dateStr = selectDateString
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //TODO 端末のバックキーでも保存処理させる
            android.R.id.home -> {
                saveClassworkInfo()
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initRealm() {
        val realmConfiguration = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(0)
            .build()
        realm = Realm.getInstance(realmConfiguration)
    }

    companion object {
        private const val DATE_PICKER_DIALOG_TAG = "DATE_PICKER"
        const val CLASSWORK_TIME_SIZE = 14

    }

}
