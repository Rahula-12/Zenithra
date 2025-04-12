package com.assignment.zenithra

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.assignment.zenithra.screens.MangaScreen
import com.assignment.zenithra.screens.ZenithraNavigation
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