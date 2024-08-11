package tj.donishomuz.megafonapk.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import tj.donishomuz.megafonapk.R
import tj.donishomuz.megafonapk.adapters.ViewPagerAdapter
import tj.donishomuz.megafonapk.databinding.ActivityNewsBinding
import tj.donishomuz.megafonapk.db.ArticleDatabase
import tj.donishomuz.megafonapk.repository.local.NewsLocalRepository
import tj.donishomuz.megafonapk.repository.remote.NewsRemoteRepository
import tj.donishomuz.megafonapk.ui.fragments.AllNewsFragment
import tj.donishomuz.megafonapk.ui.fragments.BusinessFragment
import tj.donishomuz.megafonapk.ui.fragments.PoliticsFragment
import tj.donishomuz.megafonapk.ui.fragments.TechFragment

class NewsActivity : AppCompatActivity() {

    lateinit var newsViewModel: NewsViewModel
    private lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter= ViewPagerAdapter(supportFragmentManager)

        adapter.addFragment(AllNewsFragment(),"All News")
        adapter.addFragment(BusinessFragment(),"Business")
        adapter.addFragment(PoliticsFragment(),"Politics")
        adapter.addFragment(TechFragment(),"Tech")

        binding.viewPager.adapter=adapter
        binding.tbLayout.setupWithViewPager(binding.viewPager)

        val newsRemoteRepository = NewsRemoteRepository()
        val newsLocalRepository = NewsLocalRepository(ArticleDatabase(this))
        val viewModelProviderFactory =
            NewsViewModelProviderFactory(application, newsRemoteRepository, newsLocalRepository)

        newsViewModel =
            ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navHostController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navHostController)

    }
}