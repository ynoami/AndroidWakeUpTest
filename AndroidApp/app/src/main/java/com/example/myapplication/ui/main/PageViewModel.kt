package com.example.myapplication.ui.main

import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PageViewModel : ViewModel() {
    var launchItems: MutableList<LaunchItem> = mutableListOf()

    private val _index = MutableLiveData<Int>()
    val text: LiveData<String> = Transformations.map(_index) {
        "Hello world from section: $it"
    }

    fun setIndex(index: Int) {
        _index.value = index
    }
}

class LaunchItem{
    var isLaunchable: Boolean
    var packageName: String
    var className: String

    constructor(isLaunchable: Boolean, packageName: String, className: String) {
        this.isLaunchable = isLaunchable
        this.packageName = packageName
        this.className = className
    }
}