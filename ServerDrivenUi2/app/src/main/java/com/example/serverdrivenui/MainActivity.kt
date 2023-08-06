package com.example.serverdrivenui

import android.os.Bundle
import androidx.compose.foundation.lazy.items
import android.util.Log
import android.util.Size
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.serverdrivenui.Model.ListData
import com.example.serverdrivenui.Model.UiConfig
import com.example.serverdrivenui.ui.theme.ServerDrivenUiTheme
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ServerDrivenUiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val listData = listData()
                    ServerDrivenUI(data = listData,)


                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ServerDrivenUiTheme {
        Greeting("Android")
    }
}



suspend fun getDataFromFireStore() :UiConfig {

    val database = FirebaseFirestore.getInstance()
    var data = UiConfig()


    database.collection("uiconfig").get().await().map {
        data=  it.toObject(UiConfig::class.java)

    }

    return data;




}


fun listData() :List<ListData>{

    val list= listOf<ListData>(


        ListData(R.drawable.undraw_professor)
        ,
        ListData(R.drawable.undraw_design_community_rcft),

        ListData(R.drawable.undraw_add_notes),

        ListData(R.drawable.undraw_newspaper_icon),

        ListData(R.drawable.undraw_add_notes),

        ListData(R.drawable.undraw_newspaper_icon),

        )

    return list


}


@Composable
fun ImageCard(
    imageResId: Int,
    modifier: Modifier = Modifier.padding(20.dp),
    shape: Shape = RoundedCornerShape(10.dp),
    contentScale: ContentScale = ContentScale.Crop


) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .width(200.dp)
            .height(200.dp),

    ) {
        Image(

            painter = painterResource(id = imageResId),
            contentDescription = null, // Provide a meaningful description for accessibility
            contentScale = contentScale,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .width(100.dp)
                .padding(20.dp)
                .weight(0.5f)
        )
        
        Spacer(modifier = Modifier
            .height(10.dp)
            .width(10.dp))

       
    }
}



@Composable
fun ServerDrivenUI(data: List<ListData> ,viewModel: UiConfigViewModel = viewModel()) {

    val uiConfigState by viewModel.uiConfig

    LaunchedEffect(key1 = uiConfigState, block = {

    })


    when (uiConfigState.uiConfig) {
        "grid" -> {
            LazyVerticalGrid(
                state= rememberLazyGridState(),
                columns = GridCells.Adaptive(minSize = 128.dp),
                modifier = Modifier
                    .height(100.dp)
                    .width(50.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                content = {
                    items(data) { item ->
                        // Render grid item UI here
                           ImageCard(imageResId = item.image)

                    }
                }
            )
        }
        "list" -> {
            LazyColumn(
                state= rememberLazyListState(),
                contentPadding = PaddingValues(vertical = 8.dp),
                content = {
                    items(data) { item ->
                        // Render list item UI here
                        ImageCard(imageResId = item.image)

                    }
                }
            )
        }
        // Handle other view types as needed
        else -> {
            // Default fallback or error handling

         SimpleCircularProgressComponent()
        }
    }
}

@Composable
fun SimpleCircularProgressComponent() {
    // CircularProgressIndicator is generally used
    // at the loading screen and it indicates that
    // some progress is going on so please wait.
    Column(
        // we are using column to align our
        // imageview to center of the screen.
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),

        // below line is used for specifying
        // vertical arrangement.
        verticalArrangement = Arrangement.Center,

        // below line is used for specifying
        // horizontal arrangement.
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        // below line is use to display
        // a circular progress bar.
        CircularProgressIndicator(
            // below line is use to add padding
            // to our progress bar.
            modifier = Modifier.padding(16.dp),

            // below line is use to add color
            // to our progress bar.
            color = colorResource(id = R.color.purple_200),

            // below line is use to add stroke
            // width to our progress bar.
            strokeWidth = Dp(value = 4F)
        )
    }
}

class UiConfigViewModel : ViewModel() {
    private val database = FirebaseFirestore.getInstance()

    // Create a mutable state for holding the UiConfig data
    private val _uiConfig = mutableStateOf(UiConfig())
    val uiConfig: State<UiConfig> get() = _uiConfig

    init {
        // Load data when the ViewModel is created
        loadDataFromFirestore()
    }

    private fun loadDataFromFirestore() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val querySnapshot = database.collection("uiconfig").get().await()
                val data = querySnapshot.documents.firstOrNull()?.toObject(UiConfig::class.java)
                data?.let {
                    _uiConfig.value = it
                }
            } catch (e: Exception) {
                // Handle any errors
                e.printStackTrace()
            }
        }
    }
}
