package kt.school.starlord.ui

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.runBlockingTest
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@Suppress("EXPERIMENTAL_API_USAGE")
internal class TestCoroutineRule : TestWatcher() {
    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
    private val testScope: TestCoroutineScope = TestCoroutineScope(testDispatcher)

    override fun starting(description: Description?) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
    }

    fun runBlockingTest(block: suspend TestCoroutineScope.() -> Unit) {
        testScope.runBlockingTest(block)
    }
}
