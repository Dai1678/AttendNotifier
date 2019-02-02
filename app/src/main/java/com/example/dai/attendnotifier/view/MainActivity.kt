package com.example.dai.attendnotifier.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.dai.attendnotifier.R
import com.example.dai.attendnotifier.model.ClassworkModel
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), DailyClassworkFragment.DailyClassworkFragmentClickListener {

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(main_bottom_app_bar)

        initRealm()

        //TODO わざわざ最初にデータを作らないようにする
        if (!isRealmDataCreated()) {
            createClassworkData()
        }

        if (savedInstanceState == null) {
            //TODO 今日の曜日を取得して0~6でdateNumberに渡す
            val dailyClassworkFragment = DailyClassworkFragment.newInstance(0)
            supportFragmentManager.beginTransaction()
                .add(R.id.daily_classwork_fragment_container, dailyClassworkFragment, "dailyClassworkFragment").commit()
        }

        main_floating_action_button.setOnClickListener {
            //TODO editActivityにintentしてname,timeを受け取る

            val dailyClassworkFragment =
                supportFragmentManager.findFragmentById(R.id.daily_classwork_fragment_container) as DailyClassworkFragment
            dailyClassworkFragment.insertClasswork("追加")
        }
    }

    private fun initRealm() {
        val realmConfiguration = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(0)
            .build()
        realm = Realm.getInstance(realmConfiguration)
    }

    private fun isRealmDataCreated(): Boolean {
        val result = realm.where(ClassworkModel::class.java).findAll()
        return result.size > 0
    }

    private fun createClassworkData() {
        val defaultClassworkStartTimeArray = resources.getStringArray(R.array.default_classwork_start_time)
        val defaultClassworkEndTimeArray = resources.getStringArray(R.array.default_classwork_end_time)

        for (i in 0 until 6) {  //月~土
            for (j in 0 until 6) {  //1限 ~ 6限
                realm.executeTransaction {
                    val model =
                        it.createObject(ClassworkModel::class.java, UUID.randomUUID().mostSignificantBits.toInt())
                    model.dayOfWeekId = i
                    model.classworkNumberId = j
                    model.timeRange = "${defaultClassworkStartTimeArray[j]} ~ ${defaultClassworkEndTimeArray[j]}"
                }
            }
        }
    }

    override fun headerTextClick() {
        val bottomNavigationDrawerFragment = BottomNavigationDrawerFragment()
        bottomNavigationDrawerFragment.show(supportFragmentManager, "bottomNavigationDrawer")
    }

    override fun subTextClick() {
        //TODO セメスター選択のダイアログ表示
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val bottomNavigationDrawerFragment = BottomNavigationDrawerFragment()
                bottomNavigationDrawerFragment.show(supportFragmentManager, "bottomNavigationDrawer")
            }

            R.id.menu_classwork_time_setting -> {

            }

            R.id.menu_semester_setting -> {

            }
        }
        return true
    }
}
