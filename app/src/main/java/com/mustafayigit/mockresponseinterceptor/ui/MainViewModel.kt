package com.mustafayigit.mockresponseinterceptor.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mustafayigit.mockresponseinterceptor.data.INewsRepository
import com.mustafayigit.mockresponseinterceptor.data.model.NewsModel
import com.mustafayigit.mockresponseinterceptor.util.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val newsRepository: INewsRepository
) : ViewModel() {

    private val _newsState = MutableStateFlow<State<List<NewsModel>>>(State.Loading)
    val newsState = _newsState.asStateFlow()

    fun getNews() {
        viewModelScope.launch {
            val newsState = newsRepository.getNews()
            _newsState.value = newsState
        }
    }

}
