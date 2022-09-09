package tv.badala.videoparticionapp

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tv.badala.videoparticionapp.data.CategoryItem
import java.time.LocalDateTime

class MainViewModel: ViewModel() {
    val selectedItemBar=MutableLiveData(0)
    val mainCategory=MutableLiveData(0)
    val selectcategory=MutableLiveData(listOf(
        CategoryItem(colorCategory = Color.Yellow, descriptionCategory = ""),CategoryItem(colorCategory = Color.Blue, descriptionCategory = ""),
        CategoryItem(colorCategory = Color.Red, descriptionCategory = ""),CategoryItem(colorCategory = Color.Green, descriptionCategory = ""),
        CategoryItem(colorCategory = Color.Magenta, descriptionCategory = ""),CategoryItem(colorCategory = Color.Cyan, descriptionCategory = ""),
        CategoryItem(colorCategory = Color.LightGray, descriptionCategory = ""),CategoryItem(colorCategory = Color.DarkGray, descriptionCategory = ""),
    ))
    var houractual=MutableLiveData("00:00")

    fun StartClock(){
        viewModelScope.launch {
            while (5>4){
                delay(2000)
                val now= LocalDateTime.now()
                houractual.postValue(now.hour.toString()+":"+now.minute.toString()+":"+now.second.toString())

            }
        }
    }
}