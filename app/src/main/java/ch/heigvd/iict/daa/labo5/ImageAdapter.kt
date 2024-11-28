package ch.heigvd.iict.daa.labo5

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL
import kotlin.collections.remove
import kotlin.collections.set
import kotlin.text.get

class ImageAdapter(private val items: List<Int>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    private val urlString = "https://daa.iict.ch/images/"
    private val jobs = mutableMapOf<Int, Job>()

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = items[position]
        holder.progressBar.visibility = View.VISIBLE
        holder.imageView.visibility = View.GONE

        val job = CoroutineScope(Dispatchers.Main).launch {
            val url = URL("$urlString${item + 1}.jpg")
            val bytes = downloadImage(url)
            val bmp = decodeImage(bytes)
            displayImage(holder, bmp)
        }
        jobs[position] = job
    }

    override fun onViewRecycled(holder: ImageViewHolder) {
        super.onViewRecycled(holder)
        val position = holder.adapterPosition
        jobs[position]?.cancel()
        jobs.remove(position)
    }

    override fun getItemCount(): Int = items.size

    private suspend fun downloadImage(url: URL): ByteArray? = withContext(Dispatchers.IO) {
        try {
            url.readBytes()
        } catch (e: IOException) {
            Log.w(TAG, "Exception while downloading image", e)
            null
        }
    }

    private suspend fun decodeImage(bytes: ByteArray?): Bitmap? = withContext(Dispatchers.Default) {
        try {
            BitmapFactory.decodeByteArray(bytes, 0, bytes?.size ?: 0)
        } catch (e: IOException) {
            Log.w(TAG, "Exception while decoding image", e)
            null
        }
    }

    private suspend fun displayImage(holder: ImageViewHolder, bmp: Bitmap?) = withContext(Dispatchers.Main) {
        holder.progressBar.visibility = View.GONE
        holder.imageView.visibility = View.VISIBLE
        if (bmp != null) {
            holder.imageView.setImageBitmap(bmp)
        } else {
            holder.imageView.setImageResource(R.drawable.error)
        }
    }

    fun clearCache() {
        //TODO clear all saved images, restoring to progress bar
        items.forEachIndexed { index, _ ->
            jobs[index]?.cancel()
            jobs.remove(index)
            notifyItemChanged(index)
        }
    }

    fun clearJobs() {
        jobs.forEach { it.value.cancel() }
        jobs.clear()
    }
}