package com.iberdrola.practicas2026.alejandroLO.filterViewModel

import com.iberdrola.practicas2026.alejandroLO.data.repository.filter.FilterRepository
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillStatusEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.filter.enums.FilterType
import com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel.ActiveFilterItem
import com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel.FilterViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class FilterViewModelTest {

    private lateinit var viewModel: FilterViewModel
    private val filterRepository: FilterRepository = mockk(relaxed = true)
    private val minPriceFlow = MutableStateFlow(0f)
    private val maxPriceFlow = MutableStateFlow(100f)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { filterRepository.minPrice } returns minPriceFlow
        every { filterRepository.maxPrice } returns maxPriceFlow
        viewModel = FilterViewModel(filterRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun givenPricesInRepository_whenLoadPriceRange_thenUiStateIsUpdated() = runTest {
        // Arrange
        testDispatcher.scheduler.advanceUntilIdle()

        // Act
        advanceTimeBy(600)

        // Assert
        val uiState = viewModel.uiState.value
        assertEquals(0f, uiState.minPrice)
        assertEquals(100f, uiState.maxPrice)
        assertEquals(0f..100f, uiState.priceRange)
    }

    @Test
    fun givenLoadedPrices_whenClearFilters_thenStateResetsToLimits() = runTest {
        // Arrange
        minPriceFlow.value = 10f
        maxPriceFlow.value = 50f
        testDispatcher.scheduler.advanceUntilIdle()
        advanceTimeBy(600)

        // Act
        viewModel.clearFilters()

        // Assert
        val state = viewModel.uiState.value
        assertNull(state.selectedDateFrom)
        assertNull(state.selectedDateTo)
        assertEquals(10f..50f, state.priceRange)
        assertEquals(BillStatusEnum.entries, state.selectedStates)
        verify { filterRepository.setFilterCriteria(any()) }
    }

    @Test
    fun givenFilterData_whenSumbmitButtom_thenStateAndRepositoryAreUpdated() = runTest {
        // Arrange
        val dateFrom = Date(1000L)
        val dateTo = Date(2000L)
        val range = 20f..80f
        val states = listOf(BillStatusEnum.PAGADA)

        // Act
        viewModel.sumbmitButtom(dateFrom, dateTo, range, states)

        // Assert
        val state = viewModel.uiState.value
        assertEquals(dateFrom, state.selectedDateFrom)
        assertEquals(dateTo, state.selectedDateTo)
        assertEquals(range, state.priceRange)
        assertEquals(states, state.selectedStates)
        verify { filterRepository.setFilterCriteria(state) }
    }

    @Test
    fun givenSingleSelectedState_whenOnClearState_thenStateResetsToAllEntries() = runTest {
        // Arrange
        viewModel.sumbmitButtom(null, null, 0f..100f, listOf(BillStatusEnum.PAGADA))

        // Act
        viewModel.onClearState(BillStatusEnum.PAGADA)

        // Assert
        val state = viewModel.uiState.value
        assertEquals(BillStatusEnum.entries, state.selectedStates)
    }

    @Test
    fun givenSelectedDates_whenOnClearDate_thenSpecificFieldIsNull() = runTest {
        // Arrange
        val date = Date()
        viewModel.sumbmitButtom(date, date, 0f..100f, BillStatusEnum.entries)

        // Act
        viewModel.onClearDate(0)

        // Assert
        assertNull(viewModel.uiState.value.selectedDateFrom)
        assertEquals(date, viewModel.uiState.value.selectedDateTo)

        // Act
        viewModel.onClearDate(1)

        // Assert
        assertNull(viewModel.uiState.value.selectedDateTo)
    }

    @Test
    fun givenCustomPriceRange_whenClearFilterField_thenPriceRangeResetsToLimits() = runTest {
        // Arrange
        viewModel.sumbmitButtom(null, null, 10f..20f, BillStatusEnum.entries)
        minPriceFlow.value = 0f
        maxPriceFlow.value = 100f
        testDispatcher.scheduler.advanceUntilIdle()
        advanceTimeBy(600)
        val activeItem = ActiveFilterItem(FilterType.PRICE_RANGE, "Label")

        // Act
        viewModel.clearFilterField(activeItem)

        // Assert
        assertEquals(0f..100f, viewModel.uiState.value.priceRange)
        verify { filterRepository.setFilterCriteria(any()) }
    }

    @Test
    fun givenActiveFilters_whenClearFiltersToChangeMode_thenStateResetsWithoutNotifyingRepository() = runTest {
        // Arrange
        val date = Date()
        viewModel.sumbmitButtom(date, date, 10f..50f, listOf(BillStatusEnum.PAGADA))

        // Act
        viewModel.clearFiltersToChangeMode()

        // Assert
        val state = viewModel.uiState.value
        assertNull(state.selectedDateFrom)
        assertNull(state.selectedDateTo)
        assertEquals(Float.MIN_VALUE, state.minPrice)
        assertEquals(Float.MAX_VALUE, state.maxPrice)
        assertEquals(Float.MIN_VALUE..Float.MAX_VALUE, state.priceRange)
        assertEquals(BillStatusEnum.entries, state.selectedStates)
        verify(exactly = 1) { filterRepository.setFilterCriteria(any()) }
    }
}