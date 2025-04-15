package com.assignment.zenithra.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.assignment.zenithra.screens.manga.MangaScreen
import com.assignment.zenithra.screens.manga.MangaViewModel
import com.assignment.zenithra.screens.signin.SignInScreen

enum class Screens {
    SignInScreen,MangaScreen
}

@Composable
fun ZenithraNavigation(
    modifier: Modifier=Modifier,
    user:String="",
    saveUser:(String,String)->Unit={_,_->},
    checkCredentials:suspend (String,String)->Boolean={_,_->false}
) {
    val navController= rememberNavController()
    NavHost(navController=navController, startDestination = Screens.SignInScreen.name, modifier = modifier) {
            composable(Screens.SignInScreen.name) {
                SignInScreen(
                    modifier=modifier,
                    user=user,
                    saveUser,
                    onSignedIn={
                        navController.popBackStack()
                        navController.navigate(Screens.MangaScreen.name)
                    },
                    checkCredentials
                )
            }
            composable(Screens.MangaScreen.name) {
                val mangaViewModel:MangaViewModel= hiltViewModel()
                MangaScreen(modifier=modifier,onLogOut={
                    navController.popBackStack()
                    mangaViewModel.removeCurrentUser()
                    navController.navigate(Screens.SignInScreen.name)
                },mangaItems=mangaViewModel.mangaEntityFlow.collectAsLazyPagingItems())
            }
        }
}