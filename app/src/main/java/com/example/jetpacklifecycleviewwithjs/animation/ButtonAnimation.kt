package com.example.jetpacklifecycleviewwithjs.animation

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


enum class ButtonType {
    IDLE,
    PRESSED
}

@Preview
@Composable
fun JoinButton(onClick: (Boolean) -> Unit = {}) {
    var buttonState: ButtonType
            by remember {
                mutableStateOf(ButtonType.IDLE)
            }

    val shape = RoundedCornerShape(corner = CornerSize(12.dp))

//    val buttonBackgroundColor = if (buttonState == ButtonType.PRESSED) Color.White
//    else Color.Blue

//    val buttonBackgroundColor: Color by animateColorAsState(targetValue = if (buttonState == ButtonType.PRESSED) Color.White else Color.Blue)
//
    val iconAsset: ImageVector = if (buttonState == ButtonType.PRESSED) Icons.Default.Check
    else Icons.Default.Add
//
//    val iconTintColor: Color = if (buttonState == ButtonType.PRESSED) Color.Blue
//    else Color.White

    val transition = updateTransition(targetState = buttonState, label = "JoinButtonTransition")
    val duration = 600
    val buttonBackgroundColor: Color by transition.animateColor(
        transitionSpec = { tween(duration) },
        label = "Button background color"
    ) {
        if (it == ButtonType.PRESSED) Color.White else Color.Blue
    }

    val buttonWidth: Dp by transition.animateDp(
        transitionSpec = { tween(duration) },
        label = "Button width"
    ) {
        if (it == ButtonType.PRESSED) 32.dp else 70.dp
    }

    val textMaxWidth: Dp by transition.animateDp(
        transitionSpec = { tween(duration) },
        label = " Text Max Width"
    ) {
        if (it == ButtonType.PRESSED) 0.dp else 40.dp
    }

    val iconTintColor: Color by transition.animateColor(
        transitionSpec = { tween(duration) },
        label = "Icon tint color"
    ) {
        if (it == ButtonType.PRESSED) Color.Blue else Color.White
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(shape)
                .border(width = 1.dp, color = Color.Blue, shape = shape)
                .background(color = buttonBackgroundColor)
                .size(width = buttonWidth, height = 24.dp)
                .clickable {
                    buttonState = if (buttonState == ButtonType.IDLE) {
                        onClick.invoke(true)
                        ButtonType.PRESSED
                    } else {
                        onClick.invoke(false)
                        ButtonType.IDLE
                    }
                },
            contentAlignment = Alignment.Center
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = iconAsset,
                    contentDescription = "Plus icon",
                    tint = iconTintColor,
                    modifier = Modifier.size(16.dp)
                )


                Text(
                    text = "Join",
                    color = Color.White,
                    fontSize = 14.sp,
                    maxLines = 1,
                    modifier = Modifier.size(width = textMaxWidth, height = 24.dp)
                )
            }


        }
    }


}