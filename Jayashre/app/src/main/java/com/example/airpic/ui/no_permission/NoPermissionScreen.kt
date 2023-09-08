package com.example.airpic.ui.no_permission

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.airpic.R


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
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFCCC2DC)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(180.dp)
                .offset(x = 0.dp, y = (-80.dp))
        ) {
            Image(
                painter = painterResource(id = R.mipmap.ic_no_permission_icon),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .offset(y = (-35.dp))
        ) {
            Text(
                text = "Allow your camera",
                color = Color.Black,
                fontSize = 23.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.offset( y = (10.dp))
        ){
            Text(
                text = "We will need your camera to give you better experience",
                color = Color.Black,
                fontSize = 17.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            }




        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.offset(y = 60.dp)

        ){
            Button(
                onClick = onRequestPermission,
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .width(180.dp)
                    .height(50.dp)
                    .clip(RoundedCornerShape(18.dp))
            ) {
                Text(
                    text = "Allow",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}




@Composable
fun IconWithText(
    iconResourceId: Int,
    text: String,
    modifier: Modifier = Modifier,
    iconSize: Dp = 48.dp,
    textColor: Color = Color.Black,
    fontSize: TextUnit = 16.sp,
    fontFamily: FontFamily = FontFamily.SansSerif,
    fontWeight: FontWeight = FontWeight.Bold
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconResourceId),
            contentDescription = null,
            modifier = Modifier.size(iconSize)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = textColor,
            fontSize = fontSize,
            fontFamily = fontFamily,
            fontWeight = fontWeight
        )
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
