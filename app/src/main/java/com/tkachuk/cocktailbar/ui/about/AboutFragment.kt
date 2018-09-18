package com.tkachuk.cocktailbar.ui.about

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tkachuk.cocktailbar.R
import com.tkachuk.cocktailbar.util.BASE_CONTACT_LINK
import kotlinx.android.synthetic.main.fragment_about.view.*

class AboutFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_about, container, false)

        val result: Spanned = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(BASE_CONTACT_LINK, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(BASE_CONTACT_LINK)
        }
        view.about_text2.text = result
        view.about_text2.movementMethod = LinkMovementMethod.getInstance()
        return view
    }
}