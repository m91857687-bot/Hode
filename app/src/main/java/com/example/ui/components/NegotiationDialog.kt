package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.ForeignOffer
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
fun NegotiationDialog(
    offer: ForeignOffer,
    onTermsChanged: (
        stateEquity: Double,
        taxRate: Double,
        localJobQuota: Double,
        taxHolidayYears: Int,
        contractYears: Int,
        localProductionRatio: Double,
        exportRatio: Double,
        hasTechTransfer: Boolean,
        hasLocalRdCenter: Boolean,
        hasLocalSuppliers: Boolean
    ) -> Unit,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onClose: () -> Unit
) {
    Dialog(onDismissRequest = onClose) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = CommandDarkBg,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, TacticalCyan.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                .testTag("negotiation_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "💼 طاولة التفاوض الاستثماري الدولي",
                            color = TacticalCyan,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = offer.foreignCompanyName,
                            color = SovereignGold,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    IconButton(onClick = onClose, modifier = Modifier.size(32.dp).testTag("close_negotiation_btn")) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Investor Type Badge
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(SovereignGold.copy(alpha = 0.15f))
                        .border(1.dp, SovereignGold.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "نمط المستثمر: ", color = TextSecondary, fontSize = 11.sp)
                            Text(
                                text = offer.investorType.titleAr,
                                color = SovereignGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = offer.investorType.philosophyAr,
                            color = TextPrimary,
                            fontSize = 10.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Project Overview Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CommandSurface)
                        .border(1.dp, CommandBorder, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            text = "${offer.category.icon} ${offer.projectTitleAr}",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = offer.descriptionAr,
                            color = TextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "قيمة الاستثمار: $${offer.investmentValueBillions}B",
                                color = SovereignGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "الوظائف: ${String.format(Locale.US, "%,d", offer.jobsOffered)}",
                                color = GrowthGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Negotiation Parameters
                Text(
                    text = "بنود العقد والشراكة",
                    color = TacticalCyan,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                // 1. State Equity
                NegotiationSlider(
                    title = "حصة ملكية الدولة",
                    valueText = "${(offer.negotiatedStateEquity * 100).toInt()}% (المستثمر: ${(offer.negotiatedForeignEquity * 100).toInt()}%)",
                    value = offer.negotiatedStateEquity.toFloat(),
                    range = 0.10f..0.70f,
                    onValueChange = {
                        onTermsChanged(
                            it.toDouble(),
                            offer.negotiatedTaxRate,
                            offer.negotiatedLocalJobQuota,
                            offer.negotiatedTaxHolidayYears,
                            offer.negotiatedContractYears,
                            offer.negotiatedLocalProductionRatio,
                            offer.negotiatedExportRatio,
                            offer.hasTechTransferClause,
                            offer.hasLocalRdCenter,
                            offer.hasLocalSuppliersCommitment
                        )
                    }
                )

                // 2. Corporate Tax Rate
                NegotiationSlider(
                    title = "نسبة الضرائب المفروضة",
                    valueText = "${(offer.negotiatedTaxRate * 100).toInt()}%",
                    value = offer.negotiatedTaxRate.toFloat(),
                    range = 0.05f..0.35f,
                    onValueChange = {
                        onTermsChanged(
                            offer.negotiatedStateEquity,
                            it.toDouble(),
                            offer.negotiatedLocalJobQuota,
                            offer.negotiatedTaxHolidayYears,
                            offer.negotiatedContractYears,
                            offer.negotiatedLocalProductionRatio,
                            offer.negotiatedExportRatio,
                            offer.hasTechTransferClause,
                            offer.hasLocalRdCenter,
                            offer.hasLocalSuppliersCommitment
                        )
                    }
                )

                // 3. Local Job Quota
                NegotiationSlider(
                    title = "نسبة التوظيف المحلي الإلزامي",
                    valueText = "${(offer.negotiatedLocalJobQuota * 100).toInt()}%",
                    value = offer.negotiatedLocalJobQuota.toFloat(),
                    range = 0.20f..0.80f,
                    onValueChange = {
                        onTermsChanged(
                            offer.negotiatedStateEquity,
                            offer.negotiatedTaxRate,
                            it.toDouble(),
                            offer.negotiatedTaxHolidayYears,
                            offer.negotiatedContractYears,
                            offer.negotiatedLocalProductionRatio,
                            offer.negotiatedExportRatio,
                            offer.hasTechTransferClause,
                            offer.hasLocalRdCenter,
                            offer.hasLocalSuppliersCommitment
                        )
                    }
                )

                // 4. Contract Years
                NegotiationSlider(
                    title = "مدة عقد الامتياز",
                    valueText = "${offer.negotiatedContractYears} سنة",
                    value = offer.negotiatedContractYears.toFloat(),
                    range = 5f..30f,
                    steps = 5,
                    onValueChange = {
                        onTermsChanged(
                            offer.negotiatedStateEquity,
                            offer.negotiatedTaxRate,
                            offer.negotiatedLocalJobQuota,
                            offer.negotiatedTaxHolidayYears,
                            it.toInt(),
                            offer.negotiatedLocalProductionRatio,
                            offer.negotiatedExportRatio,
                            offer.hasTechTransferClause,
                            offer.hasLocalRdCenter,
                            offer.hasLocalSuppliersCommitment
                        )
                    }
                )

                // 5. Tax Holiday
                NegotiationSlider(
                    title = "فترة الإعفاء الضريبي",
                    valueText = "${offer.negotiatedTaxHolidayYears} سنوات",
                    value = offer.negotiatedTaxHolidayYears.toFloat(),
                    range = 0f..5f,
                    steps = 4,
                    onValueChange = {
                        onTermsChanged(
                            offer.negotiatedStateEquity,
                            offer.negotiatedTaxRate,
                            offer.negotiatedLocalJobQuota,
                            it.toInt(),
                            offer.negotiatedContractYears,
                            offer.negotiatedLocalProductionRatio,
                            offer.negotiatedExportRatio,
                            offer.hasTechTransferClause,
                            offer.hasLocalRdCenter,
                            offer.hasLocalSuppliersCommitment
                        )
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Strategic Clauses & Switches
                Text(
                    text = "الشروط والالتزامات الاستراتيجية",
                    color = TacticalCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))

                ClauseSwitchRow(
                    title = "نقل التكنولوجيا وتدريب المهندسين",
                    subtitle = "توطين المعرفة وبناء الكوادر الوطنية",
                    checked = offer.hasTechTransferClause,
                    onCheckedChange = { checked ->
                        onTermsChanged(
                            offer.negotiatedStateEquity,
                            offer.negotiatedTaxRate,
                            offer.negotiatedLocalJobQuota,
                            offer.negotiatedTaxHolidayYears,
                            offer.negotiatedContractYears,
                            offer.negotiatedLocalProductionRatio,
                            offer.negotiatedExportRatio,
                            checked,
                            offer.hasLocalRdCenter,
                            offer.hasLocalSuppliersCommitment
                        )
                    }
                )

                ClauseSwitchRow(
                    title = "إنشاء مركز أبحاث وتطوير محلي (R&D)",
                    subtitle = "يلزم المستثمر بإنشاء مختبر ابتكار دائم بالدولة",
                    checked = offer.hasLocalRdCenter,
                    onCheckedChange = { checked ->
                        onTermsChanged(
                            offer.negotiatedStateEquity,
                            offer.negotiatedTaxRate,
                            offer.negotiatedLocalJobQuota,
                            offer.negotiatedTaxHolidayYears,
                            offer.negotiatedContractYears,
                            offer.negotiatedLocalProductionRatio,
                            offer.negotiatedExportRatio,
                            offer.hasTechTransferClause,
                            checked,
                            offer.hasLocalSuppliersCommitment
                        )
                    }
                )

                ClauseSwitchRow(
                    title = "الاعتماد على موردين ومصانع محلية",
                    subtitle = "دعم سلاسل التوريد والشركات الوطنية الصغيرة والمتوسطة",
                    checked = offer.hasLocalSuppliersCommitment,
                    onCheckedChange = { checked ->
                        onTermsChanged(
                            offer.negotiatedStateEquity,
                            offer.negotiatedTaxRate,
                            offer.negotiatedLocalJobQuota,
                            offer.negotiatedTaxHolidayYears,
                            offer.negotiatedContractYears,
                            offer.negotiatedLocalProductionRatio,
                            offer.negotiatedExportRatio,
                            offer.hasTechTransferClause,
                            offer.hasLocalRdCenter,
                            checked
                        )
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // AI Valuation & Likelihood Gauge
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CommandSurfaceElevated)
                        .border(1.dp, CommandBorder, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "موقف المستثمر الأجنبي:",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "احتمال القبول: ${offer.aiAcceptanceLikelihood}%",
                                color = if (offer.aiAcceptanceLikelihood >= 65) GrowthGreen else if (offer.aiAcceptanceLikelihood >= 45) SovereignGold else CrisisRed,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { (offer.aiAcceptanceLikelihood / 100f).coerceIn(0f, 1f) },
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                            color = if (offer.aiAcceptanceLikelihood >= 65) GrowthGreen else if (offer.aiAcceptanceLikelihood >= 45) SovereignGold else CrisisRed,
                            trackColor = CommandBorder
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "\"${offer.aiFeedbackMessage}\"",
                            color = TextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onReject,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CrisisRed),
                        modifier = Modifier.weight(1f).testTag("reject_offer_btn")
                    ) {
                        Text(text = "رفض العرض", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onAccept,
                        enabled = offer.aiAcceptanceLikelihood >= 35,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GrowthGreen,
                            disabledContainerColor = CommandBorder
                        ),
                        modifier = Modifier.weight(1.5f).testTag("accept_offer_btn")
                    ) {
                        Text(
                            text = "توقيع وقبول الصفقة",
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ClauseSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            Text(text = subtitle, color = TextSecondary, fontSize = 9.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = TacticalCyan,
                checkedTrackColor = TacticalCyan.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
private fun NegotiationSlider(
    title: String,
    valueText: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    steps: Int = 0,
    onValueChange: (Float) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 3.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, color = TextSecondary, fontSize = 11.sp)
            Text(text = valueText, color = TacticalCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            steps = steps,
            colors = SliderDefaults.colors(
                thumbColor = TacticalCyan,
                activeTrackColor = TacticalCyan,
                inactiveTrackColor = CommandBorder
            )
        )
    }
}
