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
import com.example.ui.components.NegotiationDialog
import com.example.ui.components.TopGameBar
import com.example.ui.screens.CompaniesScreen
import com.example.ui.screens.CountrySelectionScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.EconomyScreen
import com.example.ui.screens.InfluenceScreen
import com.example.ui.screens.InvestmentScreen
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
                onOpenDossier = { viewModel.selectCountryForDossier(it) }
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
                NavigationTab.STATE -> DashboardScreen(
                    state = state,
                    onNavigateTab = { viewModel.setTab(it) }
                )
                NavigationTab.ECONOMY -> EconomyScreen(
                    state = state,
                    onTaxRateChange = { viewModel.updateTaxRate(it) },
                    onPayDownDebt = { viewModel.payDownNationalDebt(it) }
                )
                NavigationTab.INVESTMENTS -> InvestmentScreen(
                    state = state,
                    onStartDomesticProject = { viewModel.startDomesticProject(it) },
                    onOpenNegotiation = { viewModel.openNegotiation(it) },
                    onStartOutboundInvestment = { targetId, assetType, cap, title ->
                        viewModel.startOutboundInvestment(targetId, assetType, cap, title)
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
                NavigationTab.INFLUENCE -> InfluenceScreen(
                    state = state,
                    onImproveRelations = { targetId ->
                        viewModel.improveRelations(targetId)
                    }
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
        }
    }
}
