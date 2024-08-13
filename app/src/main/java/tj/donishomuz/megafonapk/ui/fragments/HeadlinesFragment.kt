package tj.donishomuz.megafonapk.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.launch
import tj.donishomuz.megafonapk.R
import tj.donishomuz.megafonapk.adapters.NewsAdapter
import tj.donishomuz.megafonapk.adapters.ViewPagerAdapter
import tj.donishomuz.megafonapk.adapters.ViewPagerAdapter22
import tj.donishomuz.megafonapk.api.RetrofitInstance
import tj.donishomuz.megafonapk.databinding.FragmentHeadlinesBinding
import tj.donishomuz.megafonapk.models.Article
import tj.donishomuz.megafonapk.ui.NewsActivity
import tj.donishomuz.megafonapk.ui.NewsViewModel
import tj.donishomuz.megafonapk.models.Resource

class HeadlinesFragment : Fragment(R.layout.fragment_headlines) {

    lateinit var newsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var retryButton: Button
    private lateinit var errorText: TextView
    private lateinit var itemHeadlinesError: CardView
    private lateinit var binding: FragmentHeadlinesBinding

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ViewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHeadlinesBinding.bind(view)

        val adapterVp= ViewPagerAdapter22(childFragmentManager)

        adapterVp.addFragment(AllNewsFragment(),"All News")
        adapterVp.addFragment(BusinessFragment(),"Busines")
        adapterVp.addFragment(PoliticsFragment(),"Politics")
        adapterVp.addFragment(TechFragment(),"Tech")
        adapterVp.addFragment(TechFragment(),"Science")

        binding.vPager.adapter=adapterVp
        binding.tbLayout.setupWithViewPager(binding.vPager)

        viewPager = view.findViewById(R.id.view_pager)
//        val imageList = ArrayList<SlideModel>()
//
//        imageList.add(SlideModel(R.drawable.image_newspaper))
//        imageList.add(SlideModel(R.drawable.image_newspaper))
//        imageList.add(SlideModel(R.drawable.image_newspaper))
//        imageList.add(SlideModel(R.drawable.image_newspaper))
//        imageList.add(SlideModel(R.drawable.image_newspaper))
//        binding.imageSlider.setImageList(imageList, ScaleTypes.FIT)



        itemHeadlinesError = view.findViewById(R.id.itemHeadlinesError)

        val inflater =
            requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_error, null)


        fetchImages()

        retryButton = view.findViewById(R.id.retryButton)
        errorText = view.findViewById(R.id.errorText)

        newsViewModel = (activity as NewsActivity).newsViewModel

        setupHeadlinesRecycler()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_headlinesFragment_to_articleFragment, bundle)
        }

        newsViewModel.headlines.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success<*> -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / 20 + 2
                        isLastPage = newsViewModel.headlinesPage == totalPages

                        if (isLastPage) {
                            binding.recyclerHeadlines.setPadding(0, 0, 0, 0)
                        }
                    }
                }

                is Resource.Error<*> -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "Sorry error: $message", Toast.LENGTH_SHORT).show()
                        showErrorMessage(message)
                    }

                }

                is Resource.Loading<*> -> {
                    showProgressBar()
                }
            }
        })

        binding.itemHeadlinesError.retryButton.setOnClickListener {
            newsViewModel.getHeadlinesNews("us")
        }
    }


    private fun fetchImages() {
        lifecycleScope.launch {
            try {
                val images = RetrofitInstance.api.getHeadlines(
                    "us", 1
                )
                setupViewPager(images.body()!!.articles)
            } catch (e: Exception) {

            }
        }
    }

    private fun setupViewPager(images: List<Article>) {
        adapter = ViewPagerAdapter(images)
        binding.viewPager.adapter = adapter
    }


    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideErrorMessage() {
        itemHeadlinesError.visibility = View.INVISIBLE
        isError = false
    }

    private fun showErrorMessage(message: String) {
        itemHeadlinesError.visibility = View.VISIBLE
        errorText.text = message
        isError = true
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeigining = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= 20
            val shouldPaginate =
                isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeigining
                        && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                newsViewModel.getHeadlinesNews("us")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setupHeadlinesRecycler() {
        newsAdapter = NewsAdapter()
        binding.recyclerHeadlines.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@HeadlinesFragment.scrollListener)
        }
    }
}























