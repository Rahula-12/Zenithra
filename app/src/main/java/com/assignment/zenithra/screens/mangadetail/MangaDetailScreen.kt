package com.assignment.zenithra.screens.mangadetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import com.assignment.zenithra.models.MangaEntity
import com.assignment.zenithra.screens.manga.MangaItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaDetailScreen(modifier: Modifier,
                      mangaEntity: MangaEntity,
                      onLogOut:()->Unit={}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier=modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(mangaEntity.title)
                        Text("LOG OUT",modifier=modifier.clickable { onLogOut() })
                    }
                },
                colors = TopAppBarColors(
                    containerColor = Color.Black,
                    scrolledContainerColor = Color.Black,
                    navigationIconContentColor = Color.Black,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(modifier=modifier.fillMaxWidth()) {
                    Button(onClick = {},modifier=modifier.weight(1f)) {
                        Text("Manga")
                    }
                    Button(onClick = {},modifier=modifier.weight(1f)) {
                        Text("Face")
                    }
                }
            }
        }
    ) {innerPadding->

    }
}