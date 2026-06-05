package com.drnkgn.juicejuicejuice.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.drnkgn.juicejuicejuice.ui.theme.JuiceJuiceJuiceTheme
import com.drnkgn.juicejuicejuice.ui.theme.SurfaceTonalA0
import com.drnkgn.juicejuicejuice.ui.theme.SurfaceTonalA10
import com.drnkgn.juicejuicejuice.ui.theme.extColors

@Composable
fun JJJTextField(
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    value: String = "",
    isError: Boolean = false,
    leadingIcon: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SurfaceTonalA10,
            unfocusedBorderColor = SurfaceTonalA10,
            focusedContainerColor = SurfaceTonalA0,
            unfocusedContainerColor = SurfaceTonalA0,
            errorContainerColor = MaterialTheme.extColors.inputBgError.copy(alpha = 0.2f)
        ),
        isError = isError,
        placeholder = placeholder,
        value = value,
        leadingIcon = leadingIcon,
        keyboardOptions = keyboardOptions,
        onValueChange = onValueChange
    )
}

@Preview
@Composable
fun JJJTextFieldPreview() {
    JuiceJuiceJuiceTheme {
        JJJTextField(
            value = "",
            isError = false,
            placeholder = {
                Text("Enter something...", color = MaterialTheme.extColors.placeholder)
            },
            onValueChange = { }
        )
    }
}