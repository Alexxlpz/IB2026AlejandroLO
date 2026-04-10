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
import com.iberdrola.practicas2026.alejandroLO.homeViewModel.FakeConnectivityRepository
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.enums.BillTypeEnum
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel.BillsViewModel
import com.iberdrola.practicas2026.alejandroLO.ui.features.bills.viewModel.BillsViewModelFactory
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    private lateinit var viewModel: BillsViewModel

    @Before
    fun setUp() {
        fakeRepository = FakeBillsRepository()
        fakeConnectivityRepository = FakeConnectivityRepository()
        viewModel = BillsViewModel(
            fakeRepository,
            connectivityRepository = fakeConnectivityRepository
        )
    }

    @Test
    fun givenBills_whenRefreshBills_thenUiStateIsUpdated() = runTest {
        viewModel.uiState.test {
            // Arrange
            // Act
            val first = awaitItem()

            // Assert
            assertTrue(first.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }
    
    @Test
    fun givenSelectedOption_WhenChangeSelectedOption_ThenUiStateIsUpdated() = runTest {
        // Arrange
        // Act
        viewModel.updateSelectedOption(BillTypeEnum.GAS)
        val uiState = viewModel.uiState.value

        // Assert
        assertEquals(BillTypeEnum.GAS, uiState.selectedOption)
    }
    
    @Test
    fun givenIsNotOnline_WhenUpdateDataBase_ThenUiStateIsUpdated() = runTest {
        // Act
        viewModel.updateDataBase(true)
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
        val bill2 = Bill(id = 2, price = 100.0, directionId = 20) // Diferente dirección
        
        // Act
        fakeRepository.emit(listOf(bill1, bill2))
        viewModel.refreshBills()

        // Assert
        viewModel.uiState.test {
            val emission = awaitItem()
            // Debido a que refreshBills filtra por directionId
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

        every { mockApplication.container } returns mockContainer
        every { mockContainer.billsRepository } returns mockRepository
        every { mockContainer.connectivityRepository } returns mockConnectivity

        val extras = MutableCreationExtras().apply {
            set(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY, mockApplication)
        }

        // Act
        val factory = BillsViewModelFactory.Factory
        val createdViewModel = factory.create(BillsViewModel::class.java, extras)

        // Assert
        assertNotNull(createdViewModel)
        assertTrue(createdViewModel is BillsViewModel)
    }
}