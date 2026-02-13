import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabularyproject.database.models.WordTranslationModel
import com.example.vocabularyproject.ui.widgetstyles.WordListCard

// 2. The Adapter (Hybrid)
class DaftarKataAdapter(
    private val onEWordClick: EWordListener,
    private val onEDefinitionClick: () -> Unit,
    private val onIWordsClick: () -> Unit,
    private val onDeleteClick: () -> Unit
) : ListAdapter<WordTranslationModel, DaftarKataAdapter.MyViewHolder>(DaftarKataDiffCallback()) {

    class MyViewHolder(val composeView: ComposeView) :
        RecyclerView.ViewHolder(composeView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ComposeView(parent.context).apply {
                setViewCompositionStrategy(
                    ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
                )
            }
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position) // 🔥 THIS IS THE FIX
        holder.composeView.setContent {
            WordListCard(
                item,
                position,
                onEWordClick,
                {},
                {},
                {}
            )
        }
    }
}

class DaftarKataDiffCallback : DiffUtil.ItemCallback<WordTranslationModel>() {
    override fun areItemsTheSame(oldItem: WordTranslationModel, newItem: WordTranslationModel): Boolean {
        return oldItem.english.eId == newItem.english.eId
    }

    override fun areContentsTheSame(oldItem: WordTranslationModel, newItem: WordTranslationModel): Boolean {
        return oldItem == newItem
    }
}
class EWordListener(val plusListener:(wtModel: WordTranslationModel)->Unit){
    fun onPlusButtonClick(wtModel: WordTranslationModel)= plusListener(wtModel)
}

