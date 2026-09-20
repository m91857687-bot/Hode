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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.window.Dialog
import com.example.model.Company
import com.example.model.GameState
import com.example.ui.theme.CommandBorder
import com.example.ui.theme.CommandDarkBg
import com.example.ui.theme.CommandSurface
import com.example.ui.theme.CommandSurfaceElevated
import com.example.ui.theme.CrisisRed
import com.example.ui.theme.DiplomaticPurple
import com.example.ui.theme.GrowthGreen
import com.example.ui.theme.SovereignGold
import com.example.ui.theme.TacticalCyan
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun CompaniesScreen(
    state: GameState,
    onFoundCompany: (name: String, sector: String, capitalBillions: Double) -> Unit,
    onPrivatizeCompany: (companyId: String) -> Unit,
    onInjectCapital: (companyId: String, amountBillions: Double) -> Unit = { _, _ -> },
    onExpandProduction: (companyId: String, costBillions: Double) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    val player = state.playerCountry ?: return
    var showFoundCompanyDialog by remember { mutableStateOf(false) }

    val totalPortfolioValue = state.companies.sumOf { it.marketValueBillions }
    val totalAnnualDividends = state.companies.sumOf { it.stateAnnualDividendsMillions }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CommandDarkBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("companies_screen")
    ) {
        // Portfolio Overview Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(CommandSurfaceElevated)
                .border(1.2.dp, TacticalCyan.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "🏢 محفظة الكيانات الاقتصادية والشركات",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${state.companies.size} شركات تحت إدارة ورعاية الدولة",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    Button(
                        onClick = { showFoundCompanyDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = TacticalCyan),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("open_found_company_btn")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "تأسيس شركة", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(CommandDarkBg)
                            .padding(8.dp)
                    ) {
                        Column {
                            Text(text = "القيمة السوقية للمحفظة", color = TextSecondary, fontSize = 10.sp)
                            Text(text = "$${String.format(Locale.US, "%.1f", totalPortfolioValue)}B", color = TacticalCyan, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(CommandDarkBg)
                            .padding(8.dp)
                    ) {
                        Column {
                            Text(text = "عوائد سنوية للخزينة", color = TextSecondary, fontSize = 10.sp)
                            Text(text = "$${String.format(Locale.US, "%.1f", totalAnnualDividends)}M", color = GrowthGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Companies List
        Text(
            text = "الشركات الوطنية والسيادية التابعة للدولة",
            color = TextSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        state.companies.forEach { comp ->
            CompanyCard(
                company = comp,
                playerTreasury = player.treasuryBillions,
                onPrivatize = { onPrivatizeCompany(comp.id) },
                onInjectCapital = { amount -> onInjectCapital(comp.id, amount) },
                onExpandProduction = { cost -> onExpandProduction(comp.id, cost) }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Dialog for founding a new company
        if (showFoundCompanyDialog) {
            FoundCompanyDialog(
                playerTreasury = player.treasuryBillions,
                onDismiss = { showFoundCompanyDialog = false },
                onConfirm = { name, sector, cap ->
                    onFoundCompany(name, sector, cap)
                    showFoundCompanyDialog = false
                }
            )
        }
    }
}

@Composable
private fun CompanyCard(
    company: Company,
    playerTreasury: Double,
    onPrivatize: () -> Unit,
    onInjectCapital: (Double) -> Unit,
    onExpandProduction: (Double) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CommandSurface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CommandBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(TacticalCyan.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Business, contentDescription = null, tint = TacticalCyan, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = company.nameAr,
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "قطاع ${company.sector} • ${if (company.isStateOwned) "شركة وطنية حكومية" else "شركة مساهمة خاصة"}",
                            color = if (company.isStateOwned) SovereignGold else DiplomaticPurple,
                            fontSize = 11.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(CommandSurfaceElevated)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "حصة الدولة: ${(company.stateOwnershipPercent * 100).toInt()}%",
                        color = TacticalCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Key Metrics
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "القيمة السوقية", color = TextSecondary, fontSize = 10.sp)
                    Text(text = "$${String.format(Locale.US, "%.1f", company.marketValueBillions)}B", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                Column {
                    Text(text = "الأرباح السنوية", color = TextSecondary, fontSize = 10.sp)
                    Text(text = "$${String.format(Locale.US, "%.1f", company.annualProfitMillions)}M", color = GrowthGreen, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                Column {
                    Text(text = "توزيعات للدولة", color = TextSecondary, fontSize = 10.sp)
                    Text(text = "$${String.format(Locale.US, "%.1f", company.stateAnnualDividendsMillions)}M", color = SovereignGold, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                Column {
                    Text(text = "الموظفون", color = TextSecondary, fontSize = 10.sp)
                    Text(text = "${String.format(Locale.US, "%,d", company.employeesCount)}", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Secondary metrics: production index and capital
            Text(
                text = "رأس المال: $${String.format(Locale.US, "%.1f", company.capitalBillions)}B • مؤشر الإنتاج: ${company.productionVolumeIndex} • التصدير: ${(company.exportRatio * 100).toInt()}%",
                color = TextSecondary,
                fontSize = 10.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Strategic Expansion & Management Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Button(
                    onClick = { onInjectCapital(1.0) },
                    enabled = playerTreasury >= 1.0,
                    colors = ButtonDefaults.buttonColors(containerColor = TacticalCyan),
                    modifier = Modifier.weight(1f).testTag("inject_capital_${company.id}")
                ) {
                    Text(text = "ضخ $1B", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { onExpandProduction(0.8) },
                    enabled = playerTreasury >= 0.8,
                    colors = ButtonDefaults.buttonColors(containerColor = GrowthGreen),
                    modifier = Modifier.weight(1.2f).testTag("expand_prod_${company.id}")
                ) {
                    Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color.Black, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(text = "توسيع الإنتاج ($0.8B)", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                if (company.isStateOwned && company.stateOwnershipPercent >= 0.30) {
                    val cashProceeds = company.marketValueBillions * 0.25
                    OutlinedButton(
                        onClick = onPrivatize,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SovereignGold),
                        modifier = Modifier.weight(1.4f).testTag("privatize_btn_${company.id}")
                    ) {
                        Text(
                            text = "خصخصة 25% (+$${String.format(Locale.US, "%.1f", cashProceeds)}B)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FoundCompanyDialog(
    playerTreasury: Double,
    onDismiss: () -> Unit,
    onConfirm: (name: String, sector: String, capital: Double) -> Unit
) {
    var companyName by remember { mutableStateOf("") }
    var selectedSector by remember { mutableStateOf("الطاقة والتعدين") }
    var capitalText by remember { mutableStateOf("2.0") }

    val capital = capitalText.toDoubleOrNull() ?: 2.0
    val canAfford = playerTreasury >= capital

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = CommandDarkBg,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, TacticalCyan.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                .testTag("found_company_dialog")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🏛️ تأسيس كيان اقتصادي وطني",
                        color = TacticalCyan,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = companyName,
                    onValueChange = { companyName = it },
                    label = { Text("اسم الشركة (مثال: الوطنية للتعدين، القابضة للطاقة)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TacticalCyan,
                        unfocusedBorderColor = CommandBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("company_name_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "اختر قطاع النشاط الاستراتيجي:", color = TextSecondary, fontSize = 11.sp)
                Spacer(modifier = Modifier.height(6.dp))

                val sectors = listOf("الطاقة والتعدين", "التقنية والذكاء الاصطناعي", "اللوجستيات والموانئ", "الصناعات الثقيلة")
                sectors.forEach { sec ->
                    val isSecSelected = (selectedSector == sec)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSecSelected) TacticalCyan.copy(alpha = 0.15f) else CommandSurface)
                            .border(1.dp, if (isSecSelected) TacticalCyan else CommandBorder, RoundedCornerShape(8.dp))
                            .clickable { selectedSector = sec }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = sec,
                            color = if (isSecSelected) TacticalCyan else TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = if (isSecSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = capitalText,
                    onValueChange = { capitalText = it },
                    label = { Text("رأس المال المخصص من الخزينة (مليار $)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SovereignGold,
                        unfocusedBorderColor = CommandBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("company_capital_input")
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        val validName = if (companyName.isNotBlank()) companyName else "الشركة الوطنية لـ$selectedSector"
                        onConfirm(validName, selectedSector, capital)
                    },
                    enabled = canAfford && capital > 0.1,
                    colors = ButtonDefaults.buttonColors(containerColor = TacticalCyan),
                    modifier = Modifier.fillMaxWidth().testTag("confirm_found_company_btn")
                ) {
                    Text(
                        text = if (canAfford) "تأسيس الشركة وضخ رأس المال" else "السيولة في الخزينة لا تكفي",
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
