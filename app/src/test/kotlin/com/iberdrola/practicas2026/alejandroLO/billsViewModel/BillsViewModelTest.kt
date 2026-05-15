package com.iberdrola.practicas2026.alejandroLO.billsViewModel

import app.cash.turbine.test
import com.iberdrola.practicas2026.alejandroLO.MainDispatcherRule
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillStatusEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel.BillsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class BillsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeBillsRepository: FakeBillsRepository
    private lateinit var fakeConnectivityRepository: FakeConnectivityRepository
    private lateinit var fakeRemoteConfigRepository: FakeRemoteConfigRepository
    private lateinit var viewModel: BillsViewModel

    @Before
    fun setup() {
        fakeBillsRepository = FakeBillsRepository()
        fakeConnectivityRepository = FakeConnectivityRepository(initialOnline = true)
        fakeRemoteConfigRepository = FakeRemoteConfigRepository(gasEnabled = true)

        viewModel = BillsViewModel(
            billsRepository = fakeBillsRepository,
            connectivityRepository = fakeConnectivityRepository,
            remoteConfigRepository = fakeRemoteConfigRepository
        )
    }

    @Test
    fun given_onlineConnectivity_when_viewModelIsCreated_then_uiStateIsOnline() = runTest {
        advanceUntilIdle()

        assertTrue(viewModel.billsUiState.value.isOnline)
    }

    @Test
    fun given_billTypes_when_loadOptions_then_optionsAreLoaded() = runTest {
        viewModel.load_options()

        assertEquals(
            BillTypeEnum.entries.toList(),
            viewModel.billsUiState.value.options
        )
    }

    @Test
    fun given_onlineMode_when_refreshBills_then_isLoadingEventuallyFalse() = runTest {
        viewModel.refreshBills()
        advanceUntilIdle()

        assertTrue(!viewModel.billsUiState.value.isLoading)
    }

    @Test
    fun given_offlineMode_when_refreshBills_then_isLoadingEventuallyFalse() = runTest {
        fakeConnectivityRepository.setOnlineMode(false)
        viewModel = BillsViewModel(fakeBillsRepository, fakeConnectivityRepository, fakeRemoteConfigRepository)

        viewModel.refreshBills()
        advanceUntilIdle()

        assertTrue(!viewModel.billsUiState.value.isLoading)
    }

    @Test
    fun given_priceLimits_when_setPriceLimits_then_filterUiStateIsUpdated() = runTest {
        val minPrice = 10f
        val maxPrice = 50f

        viewModel.setPriceLimits(minPrice, maxPrice)

        assertEquals(minPrice, viewModel.filterUiState.value.minPrice)
        assertEquals(maxPrice, viewModel.filterUiState.value.maxPrice)
        assertEquals(minPrice..maxPrice, viewModel.filterUiState.value.priceRange)
    }

    @Test
    fun given_selectedOption_when_updateSelectedOption_then_uiStateReflectsChange() = runTest {
        val option = BillTypeEnum.GAS

        viewModel.updateSelectedOption(option)

        assertEquals(option, viewModel.billsUiState.value.selectedOption)
    }

    @Test
    fun given_newDirection_when_updateDirection_then_directionIsUpdated() = runTest {
        val directionId = 3
        val directionStreet = "Gran Via"

        viewModel.updateDirection(directionId, directionStreet)

        assertEquals(directionId, viewModel.billsUiState.value.directionId)
        assertEquals(directionStreet, viewModel.billsUiState.value.directionStreet)
    }

    @Test
    fun given_emptySelectedStates_when_submitButton_then_allStatesAreApplied() = runTest {
        val priceRange = 0f..100f

        viewModel.sumbmitButtom(
            dateFrom = null,
            dateTo = null,
            priceRange = priceRange,
            selectedStates = emptyList()
        )

        assertEquals(
            BillStatusEnum.entries,
            viewModel.filterUiState.value.selectedStates
        )
    }

    @Test
    fun given_selectedDateFrom_when_onClearDate_then_dateFromIsCleared() = runTest {
        viewModel.sumbmitButtom(
            dateFrom = Date(),
            dateTo = null,
            priceRange = 0f..100f,
            selectedStates = BillStatusEnum.entries
        )

        viewModel.onClearDate(0)

        assertNull(viewModel.filterUiState.value.selectedDateFrom)
    }

    @Test
    fun given_priceRangeModified_when_onClearPriceRange_then_rangeResetsToLimits() = runTest {
        viewModel.setPriceLimits(10f, 50f)

        viewModel.onClearPriceRange()

        assertEquals(
            10f..50f,
            viewModel.filterUiState.value.priceRange
        )
    }

    @Test
    fun given_gasDisabled_when_viewModelCreated_then_isGasEnabledIsFalse() = runTest {
        val vmNoGas = BillsViewModel(
            billsRepository = fakeBillsRepository,
            connectivityRepository = fakeConnectivityRepository,
            remoteConfigRepository = FakeRemoteConfigRepository(gasEnabled = false)
        )
        advanceUntilIdle()

        assertTrue(!vmNoGas.isGasEnabled.value)
    }

    @Test
    fun given_page_when_onPageScrollInitialized_then_pageIsMarkedAsInitialized() = runTest {
        viewModel.onPageScrollInitialized(0)

        viewModel.billsUiState.test {
            val item = awaitItem()
            assertTrue(item.scrollInitializedPages.contains(0))
            cancelAndIgnoreRemainingEvents()
        }
    }
}