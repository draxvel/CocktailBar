package com.tkachuk.cocktailbar.ui.favorites

import android.arch.lifecycle.Observer
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tkachuk.cocktailbar.R
import com.tkachuk.cocktailbar.data.repository.DrinkRepository
import com.tkachuk.cocktailbar.databinding.FragmentFavoritesBinding
import com.tkachuk.cocktailbar.ui.drinks.DrinkListViewModel
import com.tkachuk.cocktailbar.ui.fulldrink.FullDrinkActivity
import com.tkachuk.cocktailbar.ui.main.IMainActivity

class FavoritesFragment : Fragment() {

    private lateinit var root: View
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var drinkListViewModel: DrinkListViewModel
    private lateinit var iMainActivity: IMainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("draxvel", "CategoryFragment - onCreateView")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)
        root = binding.root

        iMainActivity = activity as IMainActivity
        iMainActivity.setMainToolbar()

        drinkListViewModel = DrinkListViewModel(DrinkRepository(activity!!.applicationContext))
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.drinkList.layoutManager = linearLayoutManager

        setList()

        drinkListViewModel.clickedDrinkId.observe(this, Observer { clickedDrinkId ->
            Log.d("draxvel", "observe click")
            val intent = Intent(activity, FullDrinkActivity::class.java)
            intent.putExtra("id", clickedDrinkId)
            startActivity(intent)
        })

        binding.drinkListViewModel = drinkListViewModel
        return root
    }

    override fun onResume() {
        super.onResume()
        setList()
    }

    private fun setList() {
        binding.drinkList.visibility = View.INVISIBLE

        if (drinkListViewModel.loadFavorites()) {
            binding.drinkList.visibility = View.VISIBLE
        }
    }
}