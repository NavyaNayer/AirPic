package com.example.airpic.ui.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.airpic.R

//navController.navigateUp()
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0XFF330066),
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "About",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White,
                        fontFamily = FontFamily.Cursive,
                        fontSize = 30.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Going to Main Frame",
                            tint = Color.White,
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        InfoContent(innerPadding)
    }
}

@Composable
fun InfoContent(innerPadding: PaddingValues) {
    //#CCC2DC
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(Color(0XFFccc2Dc)),
        contentAlignment = Alignment.Center
    ) {
        Image (
            painter = painterResource(id = R.drawable.animation_one),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .offset(y = -(250.dp))
        );
        Text(
            text = "AirPic",
            fontSize = 50.sp,
            modifier = Modifier.offset(y = -(125.dp)),
            color = Color(0XFF330066),
            fontFamily = FontFamily.Cursive
        );
        Text("\n" +
                "Welcome to AirPic, revolutionizing photo capture with an innovative gesture-powered experience." + "\n" +
                "Say goodbye to complexity – effortlessly snap perfect shots with intuitive motions".trimIndent(),
            color = Color.Black,
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
            modifier = Modifier.offset(y = -(35.dp)))

    }
}