package com.example.tuan6role

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan6role.ui.theme.Tuan6roleTheme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

private val PrimaryOrange = Color(0xFFFF7A00)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Tuan6roleTheme {
                LoginScreen()
            }
        }
    }
}

@Composable
fun LoginScreen() {

    var currentStep by remember {
        mutableStateOf<PermissionStep?>(null)
    }

    fun nextStep() {
        currentStep = when (currentStep) {
            PermissionStep.LOCATION -> PermissionStep.NOTIFICATION
            PermissionStep.NOTIFICATION -> PermissionStep.CAMERA
            PermissionStep.CAMERA -> null
            null -> null
        }
    }


    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Android vừa trả kết quả cấp quyền.
        // Dù cho phép hay từ chối, vẫn chuyển bước.
        nextStep()
    }


    fun requestCurrentPermission(step: PermissionStep) {

        val permission = when (step) {
            PermissionStep.LOCATION ->
                Manifest.permission.ACCESS_COARSE_LOCATION

            PermissionStep.NOTIFICATION -> {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    // Android dưới 13 không dùng quyền runtime này.
                    nextStep()
                    return
                }
                Manifest.permission.POST_NOTIFICATIONS
            }

            PermissionStep.CAMERA ->
                Manifest.permission.CAMERA
        }

        val alreadyGranted = ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED

        if (alreadyGranted) {
            // Đã có quyền thì không cần hỏi Android lần nữa.
            nextStep()
        } else {
            // Chưa có quyền: nhờ Android hiện yêu cầu cấp quyền.
            permissionLauncher.launch(permission)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 109.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Button(
            onClick = {
                currentStep = PermissionStep.LOCATION
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryOrange
            )
        ) {
            Text(
                text = "Login",
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = {
                currentStep = PermissionStep.LOCATION
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.5.dp, PrimaryOrange),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White,
                contentColor = PrimaryOrange
            )
        ) {
            Text(
                text = "Continue without account",
                fontSize = 16.sp
            )
        }
    }
    currentStep?.let { step ->
        PermissionDialog(
            step = step,
            onAllow = {
                requestCurrentPermission(step)
            },
            onSkip = {
                nextStep()
            }
        )
    }
}