package com.nrin31266.shoppingapp.presentation.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nrin31266.shoppingapp.domain.model.ProductDataModel

@Composable
@Preview(showSystemUi = true)
fun ProductItem(
    modifier: Modifier = Modifier,
    product:ProductDataModel = ProductDataModel(
        image = "https://picsum.photos/400/200"
        , name = "Son Tung MTP",
        sellingPrice = "100000",
        mrpPrice = "200000"
    ),
    onProductClick: () -> Unit = {},

) {
    Card (
        modifier = modifier.fillMaxWidth().clickable {
            onProductClick()

        },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),

    ){
        Column (

        ){
            AsyncImage(
                model = product.image, contentDescription = "", modifier= Modifier.fillMaxWidth()
                    .background(color = Color.White).height(165.dp).clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
                contentScale = ContentScale.Crop
            )
            Column (
                modifier = Modifier.padding(8.dp)
            ){
                Text(text = product.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(8.dp))
                Row (

                ){
                    Text(text = "đ"+product.sellingPrice,
                        style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f))
                    Spacer(Modifier.weight(0.1f))
                    Text(text = "đ"+product.mrpPrice,
                        style = MaterialTheme.typography.bodyMedium.copy(Color.Gray),
                        modifier = Modifier.weight(1f)
                        , textDecoration = TextDecoration.LineThrough
                    )

                }
            }

        }
    }
}