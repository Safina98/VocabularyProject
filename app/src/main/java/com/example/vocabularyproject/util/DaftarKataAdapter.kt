
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vocabularyproject.ui.widgetstyles.WordListCard

// 2. The Adapter (Hybrid)
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabularyproject.database.models.WordTranslationModel
import com.example.vocabularyproject.ui.widgetstyles.WordListCard

class DaftarKataAdapter(
    private val onEWordClick: (WordTranslationModel) -> Unit,
    private val onEDefinitionClick: (WordTranslationModel) -> Unit,
    private val onIWordsClick: (WordTranslationModel) -> Unit,
    private val onDeleteClick: (WordTranslationModel) -> Unit
) : ListAdapter<WordTranslationModel, DaftarKataAdapter.MyViewHolder>(DaftarKataDiffCallback()) {

    class MyViewHolder(val composeView: ComposeView) : RecyclerView.ViewHolder(composeView)

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
        val item = getItem(position)
        holder.composeView.setContent {
            WordListCard(
                wordTransa = item,
                index = position,
                onEWordClick = EWordListener { onEWordClick(it) },
                onEDefinitionClick = { onEDefinitionClick(item) },
                onIWordsClick = { onIWordsClick(item) },
                onDeleteClick = { onDeleteClick(item) }
            )
        }
    }
}

class DaftarKataDiffCallback : DiffUtil.ItemCallback<WordTranslationModel>() {
    override fun areItemsTheSame(old: WordTranslationModel, new: WordTranslationModel) =
        old.english.eId == new.english.eId

    override fun areContentsTheSame(old: WordTranslationModel, new: WordTranslationModel) =
        old == new
}
class EWordListener(val plusListener:(wtModel: WordTranslationModel)->Unit){
    fun onPlusButtonClick(wtModel: WordTranslationModel)= plusListener(wtModel)
}

