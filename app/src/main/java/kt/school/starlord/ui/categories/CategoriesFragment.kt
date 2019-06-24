package kt.school.starlord.ui.categories

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
import kt.school.starlord.entity.Category
import kt.school.starlord.extension.systemNotifier
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Contains a recycler that is filled by categories.
 * Clicking on category takes you to the subcategories fragment.
 */
class CategoriesFragment : Fragment() {

    private val viewModel: CategoriesViewModel by viewModel()

    private val adapter by lazy {
        CategoriesAdapter(
            onCategoryClick = {
                val direction = CategoriesFragmentDirections.toSubcategories(it.name)
                findNavController().navigate(direction)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.loadLocalCategories()
            viewModel.loadRemoteCategories()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_categories, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@CategoriesFragment.adapter
        }

        viewModel.categories.observe(viewLifecycleOwner, Observer(adapter::setData))
        viewModel.progress.observe(viewLifecycleOwner, Observer(systemNotifier::showProgress))
        viewModel.error.observe(viewLifecycleOwner, Observer(systemNotifier::showError))
    }

    private inner class CategoriesAdapter(
        onCategoryClick: (Category) -> Unit
    ) : ListDelegationAdapter<MutableList<Any>>() {

        init {
            items = mutableListOf()
            delegatesManager.addDelegate(CategoryAdapterDelegate(onCategoryClick))
        }

        fun setData(data: List<Any>) {
            items.clear()
            items.addAll(data)

            notifyDataSetChanged()
        }
    }
}
