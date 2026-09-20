package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.NavigationTab
import com.example.ui.components.BottomGameNav
import com.example.ui.components.EventDialog
import com.example.ui.components.MonthlyReportDialog
import com.example.ui.components.NegotiationDialog
import com.example.ui.components.PolicyPreviewDialog
import com.example.ui.components.ResourceInventorySheet
import com.example.ui.components.TopGameBar
import com.example.ui.screens.CompaniesScreen
import com.example.ui.screens.CountrySelectionScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.EconomyScreen
import com.example.ui.screens.GovernmentScreen
import com.example.ui.screens.IndustryScreen
import com.example.ui.screens.InfluenceScreen
import com.example.ui.screens.InvestmentScreen
import com.example.ui.screens.MilitaryScreen
import com.example.ui.screens.ResearchScreen
import com.example.ui.screens.WorldMapScreen
import com.example.ui.theme.CommandDarkBg
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    StatecraftGameApp()
                }
            }
        }
    }
}

@Composable
fun StatecraftGameApp(
    viewModel: GameViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    // 1. Country Selection Onboarding Screen
    if (state.playerCountryId == null) {
        CountrySelectionScreen(
            countries = state.countries.values.toList(),
            onCountryConfirmed = { countryId ->
                viewModel.selectPlayerCountry(countryId)
            },
            modifier = Modifier.fillMaxSize()
        )
        return
    }

    // 2. Active Game Command Center (Scaffold + HUD + Tab Content)
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("main_game_scaffold"),
        containerColor = CommandDarkBg,
        topBar = {
            TopGameBar(
                state = state,
                onSpeedChange = { viewModel.setGameSpeed(it) },
                onAdvanceMonth = { viewModel.advanceMonthManual() },
                onOpenDossier = { viewModel.selectCountryForDossier(it) },
                onOpenInventory = { viewModel.toggleResourceInventory(true) }
            )
        },
        bottomBar = {
            BottomGameNav(
                currentTab = state.currentTab,
                onTabSelected = { viewModel.setTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CommandDarkBg)
        ) {
            when (state.currentTab) {
                NavigationTab.MAP -> WorldMapScreen(
                    state = state,
                    onCountryClick = { country ->
                        viewModel.selectCountryForDossier(country.id)
                    },
                    onFilterChange = { mode ->
                        viewModel.setMapFilter(mode)
                    },
                    onDismissDossier = {
                        viewModel.selectCountryForDossier(null)
                    },
                    onInvestInProject = { project ->
                        viewModel.investInForeignCountry(project)
                    },
                    onImproveRelations = { targetId ->
                        viewModel.improveRelations(targetId)
                    }
                )
                NavigationTab.GOVERNMENT, NavigationTab.STATE -> GovernmentScreen(
                    state = state,
                    onAdjustServiceFunding = { serviceType, funding ->
                        viewModel.adjustPublicServiceFunding(serviceType, funding)
                    },
                    onTaxRateChange = { newProfile ->
                        viewModel.updateTaxProfile(newProfile)
                    },
                    onPreviewPolicy = { name, delta ->
                        viewModel.previewPolicyChange(name, delta)
                    }
                )
                NavigationTab.ECONOMY -> EconomyScreen(
                    state = state,
                    onTaxRateChange = { viewModel.updateTaxRate(it) },
                    onPayDownDebt = { viewModel.payDownNationalDebt(it) }
                )
                NavigationTab.INDUSTRY -> IndustryScreen(
                    state = state,
                    onUpgradeFactory = { factoryId ->
                        viewModel.upgradeFactory(factoryId)
                    },
                    onPrivatizeFactory = { factoryId ->
                        viewModel.privatizeFactory(factoryId)
                    },
                    onBuildFactory = { type ->
                        viewModel.buildFactory(type)
                    }
                )
                NavigationTab.RESEARCH -> ResearchScreen(
                    state = state,
                    onStartResearch = { techId ->
                        viewModel.startResearch(techId)
                    },
                    onRecruitScientists = {
                        viewModel.recruitScientists(200)
                    }
                )
                NavigationTab.INVESTMENTS -> InvestmentScreen(
                    state = state,
                    onStartDomesticProject = { viewModel.startDomesticProject(it) },
                    onOpenNegotiation = { viewModel.openNegotiation(it) },
                    onStartOutboundInvestment = { targetId, assetType, cap, title ->
                        viewModel.startOutboundInvestment(targetId, assetType, cap, title)
                    }
                )
                NavigationTab.MILITARY -> MilitaryScreen(
                    state = state,
                    onAdjustBudget = { personnel, maint, training ->
                        viewModel.adjustMilitaryBudget(personnel, maint, training)
                    },
                    onRecruitSoldiers = {
                        viewModel.recruitSoldiers(5000)
                    }
                )
                NavigationTab.COMPANIES -> CompaniesScreen(
                    state = state,
                    onFoundCompany = { name, sector, cap ->
                        viewModel.foundNewCompany(name, sector, cap)
                    },
                    onPrivatizeCompany = { companyId ->
                        viewModel.privatizeCompany(companyId)
                    },
                    onInjectCapital = { companyId, amount ->
                        viewModel.injectCapitalToCompany(companyId, amount)
                    },
                    onExpandProduction = { companyId, cost ->
                        viewModel.expandCompanyProduction(companyId, cost)
                    }
                )
                NavigationTab.INFLUENCE, NavigationTab.DIPLOMACY_TRADE, NavigationTab.STATS -> InfluenceScreen(
                    state = state,
                    onImproveRelations = { targetId ->
                        viewModel.improveRelations(targetId)
                    }
                )
                else -> DashboardScreen(
                    state = state,
                    onNavigateTab = { viewModel.setTab(it) }
                )
            }

            // 3. Dynamic Geopolitical Event Modal
            state.activePendingEvent?.let { event ->
                EventDialog(
                    event = event,
                    onChoose = { choice ->
                        viewModel.resolveActiveEvent(choice)
                    }
                )
            }

            // 4. Foreign Investment Negotiation Table Modal
            state.activeNegotiationOffer?.let { offer ->
                NegotiationDialog(
                    offer = offer,
                    onTermsChanged = { eq, tax, quota, hol, yrs, prod, exp, tech, rd, supp ->
                        viewModel.updateExtendedNegotiationTerms(eq, tax, quota, hol, yrs, prod, exp, tech, rd, supp)
                    },
                    onAccept = { viewModel.acceptForeignOffer(offer) },
                    onReject = { viewModel.rejectForeignOffer(offer) },
                    onClose = { viewModel.closeNegotiation() }
                )
            }

            // 5. Strategic Resource Inventory Sheet Modal
            if (state.isInventoryDialogOpen) {
                ResourceInventorySheet(
                    state = state,
                    onDismiss = { viewModel.toggleResourceInventory(false) },
                    onBuyCommodity = { resType, quantity ->
                        viewModel.buySpotCommodity(resType, quantity)
                    }
                )
            }

            // 6. Monthly Cabinet Report Modal
            if (state.isMonthlyReportOpen && state.monthlyReport != null) {
                MonthlyReportDialog(
                    report = state.monthlyReport!!,
                    onDismiss = { viewModel.toggleMonthlyReport(false) }
                )
            }

            // 7. "What-If" Policy Impact Preview Modal
            state.activePolicyPreview?.let { preview ->
                PolicyPreviewDialog(
                    preview = preview,
                    onConfirm = {
                        viewModel.dismissPolicyPreview()
                    },
                    onDismiss = {
                        viewModel.dismissPolicyPreview()
                    }
                )
            }
        }
    }
}
