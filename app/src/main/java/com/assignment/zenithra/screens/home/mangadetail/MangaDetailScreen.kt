package com.assignment.zenithra.screens.home.mangadetail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.assignment.zenithra.models.MangaEntity
import com.assignment.zenithra.utils.manga_subTitle
import com.assignment.zenithra.utils.manga_summary
import com.assignment.zenithra.utils.manga_thumb
import com.assignment.zenithra.utils.manga_title

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun MangaDetailScreen(modifier: Modifier = Modifier, mangaEntity: MangaEntity = MangaEntity(0,
    manga_title, manga_thumb, manga_summary, manga_subTitle
), onBackPressed:()->Unit={}) {
    BackHandler(true) {
        onBackPressed()
    }
    Column(
        modifier = modifier.fillMaxSize().padding(top=30.dp).verticalScroll(rememberScrollState(0)),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(modifier = modifier.fillMaxWidth().padding(bottom = 20.dp)) {
            AsyncImage(
                model=mangaEntity.thumb,
                contentDescription = null,
                modifier=modifier.weight(1f).wrapContentHeight().padding(end=10.dp)
            )
            Column(
                horizontalAlignment = Alignment.Start,
                modifier=modifier.weight(1f).padding(top=10.dp)
            ) {
                Text(mangaEntity.title,color= Color.White, fontSize = TextUnit(20f, TextUnitType.Sp))
                Spacer(modifier=modifier.height(20.dp))
                Text(mangaEntity.subtitle,color= Color.White)
            }
        }
        Text("Summary: ${mangaEntity.summary}",color= Color.White)
    }
}