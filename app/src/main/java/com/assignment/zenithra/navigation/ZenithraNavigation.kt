package com.assignment.zenithra.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.assignment.zenithra.screens.home.mangas.MangaScreen
import com.assignment.zenithra.screens.home.HomeViewModel
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
    val context= LocalContext.current
    val navController= rememberNavController()
    NavHost(navController=navController, startDestination = Screens.SignInScreen.name, modifier = modifier) {
            composable(Screens.SignInScreen.name) {
                SignInScreen(
                    modifier=modifier,
                    user=user,
                    saveUser,
                    onSignedIn={
                        Toast.makeText(context,"Signed In successfully",Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                        navController.navigate(Screens.MangaScreen.name)
                    },
                    checkCredentials
                )
            }
            composable(Screens.MangaScreen.name) {
                val homeViewModel:HomeViewModel= hiltViewModel()
                MangaScreen(modifier=modifier,onLogOut={
                    Toast.makeText(context,"Logged Out successfully",Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                    homeViewModel.removeCurrentUser()
                    navController.navigate(Screens.SignInScreen.name)
                },mangaItems=homeViewModel.mangaEntityFlow.collectAsLazyPagingItems())
            }
        }
}