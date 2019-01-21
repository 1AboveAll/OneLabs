package com.himanshurawat.onelabs.ui

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.himanshurawat.onelabs.R





class ContactFragment: Fragment(), LoaderManager.LoaderCallbacks<Cursor>{



    private val CONTACT_CONTRACT_ID = 1;


    override fun onAttach(context: Context?) {
        super.onAttach(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup(view)
        //getContact()
    }


    private fun setup(view: View) {
        LoaderManager.getInstance(this).initLoader(CONTACT_CONTRACT_ID,null,this)
    }


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(context!!,ContactsContract.Contacts.CONTENT_URI,
            null,null,null,null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if(data != null){

        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {

    }


}