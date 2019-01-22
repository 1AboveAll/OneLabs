package com.himanshurawat.onelabs

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.himanshurawat.onelabs.pojo.Person
import com.himanshurawat.onelabs.ui.activity.MainActivityContract
import com.himanshurawat.onelabs.ui.activity.MainActivityPresenterImpl
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton


class MainActivity : AppCompatActivity(),MainActivityContract.View, LoaderManager.LoaderCallbacks<Cursor> {


    private lateinit var presenter: MainActivityContract.Presenter
    private val CONTACT_CONTRACT_ID = 1
    private val CONTACT_REQUEST_CODE = 7
    private val personList = mutableListOf<Person>()



    override fun requestContactDialog() {
        alert{
            title = "Allow Contact Permission"
            message = "Reading Contact is an essential feature for this Application. We will be able to serve you"+
                    " better if you grant us Contact Permission."
            noButton {
                title = "Dismiss"
                presenter.dialogCancel()
            }
            yesButton {
                presenter.dialogConfirm()
            }
        }.show()
    }

    override fun requestContactPermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_CONTACTS),
            CONTACT_REQUEST_CODE)
    }

    override fun closeActivity() {
        this@MainActivity.finish()
    }

    override fun initLoader() {
        LoaderManager.getInstance(this).initLoader(CONTACT_CONTRACT_ID,null,this)
    }



    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {

        return CursorLoader(this, ContactsContract.Contacts.CONTENT_URI,
            null,null,null,null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if(data != null && data.count > 0){

            //Content Resolver  (I Should Move this Code to Contact Details Fragment)
            val cr = contentResolver

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
                    personList.add(Person(id,name,phoneNumber))
                }






            }

        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Setup


        //Presenter
        presenter = MainActivityPresenterImpl(this)
        presenter.checkContactPermission(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){

            CONTACT_REQUEST_CODE ->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Permission Granted
                    initLoader()
                }else{
                    presenter.dialogCancel()
                }
            }

        }
    }
}
