package com.medialink.sub5close.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import coil.transform.CircleCropTransformation
import com.medialink.sub5close.R
import kotlinx.android.synthetic.main.content_profile.*
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)


        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        (activity as AppCompatActivity?)?.let {
            val toolbar : Toolbar = root.findViewById(R.id.toolbar)
            it.setSupportActionBar(toolbar)
            it.supportActionBar?.title = getString(R.string.title_profile)
        }

        profileViewModel.text.observe(viewLifecycleOwner, Observer {
            tv_username_user.text = it.userName
            tv_email_user.text = it.email

            tv_name_user.text = it.name
            tv_mobile_user.text = it.phone
            tv_content_email_user.text = it.email
            tv_address_user.text = it.address
            tv_dob_user.text = it.dob

            img_profile_user.load(R.drawable.benk100) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
        })
        return root
    }
}