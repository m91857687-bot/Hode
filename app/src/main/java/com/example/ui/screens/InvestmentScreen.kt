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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.ProjectTemplates
import com.example.model.ForeignOffer
import com.example.model.GameState
import com.example.model.OfferStatus
import com.example.model.OutboundAssetType
import com.example.model.OutboundInvestment
import com.example.model.Project
import com.example.ui.theme.CommandBorder
import com.example.ui.theme.CommandDarkBg
import com.example.ui.theme.CommandSurface
import com.example.ui.theme.CrisisRed
import com.example.ui.theme.GrowthGreen
import com.example.ui.theme.SovereignGold
import com.example.ui.theme.TacticalCyan
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun InvestmentScreen(
    state: GameState,
    onStartDomesticProject: (Project) -> Unit,
    onOpenNegotiation: (ForeignOffer) -> Unit,
    onStartOutboundInvestment: (targetCountryId: String, assetType: OutboundAssetType, capital: Double, titleAr: String) -> Unit = { _, _, _, _ -> },
    modifier: Modifier = Modifier
) {
    val player = state.playerCountry ?: return
    var selectedSubTab by remember { mutableIntStateOf(0) }
    // 0: Domestic Catalog, 1: Outbound Investment, 2: FDI Offers, 3: My Projects, 4: Foreign Assets

    val domesticCatalog = remember(player.id) {
        ProjectTemplates.getDomesticProjectTemplates(player.id)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CommandDarkBg)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("investment_screen")
    ) {
        // 5-Tab Bar (Horizontal scroller / row)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(CommandSurface)
                .border(1.dp, CommandBorder, RoundedCornerShape(12.dp))
                .padding(3.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            SubTabButton(
                title = "محلياً",
                isSelected = selectedSubTab == 0,
                onClick = { selectedSubTab = 0 },
                modifier = Modifier.weight(1f),
                testTag = "subtab_domestic"
            )
            SubTabButton(
                title = "خارجياً",
                isSelected = selectedSubTab == 1,
                onClick = { selectedSubTab = 1 },
                modifier = Modifier.weight(1f),
                testTag = "subtab_outbound"
            )
            SubTabButton(
                title = "عروض FDI (${state.foreignOffers.count { it.status == OfferStatus.PENDING }})",
                isSelected = selectedSubTab == 2,
                onClick = { selectedSubTab = 2 },
                modifier = Modifier.weight(1.3f),
                testTag = "subtab_fdi"
            )
            SubTabButton(
                title = "مشاريعي (${state.domesticProjects.size})",
                isSelected = selectedSubTab == 3,
                onClick = { selectedSubTab = 3 },
                modifier = Modifier.weight(1.1f),
                testTag = "subtab_my_projects"
            )
            SubTabButton(
                title = "أصولي (${state.outboundInvestments.size + state.foreignInvestments.size})",
                isSelected = selectedSubTab == 4,
                onClick = { selectedSubTab = 4 },
                modifier = Modifier.weight(1f),
                testTag = "subtab_my_assets"
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        when (selectedSubTab) {
            0 -> {
                // Tab 1: Domestic Mega Projects Catalog (10 types)
                Text(
                    text = "دليل المشاريع الوطنية الاستراتيجية الكبرى",
                    color = TacticalCyan,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "اختر مشروعاً استراتيجياً لتدشينه وتعزيز الناتج المحلي والوظائف والبنية التحتية.",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                domesticCatalog.forEach { proj ->
                    val isAlreadyStarted = state.domesticProjects.any { it.id == proj.id }
                    if (!isAlreadyStarted) {
                        CatalogProjectCard(
                            project = proj,
                            playerTreasury = player.treasuryBillions,
                            onLaunch = { onStartDomesticProject(proj) }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }

            1 -> {
                // Tab 2: Outbound Foreign Investments Opportunities
                Text(
                    text = "فرص الاستثمار الخارجي والاستحواذ السيادي",
                    color = SovereignGold,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "استثمر فوائضك في الدول الشريكة لشراء محطات موانئ وشبكات طاقة وحصص استراتيجية تدر أرباحاً شهرية وتعزز نفوذك.",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                val partnerCountries = state.countries.values.filter { it.id != player.id }
                partnerCountries.forEach { country ->
                    OutboundOpportunityCard(
                        country = country,
                        playerTreasury = player.treasuryBillions,
                        onInvestPort = {
                            onStartOutboundInvestment(
                                country.id,
                                OutboundAssetType.PORT_TERMINAL,
                                3.5,
                                "رصيف شحن ومحطة حاويات بحرية في ${country.nameAr}"
                            )
                        },
                        onInvestEnergy = {
                            onStartOutboundInvestment(
                                country.id,
                                OutboundAssetType.ENERGY_GRID,
                                2.8,
                                "امتياز محطة توليد طاقة ونقل كهرباء في ${country.nameAr}"
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            2 -> {
                // Tab 3: FDI Offers
                val pendingOffers = state.foreignOffers.filter { it.status == OfferStatus.PENDING || it.status == OfferStatus.NEGOTIATING }
                Text(
                    text = "طلبات الاستثمار الأجنبي المباشر (FDI) الواردة",
                    color = SovereignGold,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "عروض من شركات وصناديق دولية للاستثمار داخل دولتك وفق أنماط وشروط تفاوضية مختلفة.",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                if (pendingOffers.isEmpty()) {
                    EmptyStateCard(message = "لا توجد عروض واردة حالياً. واصل تقدم الأشهر ورفع جاذبية دولتك لجذب تدفقات استثمارية جديدة.")
                } else {
                    pendingOffers.forEach { offer ->
                        ForeignOfferCard(
                            offer = offer,
                            onOpenNegotiate = { onOpenNegotiation(offer) }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }

            3 -> {
                // Tab 4: My Domestic Projects (Active Queue & Completed)
                val activeQueue = state.domesticProjects.filter { !it.isCompleted }
                val completedList = state.domesticProjects.filter { it.isCompleted }

                Text(
                    text = "قيد التنفيذ والتشييد (${activeQueue.size})",
                    color = SovereignGold,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (activeQueue.isEmpty()) {
                    EmptyStateCard(message = "لا توجد مشاريع قيد التنفيذ حالياً. توجه لتبويب 'محلياً' لبدء بناء مشاريع كبرى.")
                } else {
                    activeQueue.forEach { proj ->
                        ActiveProjectCard(project = proj)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "المشاريع المنجزة والعاملة (${completedList.size})",
                    color = GrowthGreen,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (completedList.isEmpty()) {
                    EmptyStateCard(message = "لم يكتمل أي مشروع بعد. مع تقدم الأشهر ستنتهي المشاريع وتبدأ في توليد الإيرادات.")
                } else {
                    completedList.forEach { proj ->
                        CompletedProjectCard(project = proj)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            4 -> {
                // Tab 5: Foreign Assets & Concessions
                Text(
                    text = "محفظة الأصول الخارجية والاستثمارات الدولية",
                    color = TacticalCyan,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "إجمالي القيمة: $${String.format(Locale.US, "%.1f", state.totalOutboundValuation)}B • العوائد الشهرية: +$${String.format(Locale.US, "%.2f", state.monthlyOutboundDividends)}B",
                    color = GrowthGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))

                val allForeign = state.outboundInvestments
                if (allForeign.isEmpty() && state.foreignInvestments.isEmpty()) {
                    EmptyStateCard(message = "لا تمتلك دولتك أصولاً بالخارج حتى الآن. توجه لتبويب 'خارجياً' لاقتناص موانئ ومحطات طاقة.")
                } else {
                    allForeign.forEach { inv ->
                        OutboundAssetCard(investment = inv, hostCountry = state.countries[inv.targetCountryId])
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    state.foreignInvestments.forEach { proj ->
                        ForeignProjectAssetCard(project = proj, hostCountry = state.countries[proj.targetCountryId])
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun SubTabButton(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) TacticalCyan else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 7.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = if (isSelected) Color.Black else TextSecondary,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun EmptyStateCard(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CommandSurface)
            .border(1.dp, CommandBorder, RoundedCornerShape(12.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = TextSecondary,
            fontSize = 11.sp,
            lineHeight = 16.sp
        )
    }
}

@Composable
private fun ActiveProjectCard(project: Project) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CommandSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SovereignGold.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${project.category.icon} ${project.titleAr}",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.HourglassTop, contentDescription = null, tint = SovereignGold, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "متبقي ${project.remainingMonths} شهر",
                        color = SovereignGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { project.progressPercent },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                color = SovereignGold,
                trackColor = CommandBorder
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "الفوائد عند الإنجاز: +$${project.gdpBoostBillions}B بالناتج • +${project.industryBoost} بالصناعة • ${String.format(Locale.US, "%,d", project.jobsCreated)} وظيفة",
                color = GrowthGreen,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun CompletedProjectCard(project: Project) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CommandSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, GrowthGreen.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${project.category.icon} ${project.titleAr}",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "مشروع يعمل بكفاءة • يولد وظائف ويدعم إيرادات الدولة بمقدار +$${project.monthlyRevenueBoostBillions}B شهرياً",
                    color = TextSecondary,
                    fontSize = 10.sp
                )
            }
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = GrowthGreen, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun CatalogProjectCard(
    project: Project,
    playerTreasury: Double,
    onLaunch: () -> Unit
) {
    val canAfford = playerTreasury >= project.totalCostBillions

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
                Text(
                    text = "${project.category.icon} ${project.titleAr}",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "التكلفة: $${project.totalCostBillions}B",
                    color = SovereignGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = project.descriptionAr,
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "المدة: ${project.durationMonths} شهر • الوظائف: ${String.format(Locale.US, "%,d", project.jobsCreated)} • مخاطرة: ${project.riskFactorPercent}%",
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                    Text(
                        text = "الأثر: +$${project.gdpBoostBillions}B ناتج • +${project.attractivenessBoost} جاذبية استثمار",
                        color = GrowthGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Button(
                    onClick = onLaunch,
                    enabled = canAfford,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TacticalCyan,
                        disabledContainerColor = CommandBorder
                    ),
                    modifier = Modifier.testTag("launch_project_${project.id}")
                ) {
                    Text(
                        text = if (canAfford) "بدء التشييد" else "سيولة غير كافية",
                        color = if (canAfford) Color.Black else TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun OutboundOpportunityCard(
    country: com.example.model.Country,
    playerTreasury: Double,
    onInvestPort: () -> Unit,
    onInvestEnergy: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CommandSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CommandBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${country.flag} ${country.nameAr}",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = country.relationStatus.titleAr,
                    color = Color(country.relationStatus.colorHex),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "الناتج: $${country.gdpBillions.toInt()}B • جاذبية الاستثمار: ${country.investmentAttractiveness}/100 • الاستقرار: ${country.stability}%",
                color = TextSecondary,
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onInvestPort,
                    enabled = playerTreasury >= 3.5,
                    colors = ButtonDefaults.buttonColors(containerColor = TacticalCyan),
                    modifier = Modifier.weight(1f).testTag("invest_port_${country.id}")
                ) {
                    Text(text = "🚢 محطة ميناء ($3.5B)", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onInvestEnergy,
                    enabled = playerTreasury >= 2.8,
                    colors = ButtonDefaults.buttonColors(containerColor = SovereignGold),
                    modifier = Modifier.weight(1f).testTag("invest_energy_${country.id}")
                ) {
                    Text(text = "⚡ مرفق طاقة ($2.8B)", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun OutboundAssetCard(
    investment: OutboundInvestment,
    hostCountry: com.example.model.Country?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CommandSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, TacticalCyan.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${investment.assetType.icon} ${investment.titleAr}",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = investment.riskLevel.titleAr,
                    color = Color(investment.riskLevel.colorHex),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "رأس المال: $${investment.investedCapitalBillions}B • العائد الشهري: +$${String.format(Locale.US, "%.2f", investment.monthlyDividendsBillions)}B • نفوذ مكتسب: +${investment.economicInfluenceGain}",
                color = GrowthGreen,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun ForeignProjectAssetCard(
    project: Project,
    hostCountry: com.example.model.Country?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CommandSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, GrowthGreen.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${hostCountry?.flag ?: "🌐"} ${project.titleAr}",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "في ${hostCountry?.nameAr ?: "دولة شريكة"}",
                    color = TacticalCyan,
                    fontSize = 11.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "قيمة الاستثمار: $${project.totalCostBillions}B • العائد الشهري: +$${String.format(Locale.US, "%.2f", project.monthlyReturnBillions)}B • نفوذ مكتسب: +${project.influenceGain}",
                color = GrowthGreen,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun ForeignOfferCard(
    offer: ForeignOffer,
    onOpenNegotiate: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CommandSurface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.2.dp, SovereignGold.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = offer.foreignCompanyName,
                        color = SovereignGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(TacticalCyan.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = offer.investorType.titleAr, color = TacticalCyan, fontSize = 9.sp)
                    }
                }
                Text(
                    text = "عرض بقيمة $${offer.investmentValueBillions}B",
                    color = GrowthGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
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
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "الحصة المعروضة للدولة: ${(offer.negotiatedStateEquity * 100).toInt()}% • وظائف: ${String.format(Locale.US, "%,d", offer.jobsOffered)}",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
                Button(
                    onClick = onOpenNegotiate,
                    colors = ButtonDefaults.buttonColors(containerColor = SovereignGold),
                    modifier = Modifier.testTag("open_negotiate_btn_${offer.id}")
                ) {
                    Text(
                        text = "تفاوض ودراسة العرض",
                        color = Color.Black,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
