package kt.school.starlord.ui.categories

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_category.view.*
import kt.school.starlord.R
import kt.school.starlord.domain.entity.category.Category
import kt.school.starlord.ui.global.extension.inflate
import java.util.Stack
import kotlin.properties.Delegates

/**
 * Delegate for categories in recycler view.
 * @param clickListener listener for clicks on category
 */
class CategoryAdapterDelegate(
    val context: Context,
    private val clickListener: (Category) -> Unit
) : AbsListItemAdapterDelegate<Category, Any, CategoryAdapterDelegate.ViewHolder>() {

    private val asyncLayoutInflater = AsyncLayoutInflater(context)
    private val cachedViews = Stack<View>()

    companion object {
        const val NUM_CACHED_VIEWS = 5
    }

    init {
        cacheViewsAsync(NUM_CACHED_VIEWS)
    }

    /**
     * Create some views asynchronously and add them to our stack.
     */
    @SuppressLint("InflateParams")
    fun cacheViewsAsync(num: Int) {
        repeat(num) {
            asyncLayoutInflater.inflate(
                R.layout.item_category,
                null
            ) { view, _, _ ->
                cachedViews.push(view)
            }
        }
    }

    /**
     * Use the cached views if possible, otherwise if we run out of cached views inflate a new one.
     */
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val view = if (cachedViews.isEmpty()) {
            parent.inflate(R.layout.item_category)
        } else {
            cachedViews.pop()
                .also { it.layoutParams = ViewGroup.LayoutParams(it.width, it.height) }
        }
        return ViewHolder(view)
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int) =
        items[position] is Category

    override fun onBindViewHolder(item: Category, holder: ViewHolder, payloads: MutableList<Any>) =
        holder.bind(item)

    /**
     * ViewHolder for subcategory.
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var category: Category by Delegates.notNull()

        init {
            view.setOnClickListener { clickListener(category) }
        }

        /**
         * Binds data with view.
         */
        fun bind(item: Category) {
            category = item
            itemView.name.text = item.name
        }
    }
}
