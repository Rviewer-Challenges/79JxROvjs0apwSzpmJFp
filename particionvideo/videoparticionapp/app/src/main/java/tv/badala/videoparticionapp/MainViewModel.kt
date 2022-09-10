package tv.badala.videoparticionapp

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tv.badala.videoparticionapp.data.CategoryItem
import tv.badala.videoparticionapp.data.TagItem
import java.time.LocalDateTime

class MainViewModel: ViewModel() {
    val selectedItemBar=MutableLiveData(0)
    val mainCategory=MutableLiveData(0)
    val lastChage=MutableLiveData("")
    val selectcategory=MutableLiveData(listOf(
        CategoryItem(colorCategory = Color.Yellow, descriptionCategory = ""),CategoryItem(colorCategory = Color.Blue, descriptionCategory = ""),
        CategoryItem(colorCategory = Color.Red, descriptionCategory = ""),CategoryItem(colorCategory = Color.Green, descriptionCategory = ""),
        CategoryItem(colorCategory = Color.Magenta, descriptionCategory = ""),CategoryItem(colorCategory = Color.Cyan, descriptionCategory = ""),
        CategoryItem(colorCategory = Color.LightGray, descriptionCategory = ""),CategoryItem(colorCategory = Color.DarkGray, descriptionCategory = ""),
    ))
    val isCurrent=MutableLiveData(false)
    val houractual=MutableLiveData("00:00")
    val hourStartVideo=MutableLiveData("00:00")
    val listTagItem=MutableLiveData(mutableListOf<TagItem>())
    var listTagComplete=mutableListOf<TagItem>()
    val allorOne=MutableLiveData("Todas")
    val mainTagItem=MutableLiveData(TagItem(descriptionTag = "", hourStart = "-1", hourEnd = "", category = CategoryItem(Color.Transparent,""),false))

    init {
        startClock()
    }

    fun startClock(){
        viewModelScope.launch {
            while (5>4){
                delay(2000)
                val now= LocalDateTime.now()
                houractual.postValue(now.hour.toString()+":"+now.minute.toString()+":"+now.second.toString())

            }
        }
    }

    fun reloadCategorys(index:Int,string: String){
        val sC=selectcategory.value!!
        val mS= mutableListOf<CategoryItem>()
        for (i in sC.indices){
            if(i==index){
                mS.add(CategoryItem(sC[i].colorCategory,string))
            }else{
                mS.add(sC[i])
            }
        }
        selectcategory.postValue(mS)
    }

    fun stopTag(tagItem: TagItem){
        viewModelScope.launch {
            for (lt in listTagComplete.indices) {
                if (tagItem==listTagComplete[lt]){
                    listTagComplete[lt]=TagItem(tagItem.descriptionTag,tagItem.hourStart,gethourstart(),tagItem.category,end = true)
                }
            }
            if (allorOne.value=="Todas"){
                filterByColor(Color.Transparent)
            }else{
                filterByColor(selectcategory.value!![mainCategory.value!!].colorCategory)
            }
            lastChage.postValue(houractual.value!!)

        }

    }

    fun filterByColor(color: Color){
        viewModelScope.launch {
            if (color==Color.Transparent) {
                listTagItem.postValue(listTagComplete)
            }else{
                val lTi = mutableListOf<TagItem>()
                for (lt in listTagComplete) {
                    if (lt.category.colorCategory == color) {
                        lTi.add(lt)
                    }
                }
                listTagItem.postValue(lTi)
            }
            lastChage.postValue(houractual.value!!)
        }
    }

    fun createNewTag(){
        viewModelScope.launch {
            val sC=selectcategory.value!!
            val newTag=TagItem(descriptionTag = "sin descripcion", hourStart = gethourstart(), hourEnd = houractual.value!!, category = sC[mainCategory.value!!],end = false)
            val mutaTag=listTagComplete
            mutaTag.add(newTag)
            listTagComplete=mutaTag
            if (allorOne.value=="Todas"){
                filterByColor(Color.Transparent)
            }else{
                filterByColor(selectcategory.value!![mainCategory.value!!].colorCategory)
            }
            lastChage.postValue(houractual.value!!)
        }

    }

    fun gethourstart():String{
        val now= LocalDateTime.now()
        val hourS=now.hour.toString()+":"+now.minute.toString()+":"+now.second.toString()
        return hourS
    }



    fun sethourstart(){
        val now= LocalDateTime.now()
        hourStartVideo.postValue(now.hour.toString()+":"+now.minute.toString()+":"+now.second.toString())
    }

}