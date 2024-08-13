package tj.donishomuz.megafonapk.api

import tj.donishomuz.megafonapk.models.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import tj.donishomuz.megafonapk.models.Article

interface NewsAPI {
    @GET("v2/top-headlines")
    suspend fun getHeadlines(
        @Query("country")
        countryCode: String = "us",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = "77a0be9ab5e9406b8806dea96af9cebf"
    ): Response<NewsResponse>


    @GET("/v2/everything")
    suspend fun search(
        @Query("q")
        searchQuery: String = "",
        @Query("page")
        pageNumber: Int = 1,
        @Query("apiKey")
        apiKey: String = "77a0be9ab5e9406b8806dea96af9cebf"
    ): Response<NewsResponse>

    @GET("v2/top-headlines")
    suspend fun getImages(): List<Article>


        @GET("v2/top-headlines")
        suspend fun getTopHeadlines(
            @Query("country") country: String = "us",
            @Query("apiKey") apiKey: String
        ): NewsResponse
}