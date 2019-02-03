package com.example.dai.attendnotifier.view


import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dai.attendnotifier.R
import kotlinx.android.synthetic.main.fragment_bottom_navigation_drawer.*
import java.util.*

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_navigation_drawer, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bottom_navigation_view_finish_button.setOnClickListener {
            this.dismiss()
        }

        bottom_navigation_view.setNavigationItemSelectedListener {
            var dateNumber = Calendar.MONDAY
            when (it.itemId) {
                R.id.menu_sunday -> dateNumber = Calendar.SUNDAY
                R.id.menu_monday -> dateNumber = Calendar.MONDAY
                R.id.menu_tuesday -> dateNumber = Calendar.TUESDAY
                R.id.menu_wednesday -> dateNumber = Calendar.WEDNESDAY
                R.id.menu_thursday -> dateNumber = Calendar.THURSDAY
                R.id.menu_friday -> dateNumber = Calendar.FRIDAY
                R.id.menu_saturday -> dateNumber = Calendar.SATURDAY
            }

            val dailyClassworkFragment = DailyClassworkFragment.newInstance(dateNumber)
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.daily_classwork_fragment_container, dailyClassworkFragment).commit()

            this.dismiss()

            true
        }
    }


}
