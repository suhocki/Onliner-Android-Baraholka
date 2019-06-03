package kt.school.starlord.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.fragment_categories.*
import kt.school.starlord.R
import kt.school.starlord.entity.Category
import kt.school.starlord.ui.global.CategoryAdapterDelegate
import org.koin.androidx.viewmodel.ext.android.viewModel

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

    init {
        lifecycleScope.launchWhenStarted {
            val categories = viewModel.loadCategories()
            adapter.setData(categories)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = this@CategoriesFragment.adapter
        }
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
