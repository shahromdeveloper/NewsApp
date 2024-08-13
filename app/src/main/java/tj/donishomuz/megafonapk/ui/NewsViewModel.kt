package tj.donishomuz.megafonapk.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import tj.donishomuz.megafonapk.models.Article
import tj.donishomuz.megafonapk.models.NewsResponse
import tj.donishomuz.megafonapk.repository.local.NewsLocalRepository
import tj.donishomuz.megafonapk.repository.remote.NewsRemoteRepository
import tj.donishomuz.megafonapk.models.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
    app: Application,
    private val newsRemoteRepository: NewsRemoteRepository,
    val newsLocalRepository: NewsLocalRepository
) :
    AndroidViewModel(app) {
    val headlines: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var headlinesPage = 1
    private var headlinesResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    private var searchNewsResponse: NewsResponse? = null
    private var newSearchQuery: String? = null
    private var oldSearchQuery: String? = null

    private val _isArticleSaved = MutableLiveData<Boolean>()
    val isArticleSaved: LiveData<Boolean> get() = _isArticleSaved

    init {
        getHeadlinesNews("us")
        updateArticleSaveStatus(false)
    }

    fun getHeadlinesNews(countryCode: String) = viewModelScope.launch {
        headlinesNewsRemote(countryCode)
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNewsRemote(searchQuery)
    }

    private fun handleHeadlinesResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                headlinesPage++
                if (headlinesResponse == null) {
                    headlinesResponse = resultResponse
                } else {
                    val oldArticles = headlinesResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(headlinesResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (searchNewsResponse == null || newSearchQuery != oldSearchQuery) {
                    oldSearchQuery = newSearchQuery
                    searchNewsResponse = resultResponse
                } else {
                    searchNewsPage++
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun addNewsToFavorites(article: Article) = viewModelScope.launch {
        val result = newsLocalRepository.insert(article)
        if (result > 0) {
            updateArticleSaveStatus(true)
        }
    }

    fun getFavoriteNews() = newsLocalRepository.getFavoriteNews()

    fun getFavoriteNewsByPublishedAt(publishedAt: String) {
        newsLocalRepository.getFavoriteNewsByPublishedAt(publishedAt).observeForever { article ->
            if (article?.id != null) {
                updateArticleSaveStatus(true)
            } else {
                updateArticleSaveStatus(false)
            }
        }
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        try {
            val result = newsLocalRepository.deleteArticle(article)
            if (result > 0) {
                updateArticleSaveStatus(false)
            } else {
                updateArticleSaveStatus(true)
            }
        } catch (e: Exception) {
            Log.d("", e.toString())
        }
    }

    fun deleteArticleByPublishedAt(publishedAt: String) = viewModelScope.launch {
        try {
            val result = newsLocalRepository.deleteArticleByPublishedAt(publishedAt)
            if (result > 0) {
                updateArticleSaveStatus(false)
            } else {
                updateArticleSaveStatus(true)
            }
        } catch (e: Exception) {
            Log.d("", e.toString())
        }
    }

    private fun internetConnection(context: Context): Boolean {
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            return getNetworkCapabilities(activeNetwork)?.run {
                when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } ?: false
        }
    }

    private suspend fun headlinesNewsRemote(countryCode: String) {
        headlines.postValue(Resource.Loading())
        try {
            if (internetConnection(this.getApplication())) {
                val response = newsRemoteRepository.getHeadlines(countryCode, headlinesPage)
                headlines.postValue(handleHeadlinesResponse(response))
            } else {
                headlines.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> headlines.postValue(Resource.Error("Unable to connect"))
                else -> headlines.postValue(Resource.Error("No signal"))
            }
        }
    }

    private suspend fun searchNewsRemote(searchQuery: String) {
        newSearchQuery = searchQuery
        searchNews.postValue(Resource.Loading())
        try {
            if (internetConnection(this.getApplication())) {
                val response = newsRemoteRepository.search(searchQuery, searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                searchNews.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchNews.postValue(Resource.Error("Unable to connect"))
                else -> searchNews.postValue(Resource.Error("No signal"))
            }
        }
    }

    private fun updateArticleSaveStatus(newValue: Boolean) {
        _isArticleSaved.value = newValue
    }
}
















