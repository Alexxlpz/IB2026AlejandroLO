package com.iberdrola.practicas2026.alejandroLO.homeViewModel

import app.cash.turbine.test
import com.iberdrola.practicas2026.alejandroLO.MainDispatcherRule
import com.iberdrola.practicas2026.alejandroLO.data.model.Direction
import com.iberdrola.practicas2026.alejandroLO.ui.features.home.viewModel.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var fakeDirectionRepository: FakeDirectionRepository
    private lateinit var fakeConnectivityRepository: FakeConnectivityRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        fakeDirectionRepository = FakeDirectionRepository()
        fakeConnectivityRepository = FakeConnectivityRepository()
        viewModel = HomeViewModel(
            directionRepository = fakeDirectionRepository,
            connectivityRepository = fakeConnectivityRepository
        )
    }

    @Test
    fun givenDirections_whenRefreshDirections_thenUiStateIsUpdated() = runTest {
        // Arrange
        val directions = listOf(
            Direction().apply { id = 1; street = "Calle 1" },
            Direction().apply { id = 2; street = "Calle 2" }
        )

        // Act
        fakeDirectionRepository.emit(directions)
        viewModel.refreshDirections()

        // Assert
        viewModel.uiState.test {
            val item = awaitItem()
            assertEquals(2, item.directionList.size)
            assertEquals("Calle 1", item.directionList[0].street)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenConnectivityChange_whenConnectivityRepositoryEmits_thenUiStateIsOnlineUpdated() = runTest {
        // Arrange
        // Act
        fakeConnectivityRepository.setOnlineMode(true)

        // Assert
        viewModel.uiState.test {
            val item = awaitItem()
            assertTrue(item.isOnline)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenIsOnline_whenUpdateDirectionsOnline_thenConnectivityRepositoryIsUpdated() = runTest {
        // Arrange
        val newOnlineStatus = true

        // Act
        viewModel.updateDirectionsOnline(newOnlineStatus, {})

        // Assert
        fakeConnectivityRepository.isOnline.test {
            val status = awaitItem()
            assertEquals(newOnlineStatus, status)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun givenInit_whenViewModelCreated_thenIsLoadingIsInitiallyTrue() = runTest {
        // Arrange
        // Act
        
        // Assert
        viewModel.uiState.test {
            val item = awaitItem()
            assertTrue(item.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
