package tj.donishomuz.megafonapk.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import tj.donishomuz.megafonapk.R
import tj.donishomuz.megafonapk.adapters.NewsAdapter
import tj.donishomuz.megafonapk.databinding.FragmentFavoriteBinding
import tj.donishomuz.megafonapk.databinding.FragmentSettingsBinding
import tj.donishomuz.megafonapk.ui.NewsActivity
import tj.donishomuz.megafonapk.ui.NewsViewModel

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private lateinit var binding: FragmentSettingsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)





    }


}