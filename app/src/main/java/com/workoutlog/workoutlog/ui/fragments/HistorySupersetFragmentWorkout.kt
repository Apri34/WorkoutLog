package com.workoutlog.workoutlog.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.workoutlog.workoutlog.R
import com.workoutlog.workoutlog.database.AppDatabase
import com.workoutlog.workoutlog.database.DatabaseInitializer

class HistorySupersetFragmentWorkout: Fragment() {

    companion object {
        private const val KEY_EXC1 = "exc1"
        private const val KEY_EXC2 = "exc2"
        private const val NUM_PAGES = 2

        fun newInstance(exc1: Int, exc2: Int): HistorySupersetFragmentWorkout {
            val fragment = HistorySupersetFragmentWorkout()
            val args = Bundle()
            args.putInt(KEY_EXC1, exc1)
            args.putInt(KEY_EXC2, exc2)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var excName1: String
    private lateinit var excName2: String

    private lateinit var dbInitializer: DatabaseInitializer
    private lateinit var database: AppDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_superset_history_workout, container, false)

        viewPager = view.findViewById(R.id.view_pager_history_fragment_superset)
        tabLayout = view.findViewById(R.id.tab_layout_history_fragment_superset)

        dbInitializer = DatabaseInitializer.getInstance(context)
        database = AppDatabase.getInstance(context)
        excName1 = dbInitializer.getExerciseNameById(database.exerciseDao(), arguments!!.getInt(KEY_EXC1))
        excName2 = dbInitializer.getExerciseNameById(database.exerciseDao(), arguments!!.getInt(KEY_EXC2))

        val pagerAdapter = PagerAdapter(arguments!!.getInt(KEY_EXC1), arguments!!.getInt(KEY_EXC2), childFragmentManager)
        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)

        return view
    }

    inner class PagerAdapter(private val exc1: Int, private val exc2: Int, fm: FragmentManager): FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            return when(position) {
                0 -> HistoryFragmentWorkout.newInstance(exc1)
                1 -> HistoryFragmentWorkout.newInstance(exc2)
                else -> Fragment()
            }
        }

        override fun getCount() = NUM_PAGES

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> excName1
                1 -> excName2
                else -> ""
            }
        }
    }
}