package com.example.init100.ui.mainScreen

import com.example.init100.domain.Album

data class MainScreenState(
    val albums: List<Album> = emptyList(),
    val isDialogOpened: Boolean = false,
    val selectedAlbum: Album? = null
) {
    companion object {
        fun mock() = MainScreenState(albums = listOf(
            Album(
                id = 1,
                thumbnail = "https://sun9-6.userapi.com/impg/JWoqJNwZLcIXm6yGNl0ILvvwxBdKLiCqbyhNZQ/HMMnlLQtWTI.jpg?size=614x405&quality=95&sign=addf3096ef617d59c06a1ffd74fc8b90&type=album",
                title = "Заголовок"
            ),
            Album(
                id = 2,
                thumbnail = "https://sun9-6.userapi.com/impg/JWoqJNwZLcIXm6yGNl0ILvvwxBdKLiCqbyhNZQ/HMMnlLQtWTI.jpg?size=614x405&quality=95&sign=addf3096ef617d59c06a1ffd74fc8b90&type=album",
                title = "Заголовок"
            ),
            Album(
                id = 3,
                thumbnail = "https://sun9-7.userapi.com/impg/W_i4VUBH5AR1nkweI7KcSGpbgpvpIhpNus50VQ/hVISlVRVEic.jpg?size=350x350&quality=95&sign=43fc408875ad984c2a0cc837028c9d91&type=album",
                title = "Заголовок"
            ),
            Album(
                id = 4,
                thumbnail = "https://sun9-6.userapi.com/impg/JWoqJNwZLcIXm6yGNl0ILvvwxBdKLiCqbyhNZQ/HMMnlLQtWTI.jpg?size=614x405&quality=95&sign=addf3096ef617d59c06a1ffd74fc8b90&type=album",
                title = "Заголовок"
            ),
            Album(
                id = 5,
                thumbnail = "https://sun9-6.userapi.com/impg/JWoqJNwZLcIXm6yGNl0ILvvwxBdKLiCqbyhNZQ/HMMnlLQtWTI.jpg?size=614x405&quality=95&sign=addf3096ef617d59c06a1ffd74fc8b90&type=album",
                title = "Заголовок"
            ),
            Album(
                id = 6,
                thumbnail = "https://sun9-6.userapi.com/impg/JWoqJNwZLcIXm6yGNl0ILvvwxBdKLiCqbyhNZQ/HMMnlLQtWTI.jpg?size=614x405&quality=95&sign=addf3096ef617d59c06a1ffd74fc8b90&type=album",
                title = "Заголовок"
            ),
        ))
    }
}
