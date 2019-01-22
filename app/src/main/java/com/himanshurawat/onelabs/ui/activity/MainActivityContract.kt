package com.himanshurawat.onelabs.ui.activity

import android.content.Context

interface MainActivityContract{

    interface View{
        fun initLoader()
        fun requestContactDialog()
        fun requestContactPermission()
        fun closeActivity()

    }

    interface Presenter{

        fun checkContactPermission(context: Context)
        fun contactPermissionGranted()
        fun dialogConfirm()
        fun dialogCancel()

    }
}