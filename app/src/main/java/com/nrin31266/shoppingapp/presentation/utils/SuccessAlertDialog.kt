package com.nrin31266.shoppingapp.presentation.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nrin31266.shoppingapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showSystemUi = true)
fun SuccessAlertDialog (
    onClick : ()->Unit = {},
    desc : String = "Congratulation, you have completed your registration!",
    buttonText : String = "Go to home"
){
    BasicAlertDialog (
        onDismissRequest = {},
        modifier = Modifier.background(shape = RoundedCornerShape(16.dp), color = Color.White),
        content = {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)

                ,
                horizontalAlignment = Alignment.CenterHorizontally

            ){
                Box(
                    modifier = Modifier.size(64.dp)
                        .background(color = colorResource(R.color.success_green), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ){
                    Icon(imageVector = Icons.Default.Check, contentDescription = ""
                    , tint = Color.Gray, modifier = Modifier.size(50.dp)
                    )

                }
                Spacer(Modifier.height(16.dp))
                Text("Success", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                Text(desc
                , textAlign = TextAlign.Center, fontSize = 16.sp)
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = { onClick() },
                    modifier = Modifier.height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(colorResource(id = R.color.elegant_gold))
                ) {
                    Text(
                        text = buttonText,
                        fontSize = 20.sp
                    )
                }

            }
        }
    )
}