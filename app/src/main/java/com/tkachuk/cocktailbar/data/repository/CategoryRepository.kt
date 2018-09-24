package com.tkachuk.cocktailbar.data.repository

import android.content.Context
import android.os.AsyncTask
import com.tkachuk.cocktailbar.data.database.AppDatabase
import com.tkachuk.cocktailbar.data.database.CallBack
import com.tkachuk.cocktailbar.data.database.dao.CategoryDao
import com.tkachuk.cocktailbar.model.Category
import com.tkachuk.cocktailbar.network.Api
import com.tkachuk.cocktailbar.ui.base.BaseRepositoryModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CategoryRepository(val context: Context) : BaseRepositoryModel() {

    @Inject
    lateinit var api: Api

    private val categoryDao = AppDatabase.getDatabase(context).categoryDao

    fun getCategories(loadCategoriesCallBack: CallBack.LoadCategoriesCallBack) {
        if (categoryDao.allCategories != null && categoryDao.allCategories!!.isNotEmpty()) {
            loadCategoriesCallBack.onLoad(categoryDao.allCategories!!)
        } else {
            api.getCategoriesList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            //Add result
                            { result ->
                                loadCategoriesCallBack.onLoad(result.drinks)
                                saveCategoriesToBD(result.drinks)
                            },
                            { msg ->
                                loadCategoriesCallBack.onError(msg.localizedMessage ?: "Error")
                            }
                    )
        }
    }

    private fun saveCategoriesToBD(list: List<Category>) {
        InsertTask(categoryDao).execute(list)
    }

    class InsertTask(categoryDao: CategoryDao) : AsyncTask<List<Category>, Void, Void>() {

        private var dao: CategoryDao = categoryDao

        override fun doInBackground(vararg params: List<Category>): Void? {
            dao.insertAll(params[0])
            return null
        }
    }
}