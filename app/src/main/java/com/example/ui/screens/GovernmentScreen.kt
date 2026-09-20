package com.example.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.EconomyScaleEngine
import com.example.model.GameState
import com.example.model.PublicServiceType
import com.example.model.TaxProfile
import com.example.ui.theme.CommandBorder
import com.example.ui.theme.CommandDarkBg
import com.example.ui.theme.CommandSurface
import com.example.ui.theme.CommandSurfaceElevated
import com.example.ui.theme.CrisisRed
import com.example.ui.theme.GrowthGreen
import com.example.ui.theme.SovereignGold
import com.example.ui.theme.TacticalCyan
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun GovernmentScreen(
    state: GameState,
    onAdjustServiceFunding: (PublicServiceType, Int) -> Unit,
    onTaxRateChange: (TaxProfile) -> Unit,
    onPreviewPolicy: (String, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val player = state.playerCountry ?: return
    val gov = state.governmentServices

    var selectedTab by remember { mutableStateOf(0) } // 0: Services, 1: Taxes & Fiscal, 2: Debt & Welfare

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CommandDarkBg)
            .padding(14.dp)
            .verticalScroll(rememberScrollState())
            .testTag("government_screen")
    ) {
        // Title Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "🏛️ إدارة الحكومة والخدمات العامة",
                    color = SovereignGold,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "الخدمات المدنية، السياسة الضريبية، والرعاية الاجتماعية",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
            Text(
                text = "الرضا العام: ${player.stability}%",
                color = if (player.stability >= 60) GrowthGreen else SovereignGold,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tab Selector (Services / Taxes / Debt)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(CommandSurfaceElevated)
                .border(1.dp, CommandBorder, RoundedCornerShape(10.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            GovTabButton("الخدمات والمرافق", selectedTab == 0) { selectedTab = 0 }
            GovTabButton("الضرائب والجمارك", selectedTab == 1) { selectedTab = 1 }
            GovTabButton("الديون والرعاية", selectedTab == 2) { selectedTab = 2 }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (selectedTab) {
            0 -> {
                // Public Services Section
                gov?.services?.values?.forEach { service ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(CommandSurface)
                            .border(1.dp, CommandBorder, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = service.type.icon, fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = service.type.titleAr,
                                            color = TextPrimary,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "مستوى التمويل: ${service.fundingLevelPercent}% • كفاءة: ${service.efficiencyPercent}%",
                                            color = TextSecondary,
                                            fontSize = 10.sp
                                        )
                                    }
                                }

                                Text(
                                    text = "$${String.format(Locale.US, "%.1f", service.totalMonthlyExpenseMillions)}M/شهر",
                                    color = SovereignGold,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = (service.efficiencyPercent / 100f).coerceIn(0f, 1f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = TacticalCyan,
                                trackColor = CommandSurfaceElevated
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // Adjust funding buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedButton(
                                    onClick = { onAdjustServiceFunding(service.type, service.fundingLevelPercent - 10) },
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Text(text = "-10% خفض", color = TextSecondary, fontSize = 10.sp)
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Button(
                                    onClick = { onAdjustServiceFunding(service.type, service.fundingLevelPercent + 10) },
                                    colors = ButtonDefaults.buttonColors(containerColor = TacticalCyan),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Text(text = "+10% دعم", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
            1 -> {
                // Taxes and Fiscal Policy
                val profile = player.taxProfile
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(CommandSurface)
                        .border(1.dp, CommandBorder, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        TaxControlRow(
                            label = "ضريبة الشركات",
                            currentPercent = (profile.corporateTaxRate * 100).toInt(),
                            onPreview = { onPreviewPolicy("ضريبة الشركات", 5.0) },
                            onIncrease = { onTaxRateChange(profile.copy(corporateTaxRate = (profile.corporateTaxRate + 0.02).coerceAtMost(0.40))) },
                            onDecrease = { onTaxRateChange(profile.copy(corporateTaxRate = (profile.corporateTaxRate - 0.02).coerceAtLeast(0.05))) }
                        )

                        TaxControlRow(
                            label = "ضريبة الدخل العامة",
                            currentPercent = (profile.incomeTaxRate * 100).toInt(),
                            onPreview = { onPreviewPolicy("ضريبة الدخل", 3.0) },
                            onIncrease = { onTaxRateChange(profile.copy(incomeTaxRate = (profile.incomeTaxRate + 0.02).coerceAtMost(0.45))) },
                            onDecrease = { onTaxRateChange(profile.copy(incomeTaxRate = (profile.incomeTaxRate - 0.02).coerceAtLeast(0.05))) }
                        )

                        TaxControlRow(
                            label = "ضريبة القيمة المضافة (VAT)",
                            currentPercent = (profile.vatRate * 100).toInt(),
                            onPreview = { onPreviewPolicy("ضريبة القيمة المضافة", 2.0) },
                            onIncrease = { onTaxRateChange(profile.copy(vatRate = (profile.vatRate + 0.01).coerceAtMost(0.25))) },
                            onDecrease = { onTaxRateChange(profile.copy(vatRate = (profile.vatRate - 0.01).coerceAtLeast(0.0))) }
                        )

                        TaxControlRow(
                            label = "الرسوم الجمركية على الواردات",
                            currentPercent = (profile.importTariffRate * 100).toInt(),
                            onPreview = { onPreviewPolicy("التعريفة الجمركية", 4.0) },
                            onIncrease = { onTaxRateChange(profile.copy(importTariffRate = (profile.importTariffRate + 0.02).coerceAtMost(0.35))) },
                            onDecrease = { onTaxRateChange(profile.copy(importTariffRate = (profile.importTariffRate - 0.02).coerceAtLeast(0.0))) }
                        )
                    }
                }
            }
            2 -> {
                // Debt and Sovereign Welfare
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(CommandSurface)
                        .border(1.dp, CommandBorder, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "📊 الدين العام والسيادة المالية", color = SovereignGold, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "إجمالي الدين السيادي:", color = TextSecondary, fontSize = 12.sp)
                            Text(
                                text = EconomyScaleEngine.formatCurrency(player.sovereignDebtBillions),
                                color = if (player.sovereignDebtBillions > player.gdpBillions * 0.7) CrisisRed else TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        val debtRatio = if (player.gdpBillions > 0) (player.sovereignDebtBillions / player.gdpBillions) * 100.0 else 0.0
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "نسبة الدين إلى الناتج:", color = TextSecondary, fontSize = 12.sp)
                            Text(
                                text = "${String.format(Locale.US, "%.1f", debtRatio)}%",
                                color = if (debtRatio > 80.0) CrisisRed else GrowthGreen,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        gov?.welfare?.let { welfare ->
                            Text(text = "🤝 شبكة الحماية والرعاية الاجتماعية", color = TacticalCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "مخصصات التقاعد: $${String.format(Locale.US, "%.1f", welfare.pensionsSpendingMonthlyMillions)}M • دعم السلع: $${String.format(Locale.US, "%.1f", welfare.foodSubsidyMonthlyMillions)}M",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GovTabButton(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) TacticalCyan else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.Black else TextSecondary,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun TaxControlRow(
    label: String,
    currentPercent: Int,
    onPreview: () -> Unit,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = label, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(text = "المعدل الحالي: $currentPercent%", color = SovereignGold, fontSize = 11.sp)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(
                onClick = onPreview,
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.height(28.dp)
            ) {
                Text(text = "🔮 محاكاة", color = TacticalCyan, fontSize = 10.sp)
            }
            Spacer(modifier = Modifier.width(6.dp))
            Button(
                onClick = onDecrease,
                colors = ButtonDefaults.buttonColors(containerColor = CommandSurfaceElevated),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.height(28.dp)
            ) {
                Text(text = "-", color = TextPrimary, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.width(4.dp))
            Button(
                onClick = onIncrease,
                colors = ButtonDefaults.buttonColors(containerColor = TacticalCyan),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.height(28.dp)
            ) {
                Text(text = "+", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
