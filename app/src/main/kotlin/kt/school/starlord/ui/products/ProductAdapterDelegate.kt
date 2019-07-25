package kt.school.starlord.ui.products

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlin.properties.Delegates
import kotlinx.android.synthetic.main.item_product.view.*
import kt.school.starlord.R
import kt.school.starlord.entity.product.Product
import kt.school.starlord.entity.product.ProductPrice
import kt.school.starlord.entity.product.ProductType
import kt.school.starlord.extension.inflate
import kt.school.starlord.extension.toRoundedPrice
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.textResource

/**
 * Delegate for product item in recycler view.
 * @param clickListener listener for clicks on product item.
 */
class ProductAdapterDelegate(
    private val clickListener: (Product) -> Unit
) : AbsListItemAdapterDelegate<Product, Any, ProductAdapterDelegate.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup) = ViewHolder(parent.inflate(R.layout.item_product))

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int) = items[position] is Product

    override fun onBindViewHolder(item: Product, holder: ViewHolder, payloads: MutableList<Any>) = holder.bind(item)

    /**
     * ViewHolder for subcategory.
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var product: Product by Delegates.notNull()

        init {
            view.setOnClickListener { clickListener(product) }
        }

        /**
         * Binds data with view.
         */
        fun bind(item: Product) = with(itemView) {
            product = item
            Glide.with(image).load(product.image).into(image)
            title.text = product.title
            description.text = product.description
            location.text = product.location
            owner.text = product.owner.name
            lastUpdate.text = product.lastUpdate
            bindType(type, product.type)
            bindCommentsCount(commentsCount, product.commentsCount)
            bindPrice(price, product.price)
            bindBargain(bargain, product.price)
        }

        private fun bindType(typeView: TextView, type: ProductType) {
            typeView.text = type.toString()
            typeView.backgroundColorResource = type.color
        }

        private fun bindCommentsCount(commentsView: TextView, commentsCount: Long) {
            if (commentsCount > 0) {
                commentsView.visibility = View.VISIBLE
                commentsView.text = product.commentsCount.toString()
            } else {
                commentsView.visibility = View.GONE
            }
        }

        private fun bindPrice(priceView: TextView, price: ProductPrice) = when (price.amount) {
            null -> priceView.visibility = View.GONE
            0.0 -> {
                priceView.visibility = View.VISIBLE
                priceView.textResource = R.string.for_free
            }
            else -> priceView.text = itemView.resources.getString(R.string.price, price.amount.toRoundedPrice())
        }

        private fun bindBargain(bargainView: TextView, price: ProductPrice) {
            if (price.amount == 0.0) {
                bargainView.visibility = View.GONE
            } else if (price.isBargainAvailable) {
                bargainView.visibility = View.VISIBLE
            }
        }
    }
}
