package kt.school.starlord.ui.categories

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
import kt.school.starlord.extension.setVisible
import kt.school.starlord.extension.showError
import kt.school.starlord.ui.global.AppRecyclerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Contains a recycler that is filled by categories.
 * Clicking on category takes you to the subcategories fragment.
 */
class CategoriesFragment : Fragment() {

    private val viewModel: CategoriesViewModel by viewModel()

    private val adapter by lazy {
        AppRecyclerAdapter(
            CategoryAdapterDelegate {
                val direction = CategoriesFragmentDirections.toSubcategories(it.name)
                findNavController().navigate(direction)
            }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_categories, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        viewModel.getCategories().observe(viewLifecycleOwner, Observer(adapter::setData))
        viewModel.getProgress().observe(viewLifecycleOwner, Observer(progressBar::setVisible))
        viewModel.getError().observe(viewLifecycleOwner, Observer(requireContext()::showError))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView.adapter = null
    }
}
