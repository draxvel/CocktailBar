package com.tkachuk.cocktailbar.util

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.tkachuk.cocktailbar.util.extension.getParentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.bitmap.CenterCrop

@BindingAdapter("mutableVisibility")
fun setMutableVisibility(view: View, visibility: MutableLiveData<Int>?) {
    val parentActivity = view.getParentActivity()
    if (parentActivity != null && visibility != null) {
        visibility.observe(parentActivity, Observer { value ->
//            if (value == View.VISIBLE) {
//                parentActivity.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
//            } else {
//                parentActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
//            }
            view.visibility = value ?: View.VISIBLE
        })
    }
}

@BindingAdapter("mutableText")
fun setMutableText(view: TextView, text: MutableLiveData<String>?) {
    val parentActivity = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value -> view.text = value ?: "" })
    }
}

@BindingAdapter("mutableImage")
fun setMutableImage(view: ImageView, url: MutableLiveData<String>?) {
    val parentActivity = view.getParentActivity()
    if (parentActivity != null && url != null) {
        url.observe(parentActivity, Observer { value ->
            view.visibility = View.VISIBLE

            var requestOptions = RequestOptions()
            requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(10))
            requestOptions.override(650, 650)

            Glide.with(parentActivity)
                    .load(value)
                    .apply(requestOptions)
                    .into(view)
        })
    }
}

@BindingAdapter("adapter")
fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter
}