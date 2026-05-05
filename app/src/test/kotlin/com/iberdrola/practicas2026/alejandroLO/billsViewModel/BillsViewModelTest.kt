package com.iberdrola.practicas2026.alejandroLO.billsViewModel

import com.iberdrola.practicas2026.alejandroLO.MainDispatcherRule
import com.iberdrola.practicas2026.alejandroLO.data.repository.bill.BillsRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.conectivity.ConnectivityRepository
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillStatusEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel.BillsViewModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
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

    private lateinit var billsRepository: BillsRepository
    private lateinit var connectivityRepository: ConnectivityRepository
    private lateinit var viewModel: BillsViewModel

    @Before
    fun setup() {
        billsRepository = mockk(relaxed = true)
        connectivityRepository = mockk(relaxed = true)

        // Mock mandatory flows for init block
        every { connectivityRepository.isOnline } returns MutableStateFlow(true)
        every { billsRepository.getAllBillsByDirectionId(any()) } returns flowOf(emptyList())

        viewModel = BillsViewModel(
            billsRepository = billsRepository,
            connectivityRepository = connectivityRepository
        )
    }

    @Test
    fun given_onlineConnectivity_when_viewModelIsCreated_then_uiStateIsOnline() = runTest {
        // Act
        advanceUntilIdle()

        // Assert
        assertTrue(viewModel.billsUiState.value.isOnline)
    }

    @Test
    fun given_billTypes_when_loadOptions_then_optionsAreLoaded() = runTest {
        // Act
        viewModel.load_options()

        // Assert
        assertEquals(
            BillTypeEnum.entries.toList(),
            viewModel.billsUiState.value.options
        )
    }

    @Test
    fun given_onlineMode_when_refreshBills_then_refreshOnlineIsCalled() = runTest {
        // Arrange
        every { connectivityRepository.isOnline } returns MutableStateFlow(true)

        // Act
        viewModel.refreshBills()
        advanceUntilIdle()

        // Assert
        coVerify { billsRepository.refreshBillsOnline() }
    }

    @Test
    fun given_offlineMode_when_refreshBills_then_insertMockBillsIsCalled() = runTest {
        // Arrange
        every { connectivityRepository.isOnline } returns MutableStateFlow(false)
        // Re-instantiate to apply the new mock state in init/load_connectivity
        viewModel = BillsViewModel(billsRepository, connectivityRepository)

        // Act
        viewModel.refreshBills()
        advanceUntilIdle()

        // Assert
        coVerify { billsRepository.insertMockBillsFromAssets() }
    }

    @Test
    fun given_priceLimits_when_setPriceLimits_then_filterUiStateIsUpdated() = runTest {
        // Arrange
        val minPrice = 10f
        val maxPrice = 50f

        // Act
        viewModel.setPriceLimits(minPrice, maxPrice)

        // Assert
        assertEquals(minPrice, viewModel.filterUiState.value.minPrice)
        assertEquals(maxPrice, viewModel.filterUiState.value.maxPrice)
        assertEquals(minPrice..maxPrice, viewModel.filterUiState.value.priceRange)
    }

    @Test
    fun given_selectedOption_when_updateSelectedOption_then_uiStateReflectsChange() = runTest {
        // Arrange
        val option = BillTypeEnum.GAS

        // Act
        viewModel.updateSelectedOption(option)

        // Assert
        assertEquals(option, viewModel.billsUiState.value.selectedOption)
    }

    @Test
    fun given_newDirection_when_updateDirection_then_directionIsUpdated() = runTest {
        // Arrange
        val directionId = 3
        val directionStreet = "Gran Via"

        // Act
        viewModel.updateDirection(directionId, directionStreet)

        // Assert
        assertEquals(directionId, viewModel.billsUiState.value.directionId)
        assertEquals(directionStreet, viewModel.billsUiState.value.directionStreet)
    }

    @Test
    fun given_emptySelectedStates_when_submitButton_then_allStatesAreApplied() = runTest {
        // Arrange
        val priceRange = 0f..100f

        // Act
        viewModel.sumbmitButtom(
            dateFrom = null,
            dateTo = null,
            priceRange = priceRange,
            selectedStates = emptyList()
        )

        // Assert
        assertEquals(
            BillStatusEnum.entries,
            viewModel.filterUiState.value.selectedStates
        )
    }

    @Test
    fun given_selectedDateFrom_when_onClearDate_then_dateFromIsCleared() = runTest {
        // Arrange
        viewModel.sumbmitButtom(
            dateFrom = Date(),
            dateTo = null,
            priceRange = 0f..100f,
            selectedStates = BillStatusEnum.entries
        )

        // Act
        viewModel.onClearDate(0)

        // Assert
        assertNull(viewModel.filterUiState.value.selectedDateFrom)
    }

    @Test
    fun given_priceRangeModified_when_onClearPriceRange_then_rangeResetsToLimits() = runTest {
        // Arrange
        viewModel.setPriceLimits(10f, 50f)

        // Act
        viewModel.onClearPriceRange()

        // Assert
        assertEquals(
            10f..50f,
            viewModel.filterUiState.value.priceRange
        )
    }
}
