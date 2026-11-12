package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ShopProduct
import com.example.data.repository.GameRepository
import com.example.ui.components.TopBarHeader
import com.example.ui.theme.CoinYellow
import com.example.ui.theme.GemBlue
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldSecondary
import java.text.NumberFormat
import java.util.Locale

@Composable
fun BazaarStoreScreen(
    coins: Int,
    gems: Int,
    bonusWordsCount: Int,
    isSoundEnabled: Boolean,
    isWatchingAd: Boolean,
    adProgress: Float,
    isVip: Boolean,
    isAdsRemoved: Boolean,
    onToggleSound: () -> Unit,
    onSelectProduct: (ShopProduct) -> Unit,
    onWatchAd: () -> Unit,
    onBack: () -> Unit
) {
    val formatter = NumberFormat.getNumberInstance(Locale("fa", "IR"))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopBarHeader(
            coins = coins,
            gems = gems,
            bonusWordsCount = bonusWordsCount,
            isSoundEnabled = isSoundEnabled,
            onToggleSound = onToggleSound,
            onOpenStore = {},
            onBack = onBack
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .testTag("bazaar_store_list"),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Bazaar Official Store Header
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "فروشگاه رسمی بازار",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00A651)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF00A651)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.ShoppingBag, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "سیستم پرداخت درون‌برنامه‌ای بازار با کارت‌های عضو شتاب",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Rewarded Video Ad Free Coins Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF004D40)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = onWatchAd,
                                enabled = !isWatchingAd,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A651)),
                                modifier = Modifier.testTag("watch_rewarded_ad_btn")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (isWatchingAd) "در حال مشاهده..." else "مشاهده و دریافت",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(imageVector = Icons.Default.Videocam, contentDescription = null, tint = Color.White)
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "سکه و الماس رایگان!",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "تماشای ویدیوی کوتاه (+۶۰ سکه + ۱ الماس)",
                                    fontSize = 11.sp,
                                    color = Color(0xFFFDE68A)
                                )
                            }
                        }

                        if (isWatchingAd) {
                            Spacer(modifier = Modifier.height(10.dp))
                            LinearProgressIndicator(
                                progress = { adProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = Color(0xFF00A651)
                            )
                        }
                    }
                }
            }

            // Store Products List
            items(GameRepository.shopProducts) { product ->
                ShopProductCard(
                    product = product,
                    formatter = formatter,
                    onClick = { onSelectProduct(product) }
                )
            }

            // Trust & Security Footer
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "تمامی پرداخت‌ها از درگاه امن شاپرک و حساب کاربری کافه بازار انجام می‌شود.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = Color(0xFF00A651), modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ShopProductCard(
    product: ShopProduct,
    formatter: NumberFormat,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .then(
                if (product.isBestValue) Modifier.border(2.dp, GoldSecondary, RoundedCornerShape(18.dp))
                else Modifier
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Badge tag
            if (product.badgeText != null) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (product.isBestValue) Color(0xFFF59E0B) else Color(0xFF00796B),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = product.badgeText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Buy Button with Price
                Button(
                    onClick = onClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A651)),
                    modifier = Modifier.testTag("buy_btn_${product.id}")
                ) {
                    Text(
                        text = "${formatter.format(product.priceToman)} تومان",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // Title & Details
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = product.titleFa,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = product.descriptionFa,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        textAlign = TextAlign.End
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Rewards included
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (product.coins > 0) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "+${formatter.format(product.coins)} سکه", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CoinYellow)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(imageVector = Icons.Default.MonetizationOn, contentDescription = null, tint = CoinYellow, modifier = Modifier.size(16.dp))
                    }
                }
                if (product.gems > 0) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "+${formatter.format(product.gems)} الماس", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GemBlue)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(imageVector = Icons.Default.Diamond, contentDescription = null, tint = GemBlue, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}
