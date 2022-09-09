package tv.badala.videoparticionapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import tv.badala.videoparticionapp.data.CategoryItem
import tv.badala.videoparticionapp.ui.theme.VideoparticionappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model:MainViewModel by viewModels()
        setContent {
            VideoparticionappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainNavigation(model = model)

                }
            }
        }
    }
}

@Composable
fun MainNavigation(model: MainViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "tag") {
        composable("main"){ MainScreen(navController = navController, model = model) }
        composable("tag"){ TagScreen(model = model,navController = navController) }
        composable("category"){ CategoryScreen(model = model,navController = navController)}
        composable("video"){ VideoScreen(model = model,navController = navController)}
    }

}

@Composable
fun BottomNavItem(model: MainViewModel,navController: NavController){
    val selectedItem=model.selectedItemBar.observeAsState(0).value
    val items = listOf("Etiquetas", "Categorias", "Exportar Video")
    val icons = listOf(Icons.Filled.Style, Icons.Filled.Category, Icons.Filled.VideoLibrary)
    val navItem = listOf("tag", "category", "video")
    BottomNavigation() {
        items.forEachIndexed { index, item ->
            BottomNavigationItem(
                icon = { Icon(icons[index], contentDescription = null) },
                label = { Text(item) },
                selected =selectedItem == index,
                onClick = {
                    model.selectedItemBar.postValue(index)
                    navController.navigate(navItem[index])
                })
        }

    }

}

@Composable
fun MainScreen(model: MainViewModel,navController: NavController){

    Column(modifier = Modifier)
    {
        Text(text = "Mis videos:")
    }

}

@Composable
fun TagScreen(model: MainViewModel,navController: NavController){
    val selectcategory=model.selectcategory.observeAsState().value!!
    val mainCategory=model.mainCategory.observeAsState().value!!
    var allOrOne by remember { mutableStateOf("Todas") }

    Scaffold(
        bottomBar = { BottomNavItem(model = model, navController = navController)}
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Text("Etiquetas", style = MaterialTheme.typography.h6)
            Text("Hora: ", style = MaterialTheme.typography.h6)
            Row(modifier = Modifier.height(500.dp)) {
                Button(modifier = Modifier.weight(1f),
                    border = if(allOrOne=="Todas"){BorderStroke(6.dp,Color.Black)}
                    else{BorderStroke(0.dp,Color.Black) },
                    onClick = { allOrOne="Todas" },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                ) {
                    Text(text = "Todas")
                }
                Button(modifier = Modifier.weight(1f),
                    border = if(allOrOne=="Una"){BorderStroke(6.dp,Color.Black)}
                    else{BorderStroke(0.dp,Color.Black) },
                    onClick = { allOrOne="Una" },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                ) {
                    Text(text = "Una")
                }
                selectcategory.forEachIndexed { index, categoryItem ->
                    Button(modifier = Modifier.weight(1f),
                        border = if(mainCategory==index){BorderStroke(6.dp,Color.Black)}
                        else{BorderStroke(0.dp,Color.Black) },
                        onClick = { model.mainCategory.postValue(index) },
                        colors = ButtonDefaults.buttonColors(backgroundColor = categoryItem.colorCategory)
                    ) {

                    }
                    
                }
            }
        }
    }
}

@Composable
fun VideoScreen(navController: NavController,model: MainViewModel){
    Scaffold(
        bottomBar = { BottomNavItem(model = model, navController = navController)}
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Text("Video:", style = MaterialTheme.typography.h6)

        }
    }

}

@Composable
fun CategoryScreen(navController: NavController,model: MainViewModel){
    Scaffold(
        bottomBar = { BottomNavItem(model = model, navController = navController)}
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Text("Categorias:", style = MaterialTheme.typography.h6)


        }
    }
}





@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    VideoparticionappTheme {
    }
}