package com.example.managementapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.managementapp.R

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    var passwordVisible by remember { mutableStateOf(false) }
    val iconVisible = painterResource(id = R.drawable.see_icon)
    val iconInvisible = painterResource(id = R.drawable.un_see_icon)

    val effectiveVisualTransformation = if (keyboardType == KeyboardType.Password && !passwordVisible) {
        PasswordVisualTransformation()
    } else {
        VisualTransformation.None
    }

    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text(label, color = Color.LightGray) },
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp)
            .border(
                width = 1.dp,
                brush = if (isFocused) {
                    Brush.linearGradient(
                        colors = listOf(
                            colorResource(id = R.color.orange120),
                            colorResource(id = R.color.lightOrange)
                        )
                    )
                } else {
                    Brush.linearGradient(
                        colors = listOf(
                            colorResource(id = R.color.gray),
                            colorResource(id = R.color.gray)
                        )
                    )
                },
                shape = RoundedCornerShape(10.dp)
            ),
        placeholder = { Text(placeholder) },
        leadingIcon = {
            if (keyboardType == KeyboardType.Text) {
                Icon(Icons.Filled.Person, contentDescription = "Person")
            } else if (keyboardType == KeyboardType.Password) {
                Icon(Icons.Filled.Lock, contentDescription = "Lock")
            }
        },
        visualTransformation = effectiveVisualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedLabelColor = colorResource(id = R.color.purple_700),
        ),
        trailingIcon = if (keyboardType == KeyboardType.Password) {
            {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = if (passwordVisible) iconVisible else iconInvisible,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        } else null,
        shape = RoundedCornerShape(30.dp),
        interactionSource = interactionSource
    )
}