package com.tkachuk.cocktailbar.ui.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import com.tkachuk.cocktailbar.R
import com.tkachuk.cocktailbar.ui.categories.CategoryFragment

class MainActivity : AppCompatActivity(), IMainActivity {

    private lateinit var mDrawerLayout: DrawerLayout

    override fun replaceFragment(fragment: Fragment, addToBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }

    override fun setTitleSupportActionBar(string: String) {
        if (string.isEmpty()) supportActionBar?.title = getString(R.string.app_name)
        else supportActionBar?.title = string
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDrawerLayout = findViewById(R.id.drawer_layout)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_main -> replaceFragment(MainFragment(), false)
                R.id.nav_categories -> replaceFragment(CategoryFragment(), false)
            }
            menuItem.isChecked = true
            mDrawerLayout.closeDrawers()
            true
        }
        replaceFragment(MainFragment(), false)
    }
}