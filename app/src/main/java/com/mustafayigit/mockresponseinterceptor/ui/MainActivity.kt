package com.mustafayigit.mockresponseinterceptor.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mustafayigit.mockresponseinterceptor.databinding.ActivityMainBinding
import com.mustafayigit.mockresponseinterceptor.ui.adapter.NewsAdapter
import com.mustafayigit.mockresponseinterceptor.util.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initAdapter()
        initListener()
        initObserver()

        viewModel.getNews()
    }

    private fun initAdapter() {
        binding.recyclerNews.adapter = NewsAdapter { item ->
            // on click
        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            viewModel.newsState.collect {
                when (it) {
                    is State.Loading -> {
                        // show loading
                    }
                    is State.Error -> {
                        // show error
                        Toast.makeText(this@MainActivity, it.exception.message, Toast.LENGTH_SHORT).show()
                    }
                    is State.Success -> {
                        // show data
                        (binding.recyclerNews.adapter as? NewsAdapter)?.submitList(it.data)
                    }
                }
            }
        }
    }

    private fun initListener() {
        binding.btnChangeSource.setOnClickListener {
            isGlobalMockingEnabled = !isGlobalMockingEnabled
            viewModel.getNews()
        }
    }

    companion object {
        var isGlobalMockingEnabled = true
    }
}


