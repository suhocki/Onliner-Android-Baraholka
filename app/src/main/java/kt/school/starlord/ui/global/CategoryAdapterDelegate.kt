package kt.school.starlord.ui.global

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_category.view.*
import kt.school.starlord.R
import kt.school.starlord.entity.Category
import kt.school.starlord.extension.inflate

/**
 * Delegate for categories in recycler view
 * @param clickListener listener for clicks on category
 */
class CategoryAdapterDelegate(
    private val clickListener: (Category) -> Unit
) : AbsListItemAdapterDelegate<Category, Any, CategoryAdapterDelegate.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup
    ) = ViewHolder(parent.inflate(R.layout.item_category))

    override fun isForViewType(
        item: Any,
        items: MutableList<Any>,
        position: Int
    ) = items[position] is Category

    override fun onBindViewHolder(
        item: Category,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) = holder.bind(item)

    /**
     * ViewHolder for subcategory
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item: Category

        init {
            view.setOnClickListener { clickListener(item) }
        }

        /**
         * Binds data with view
         */
        fun bind(item: Category) = with(itemView) {
            this@ViewHolder.item = item
            name.text = item.name
        }
    }
}