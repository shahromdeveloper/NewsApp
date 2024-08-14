package tj.donishomuz.megafonapk.api

import tj.donishomuz.megafonapk.models.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import tj.donishomuz.megafonapk.models.Article

interface NewsAPI {

    companion object {
        const val API_KEY = "fcb16e35e0cb4eb287d939f2191d6ccd"
    }

    @GET("v2/top-headlines")
    suspend fun getHeadlines(
        @Query("country")
        countryCode: String = "us",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>

    @GET("/v2/everything")
    suspend fun search(
        @Query("q")
        searchQuery: String = "",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>

}