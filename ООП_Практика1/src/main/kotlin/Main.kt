import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import com.google.gson.Gson
import java.awt.Desktop

data class SearchResult(val query: Query)
data class Query(val search: List<Article>)
data class Article(val title: String, val pageid: Int)

class WikiApiClient {
    fun search(query: String): List<Article> {
        val encodedQuery = URLEncoder.encode(query, "UTF-8")
        val apiUrl = "https://ru.wikipedia.org/w/api.php?action=query&list=search&utf8=&format=json&srsearch=\"$encodedQuery\""
        val response = makeApiCall(apiUrl)
        val searchResults = Gson().fromJson(response, SearchResult::class.java)
        return searchResults.query.search
    }

    private fun makeApiCall(apiUrl: String): String {
        val url = URL(apiUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        return connection.inputStream.bufferedReader().readText()
    }
}

fun openInBrowser(url: String) {
    if (Desktop.isDesktopSupported()) {
        Desktop.getDesktop().browse(URL(url).toURI())
    } else {
        println("Открытие в браузере не поддерживается.")
    }
}

fun main() {
    println("Введите поисковый запрос:")
    val query = readLine() ?: return

    val apiClient = WikiApiClient()
    val articles = apiClient.search(query)

    if (articles.isNotEmpty()) {
        println("Результаты поиска:")
        articles.forEachIndexed { index, article ->
            println("${index + 1}: ${article.title} (pageid: ${article.pageid})")
        }

        println("Выберите номер статьи для открытия в браузере:")
        val selectedIndex = readLine()?.toIntOrNull()?.minus(1)

        selectedIndex?.let {
            if (it in articles.indices) {
                val pageId = articles[it].pageid
                openInBrowser("https://ru.wikipedia.org/w/index.php?curid=$pageId")
            } else {
                println("Неверный номер статьи.")
            }
        } ?: println("Некорректный ввод.")
    } else {
        println("Не найдено результатов.")
    }
}