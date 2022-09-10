package tv.badala.videoparticionapp.data

data class TagItem(
    val descriptionTag:String,
    val hourStart:String,
    val hourEnd:String,
    val category:CategoryItem,
    val end:Boolean
)
