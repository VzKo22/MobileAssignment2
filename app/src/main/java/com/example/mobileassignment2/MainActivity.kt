package com.example.mobileassignment2

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*
import java.util.Locale

val DarkBackground = Color(0xB7000000)
val CardBackground = Color(0x32929DA2)
val color1 = Color(0xFF0099FF)
val color2 = Color(0xFFFF0000)
val color3 = Color(0xFFECEC8A)
val color4 = Color(0xFF1BD0B2)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = DarkBackground
            ) {
                StudentFormScreen()
            }
        }
    }
}

@Preview
@Composable
fun StudentFormScreen() {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var nameState by remember { mutableStateOf("") }
    var lastNameState by remember { mutableStateOf("") }
    var emailState by remember { mutableStateOf("") }
    var dateState by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("") }
    var isAgreed by remember { mutableStateOf(false) }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val formattedDay = String.format(Locale.US, "%02d", dayOfMonth)
            val formattedMonth = String.format(Locale.US, "%02d", month + 1)
            dateState = "$formattedDay/$formattedMonth/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

    val directions = listOf("Android", "iOS", "Web")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "STUDENT REGISTRATION",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = color1,
                letterSpacing = 2.sp
            ),
            modifier = Modifier.padding(vertical = 12.dp)
        )

        CustomTextField(value = nameState, onValueChange = { nameState = it }, label = "First Name")
        CustomTextField(value = lastNameState, onValueChange = { lastNameState = it }, label = "Last Name")
        CustomTextField(value = emailState, onValueChange = { emailState = it }, label = "Email")

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBackground, RoundedCornerShape(12.dp))
                .border(1.dp, color1.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .clickable { datePickerDialog.show() }
                .padding(16.dp)
        ) {
            Text(
                text = dateState.ifEmpty { "Select Date of Birth (DD/MM/YYYY)" },
                color = if (dateState.isEmpty()) color4 else color3,
                fontSize = 16.sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBackground, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "Your Favorite Direction",
                color = color1,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            directions.forEach { direction ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedOption = direction }
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (selectedOption == direction),
                        onClick = { selectedOption = direction },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = color1,
                            unselectedColor = color4
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = direction, color = color3, fontSize = 16.sp)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBackground, RoundedCornerShape(12.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "I agree to the terms and conditions",
                color = color3,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = isAgreed,
                onCheckedChange = { isAgreed = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = DarkBackground,
                    checkedTrackColor = color1,
                    uncheckedThumbColor = color4,
                    uncheckedTrackColor = CardBackground
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val isValidEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(emailState.trim()).matches()
                val allTextFieldsFilled = nameState.isNotBlank() &&
                        lastNameState.isNotBlank() &&
                        emailState.isNotBlank() &&
                        isValidEmail &&
                        dateState.isNotBlank()
                val isDirectionSelected = selectedOption.isNotBlank()

                if (allTextFieldsFilled && isDirectionSelected && isAgreed) {
                    Toast.makeText(context, "მონაცემები გაიგზავნა!", Toast.LENGTH_LONG).show()
                }
                else
                    Toast.makeText(context, "შეავსეთ ყველა ველი!", Toast.LENGTH_LONG).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = color2)
        ) {
            Text(
                text = "SUBMIT",
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    letterSpacing = 1.sp
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label, color = color4) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = color3,
            unfocusedTextColor = color3,
            focusedBorderColor = color1,
            unfocusedBorderColor = color4.copy(alpha = 0.5f),
            focusedContainerColor = CardBackground,
            unfocusedContainerColor = CardBackground
        ),
        singleLine = true
    )
}