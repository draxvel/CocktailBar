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
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import android.graphics.drawable.Drawable
import com.squareup.picasso.Picasso
import java.lang.Exception


@BindingAdapter("mutableVisibility")
fun setMutableVisibility(view: View, visibility: MutableLiveData<Int>?) {
    val parentActivity = view.getParentActivity()
    if (parentActivity != null && visibility != null) {
        visibility.observe(parentActivity, Observer { value ->
            if (value == View.VISIBLE) {
                parentActivity.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            } else {
                parentActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
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

            Picasso.get()
                    .load(value)
                    .into(object: com.squareup.picasso.Target {
                        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                            TODO()
                        }

                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                            TODO()
                        }

                        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                            val stream = ByteArrayOutputStream()
                            bitmap?.compress(Bitmap.CompressFormat.JPEG, 50, stream)
                            val byteArray = stream.toByteArray()
                            val compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

                            view.setImageBitmap(compressedBitmap)
                        }
                    })
//            var image: Image? = null
//            Glide.with(parentActivity)
//                    .load(value)
//                    .override(600, 600)
//                    .into(view)
        })
    }
}

@BindingAdapter("adapter")
fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter
}

fun TODO(){}
