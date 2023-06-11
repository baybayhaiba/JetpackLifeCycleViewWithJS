package com.example.jetpacklifecycleviewwithjs

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import com.example.jetpacklifecycleviewwithjs.ui.theme.JetpackLifeCycleViewWithJSTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration


class MainActivity : ComponentActivity() {

    private val BASE_URL = "https://jsonplaceholder.typicode.com/"
    private val TAG = MainActivity::class.java.simpleName
    private val userIdState = MutableLiveData<Int>()
    private lateinit var jsonPlaceholderApi: JsonPlaceholderApi

    suspend fun getUser(userId: Int) = jsonPlaceholderApi.getUser(userId)

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create Retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create API client
        jsonPlaceholderApi = retrofit.create(JsonPlaceholderApi::class.java)


        setContent {
            JetpackLifeCycleViewWithJSTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Screen(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Transparent)
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Screen(modifier: Modifier = Modifier) {
        val scope = rememberCoroutineScope()
        var searchText by remember {
            mutableStateOf("")
        }
        var currentJob by remember {
            mutableStateOf<Job?>(null)
        }
        Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            TextField(value = searchText, onValueChange = {
                searchText = it
                Log.d(TAG, "Screen: ==> $it")
                currentJob?.cancel()
                currentJob = scope.async {
                    delay(300)
                    userIdState.value = it.toIntOrNull() ?: 1
                }
            })

            Spacer(modifier = Modifier.padding(16.dp))

            User()
        }
    }

    @Composable
    fun User(modifier: Modifier = Modifier) {
//        val scope = rememberCoroutineScope()
        val state by this.userIdState.observeAsState()
        val stateUI by loadUserState(userId = state ?: 1, jsonPlaceholderApi = jsonPlaceholderApi)

//        LaunchedEffect(Unit) {
//            val user = getUser(userId)
//            if (user != null)
//                this@MainActivity.state.postValue(user)
//        }


//        Text(
//            text = "Hello ${state.value?.name}!",
//            modifier = modifier
//        )

        stateUI.HandleResult(loadingContent = { LoadingView() }, successContent = { data: User? ->
            SuccessView(data = data) {
                Text(text = "Welcome to my channel : ${data?.name}")
            }
        }, errorContent = { errorMessage: String -> ErrorView(errorMessage = errorMessage) })

    }

    @Composable
    fun LoadingView(modifier: Modifier = Modifier) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            CircularProgressIndicator()
        }
    }

    @Composable
    fun <T> SuccessView(
        data: T,
        modifier: Modifier = Modifier,
        content: @Composable (data: T) -> Unit
    ) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            content(data)
        }
    }

    @Composable
    fun ErrorView(
        errorMessage: String,
        modifier: Modifier = Modifier
    ) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = errorMessage,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
    }

    @Composable
    fun loadUserState(userId: Int, jsonPlaceholderApi: JsonPlaceholderApi): State<Result<User>> {
        return produceState<Result<User>>(
            initialValue = Result.Loading,
            userId,
            jsonPlaceholderApi
        ) {
            val user = jsonPlaceholderApi.getUser(userId)

            value = if (user == null) Result.Error("no fetch")
            else Result.Success(user)
        }
    }


}



