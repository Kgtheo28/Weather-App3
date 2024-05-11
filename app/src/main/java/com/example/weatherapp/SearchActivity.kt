package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherapp.databinding.ActivitySearchBinding
import com.example.weatherapp.databinding.BottomItemLayoutBinding
import com.example.weatherapp.databinding.SheetLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private lateinit var sheetLayoutBinding: SheetLayoutBinding
    private lateinit var dialog: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sheetLayoutBinding = SheetLayoutBinding.inflate(layoutInflater)
        dialog = BottomSheetDialog(this, R.style.BottomSheetTheme)
        dialog.setContentView(sheetLayoutBinding.root)

        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

        binding.btnSearchLocation.setOnClickListener {
            dialog.show()
        }

        sheetLayoutBinding.button.setOnClickListener {
            val text =  sheetLayoutBinding.editTextText2.text.toString()
            binding.textView.text = text

            dialog.dismiss()
        }
    }
}