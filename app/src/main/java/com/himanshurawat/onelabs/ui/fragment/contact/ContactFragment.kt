package com.himanshurawat.onelabs.ui.fragment.contact

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.himanshurawat.onelabs.R
import com.himanshurawat.onelabs.adapter.PersonAdapter
import com.himanshurawat.onelabs.pojo.Person
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.uiThread
import kotlin.Comparator


class ContactFragment: Fragment(),
    LoaderManager.LoaderCallbacks<Cursor>,
    PersonAdapter.OnPersonClickListener {

    override fun onPersonClick(person: Person) {
        Log.i("PersonClick",person.name)
        listener.onContactClick(person)
    }


    private val CONTACT_CONTRACT_ID = 1

    private lateinit var personList: MutableList<Person>
    private lateinit var personAdapter: PersonAdapter
    private lateinit var personRecyclerView: RecyclerView
    private lateinit var listener: OnContactClickListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            if(context != null){
                listener = context as OnContactClickListener
            }
        }catch (e : Exception){}

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
    }


    private fun setup(view: View) {
        personRecyclerView = view.find(R.id.fragment_contact_recycler_view)
        personList = arrayListOf()
        personAdapter = PersonAdapter(context!!,personList,this)
        personRecyclerView.adapter = personAdapter
        personRecyclerView.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        val dividerItemDecoration = DividerItemDecoration(personRecyclerView.context,DividerItemDecoration.VERTICAL)
        personRecyclerView.addItemDecoration(dividerItemDecoration)


        LoaderManager.getInstance(this).initLoader(CONTACT_CONTRACT_ID,null,this)

    }


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {

        return CursorLoader(context!!, ContactsContract.Contacts.CONTENT_URI,
            null,null,null,null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if(data != null && data.count > 0){

            doAsync {
                val cr = context!!.contentResolver
                Log.i("ContactCount","${data.count}")
                while( data.moveToNext()){

                    //Only Adds Contact with at least one Phone Number
                    if(data.getInt(data.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0){
                        //Contact ID
                        val id = data.getString(data.getColumnIndex(ContactsContract.Contacts._ID))
                        //Note DISPLAY_NAME is used for < Honeycomb
                        val name = data.getString(data.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))

                        val phoneNumber = mutableListOf<String>()

                        val phoneNumberCursor = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf(id), null)

                        while(phoneNumberCursor!=null && phoneNumberCursor.moveToNext()){
                            phoneNumber.add(phoneNumberCursor.getString(phoneNumberCursor.
                                getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)))
                        }

                        phoneNumberCursor?.close()


//                        personList.sortWith(object: Comparator<Person>{
//                            override fun compare(o1: Person?, o2: Person?): Int {
//                                return o1!!.name.compareTo(o2!!.name)
//                            }
//                        })
                        uiThread {
                            personList.add(Person(id,name,phoneNumber))
                            if(personList.size == 1){
                                listener.setDefaultContact(personList[0])
                            }
                            personAdapter.notifyItemInserted(personList.size - 1)
                        }
                    }
                }
            }

        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }


    interface OnContactClickListener{
        fun onContactClick(person: Person)
        fun setDefaultContact(person: Person)
    }
}