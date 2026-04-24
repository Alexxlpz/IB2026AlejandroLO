package com.iberdrola.practicas2026.alejandroLO.billsViewModel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.MutableCreationExtras
import app.cash.turbine.test
import com.iberdrola.practicas2026.alejandroLO.IberdrolaApplication
import com.iberdrola.practicas2026.alejandroLO.MainDispatcherRule
import com.iberdrola.practicas2026.alejandroLO.data.AppContainer
import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import com.iberdrola.practicas2026.alejandroLO.data.repository.bill.BillsRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.conectivity.ConnectivityRepository
import com.iberdrola.practicas2026.alejandroLO.data.repository.filter.FilterRepository
import com.iberdrola.practicas2026.alejandroLO.homeViewModel.FakeConnectivityRepository
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel.BillsViewModel
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel.BillsViewModelFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BillsViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var fakeRepository: FakeBillsRepository
    private lateinit var fakeConnectivityRepository: FakeConnectivityRepository
    private lateinit var fakeFilterRepository: FakeFilterRepository
    private lateinit var viewModel: BillsViewModel

    @Before
    fun setUp() {
        fakeRepository = FakeBillsRepository()
        fakeConnectivityRepository = FakeConnectivityRepository()
        fakeFilterRepository = spyk(FakeFilterRepository())

        viewModel = BillsViewModel(
            billsRepository = fakeRepository,
            connectivityRepository = fakeConnectivityRepository,
            filterRepository = fakeFilterRepository
        )
    }

    @Test
    fun givenBills_whenRefreshBills_thenUiStateIsUpdated() = runTest {
        // Arrange
        viewModel.uiState.test {
            // Act
            val first = awaitItem()

            // Assert
            assertTrue(first.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenSelectedOption_whenChangeSelectedOption_thenUiStateIsUpdated() = runTest {
        // Arrange
        val option = BillTypeEnum.GAS

        // Act
        viewModel.updateSelectedOption(option)
        val uiState = viewModel.uiState.value

        // Assert
        assertEquals(option, uiState.selectedOption)
    }

    @Test
    fun givenIsNotOnline_whenUpdateDataBase_thenUiStateIsUpdated() = runTest {
        // Arrange
        val isOnline = true

        // Act
        viewModel.updateDataBase(isOnline)
        val uiState = viewModel.uiState.value

        // Assert
        assertTrue(uiState.isOnline)
    }

    @Test
    fun givenBills_whenGetAllBillsByDirectionId_thenUiStateIsUpdated() = runTest {
        // Arrange
        val directionId = 10
        viewModel.updateDirection(directionId, "Calle Inventada")
        val bill1 = Bill(id = 1, price = 50.0, directionId = directionId)
        val bill2 = Bill(id = 2, price = 100.0, directionId = 20)

        // Act
        fakeRepository.emit(listOf(bill1, bill2))
        viewModel.refreshBills()

        // Assert
        viewModel.uiState.test {
            val emission = awaitItem()
            assertEquals(1, emission.billsList.size)
            assertEquals(1, emission.billsList[0].id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenFactory_whenCreateViewModel_thenReturnsBillsViewModelWithRepository() {
        // Arrange
        val mockApplication = mockk<IberdrolaApplication>(relaxed = true)
        val mockContainer = mockk<AppContainer>()
        val mockRepository = mockk<BillsRepository>()
        val mockConnectivity = mockk<ConnectivityRepository>()
        val mockFilter = mockk<FilterRepository>()

        every { mockFilter.filterCriteria } returns MutableStateFlow(com.iberdrola.practicas2026.alejandroLO.ui.features.filter.viewModel.FilterUiState()).asStateFlow()
        every { mockFilter.maxPrice } returns MutableStateFlow(0f).asStateFlow()
        every { mockFilter.minPrice } returns MutableStateFlow(0f).asStateFlow()
        every { mockConnectivity.isOnline } returns MutableStateFlow(true).asStateFlow()
        every { mockApplication.container } returns mockContainer
        every { mockContainer.billsRepository } returns mockRepository
        every { mockContainer.connectivityRepository } returns mockConnectivity
        every { mockContainer.filterRepository } returns mockFilter

        val extras = MutableCreationExtras().apply {
            set(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY, mockApplication)
        }

        // Act
        val factory = BillsViewModelFactory.Factory
        val createdViewModel = factory.create(BillsViewModel::class.java, extras)

        // Assert
        assertNotNull(createdViewModel)
    }

    @Test
    fun givenClearFilters_whenOnlineStatusChanges_thenFiltersAreCleared() = runTest {
        // Arrange
        fakeConnectivityRepository.setOnlineMode(true)

        // Act
        viewModel.clearFilters(initialValueIsOnline = true)

        // Assert
        verify(exactly = 0) { fakeFilterRepository.clearFilter() }
        fakeConnectivityRepository.setOnlineMode(false)
        advanceTimeBy(301)
        verify(exactly = 1) { fakeFilterRepository.clearFilter() }
    }
}