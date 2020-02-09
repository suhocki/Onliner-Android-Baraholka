package kt.school.starlord.ui.subcategories

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_subcategory.view.*
import kt.school.starlord.R
import kt.school.starlord.ui.global.extension.inflate
import kt.school.starlord.ui.subcategories.entity.UiSubcategory
import java.util.Stack
import kotlin.properties.Delegates

/**
 * Delegate for subcategories in recycler view.
 * @param clickListener listener for clicks on subcategory
 */
class SubcategoryAdapterDelegate(
    val context: Context,
    private val clickListener: (UiSubcategory) -> Unit
) : AbsListItemAdapterDelegate<UiSubcategory, Any, SubcategoryAdapterDelegate.ViewHolder>() {

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
                R.layout.item_subcategory,
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
            parent.inflate(R.layout.item_subcategory)
        } else {
            cachedViews.pop()
                .also { it.layoutParams = LinearLayout.LayoutParams(it.width, it.height) }
        }
        return ViewHolder(view)
    }

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int) = items[position] is UiSubcategory

    override fun onBindViewHolder(item: UiSubcategory, holder: ViewHolder, payloads: MutableList<Any>) =
        holder.bind(item)

    /**
     * ViewHolder for subcategory.
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var subcategory: UiSubcategory by Delegates.notNull()

        init {
            view.setOnClickListener { clickListener(subcategory) }
        }

        /**
         * Binds data with view.
         */
        fun bind(item: UiSubcategory) = with(itemView) {
            subcategory = item
            name.text = item.name
            count.visibility = item.countVisibility
            count.text = item.count
        }
    }
}
