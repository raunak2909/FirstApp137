package gov.rajasthan.firstapp137

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gov.rajasthan.firstapp137.databinding.NoteRowBinding

class RecyclerNotesAdapter(val context: android.content.Context, val arrNotes: ArrayList<NoteModel>)
    : RecyclerView.Adapter<RecyclerNotesAdapter.ViewHolder>(){
    class ViewHolder(val binding: NoteRowBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(NoteRowBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
       return arrNotes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.txtTitle.text = arrNotes[position].title
        holder.binding.txtDesc.text = arrNotes[position].desc
    }


}