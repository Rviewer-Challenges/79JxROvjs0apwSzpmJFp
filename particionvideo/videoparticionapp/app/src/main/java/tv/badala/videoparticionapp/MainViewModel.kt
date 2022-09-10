package tv.badala.videoparticionapp

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tv.badala.videoparticionapp.data.CategoryItem
import tv.badala.videoparticionapp.data.Diapo
import tv.badala.videoparticionapp.data.TagItem
import java.time.LocalDateTime

class MainViewModel: ViewModel() {
    val listdiapo=MutableLiveData(mutableListOf(Diapo("Algunas veces no tienes tiempo para ver todo el stremer o toda  tu clase. Pero quieres enterarte. ¿Y si alguien que tenga el tiempo asiste y toma notas? y de esta forma ir a las partes mas importantes al ver el video o repetición ",R.drawable.maure),
        Diapo(texto = "Pues esta app ayuda a tomar esas notas y despues crea un json con lo que puedes hacer lo que quieras desde un resumen hasta como entrada a otra API",imagen=R.drawable.appmm),
        Diapo(texto = "Darle Empezo stream para iniciar, te creara una etiqueta por defecto, los colores representan una categoria, dale todos para mostrar todas las etiquetas o una para solo las de un color", imagen = R.drawable.inicia),
        Diapo(texto = "Para crear otra etiqueta presiona otra etiqueta,para editar haz click en el comienzo de la etiqueta,para finalizar el conteo dale a la manito", imagen = R.drawable.etiqu),
        Diapo(texto = "Agrega nombre que quieras a tus etiquetas", imagen = R.drawable.catapp),
        Diapo(texto = "crea Json y extiende las posibilidades", imagen = R.drawable.jsontag)

        ))
    val numdi=MutableLiveData(0)
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
    val link=MutableLiveData("")
    val json=MutableLiveData("")

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

    fun updateTag(tagItem: TagItem){
        viewModelScope.launch {
            for (lt in listTagComplete.indices) {
                if (tagItem.hourStart==listTagComplete[lt].hourStart){
                    listTagComplete[lt]=tagItem
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

    fun updateAndStop(tagItem: TagItem){
        viewModelScope.launch {
            for (lt in listTagComplete.indices) {
                if (tagItem.hourStart==listTagComplete[lt].hourStart){
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
            val lTe = mutableListOf<TagItem>()
            for (e in listTagComplete){
                for (iy in selectcategory.value!!.indices){
                    if (e.category.colorCategory ==selectcategory.value!![iy].colorCategory) {
                        lTe.add(TagItem(descriptionTag = e.descriptionTag, hourStart = e.hourStart,e.hourEnd,selectcategory.value!![iy],e.end))
                    }
                }
            }
            listTagComplete=lTe
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

    fun createJson(){
        var resumeTag="{\"link\":\""+link.value!!+
                ",\"hora_actual\":"+hourStartVideo.value!!+
                ",\"Etiquetas:\":["
        for (ta in listTagComplete){
            var textTemporal=""
            textTemporal="{\"descripcion\":"+ta.descriptionTag+
                    ",\"hora_start\":"+ta.hourStart+
                    ",\"hora_end\":"+ta.hourEnd+
                    ",\"Categoria\":"+ta.category.descriptionCategory+
                    "},"
            resumeTag+=textTemporal
        }
        resumeTag+="]}"
        json.postValue(resumeTag)
    }



    fun sethourstart(){
        val now= LocalDateTime.now()
        hourStartVideo.postValue(now.hour.toString()+":"+now.minute.toString()+":"+now.second.toString())
    }

}