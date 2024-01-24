package com.example.init100.ui.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.init100.data.VkRepository
import com.example.init100.di.SingletonObject
import com.example.init100.domain.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel() {
    private val _state = MutableStateFlow(MainScreenState())
    val state = _state.asStateFlow()

    fun uploadPhoto(bytes: ByteArray, selectedAlbumId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            SingletonObject.vkRepo.uploadPhoto(bytes, selectedAlbumId)
            delay(5000)
            loadData()
        }
    }

    fun openDialog() {
        viewModelScope.launch {
            _state.update { it.copy(isDialogOpened = true) }
        }
    }

    fun closeDialog() {
        viewModelScope.launch {
            _state.update { it.copy(isDialogOpened = false) }
        }
    }

    fun selectAlbum(album: Album) {
        viewModelScope.launch {
            _state.update { it.copy(selectedAlbum = album) }
        }
    }

    fun loadData() {
        viewModelScope.launch {
            SingletonObject.vkRepo.getAlbums().collect { albums ->
                _state.update { it.copy(albums = albums) }
            }
        }
    }

    init {
        loadData()
    }
}
