package kt.school.starlord.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.DataSource
import androidx.paging.PositionalDataSource
import kt.school.starlord.domain.data.mapper.BaseConverter
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.core.AllOf

inline fun <reified T> LiveData<T>.observeForTesting(
    block: (T) -> Unit
) {
    val observer = Observer<T> { Unit }
    try {
        observeForever(observer)
        block(value as T)
    } finally {
        removeObserver(observer)
    }
}

inline fun <reified FROM : Any, reified TO : Any> createConverter(map: Map<FROM, TO>) =
    object : BaseConverter<FROM, TO>(FROM::class, TO::class) {
        override fun convert(value: FROM) = map[value] ?: error("Could not convert $value.")
    }

inline fun <reified FROM : Any, reified TO : Any> createConverter(pair: Pair<FROM, TO>) =
    object : BaseConverter<FROM, TO>(FROM::class, TO::class) {
        override fun convert(value: FROM) = if (pair.first == value) pair.second else error("Could not convert $value.")
    }

fun <T> createDataSource(items: List<T>) = object : DataSource.Factory<Int, T>() {
    override fun create() = object : PositionalDataSource<T>() {
        override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<T>) =
            callback.onResult(items)

        override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<T>) =
            callback.onResult(items, 0, items.count())
    }
}

fun checkSnackBarDisplayedByMessage(message: String) {
    onView(AllOf.allOf(withId(com.google.android.material.R.id.snackbar_text), withText(message)))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
}
