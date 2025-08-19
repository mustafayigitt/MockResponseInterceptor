package com.mustafayigit.mockresponseinterceptor.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.mustafayigit.mockresponseinterceptor.data.model.NewsModel
import com.mustafayigit.mockresponseinterceptor.util.State
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val newsState by viewModel.newsState.collectAsStateWithLifecycle()
            var isMockingEnabled by remember { mutableStateOf(isGlobalMockingEnabled) }
            val context = LocalContext.current

            LaunchedEffect(newsState) {
                if (newsState is State.Error) {
                    Toast.makeText(
                        context,
                        (newsState as State.Error).exception.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            MainScreen(
                newsState = newsState,
                isMockingEnabled = isMockingEnabled,
                onToggleMock = {
                    isMockingEnabled = !isMockingEnabled
                    isGlobalMockingEnabled = isMockingEnabled
                    viewModel.getNews()
                }
            )
        }
    }

    companion object {
        var isGlobalMockingEnabled = true
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    newsState: State<List<NewsModel>>,
    isMockingEnabled: Boolean,
    onToggleMock: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("News") })
        },
        floatingActionButton = {
            Button(onClick = onToggleMock) {
                Text(if (isMockingEnabled) "Disable Mock" else "Enable Mock")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (newsState) {
                is State.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is State.Error -> {
                    Text(
                        text = newsState.exception.message ?: "Unknown error",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )
                }

                is State.Success -> {
                    NewsList(news = newsState.data)
                }
            }
        }
    }
}

@Composable
fun NewsList(news: List<NewsModel>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(news) { item ->
            NewsItem(newsModel = item)
        }
    }
}

@Composable
fun NewsItem(newsModel: NewsModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            if (!newsModel.coverImage.isNullOrEmpty()) {
                AsyncImage(
                    model = newsModel.coverImage,
                    contentDescription = newsModel.title,
                    modifier = Modifier.size(64.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = newsModel.title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
