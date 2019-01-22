package com.himanshurawat.onelabs

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.ViewPager
import com.himanshurawat.onelabs.adapter.FragmentAdapter
import com.himanshurawat.onelabs.pojo.Person
import com.himanshurawat.onelabs.ui.activity.MainActivityContract
import com.himanshurawat.onelabs.ui.activity.MainActivityPresenterImpl
import com.himanshurawat.onelabs.ui.fragment.viewcontact.ViewContactFragment
import com.himanshurawat.onelabs.ui.fragment.contact.ContactFragment
import org.jetbrains.anko.*


class MainActivity : AppCompatActivity(),
    MainActivityContract.View,
    ContactFragment.OnContactClickListener,
    ViewContactFragment.OnViewContactClickListener{


    private lateinit var presenter: MainActivityContract.Presenter
    private val CONTACT_REQUEST_CODE = 7
    private lateinit var viewPager: ViewPager
    private lateinit var fragmentAdapter: FragmentAdapter


    override fun requestContactDialog() {
        alert{
            title = "Allow Contact Permission"
            message = "Reading Contacts is an essential feature for this Application. We will be able to serve you"+
                    " better if you grant us Contact Permission."
            isCancelable = false
            noButton {
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
        setup()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Presenter
        presenter = MainActivityPresenterImpl(this)
        presenter.checkContactPermission(this)
        // Setup

    }

    private fun setup() {
        viewPager = find(R.id.activity_main_view_pager)
        fragmentAdapter = FragmentAdapter(supportFragmentManager)
        viewPager.adapter = fragmentAdapter
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

    override fun onBackPressed() {
        if(viewPager.currentItem == 1){
            viewPager.currentItem = 0
        }else {
            super.onBackPressed()
        }
    }

    override fun onCallClick(phoneNumber: String) {
        val number = Uri.parse("tel:$phoneNumber")
        val callIntent = Intent(Intent.ACTION_DIAL,number)
        startActivity(callIntent)
    }

    override fun onContactClick(person: Person) {
        val tag = "android:switcher:${R.id.activity_main_view_pager}:1"
        val fragment: ViewContactFragment? = supportFragmentManager.findFragmentByTag(tag) as ViewContactFragment?
        fragment?.setPersonValue(person)
        viewPager.currentItem = 1
    }

    override fun setDefaultContact(person: Person) {
        val tag = "android:switcher:${R.id.activity_main_view_pager}:1"
        val fragment: ViewContactFragment? = supportFragmentManager.findFragmentByTag(tag) as ViewContactFragment?
        fragment?.setPersonValue(person)
    }
}
