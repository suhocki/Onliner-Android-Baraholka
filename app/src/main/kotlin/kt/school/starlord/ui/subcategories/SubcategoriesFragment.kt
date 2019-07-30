package kt.school.starlord.ui.subcategories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_categories.*
import kt.school.starlord.R
import kt.school.starlord.ui.global.AppRecyclerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * Contains a recycler that is filled by subcategories.
 * Clicking on subcategory takes you to a products fragment.
 */
class SubcategoriesFragment : Fragment() {

    private val viewModel: SubcategoriesViewModel by viewModel(parameters = {
        val safeArgs = SubcategoriesFragmentArgs.fromBundle(requireArguments())
        parametersOf(safeArgs.categoryName)
    })

    private val adapter by lazy {
        AppRecyclerAdapter(
            SubcategoryAdapterDelegate {
                val direction = SubcategoriesFragmentDirections.toProducts(it.link)
                findNavController().navigate(direction)
            }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_subcategories, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        viewModel.getSubcategories().observe(viewLifecycleOwner, Observer(adapter::setData))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView.adapter = null
    }
}
