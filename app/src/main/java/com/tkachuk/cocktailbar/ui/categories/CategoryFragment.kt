package com.tkachuk.cocktailbar.ui.categories

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tkachuk.cocktailbar.R
import com.tkachuk.cocktailbar.databinding.FragmentCategoryBinding
import com.tkachuk.cocktailbar.ui.main.IMainActivity

class CategoryFragment : Fragment() {

    private lateinit var root: View
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var categoryListViewModel: CategoryListViewModel
    private var errorSnackBar: Snackbar? = null
    private lateinit var iMainActivity: IMainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("draxvel", "CategoryFragment - onCreateView")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false)
        root = binding.root

        iMainActivity = activity as IMainActivity
        iMainActivity.setMainToolbar()

        categoryListViewModel = CategoryListViewModel(activity!!)
        binding.categoriesList.layoutManager = GridLayoutManager(activity, 2)
        categoryListViewModel.loadCategories()

        categoryListViewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage, categoryListViewModel.errorClickListener) else hideError()
        })

        binding.categoryListViewModel = categoryListViewModel

        return root
    }

    private fun showError(@StringRes errorMessage: Int, errorClickListener: View.OnClickListener) {
        errorSnackBar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        if (errorMessage != R.string.not_found) {
            errorSnackBar?.setAction(R.string.retry, errorClickListener)
        }
        errorSnackBar?.show()
    }

    private fun hideError() {
        errorSnackBar?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        hideError()
    }
}