package com.assignment.zenithra.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.assignment.zenithra.R
import com.assignment.zenithra.ui.theme.LightBlue50_100

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun SignInScreen(
    modifier: Modifier=Modifier,
    onSignedIn:()->Unit={}
) {
    Scaffold (
        topBar = {
            TopAppBar(title = {
                Text("Sign in")
            }, colors = TopAppBarColors(
                containerColor = Color.Black,
                scrolledContainerColor = Color.Black,
                navigationIconContentColor = Color.Black,
                titleContentColor = Color.White,
                actionIconContentColor = Color.Black
            ))
        }
    ){ paddingValues ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.padding(paddingValues).fillMaxSize().background(Color.Black).verticalScroll(
                rememberScrollState(0)
            )
        ) {
            Card(
                modifier = Modifier.wrapContentHeight().fillMaxWidth()
                    .padding(end = 10.dp, start = 10.dp),
                colors = CardColors(
                    contentColor = Color.White,
                    containerColor = Color.DarkGray,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = Color.White
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth().padding(10.dp)
                ) {
                    Text(
                        "Zenithra",
                        fontSize = TextUnit(20f, TextUnitType.Sp),
                        modifier = modifier.padding(10.dp)
                    )
                    Text(
                        "Welcome Back",
                        fontSize = TextUnit(30f, TextUnitType.Sp),
                        modifier = modifier.padding(10.dp)
                    )
                    Text("Please enter your details to sign in",modifier=modifier.padding(10.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = modifier.padding(10.dp).width(120.dp)
                    ) {
                        Icon(painter = painterResource(R.drawable.google_logo), null,modifier=modifier.size(50.dp).border(2.dp, color = Color.White,CircleShape).padding(10.dp))
                        Icon(painter = painterResource(R.drawable.apple_logo), null,modifier=modifier.size(50.dp).border(2.dp, color = Color.White,CircleShape).padding(10.dp))
                    }
                    Text("OR", fontSize = TextUnit(15f,TextUnitType.Sp),modifier=modifier.padding(10.dp))
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        placeholder={
                            Text("Your Email Address", color = Color.LightGray)
                        },
                        modifier = modifier.padding(10.dp).fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = {
                            Text("Password", color = Color.LightGray)
                        },
                        modifier = modifier.padding(10.dp).fillMaxWidth()
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Forgot Password?",
                        textAlign = TextAlign.End,
                        color=LightBlue50_100
                    )
                    Button(
                        onClick = {},
                        modifier=modifier.fillMaxWidth().padding(10.dp)
                    ) {
                        Text("Sign up")
                    }
                    Text(
                        modifier = modifier.padding(10.dp),
                        text = "Don't have an account? Sign up"
                    )
                }
            }
        }
    }
}