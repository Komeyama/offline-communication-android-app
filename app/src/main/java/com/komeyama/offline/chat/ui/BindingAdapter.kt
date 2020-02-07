package com.komeyama.offline.chat.ui

import android.graphics.Typeface
import android.widget.TextView
import androidx.core.view.forEach
import androidx.databinding.BindingAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.komeyama.offline.chat.R

class BindingAdapter {
    companion object {
        @JvmStatic
        @BindingAdapter("menuEnable")
        fun BottomNavigationView.setMenuEnable(isLoading: Boolean) {
            this.menu.forEach {
                it.isEnabled = !isLoading
            }
        }

        @JvmStatic
        @BindingAdapter( "familyFont")
        fun TextView.setFamilyFont(fontName: String) {
            if (fontName != "font_system_default") {
                val path = context.getString(R.string.font_asset_folder) + fontName
                this.typeface = Typeface.createFromAsset(context.assets,path)
            }
        }
    }
}
