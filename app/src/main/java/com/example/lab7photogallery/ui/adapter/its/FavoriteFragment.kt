package com.example.lab7photogallery.ui.adapter.its

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cyberbyte.photogallery.ui.adapter.PhotoAdapter
import com.cyberbyte.photogallery.ui.adapter.PhotoListener
import com.cyberbyte.photogallery.R
import com.cyberbyte.photogallery.ui.viewmodel.MainViewModel
import com.cyberbyte.photogallery.databinding.FragmentMainBinding
import com.cyberbyte.photogallery.model.Photo
import com.cyberbyte.photogallery.model.Src
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI
import org.kodein.di.instance

class FavoriteFragment : Fragment(), DIAware, PhotoListener {

    override val di: DI by closestDI()

    private lateinit var binding: FragmentMainBinding
    private lateinit var photoAdapter: PhotoAdapter
    private val viewModel: MainViewModel by instance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentMainBinding.inflate(inflater, container, false)

        val root: View = binding.root

        binding.viewModel = viewModel
        photoAdapter = PhotoAdapter(emptyList(), this)
        binding.recyclerViewMovie.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewMovie.adapter = photoAdapter

        viewModel.favoritePhotos.observe(viewLifecycleOwner) { photos ->
            photoAdapter.updatePhotos(photos.map{
                Photo(
                    id = it.id,
                    photographer = it.author,
                    src = Src(original = it.url),
                    favorite = it.favorite
                )
            })
        }
        viewModel.loadPhotos()

        return root
    }

    override fun onPhotoClicked(photo: Photo) {
        Toast.makeText(this.context, "Clicked on movie: ${photo.photographer}", Toast.LENGTH_SHORT).show()
        viewModel.onFavouriteClicked(photo)
        photoAdapter.notifyDataSetChanged()
    }
}