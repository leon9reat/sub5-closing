package com.medialink.sub5close.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.medialink.sub5close.model.Profile

class ProfileViewModel : ViewModel() {

    private val _profile = MutableLiveData<Profile>().apply {
        val profile = Profile(1,
            "bambang",
            "Bambang Ariyanto",
            "cyber.hecker@gmail.com",
            "+62-813 4501 9699",
            "Jl. Gajahmada No.2-4 Pontianak",
            "06-02-1982")

        value = profile
    }
    val text: LiveData<Profile> = _profile
}