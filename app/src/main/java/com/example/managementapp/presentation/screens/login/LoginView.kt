package com.example.managementapp.presentation.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.managementapp.R
import com.example.managementapp.presentation.components.CustomGradientButton
import com.example.managementapp.presentation.components.CustomTextField

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginViewPreview() {
    val usernameState = remember { mutableStateOf("sampleUser") }
    val tokenState = remember { mutableStateOf("sampleToken") }
    val isLoadingState = remember { mutableStateOf(false) }
    val errorState = remember { mutableStateOf<String?>(null) }

    LoginView(
        username = usernameState.value,
        token = tokenState.value,
        isLoading = isLoadingState.value,
        error = errorState.value,
        onUsernameChanged = { usernameState.value = it },
        onTokenChanged = { tokenState.value = it },
        onLoginClick = { }
    )
}

@Composable
fun LoginView(
    username: String,
    token: String,
    isLoading: Boolean,
    error: String?,
    onUsernameChanged: (String) -> Unit,
    onTokenChanged: (String) -> Unit,
    onLoginClick: () -> Unit
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Spacer(modifier = Modifier.height(5.dp))
                    Row (
                        modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.logo3),
                            contentDescription = "logo",
                            modifier = Modifier
                                .size(50.dp),
                            )
                        Text("GitHub Login", style = MaterialTheme.typography.h6)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    CustomTextField(
                        value = username,
                        onValueChange = onUsernameChanged,
                        label = "GitHub Username",
                        placeholder = "e.g., Mahmoud22",
                        keyboardType = KeyboardType.Text
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomTextField(
                        value = token,
                        onValueChange = onTokenChanged,
                        label = "Personal Access Token",
                        placeholder = "e.g., PAT",
                        keyboardType = KeyboardType.Password,
                        visualTransformation = PasswordVisualTransformation()
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    CustomGradientButton(
                        onClick = onLoginClick,
                        isLoading = isLoading
                    )

                    error?.let {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = it, color = MaterialTheme.colors.error)
                    }
                }
            }
        }
    }
}