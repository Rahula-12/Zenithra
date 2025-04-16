package com.assignment.zenithra.screens.home.mangas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import com.assignment.zenithra.R
import com.assignment.zenithra.models.MangaEntity
import com.assignment.zenithra.screens.home.facedetection.FaceDetectionScreen
import com.assignment.zenithra.screens.home.mangadetail.MangaDetailScreen

val mangaSaver = Saver<MangaEntity, Map<String, Any>>(
    save = { mapOf("id" to it.id, "title" to it.title,"thumb" to it.thumb,"summary" to it.summary,"subtitle" to it.subtitle) },
    restore = { MangaEntity(it["id"] as Int,it["title"] as String,it["thumb"] as String,it["summary"] as String,it["subtitle"] as String) }
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaScreen(
    onLogOut:()->Unit={},
    modifier: Modifier,
    mangaItems: LazyPagingItems<MangaEntity>
) {
    var mangaSelected by rememberSaveable(stateSaver = mangaSaver) {
        mutableStateOf(MangaEntity(0,"","","",""))
    }
    var showDetail by rememberSaveable { mutableStateOf(false)  }
    var faceRecognition by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier=modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            if(faceRecognition) "Face Recognition"
                            else if(!showDetail) "Mangas"
                            else mangaSelected.title,overflow= TextOverflow.Ellipsis,modifier=modifier.width(150.dp),maxLines=1)
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
            BottomAppBar(containerColor=Color.Black) {
                Row(modifier=modifier.fillMaxWidth()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.weight(1f).clickable {
                        faceRecognition=false
                    }) {
                        Icon(painter= painterResource(R.drawable.open_book),null,modifier=modifier.size(20.dp),tint=Color.White)
                        Text("Manga", color = Color.White)
                    }
                    Box(modifier=modifier.weight(1f).fillMaxWidth().clickable {
                        faceRecognition=true
                    }, contentAlignment = Alignment.Center) {
                        Text("Face",modifier=modifier.background(Color.White))
                    }
                }
            }
        }
    ) {innerPadding->
        Box(modifier=modifier.fillMaxSize().padding(innerPadding).background(Color.Black)){
            if(faceRecognition) {
                FaceDetectionScreen()
            }
            else if(!showDetail){
                LazyVerticalGrid(GridCells.Fixed(3)) {
                    items(mangaItems.itemCount) { index ->
                        MangaItem(
                            modifier, mangaItems[index]?.thumb, mangaItems[index]?.title,
                        ){
                            mangaItems[index]?.let {
                                showDetail=true
                                mangaSelected=it
                            }
                        }
                    }
                    item {
                        if (mangaItems.loadState.append is LoadState.Loading) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
            else {
                MangaDetailScreen(modifier,mangaSelected){
                    showDetail=false
                }
            }
        }

    }
}

@Composable
fun MangaItem(
    modifier: Modifier=Modifier,
    thumbUrl:String?="https://usc1.contabostorage.com/scraper/mangas/65a52ea8f64a55128b487e1b/thumb.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=c10e9464b360c31ce8abea9b266076f6%2F20250413%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20250413T181208Z&X-Amz-Expires=86400&X-Amz-SignedHeaders=host&X-Amz-Signature=fdcb3eed4243931db68e7be5022e2e568800bf4be9fc7a8bb569b5ed3bbf3449",title:String?="A World of Gold to You",
    onClick: () -> Unit) {
    AsyncImage(
        model = thumbUrl,
        contentDescription = "$title image",
        modifier =modifier.height(200.dp).width(100.dp).clipToBounds().padding(end=10.dp).clip(
        RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp, topEnd = 20.dp, bottomEnd = 20.dp)
    ).clickable {
            onClick()
        }
    )
}