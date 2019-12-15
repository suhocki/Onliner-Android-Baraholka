package kt.school.starlord.model.data.android

import android.content.res.Resources
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

/**
 *  Wrapper class for hide Android Resources dependency.
 *  Needed to clean domain layer.
 */
@Suppress("SpreadOperator")
class ResourceManager constructor(private val resources: Resources) {
    /**
     * @return string from provided id.
     */
    fun getString(@StringRes id: Int): String =
        resources.getString(id)

    /**
     * @return string from provided id and args.
     */
    fun getString(@StringRes id: Int, vararg arguments: Any): String =
        resources.getString(id, *arguments)

    /**
     * @return plural from provided id and args.
     */
    fun getPlural(@PluralsRes id: Int, quantity: Int, vararg arguments: Any): String =
        resources.getQuantityString(id, quantity, *arguments)
}
