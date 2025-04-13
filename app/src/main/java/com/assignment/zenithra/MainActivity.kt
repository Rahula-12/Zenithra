package com.assignment.zenithra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.assignment.zenithra.navigation.ZenithraNavigation
import com.assignment.zenithra.screens.signin.SignInViewModel
import com.assignment.zenithra.ui.theme.ZenithraTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val signInViewModel:SignInViewModel by viewModels()
        setContent {
            ZenithraTheme {
                    ZenithraNavigation(
                        modifier = Modifier,
                        user=signInViewModel.currentUser.collectAsStateWithLifecycle().value,
                        saveUser = {email,password->
                            signInViewModel.saveUser(email,password)
                        },
                        checkCredentials={email,password->
                            signInViewModel.checkUser(email,password)
                        }
                    )
                }
        }
    }
}