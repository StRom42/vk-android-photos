package com.example.init100.di

import com.example.init100.data.VkRepository
import okhttp3.OkHttpClient

// Bad pattern, but it's just for an example. If need, replace with Hilt or Koin
object SingletonObject {
    private val okHttpClient = OkHttpClient()
    val vkRepo = VkRepository(okHttpClient)
}