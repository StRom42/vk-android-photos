package com.example.init100.data

import android.util.Log
import com.example.init100.domain.Album
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.sdk.api.photos.PhotosService
import com.vk.sdk.api.photos.dto.PhotosGetAlbumsResponseDto
import com.vk.sdk.api.photos.dto.PhotosPhotoAlbumFullDto
import com.vk.sdk.api.photos.dto.PhotosPhotoDto
import com.vk.sdk.api.photos.dto.PhotosPhotoUploadDto
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody


class VkRepository(private val okHttpClient: OkHttpClient) {
    fun getAlbums() = callbackFlow {
        VK.execute(
            PhotosService().photosGetAlbums(
                needCovers = true
            ),
            object : VKApiCallback<PhotosGetAlbumsResponseDto> {
                override fun fail(error: Exception) {
                    Log.d("TAG", "error: $error")
                    cancel()
                }

                override fun success(result: PhotosGetAlbumsResponseDto) {
                    Log.d("TAG", "success: $result")
                    trySend(result.items.map { it.toDomain() }.reversed())
                    channel.close()
                }
            }
        )
        awaitClose()
    }

    suspend fun uploadPhoto(bytes: ByteArray, albumId: Int) {
        val photosUploadDto = callbackFlow {
            VK.execute(
                PhotosService().photosGetUploadServer(albumId = albumId),
                object : VKApiCallback<PhotosPhotoUploadDto> {
                    override fun fail(error: Exception) {
                        Log.d("TAG", "error: $error")
                        cancel()
                    }

                    override fun success(result: PhotosPhotoUploadDto) {
                        Log.d("TAG", "success: $result")

                        trySend(result)
                        channel.close()
                    }
                }
            )

            awaitClose()
        }.first()
        //
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file1",
                "image1.png",
                RequestBody.create("image/png".toMediaTypeOrNull(), bytes)
            )
            .build()
        val request = Request.Builder()
            .post(requestBody)
            .url(photosUploadDto.uploadUrl)
            .build()
        val response = okHttpClient.newCall(request).execute()
        val uploadResponse =
            response.body?.string()?.let { Json.decodeFromString<UploadResponse>(it) }
        //
        VK.execute(
            PhotosService().photosSave(albumId = albumId, photosList = uploadResponse?.photosList, server = uploadResponse?.server, hash = uploadResponse?.hash),
            object : VKApiCallback<List<PhotosPhotoDto>> {
                override fun fail(error: Exception) {
                    Log.d("TAG", "error: $error")
                }

                override fun success(result: List<PhotosPhotoDto>) {
                    Log.d("TAG", "success: $result")
                }
            }
        )
    }
}

private fun PhotosPhotoAlbumFullDto.toDomain() = Album(
    id = id,
    thumbnail = thumbSrc,
    title = title
)