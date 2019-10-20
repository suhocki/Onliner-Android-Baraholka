package kt.school.starlord.ui.products

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_product.view.*
import kt.school.starlord.R
import kt.school.starlord.ui.global.UiEntity
import kt.school.starlord.ui.global.extension.inflate
import kt.school.starlord.ui.products.entity.UiProduct
import kotlin.properties.Delegates

/**
 * Delegate for product item in recycler view.
 * @param clickListener listener for clicks on product item.
 */
class ProductAdapterDelegate(
    private val clickListener: (UiProduct) -> Unit
) : AbsListItemAdapterDelegate<UiProduct, UiEntity, ProductAdapterDelegate.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup) = ViewHolder(parent.inflate(R.layout.item_product))

    override fun isForViewType(item: UiEntity, items: MutableList<UiEntity>, position: Int) =
        items[position] is UiProduct

    override fun onBindViewHolder(item: UiProduct, holder: ViewHolder, payloads: MutableList<Any>) = holder.bind(item)

    /**
     * ViewHolder for subcategory.
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var product: UiProduct by Delegates.notNull()

        init {
            view.setOnClickListener { clickListener(product) }
        }

        /**
         * Binds data with view.
         */
        fun bind(item: UiProduct) = with(itemView) {
            product = item

            Glide.with(image).load(item.image).into(image)
            lastUpdate.text = item.lastUpdate
            title.text = item.title
            description.text = item.description
            location.text = item.location
            owner.text = item.owner
            type.setBackgroundResource(item.typeColor)
            type.text = item.type
            commentsCount.text = item.comments
            price.text = item.price
            commentsCount.visibility = item.commentsCountVisibility
            price.visibility = item.priceVisibility
            bargain.visibility = item.bargainVisibility
        }
    }
}
