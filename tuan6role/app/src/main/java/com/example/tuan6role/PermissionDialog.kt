package com.example.tuan6role

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Orange = Color(0xFFFF7A00)

enum class PermissionStep {
    LOCATION,
    NOTIFICATION,
    CAMERA
}

@Composable
fun PermissionDialog(
    step: PermissionStep,
    onAllow: () -> Unit,
    onSkip: () -> Unit
) {
    val title: String
    val description: String
    val buttonLabel: String
    val icon: ImageVector

    when (step) {
        PermissionStep.LOCATION -> {
            title = "Access your location"
            description = "Allow location access to discover nearby services and useful information."
            buttonLabel = "Allow"
            icon = Icons.Outlined.LocationOn
        }

        PermissionStep.NOTIFICATION -> {
            title = "Enable notifications"
            description = "Turn on notifications to receive important updates and reminders."
            buttonLabel = "Turn on"
            icon = Icons.Outlined.Notifications
        }

        PermissionStep.CAMERA -> {
            title = "Access your camera"
            description = "Enable camera access when you need to scan or capture information."
            buttonLabel = "Turn on"
            icon = Icons.Outlined.CameraAlt
        }
    }

    AlertDialog(
        onDismissRequest = {
            // Không đóng bằng cách chạm ra ngoài.
            // Người dùng chọn Allow hoặc Skip.
        },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Orange,
                modifier = Modifier.size(54.dp)
            )
        },
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text = description,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            Button(
                onClick = onAllow,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(buttonLabel)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onSkip,
                border = BorderStroke(1.dp, Orange),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "Skip for now",
                    color = Orange
                )
            }
        },
        containerColor = Color.White,
        titleContentColor = Color(0xFF202020),
        textContentColor = Color(0xFF666666),
        shape = RoundedCornerShape(24.dp)
    )
}
