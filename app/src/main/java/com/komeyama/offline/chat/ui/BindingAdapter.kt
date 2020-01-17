package com.komeyama.offline.chat.ui

import androidx.core.view.forEach
import androidx.databinding.BindingAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class BindingAdapter {
    companion object {
        @JvmStatic
        @BindingAdapter("menuEnable")
        fun BottomNavigationView.setMenuEnable(isLoading: Boolean) {
            this.menu.forEach {
                it.isEnabled = !isLoading
            }
        }
    }
}
