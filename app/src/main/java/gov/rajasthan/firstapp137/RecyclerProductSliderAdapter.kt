package gov.rajasthan.firstapp137

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gov.rajasthan.firstapp137.databinding.AddProductSliderRowBinding

class RecyclerProductSliderAdapter(val context: Context, val arrSliderBitmaps: ArrayList<Bitmap>)
    : RecyclerView.Adapter<RecyclerProductSliderAdapter.ViewHolder>(){
    class ViewHolder(val binding: AddProductSliderRowBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AddProductSliderRowBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
       return arrSliderBitmaps.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.imgSliderProduct.setImageBitmap(arrSliderBitmaps[position])
    }


}