
package com.example.tuan4bai3

import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Blue = Color(0xFF489BEA)
private val Dark = Color(0xFF222831)

data class ResetPasswordData(
    val email: String,
    val otp: String,
    val password: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                SmartTasksApp()
            }
        }
    }
}

@Composable
fun SmartTasksApp() {

    // 0: Email, 1: OTP, 2: Password, 3: Confirm
    var screen by rememberSaveable {
        mutableIntStateOf(0)
    }

    var email by rememberSaveable {
        mutableStateOf("")
    }
    var otp by rememberSaveable {
        mutableStateOf("")
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }
    var confirmPassword by rememberSaveable {
        mutableStateOf("")
    }

    var error by rememberSaveable {
        mutableStateOf("")
    }

    var submitted by rememberSaveable {
        mutableStateOf(false)
    }

    var returnedEmail by rememberSaveable {
        mutableStateOf("")
    }
    var returnedOtp by rememberSaveable {
        mutableStateOf("")
    }
    var returnedPassword by rememberSaveable {
        mutableStateOf("")
    }

    // Khi nhan Back, quay ve man hinh truoc
    BackHandler(enabled = screen > 0) {
        screen--
        error = ""
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {

        // Nut quay lai cho man hinh 2, 3, 4
        Spacer(Modifier.height(16.dp))

        if (screen > 0) {
            TextButton(
                onClick = {
                    screen--
                    error = ""
                }
            ) {
                Text("‹ Back", color = Blue, fontSize = 18.sp)
            }
        } else {
            Spacer(Modifier.height(48.dp))
        }

        Spacer(Modifier.height(32.dp))

        // Logo chu de mo phong theo slide
        Text(
            text = "UTH",
            color = Color(0xFF218B80),
            fontSize = 40.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "UNIVERSITY OF TRANSPORT\nHO CHI MINH CITY",
            color = Color(0xFFC74C40),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(18.dp))

        Text(
            text = "SmartTasks",
            color = Blue,
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(36.dp))

        // Man hinh hien tai
        when (screen) {

            // MAN HINH 1: FORGOT PASSWORD
            0 -> {
                ScreenTitle(
                    "Forget Password?",
                    "Enter your Email, we will send you a verification code."
                )

                Spacer(Modifier.height(28.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        error = ""
                    },
                    label = { Text("Your Email") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(20.dp))

                PrimaryButton("Next") {
                    if (!Patterns.EMAIL_ADDRESS
                            .matcher(email.trim()).matches()) {
                        error = "Email không hợp lệ"
                    } else {
                        error = ""
                        submitted = false
                        screen = 1
                    }
                }

                if (submitted) {
                    Spacer(Modifier.height(24.dp))

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFDDEBFC)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                "Dữ liệu nhận từ màn hình 4",
                                fontWeight = FontWeight.Bold
                            )
                            Text("Email: $returnedEmail")
                            Text("OTP: $returnedOtp")
                            Text("Password: $returnedPassword")
                        }
                    }
                }
            }

            // MAN HINH 2: VERIFY CODE
            1 -> {
                ScreenTitle(
                    "Verify Code",
                    "Enter the code sent to your registered Email"
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = email,
                    color = Blue,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(24.dp))

                OutlinedTextField(
                    value = otp,
                    onValueChange = {
                        if (it.length <= 6 &&
                            it.all { char -> char.isDigit() }) {
                            otp = it
                            error = ""
                        }
                    },
                    label = { Text("Verification code") },
                    placeholder = { Text("6 digits") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    "Mã OTP demo: 123456",
                    color = Color.Gray,
                    fontSize = 12.sp
                )

                Spacer(Modifier.height(24.dp))

                PrimaryButton("Next") {
                    if (otp != "123456") {
                        error = "Mã OTP không chính xác"
                    } else {
                        error = ""
                        screen = 2
                    }
                }
            }

            // MAN HINH 3: RESET PASSWORD
            2 -> {
                ScreenTitle(
                    "Create new password",
                    "Your new password must be different from your previously used password"
                )

                Spacer(Modifier.height(28.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        error = ""
                    },
                    label = { Text("Password") },
                    visualTransformation =
                        PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        error = ""
                    },
                    label = { Text("Confirm Password") },
                    visualTransformation =
                        PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(24.dp))

                PrimaryButton("Next") {
                    if (password.length < 6) {
                        error = "Mật khẩu phải có ít nhất 6 ký tự"
                    } else if (password != confirmPassword) {
                        error = "Hai mật khẩu không khớp"
                    } else {
                        error = ""
                        screen = 3
                    }
                }
            }

            // MAN HINH 4: CONFIRM
            3 -> {
                ScreenTitle(
                    "Confirm",
                    "We are here to help you!"
                )

                // Gom du lieu tu cac man hinh truoc
                val data = ResetPasswordData(
                    email = email,
                    otp = otp,
                    password = password
                )

                Spacer(Modifier.height(28.dp))

                ReadOnlyField("Email", data.email)
                Spacer(Modifier.height(12.dp))

                ReadOnlyField("OTP", data.otp)
                Spacer(Modifier.height(12.dp))

                ReadOnlyField(
                    "Password",
                    "•".repeat(data.password.length)
                )

                Spacer(Modifier.height(24.dp))

                PrimaryButton("Submit") {

                    // Truyen du lieu tro lai man hinh 1
                    returnedEmail = data.email
                    returnedOtp = data.otp

                    // Chi luu gia tri da che de hien thi
                    returnedPassword =
                        "•".repeat(data.password.length)

                    submitted = true
                    password = ""
                    confirmPassword = ""
                    otp = ""
                    error = ""
                    screen = 0
                }
            }
        }

        // Hien thi thong bao loi
        if (error.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = error,
                color = Color.Red,
                fontSize = 13.sp
            )
        }

        Spacer(Modifier.height(40.dp))
    }
}

@Composable
fun ScreenTitle(title: String, description: String) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Dark,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )

    Spacer(Modifier.height(12.dp))

    Text(
        text = description,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = Color.Gray,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
fun PrimaryButton(
    title: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Blue
        )
    ) {
        Text(title, fontSize = 16.sp)
    }
}

@Composable
fun ReadOnlyField(label: String, value: String) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        label = { Text(label) },
        readOnly = true,
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun SmartTasksPreview() {
    MaterialTheme {
        SmartTasksApp()
    }
}
