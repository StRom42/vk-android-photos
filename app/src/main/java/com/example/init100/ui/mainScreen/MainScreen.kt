package com.example.init100.ui.mainScreen

import android.app.Activity
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.init100.domain.Album
import com.example.init100.ui.MainActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val pickVisualMedia =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                val inputStream = context.contentResolver.openInputStream(uri)
                if (inputStream != null && state.selectedAlbum != null) {
                    inputStream.use {
                        viewModel.uploadPhoto(it.readBytes(), state.selectedAlbum!!.id)
                    }
                }
                viewModel.closeDialog()
            }
        }
    val authLauncher = rememberLauncherForActivityResult(
        contract = VK.getVKAuthActivityResultContract(),
        onResult = {
            when (it) {
                is VKAuthenticationResult.Success -> {
                    viewModel.loadData()
                }

                is VKAuthenticationResult.Failed -> {}
            }
        }
    )
    val authRequest = {
        authLauncher.launch(arrayListOf(VKScope.PHOTOS))
    }
    MainScreen(
        state = state,
        onUploadClick = viewModel::openDialog,
        onAuthClick = authRequest,
        onDialogDismiss = viewModel::closeDialog,
        onAlbumClick = {
            viewModel.selectAlbum(it)
            pickVisualMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun MainScreen(
    state: MainScreenState,
    onUploadClick: () -> Unit,
    onAuthClick: () -> Unit,
    onDialogDismiss: () -> Unit,
    onAlbumClick: (Album) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ElevatedButton(onClick = onAuthClick) {
            Text(text = "Авторизоваться в VK")
        }

        ElevatedButton(onClick = onUploadClick) {
            Text(text = "Загрузить фотографии")
        }

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.albums) {
                Box {
                    GlideImage(
                        model = it.thumbnail,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                    Text(text = it.title, modifier = Modifier.align(Alignment.BottomCenter))
                }
            }
        }
    }

    if (state.isDialogOpened) {
        Dialog(onDismissRequest = onDialogDismiss) {
            Surface {
                Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Выберите альбом")
                    LazyColumn(modifier = Modifier.size(256.dp)) {
                        items(state.albums) {
                            ElevatedButton(onClick = { onAlbumClick(it) }) {
                                Text(text = it.title)
                            }
                        }
                    }
                }
            }
        }
    }
}