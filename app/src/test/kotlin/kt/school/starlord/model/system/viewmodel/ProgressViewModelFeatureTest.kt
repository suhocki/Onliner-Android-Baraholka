package kt.school.starlord.model.system.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kt.school.starlord.ui.TestCoroutineRule
import kt.school.starlord.ui.observeForTesting
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyBoolean

class ProgressViewModelFeatureTest {

    @get:Rule
    internal val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    internal val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val feature = ProgressViewModelFeature()

    @Test
    fun `show progress`() {
        // Given
        val isVisible = anyBoolean()

        // When
        feature.showProgress(isVisible)

        // Then
        feature.getProgress().observeForTesting {
            assert(it == isVisible)
        }
    }
}
