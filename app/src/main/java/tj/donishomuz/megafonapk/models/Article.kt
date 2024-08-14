package tj.donishomuz.megafonapk.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import tj.donishomuz.megafonapk.db.ARTICLES_TABLE
import java.io.Serializable

@Entity(
    tableName = ARTICLES_TABLE
)
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = 0,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?,
) : Serializable