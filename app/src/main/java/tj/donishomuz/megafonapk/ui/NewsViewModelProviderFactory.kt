package tj.donishomuz.megafonapk.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tj.donishomuz.megafonapk.repository.local.NewsLocalRepository
import tj.donishomuz.megafonapk.repository.remote.NewsRemoteRepository

class NewsViewModelProviderFactory(
    val app: Application,
    private val newsRemoteRepository: NewsRemoteRepository,
    private val newsLocalRepository: NewsLocalRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(app, newsRemoteRepository, newsLocalRepository) as T
    }
}