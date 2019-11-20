package kt.school.starlord.di

import org.koin.core.qualifier.named

/**
 * Contains constants to describe objects of the same types for our DI.
 * Qualifier - it is a name of the definition (when specified name parameter in your definition).
 */
object Qualifier {
    val LOCALIZED = named("localized")
}
