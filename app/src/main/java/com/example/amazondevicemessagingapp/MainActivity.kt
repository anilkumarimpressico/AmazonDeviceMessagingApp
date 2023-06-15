package com.example.amazondevicemessagingapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import com.example.amazondevicemessagingapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var mBinding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


        mBinding.btnRegister.setOnClickListener {
        registerADM()
        }

    }

    private fun registerADM() {
    }
}