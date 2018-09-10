package com.tkachuk.cocktailbar.ui.main

import android.support.v4.app.Fragment

interface IMainActivity {
    fun replaceFragment(fragment: Fragment, addToBackStack: Boolean)
    fun setTitleSupportActionBar(string: String)
}