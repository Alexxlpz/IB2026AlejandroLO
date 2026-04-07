package com.iberdrola.practicas2026.alejandroLO.billsViewModel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.MutableCreationExtras
import app.cash.turbine.test
import com.iberdrola.practicas2026.alejandroLO.IberdrolaApplication
import com.iberdrola.practicas2026.alejandroLO.MainDispatcherRule
import com.iberdrola.practicas2026.alejandroLO.data.AppContainer
import com.iberdrola.practicas2026.alejandroLO.data.model.Bill
import com.iberdrola.practicas2026.alejandroLO.data.repository.bill.BillsRepository
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
    private lateinit var viewModel: BillsViewModel

    @Before
    fun setUp() {
        fakeRepository = FakeBillsRepository()
        viewModel = BillsViewModel(fakeRepository)
    }

    @Test
    fun givenBills_whenRefreshBills_thenUiStateIsUpdated() = runTest {
        viewModel.uiState.test {
            // Arrange
            val first = awaitItem()

            // Act

            // Assert
            assertTrue(first.isLoading) // al iniciar se lanza refreshBills()
            //cerramos turbine, ya hemos comprobado que carga mientras espera
            cancelAndIgnoreRemainingEvents()
        }
    }
    @Test
    fun givenSelectedOption_WhenChangeSelectedOption_ThenUiStateIsUpdated() = runTest {
        // Arrange
        // Act
        viewModel.updateSelectedOption(BillTypeEnum.LUZ)
        val uiState = viewModel.uiState.value

        // Assert
        assertEquals(BillTypeEnum.LUZ, uiState.selectedOption)
    }
    @Test
    fun givenIsNotOnline_WhenUpdateDataBase_ThenUiStateIsUpdated() = runTest {
        // Arrange
        val initialIsOnline = viewModel.uiState.value.isOnline

        // Act
        viewModel.updateDataBase(true)
        val uiState = viewModel.uiState.value
        // Assert
        assertFalse(initialIsOnline)
        assertTrue(uiState.isOnline)
    }
    @Test
    fun givenBills_whenGetAllBills_thenUiStateIsUpdated() = runTest {
        // Arrange
        val bill1 = Bill(id = 1, price = 50.0, typeId = 1)
        val bill2 = Bill(id = 2, price = 100.0, typeId = 0)
        // Act
        fakeRepository.emit(listOf(bill1, bill2))

        // assert
        viewModel.uiState.test {
            val emission = awaitItem()
            assertEquals(2, emission.billsList.size)
            assertTrue(emission.billsList.any { it.id == 1 && it.price == 50.0 && it.typeId == 1 })
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenFactory_whenCreateViewModel_thenReturnsBillsViewModelWithRepository() {
        // Arrangle
        val mockApplication = mockk<IberdrolaApplication>(relaxed = true)
        val mockContainer = mockk<AppContainer>()
        val mockRepository = mockk<BillsRepository>()

        every { mockApplication.container } returns mockContainer
        every { mockContainer.billsRepository } returns mockRepository

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