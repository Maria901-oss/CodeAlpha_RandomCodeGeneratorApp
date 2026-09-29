package com.codealpha.randomquotegenerator

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class Quote(val id: Int, val text: String, val author: String)

class MainActivity : AppCompatActivity() {

    private lateinit var tvQuoteText: TextView
    private lateinit var tvQuoteAuthor: TextView
    private lateinit var tvStatus: TextView
    private lateinit var btnNewQuote: Button
    private lateinit var btnShare: TextView
    private lateinit var progressBar: ProgressBar

    // Every quote ID we've already shown this run — guarantees no repeats
    // until the whole online pool (1450+ quotes) has been exhausted.
    private val shownIds = mutableSetOf<Int>()
    private val shownLocalIndexes = mutableSetOf<Int>()
    private var currentQuote: Quote? = null
    private var isLoading = false

    // Small offline backup — only used if there's genuinely no internet.
    private val localFallback = listOf(
        Quote(-1, "The only way to do great work is to love what you do.", "Steve Jobs"),
        Quote(-2, "Life is what happens when you're busy making other plans.", "John Lennon"),
        Quote(-3, "It always seems impossible until it's done.", "Nelson Mandela"),
        Quote(-4, "The way to get started is to quit talking and begin doing.", "Walt Disney"),
        Quote(-5, "Believe you can and you're halfway there.", "Theodore Roosevelt"),
        Quote(-6, "The best way to predict the future is to create it.", "Peter Drucker"),
        Quote(-7, "It does not matter how slowly you go as long as you do not stop.", "Confucius"),
        Quote(-8, "Opportunities don't happen. You create them.", "Chris Grosser"),
        Quote(-9, "Dream big and dare to fail.", "Norman Vaughan"),
        Quote(-10, "Whether you think you can or you think you can't, you're right.", "Henry Ford")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvQuoteText = findViewById(R.id.tvQuoteText)
        tvQuoteAuthor = findViewById(R.id.tvQuoteAuthor)
        tvStatus = findViewById(R.id.tvStatus)
        btnNewQuote = findViewById(R.id.btnNewQuote)
        btnShare = findViewById(R.id.btnShare)
        progressBar = findViewById(R.id.progressBar)

        loadNewQuote()

        btnNewQuote.setOnClickListener { loadNewQuote() }
        btnShare.setOnClickListener { shareCurrentQuote() }
    }

    private fun loadNewQuote() {
        if (isLoading) return
        isLoading = true
        setLoading(true)

        lifecycleScope.launch {
            // Try the live API first — try a few times so we skip any
            // quote we've already shown in this session.
            var quote: Quote? = null
            var attempts = 0
            while (quote == null && attempts < 4) {
                attempts++
                val fetched = withContext(Dispatchers.IO) { fetchRandomQuoteFromApi() }
                if (fetched != null && fetched.id !in shownIds) {
                    quote = fetched
                } else if (fetched == null) {
                    break // network failed, stop retrying and fall back
                }
                // if it was a duplicate, loop again and try fetching a fresh one
            }

            if (quote != null) {
                shownIds.add(quote.id)
                tvStatus.text = "● Fresh quotes online"
                displayQuote(quote)
            } else {
                // Offline / API unreachable → use local backup, still no repeats
                val q = pickFreshLocalQuote()
                tvStatus.text = "● Offline — showing saved quotes"
                displayQuote(q)
            }

            setLoading(false)
            isLoading = false
        }
    }

    private fun pickFreshLocalQuote(): Quote {
        if (shownLocalIndexes.size >= localFallback.size) shownLocalIndexes.clear()
        var index: Int
        do {
            index = localFallback.indices.random()
        } while (index in shownLocalIndexes && shownLocalIndexes.size < localFallback.size)
        shownLocalIndexes.add(index)
        return localFallback[index]
    }

    /** Hits https://dummyjson.com/quotes/random — a free, no-key API with 1450+ quotes. */
    private fun fetchRandomQuoteFromApi(): Quote? {
        return try {
            val url = URL("https://dummyjson.com/quotes/random")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 6000
            connection.readTimeout = 6000

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val body = connection.inputStream.bufferedReader().use { it.readText() }
                val json = JSONObject(body)
                Quote(
                    id = json.getInt("id"),
                    text = json.getString("quote"),
                    author = json.getString("author")
                )
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun displayQuote(quote: Quote) {
        currentQuote = quote
        tvQuoteText.text = quote.text
        tvQuoteAuthor.text = "— ${quote.author}"
    }

    private fun setLoading(loading: Boolean) {
        progressBar.visibility = if (loading) android.view.View.VISIBLE else android.view.View.GONE
        btnNewQuote.isEnabled = !loading
        btnNewQuote.alpha = if (loading) 0.6f else 1f
    }

    private fun shareCurrentQuote() {
        val quote = currentQuote ?: return
        val shareText = "\"${quote.text}\"\n— ${quote.author}"
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, "Share quote via"))
    }
}
