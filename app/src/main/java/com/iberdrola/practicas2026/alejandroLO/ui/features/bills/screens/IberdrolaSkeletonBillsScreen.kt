package com.iberdrola.practicas2026.alejandroLO.ui.features.bills.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.alejandroLO.ui.theme.IberdrolaTheme
import com.valentinilk.shimmer.ShimmerTheme
import com.valentinilk.shimmer.defaultShimmerTheme


@Composable
fun SkeletonScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("bills_skeleton")
    ) {
        LastBillSkeleton()
        Spacer(modifier = Modifier.height(8.5.dp))
        ElectronicBillsSkeleton()
        Spacer(modifier = Modifier.height(34.dp))
        TitleAndFilterSkeleton()
        Spacer(modifier = Modifier.height(24.dp))
        BillSkeletonYear()
        Spacer(modifier = Modifier.height(8.dp))
        BillSkeletonList()
    }
}

@Composable
fun SkeletonBox(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(4.dp)
) {

    Box(
        modifier = modifier
            .size(width, height)
            .background(Color(0x93E0E6E2), shape = shape) // Gris verdoso suave como la foto
    )
}

@Composable
fun LastBillSkeleton() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFFE0E6E2)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Column {
                    SkeletonBox(width = 120.dp, height = 20.dp)
                    Spacer(Modifier.height(8.dp))
                    SkeletonBox(width = 77.dp, height = 15.dp)
                }
                SkeletonBox(width = 30.dp, height = 30.dp, shape = RoundedCornerShape(8.dp))
            }
            Spacer(Modifier.height(24.dp))
            SkeletonBox(width = 70.dp, height = 35.dp)
            Spacer(Modifier.height(7.dp))
            SkeletonBox(width = 170.dp, height = 15.dp)
            Spacer(Modifier.height(7.dp))
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = IberdrolaTheme.colors.onSurfaceVariant.copy(alpha = 0.15f)
            )
            Spacer(Modifier.height(14.dp))
            SkeletonBox(width = 60.dp, height = 20.dp)
            Spacer(Modifier.height(3.dp))
        }
    }
}

@Composable
fun BillItemSkeleton() {
    Column() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = CenterVertically
        ) {
            Column {
                SkeletonBox(width = 100.dp, height = 17.dp, shape = RoundedCornerShape(8.dp)) // Cuadradito izquierda
                Spacer(Modifier.height(5.dp))
                SkeletonBox(width = 70.dp, height = 15.dp, shape = RoundedCornerShape(8.dp)) // Cuadradito izquierda
                Spacer(Modifier.height(5.dp))
                SkeletonBox(width = 57.dp, height = 23.dp, shape = RoundedCornerShape(8.dp)) // Cuadradito izquierda
            }

            Row(verticalAlignment = CenterVertically) {
                SkeletonBox(width = 50.dp, height = 20.dp, shape = RoundedCornerShape(4.dp)) // Flecha derecha
                Spacer(Modifier.width(15.dp))
                SkeletonBox(width = 25.dp, height = 30.dp, shape = RoundedCornerShape(4.dp))
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            thickness = 1.dp,
            color = Color(0xFFE0E0E0)
        )
    }
}

@Composable
fun BillSkeletonList(){
    Column(modifier = Modifier.fillMaxWidth()) {
        BillItemSkeleton()
        BillItemSkeleton()
        BillItemSkeleton()
        BillItemSkeleton()
    }
}

@Composable
fun ElectronicBillsSkeleton(){
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        SkeletonBox(
            width = 1000.dp,
            height = 65.dp,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
fun TitleAndFilterSkeleton(){
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SkeletonBox(width = 190.dp, height = 30.dp)
        SkeletonBox(
            width = 85.dp,
            height = 38.dp,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
fun BillSkeletonYear() {
    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
        SkeletonBox(width = 50.dp, height = 20.dp)
    }
}

fun shimmerTheme(): ShimmerTheme {
    return defaultShimmerTheme.copy(
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000, // Velocidad del brillo
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        shaderColors = listOf(
            Color.Gray.copy(alpha = 0.7f),
            Color.Gray.copy(alpha = 0.9f),
            Color.Gray.copy(alpha = 0.7f),
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLastBillSkeleton() {
    Column(modifier = Modifier.padding(10.dp)) {
        SkeletonScreen(modifier = Modifier)
    }
}