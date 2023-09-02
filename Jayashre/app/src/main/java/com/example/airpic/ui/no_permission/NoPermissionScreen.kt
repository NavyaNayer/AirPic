package com.example.airpic.ui.no_permission

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun NoPermissionScreen(
    onRequestPermission: () -> Unit
) {

    NoPermissionContent(
        onRequestPermission = onRequestPermission
    )
}

@Composable
fun NoPermissionContent(
    onRequestPermission: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFCCC2DC)
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Please grant the permission to use the camera to use this app",
            // Modify the text font and color here
            color = Color.Black, // Change the text color to black
            fontSize = 16.sp, // Change the text size
            fontFamily = FontFamily.SansSerif, // Change the font family
            fontWeight = FontWeight.Bold, // Change the font weight
        )
        Button(
            onClick = onRequestPermission,
            // Modify the button style here
            modifier = Modifier.padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Blue, // Change the button background color
                contentColor = Color.White // Change the button text color
            )
        ) {
            Icon(imageVector = Icons.Default.Camera, contentDescription = "Camera")
            Text(
                text = "Grant permission",
                // Modify the button text style here
                fontSize = 18.sp, // Change the button text size
                fontWeight = FontWeight.Bold // Change the button text font weight
            )
        }
    }
}

@Preview
@Composable
private fun Preview_NoPermissionContent() {
    NoPermissionContent(
        onRequestPermission = {}
    )
}

@Preview
@Composable
fun NoPermissionContentPreview() {
    NoPermissionContent(onRequestPermission = {})
}