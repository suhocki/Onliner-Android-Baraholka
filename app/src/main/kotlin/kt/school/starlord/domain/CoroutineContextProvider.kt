package kt.school.starlord.domain

import kotlin.coroutines.CoroutineContext

/**
 * Interface for describing coroutine contexts
 */
interface CoroutineContextProvider {
    val main: CoroutineContext
    val default: CoroutineContext
    val io: CoroutineContext
}
