package com.nrin31266.shoppingapp.presentation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nrin31266.shoppingapp.R
import com.nrin31266.shoppingapp.presentation.utils.CustomTextField
import com.nrin31266.shoppingapp.presentation.utils.OtherLogin
import com.nrin31266.shoppingapp.presentation.utils.TextDivider

@Preview
@Composable
fun SignupScreen() {
    val context = LocalContext.current

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize().padding(horizontal = 12.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Signup", fontSize = 24.sp, style = TextStyle(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(vertical = 16.dp).align(Alignment.Start))

        CustomTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = "Full Name",
            modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
            leadingIcon = Icons.Default.Person,
        )
        CustomTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
            leadingIcon = Icons.Default.Email,
        )
        CustomTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
            leadingIcon = Icons.Default.Lock,
            visualTransformation = PasswordVisualTransformation()
        )

        Button({
            if(email.isNotEmpty() && password.isNotEmpty() && fullName.isNotEmpty()){

            }else{
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }

        }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), shape = RoundedCornerShape(8.dp)
        , colors = ButtonDefaults.buttonColors(colorResource(id = R.color.elegant_gold))) {
            Text("Signup", color = colorResource(id = R.color.white), fontSize = 16.sp)
        }

        TextDivider(text = "OR")

        Row (modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            , verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center){
            Text("You have an account?")
            Spacer(modifier = Modifier.size(4.dp))
            TextButton({

            }) {
                Text("Login", color = colorResource(id = R.color.elegant_gold), fontSize = 16.sp)
            }
        }

        OtherLogin()

    }


}