package com.example.dai.attendnotifier.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.dai.attendnotifier.R
import com.example.dai.attendnotifier.adapter.ViewPagerAdapter
import com.example.dai.attendnotifier.model.ClassworkModel
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRealm()

        if (!isRealmDataCreated()) {
            createClassworkData()
        }

        if (savedInstanceState == null) {
            val pagerAdapter = ViewPagerAdapter(supportFragmentManager, this)
            viewpager.adapter = pagerAdapter
            tab_layout.setupWithViewPager(viewpager)
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

        for (i in 0 until 6) {
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
}
