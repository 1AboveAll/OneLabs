package com.himanshurawat.onelabs.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.button.MaterialButton
import com.himanshurawat.onelabs.R
import com.himanshurawat.onelabs.pojo.Person
import org.jetbrains.anko.find

class ViewContactFragment: Fragment(){


    private var person: Person? = null

    private lateinit var name: TextView
    private lateinit var iconImage: ImageView
    private lateinit var phoneNumber: TextView
    private lateinit var callButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_contact,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        name = view.find(R.id.fragment_view_contact_name_text_view);
        phoneNumber = view.find(R.id.fragment_view_contact_phone_number_text_view)
        callButton = view.find(R.id.fragment_view_contact_call_button)
        iconImage = view.find(R.id.fragment_view_contact_icon_image_view)
    }

    override fun onResume() {
        super.onResume()
        //setVal(person)
    }

    private fun setVal(person: Person?) {
        if(person != null){
            name.text = person.name
            phoneNumber.text = person.phoneNumber[0]
            Glide.with(context!!).load(R.drawable.person_icon_grey).apply(RequestOptions().circleCrop()).into(iconImage)
        }
    }


    fun setPersonValue(person: Person){
        setVal(person)
    }
}