package com.himanshurawat.onelabs.ui.activity

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class MainActivityPresenterImpl(private val view: MainActivityContract.View): MainActivityContract.Presenter{


    override fun contactPermissionGranted() {
        view.initLoader()
    }

    override fun dialogConfirm() {
        view.requestContactPermission()
    }

    override fun dialogCancel() {
        view.closeActivity()
    }

    override fun checkContactPermission(context: Context) {
        if(ContextCompat.checkSelfPermission(context.applicationContext,
                android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            view.requestContactDialog()
        }else{
            view.initLoader()
        }
    }



}