package com.assignment.zenithra.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.assignment.zenithra.screens.manga.MangaScreen
import com.assignment.zenithra.screens.signin.SignInScreen

enum class Screens {
    SignInScreen,MangaScreen,FaceRecognitionScreen
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
                MangaScreen()
            }
        }
}