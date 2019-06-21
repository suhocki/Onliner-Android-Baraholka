package kt.school.starlord.ui.subcategories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.fragment_categories.*
import kt.school.starlord.R
import kt.school.starlord.entity.Subcategory
import kt.school.starlord.extension.systemNotifier
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Contains a recycler that is filled by subcategories.
 * Clicking on subcategory takes you to a products fragment.
 */
class SubcategoriesFragment : Fragment() {

    private val viewModel: SubcategoriesViewModel by viewModel()
    private val adapter by lazy {
        SubcategoriesAdapter(
            onSubCategoryClick = {
                val direction = SubcategoriesFragmentDirections.toProducts(it.name)
                findNavController().navigate(direction)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val safeArgs = SubcategoriesFragmentArgs.fromBundle(requireArguments())
            val categoryName = safeArgs.categoryName
            viewModel.loadSubcategories(categoryName)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_subcategories, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@SubcategoriesFragment.adapter
        }

        viewModel.subcategories.observe(viewLifecycleOwner, Observer(adapter::setData))
        viewModel.progress.observe(viewLifecycleOwner, Observer(systemNotifier::showProgress))
        viewModel.error.observe(viewLifecycleOwner, Observer(systemNotifier::showError))
    }

    private inner class SubcategoriesAdapter(
        onSubCategoryClick: (Subcategory) -> Unit
    ) : ListDelegationAdapter<MutableList<Any>>() {
        init {
            items = mutableListOf()
            delegatesManager.addDelegate(SubcategoryAdapterDelegate { onSubCategoryClick(it) })
        }

        fun setData(data: List<Any>) {
            items.clear()
            items.addAll(data)

            notifyDataSetChanged()
        }
    }
}
