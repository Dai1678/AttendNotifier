package com.example.dai.attendnotifier.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.dai.attendnotifier.R
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), DailyClassworkFragment.DailyClassworkFragmentClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(main_bottom_app_bar)

        if (savedInstanceState == null) {
            val today = Calendar.getInstance()
            val dateNumber = today.get(Calendar.DAY_OF_WEEK)

            val dailyClassworkFragment = DailyClassworkFragment.newInstance(dateNumber)
            supportFragmentManager.beginTransaction()
                .add(R.id.daily_classwork_fragment_container, dailyClassworkFragment, "dailyClassworkFragment").commit()
        }

        main_floating_action_button.setOnClickListener {
            //TODO editActivityにintentしてnameとtimeRange取得

            val dailyClassworkFragment =
                supportFragmentManager.findFragmentById(R.id.daily_classwork_fragment_container) as DailyClassworkFragment
            dailyClassworkFragment.insertClasswork()
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
