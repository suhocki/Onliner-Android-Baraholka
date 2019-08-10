package kt.school.starlord.model.system.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import kt.school.starlord.ui.TestCoroutineRule
import kt.school.starlord.ui.observeForTesting
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import timber.log.Timber

class ErrorViewModelFeatureTest {

    @get:Rule
    internal val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    internal val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val feature = ErrorViewModelFeature()

    @Before
    fun before() {
        mockkStatic(Timber::class)
    }

    @After
    fun after() {
        unmockkStatic(Timber::class)
    }

    @Test
    fun `show error`() {
        // Given
        val error: Throwable = mockk()

        // When
        feature.showError(error)

        // Then
        verify { Timber.e(error) }

        feature.getError().observeForTesting {
            assert(it == error)
        }
    }
}
