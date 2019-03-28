package kt.school.starlord.ui.appbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import kt.school.starlord.R

class AppBarFragment : Fragment(), OnBackPressedCallback {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_appbar, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requireActivity().addOnBackPressedCallback(viewLifecycleOwner, this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().removeOnBackPressedCallback(this)
    }

    override fun handleOnBackPressed(): Boolean {
        return NavHostFragment.findNavController(this).navigateUp()
    }
}