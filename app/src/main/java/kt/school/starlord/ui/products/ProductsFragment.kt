package kt.school.starlord.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kt.school.starlord.R

/**
 * Contains a recycler that is filled by products.
 *
 * Clicking on an item takes you to a product fragment.
 */
class ProductsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_products, container, false)
}
