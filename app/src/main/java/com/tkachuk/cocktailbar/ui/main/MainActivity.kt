package com.tkachuk.cocktailbar.ui.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.util.Log
import android.view.MenuItem
import com.tkachuk.cocktailbar.R
import com.tkachuk.cocktailbar.ui.categories.CategoryFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IMainActivity {

    private lateinit var mDrawerLayout: DrawerLayout

    override fun setMainToolbar() {
        Log.d("draxvel", "setMainToolbar")
        setSupportActionBar(main_toolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
            show()
        }
    }

    override fun replaceFragment(fragment: Fragment, addToBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.replace(R.id.frame_layout, fragment)
        transaction.commit()
    }

    override fun setTitleSupportActionBar(string: String) {
        if (string.isEmpty())
            supportActionBar?.title = getString(R.string.categories)
        else supportActionBar?.title = string
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDrawerLayout = drawer_layout

        setMainToolbar()

        val navigationView: NavigationView = nav_view
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_main -> {
                    replaceFragment(MainFragment(), false)
                    setTitleSupportActionBar(getString(R.string.app_name))
                }
                R.id.nav_categories -> {
                    replaceFragment(CategoryFragment(), false)
                    setTitleSupportActionBar(getString(R.string.categories))
                }
            }
            menuItem.isChecked = true
            mDrawerLayout.closeDrawers()
            true
        }
        replaceFragment(MainFragment(), false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                mDrawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}