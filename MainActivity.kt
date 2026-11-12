package com.followerplus.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Black = Color(0xFF090909)
private val Panel = Color(0xFF171717)
private val Gold = Color(0xFFD6AD55)
private val Cream = Color(0xFFF5E7C4)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { FollowerPlusApp() }
    }
}

@Composable
fun FollowerPlusApp() {
    var page by remember { mutableStateOf("خانه") }
    var phone by remember { mutableStateOf("") }
    var signedIn by remember { mutableStateOf(false) }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Gold, background = Black, surface = Panel,
            onPrimary = Black, onBackground = Cream, onSurface = Cream
        )
    ) {
        Surface(Modifier.fillMaxSize(), color = Black) {
            if (!signedIn) {
                LoginScreen(phone, { phone = it }) {
                    // Prototype only: real SMS OTP must be connected to Firebase/Auth provider.
                    signedIn = phone.filter { it.isDigit() }.length >= 10
                }
            } else {
                Column(Modifier.fillMaxSize()) {
                    Header()
                    when (page) {
                        "خانه" -> HomeScreen { page = it }
                        "خدمات" -> ServicesScreen()
                        "سفارش‌ها" -> OrdersScreen()
                        "کیف پول" -> WalletScreen()
                        "پروفایل" -> ProfileScreen(phone)
                        "مدیریت" -> AdminScreen()
                    }
                    Spacer(Modifier.weight(1f))
                    NavigationBar(containerColor = Panel, contentColor = Gold) {
                        listOf("خانه", "خدمات", "سفارش‌ها", "کیف پول", "پروفایل").forEach {
                            NavigationBarItem(
                                selected = page == it,
                                onClick = { page = it },
                                icon = { Text(when(it) {
                                    "خانه" -> "⌂"; "خدمات" -> "✦"; "سفارش‌ها" -> "▤"
                                    "کیف پول" -> "◈"; else -> "●"
                                }, fontSize = 18.sp) },
                                label = { Text(it, fontSize = 10.sp) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Gold, selectedTextColor = Gold,
                                    indicatorColor = Color(0xFF3A2D16),
                                    unselectedIconColor = Cream, unselectedTextColor = Cream
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Header() {
    Row(
        Modifier.fillMaxWidth().background(Panel).padding(18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("FOLLOWER PLUS", color = Gold, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
            Text("فالوور پلاس", color = Cream, fontSize = 12.sp)
        }
        Text("🌶", fontSize = 34.sp) // جایگزین موقت لوگوی فلفل طلایی اختصاصی
    }
}

@Composable
fun LoginScreen(phone: String, onPhone: (String) -> Unit, onContinue: () -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(26.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🌶", fontSize = 76.sp)
        Text("FOLLOWER PLUS", color = Gold, fontSize = 25.sp, fontWeight = FontWeight.Black)
        Text("ورود به حساب کاربری", color = Cream, modifier = Modifier.padding(top = 28.dp, bottom = 18.dp))
        OutlinedTextField(
            value = phone, onValueChange = onPhone,
            label = { Text("شماره موبایل") },
            placeholder = { Text("مثلاً 0912xxxxxxx") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Gold, focusedLabelColor = Gold,
                unfocusedBorderColor = Color.Gray, focusedTextColor = Cream,
                unfocusedTextColor = Cream
            )
        )
        GoldButton("ادامه") { onContinue() }
        Text("نسخه نمایشی؛ ورود پیامکی واقعی پس از اتصال سرویس احراز هویت فعال می‌شود.",
            color = Color.LightGray, fontSize = 11.sp, textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 14.dp))
    }
}

@Composable
fun HomeScreen(open: (String) -> Unit) {
    LazyColumn(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Text("خوش آمدی 👋", color = Cream, fontSize = 23.sp, fontWeight = FontWeight.Bold)
            Text("مرکز مدیریت خدمات و شبکه‌های اجتماعی", color = Color.LightGray)
        }
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(18.dp)) {
                Column(Modifier.fillMaxWidth().padding(18.dp)) {
                    Text("موجودی کیف پول", color = Color.LightGray)
                    Text("۰ تومان", color = Gold, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Text("برای نمایش موجودی واقعی، درگاه پرداخت و سرور باید متصل شوند.", color = Color.Gray, fontSize = 11.sp)
                }
            }
        }
        item { FeatureCard("خدمات و بسته‌ها", "مشاهده خدمات مجاز و تعرفه‌ها") { open("خدمات") } }
        item { FeatureCard("پیگیری سفارش‌ها", "وضعیت و تاریخچه درخواست‌ها") { open("سفارش‌ها") } }
        item { FeatureCard("کیف پول", "شارژ حساب و سوابق پرداخت") { open("کیف پول") } }
        item { FeatureCard("پنل مدیریت", "مدیریت تعرفه‌ها و سفارش‌ها (نمایشی)") { open("مدیریت") } }
    }
}

@Composable
fun FeatureCard(title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Panel),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("🌶", fontSize = 25.sp, modifier = Modifier.padding(end = 14.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = Gold, fontWeight = FontWeight.Bold)
                Text(subtitle, color = Color.LightGray, fontSize = 12.sp)
            }
            Text("‹", color = Gold, fontSize = 28.sp)
        }
    }
}

@Composable
fun ServicesScreen() {
    Column(Modifier.fillMaxSize().padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("خدمات", color = Gold, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("بسته‌های قابل خرید را مدیر پس از راه‌اندازی سرور تعریف می‌کند.", color = Cream)
        listOf(
            "تحلیل و گزارش عملکرد صفحه",
            "تقویم و زمان‌بندی محتوا",
            "طراحی و آماده‌سازی محتوای شبکه اجتماعی",
            "گزارش تعامل و رشد ارگانیک"
        ).forEach { FeatureCard(it, "تعرفه و جزئیات پس از اتصال پنل مدیریت") {} }
    }
}

@Composable
fun OrdersScreen() {
    EmptyState("سفارش‌های من", "هنوز سفارشی ثبت نشده است.")
}

@Composable
fun WalletScreen() {
    EmptyState("کیف پول", "پرداخت واقعی پس از انتخاب درگاه و اتصال امن سرور فعال می‌شود.")
}

@Composable
fun ProfileScreen(phone: String) {
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("پروفایل کاربری", color = Gold, fontSize = 23.sp, fontWeight = FontWeight.Bold)
        Text("شماره موبایل: $phone", color = Cream)
        Text("حساب آزمایشی", color = Color.LightGray)
    }
}

@Composable
fun AdminScreen() {
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("مدیریت", color = Gold, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("این صفحه فعلاً نمایشی است. دسترسی مدیر باید سمت سرور و با نقش امن کنترل شود.", color = Cream)
        listOf("تعریف خدمات و تعرفه‌ها", "مدیریت سفارش‌ها", "مدیریت کاربران", "گزارش پرداخت‌ها").forEach {
            FeatureCard(it, "پس از راه‌اندازی بک‌اند فعال می‌شود") {}
        }
    }
}

@Composable
fun EmptyState(title: String, message: String) {
    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(title, color = Gold, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(message, color = Cream, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 12.dp))
    }
}

@Composable
fun GoldButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick, modifier = Modifier.fillMaxWidth().padding(top = 18.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Black),
        shape = RoundedCornerShape(12.dp)) {
        Text(text, fontWeight = FontWeight.Bold)
    }
}
