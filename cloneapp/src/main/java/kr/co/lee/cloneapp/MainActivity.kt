package kr.co.lee.cloneapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kr.co.lee.cloneapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(this)[SimpleViewModel::class.java]

        binding.recycler.layoutManager = LinearLayoutManager(this)

        viewModel.insertAll()
        viewModel.getAll().observe(this, {
            val adapter = RecyclerAdapter(it)
            binding.recycler.adapter = adapter
        })
    }
}