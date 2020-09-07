package world.trav.lazyfood.androidApp.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.esafirm.imagepicker.features.ImagePicker
import timber.log.Timber
import world.trav.lazyfood.androidApp.R
import world.trav.lazyfood.androidApp.databinding.DefaultFragmentBinding


class DefaultFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: DefaultFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DefaultFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        Timber.d("create")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        Timber.d("create menu")
        inflater.inflate(R.menu.default_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            R.id.action_add -> {
                Timber.d("on add food photo")
                ImagePicker.create(this).start()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }


    }

}