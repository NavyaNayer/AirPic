package com.example.airpic.ui.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun InfoScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0XFF330066))
    ) {

        Button(
            onClick = {

                navController.navigateUp()
            },
            modifier = Modifier
                .size(with(LocalDensity.current) { 80.dp })
                .offset(x = 30.dp, y = 60.dp),
        ) {
            Text("Back to Camera")
        }
    }
}
