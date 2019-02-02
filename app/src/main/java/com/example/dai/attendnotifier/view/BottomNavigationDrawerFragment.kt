package com.example.dai.attendnotifier.view


import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dai.attendnotifier.R
import kotlinx.android.synthetic.main.fragment_bottom_navigation_drawer.*

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
            var dateNumber = 0
            when (it.itemId) {
                R.id.menu_monday -> dateNumber = 0
                R.id.menu_tuesday -> dateNumber = 1
                R.id.menu_wednesday -> dateNumber = 2
                R.id.menu_thursday -> dateNumber = 3
                R.id.menu_friday -> dateNumber = 4
                R.id.menu_saturday -> dateNumber = 5
                //R.id.menu_sunday -> dateNumber = 6
            }

            val dailyClassworkFragment = DailyClassworkFragment.newInstance(dateNumber)
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.daily_classwork_fragment_container, dailyClassworkFragment).commit()

            this.dismiss()

            true
        }
    }


}
