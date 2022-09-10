package tv.badala.videoparticionapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import tv.badala.videoparticionapp.data.CategoryItem
import tv.badala.videoparticionapp.data.TagItem
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
    NavHost(navController = navController, startDestination = "main") {
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
    val diapolist=model.listdiapo.observeAsState().value!!
    val numd=model.numdi.observeAsState().value!!
    Image(
        painter = painterResource(id = R.drawable.fondo),
        contentDescription = "",
        modifier = Modifier
            .fillMaxSize(),
        contentScale = ContentScale.FillBounds
    )
    Column(modifier = Modifier)
    {
        Row() {
            IconButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if (numd>0){
                        model.numdi.postValue(numd-1)
                    }
            }) {
                Icon(Icons.Filled.ChevronLeft, contentDescription = "Localized description")
            }
            IconButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(numd==diapolist.size-1){
                        navController.navigate("tag") }
                    else{
                        model.numdi.postValue(numd+1)
                    }
            }) {
                Icon(Icons.Filled.ChevronRight, contentDescription = "Localized description")
            }
            TextButton(modifier = Modifier.weight(1f),
                onClick = { navController.navigate("tag") }) {
                Text("omitir", style = MaterialTheme.typography.h5)
            }
        }
        Text(text = diapolist[numd].texto, style = MaterialTheme.typography.h5)
        Column(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)) {
            Image(
                painter = painterResource(id = diapolist[numd].imagen),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
                contentScale = ContentScale.Fit
            )

        }
    }

}

@Composable
fun TagScreen(model: MainViewModel,navController: NavController){
    val lastChage=model.lastChage.observeAsState().value!!
    val selectcategory=model.selectcategory.observeAsState().value!!
    val mainCategory=model.mainCategory.observeAsState().value!!
    val isCurrent=model.isCurrent.observeAsState().value!!
    val allOrOne=model.allorOne.observeAsState().value!!
    val hourStart=model.hourStartVideo.observeAsState().value!!
    val listTagItem=model.listTagItem.observeAsState().value!!
    val scrollState= rememberScrollState()
    val mainTag=model.mainTagItem.observeAsState().value!!

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
            Text("Etiquetas", style = MaterialTheme.typography.h5)
            if(isCurrent){
                Text("Inicio: $hourStart",style = MaterialTheme.typography.h5)
                
            }else{
                Button(onClick = { model.isCurrent.postValue(true)
                    model.createNewTag()
                    model.sethourstart()
                }) {
                    Text(text = "Empezo el stream")

                }
            }
            Text("Utimo cambio: $lastChage", style = MaterialTheme.typography.h5)
            Row(modifier = Modifier) {
                Button(modifier = Modifier.weight(1f),
                    border = if(allOrOne=="Todas"){BorderStroke(6.dp,Color.Black)}
                    else{BorderStroke(0.dp,Color.Black) },
                    onClick = { model.allorOne.postValue("Todas")
                        model.filterByColor(Color.Transparent)
                              },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                ) {
                    Text(text = "Todas")
                }
                Button(modifier = Modifier.weight(1f),
                    border = if(allOrOne=="Una"){BorderStroke(6.dp,Color.Black)}
                    else{BorderStroke(0.dp,Color.Black) },
                    onClick = { model.allorOne.postValue("Una")
                        model.filterByColor(selectcategory[mainCategory].colorCategory)
                              },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                ) {
                    Text(text = "Una")
                }
                selectcategory.forEachIndexed { index, categoryItem ->
                    Button(modifier = Modifier.weight(1f),
                        border = if(mainCategory==index){BorderStroke(6.dp,Color.Black) }
                        else{BorderStroke(0.dp,Color.Black) },
                        onClick = { model.mainCategory.postValue(index)
                            if(allOrOne=="Una"){
                                model.filterByColor(selectcategory[index].colorCategory)
                            } },
                        colors = ButtonDefaults.buttonColors(backgroundColor = categoryItem.colorCategory)
                    ) {

                    }
                }
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(it)
                    .verticalScroll(scrollState)
                    .fillMaxSize()
            ) {if(isCurrent) {
                Button(onClick = { model.createNewTag() }) {
                    Text(text = "Otra etiqueta")
                }
                listTagItem.forEachIndexed { _, tagItem ->
                    MyTag(tagItem = tagItem, model = model)
                }
            }
            }
        }
    }
    if (mainTag.hourStart!="-1") {
        DialogEdit(model = model)
    }
}

@Composable
fun MyTag(tagItem: TagItem,model: MainViewModel){
    val horaactual=model.houractual.observeAsState().value!!

    Card (modifier = Modifier
        .padding(15.dp)
        .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ){
        Row(modifier = Modifier.height(100.dp)) {
            Card(modifier= Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable { model.mainTagItem.postValue(tagItem) }) {
                    Text(modifier= Modifier.weight(1f),text = tagItem.descriptionTag)
            }
            Column(
                modifier= Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Button(
                    modifier= Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = tagItem.category.colorCategory),
                    onClick = { /*TODO*/ }) {
                    Text(text = "C")
                }
            }
            Column(
                modifier= Modifier.padding(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(modifier = Modifier.weight(1f), text = "Inicio:")
                Text(modifier = Modifier.weight(1f), text = tagItem.hourStart)
            }
            if (!tagItem.end) {
                IconButton(onClick = {
                    model.stopTag(tagItem)
                }) {
                    Icon(Icons.Filled.PanTool, contentDescription = "Localized description")
                }
            }
            Column(
                modifier= Modifier.padding(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(modifier = Modifier.weight(1f), text = "Final:")
                if (tagItem.end){
                    Text(modifier = Modifier.weight(1f), text = tagItem.hourEnd)
                }else{
                    Text(modifier = Modifier.weight(1f), text = horaactual)
                }
            }
            IconButton(onClick = {

            }) {
                Icon(Icons.Filled.Clear, contentDescription = "Localized description")
            }
        }
    }
}

@Composable
fun VideoScreen(navController: NavController,model: MainViewModel){
    val link=model.link.observeAsState().value!!
    val json=model.json.observeAsState().value!!
    val scrollState= rememberScrollState()
    Scaffold(
        bottomBar = { BottomNavItem(model = model, navController = navController)}
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Text("Json:", style = MaterialTheme.typography.h6)
            OutlinedTextField(
                value = link,
                onValueChange = { text ->
                    model.link.postValue(text)
                },
                label = { Text(text = "Descripcion:") },
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            )
            Button(onClick = { model.createJson() }) {
                Text(text = "actualiza el json" )
            }
            Text(text = json)

        }
    }

}

@Composable
fun CategoryScreen(navController: NavController,model: MainViewModel){
    val selectcategory=model.selectcategory.observeAsState().value!!
    val scrollState= rememberScrollState()
    Scaffold(
        bottomBar = { BottomNavItem(model = model, navController = navController)}
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(it)
                .verticalScroll(scrollState)
                .fillMaxSize()
        ) {
            Text("Categorias:", style = MaterialTheme.typography.h6)
            selectcategory.forEachIndexed { index, categoryItem ->
                Card(modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(categoryItem.colorCategory)

                    ) {
                        OutlinedTextField(
                            value = categoryItem.descriptionCategory,
                            onValueChange = { text ->
                                model.reloadCategorys(index, text)
                            },
                            label = { Text(text = "Descripcion:") },
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DialogEdit(model: MainViewModel){
    val selectcategory=model.selectcategory.observeAsState().value!!
    val mainCategory=model.mainCategory.observeAsState().value!!
    val mainTag=model.mainTagItem.observeAsState().value!!
    val horaactual=model.houractual.observeAsState().value!!
    Dialog(onDismissRequest = {
        model.updateTag(mainTag)
        model.mainTagItem.postValue(TagItem(descriptionTag = "", hourStart = "-1", hourEnd = "", category = CategoryItem(Color.Transparent,""),false)) }
    ) {
        Card(shape = RoundedCornerShape(20.dp)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()


            ) {
                OutlinedTextField(
                    value = mainTag.descriptionTag,
                    onValueChange = { text ->
                        model.mainTagItem.postValue(
                            TagItem(
                                text,
                                mainTag.hourStart,
                                mainTag.hourEnd,
                                mainTag.category,
                                mainTag.end
                            )
                        )
                    },
                    label = { Text(text = "Descripción etiqueta") },
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                )
                Row() {
                    selectcategory.forEachIndexed { index, categoryItem ->
                        Button(
                            modifier = Modifier.weight(1f),
                            border = if (mainCategory == index) {
                                BorderStroke(6.dp, Color.Black)
                            } else {
                                BorderStroke(0.dp, Color.Black)
                            },
                            onClick = {
                                model.mainCategory.postValue(index)
                                model.mainTagItem.postValue(
                                TagItem(
                                    mainTag.descriptionTag,
                                    mainTag.hourStart,
                                    mainTag.hourEnd,
                                    categoryItem,
                                    mainTag.end
                                )
                            ) },
                            colors = ButtonDefaults.buttonColors(backgroundColor = categoryItem.colorCategory)
                        ) {

                        }
                    }
                }
                Text(text = "Hora inicio:"+mainTag.hourStart)
                Row() {
                    Text(text = "Hora Final: ")
                    if (mainTag.end) {
                        Text( text = mainTag.hourEnd)
                    } else {
                        Text( text = horaactual)
                        IconButton(onClick = {
                            model.updateAndStop(mainTag)
                            model.mainTagItem.postValue(TagItem(descriptionTag = "", hourStart = "-1", hourEnd = "", category = CategoryItem(Color.Transparent,""),false))
                    }) {
                            Icon(Icons.Filled.PanTool, contentDescription = "Localized description")
                        }
                    }
                }

            }
        }
        
    }
    
}





@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    VideoparticionappTheme {
    }
}