package com.stepashka.buildinglocator.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.stepashka.buildinglocator.DetailActivity
import com.stepashka.buildinglocator.MainActivity
import com.stepashka.buildinglocator.R
import com.stepashka.buildinglocator.models.PostedMaps
import com.stepashka.buildinglocator.models.User
import com.stepashka.buildinglocator.services.ServiceBuilder
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_view.view.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecyclerViewAdapter(private var postedMaps: MutableList<PostedMaps>?) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>()  {

    private var users: MutableList<User>? = null


    private var context: Context? = null
    var key : Long = 1


    companion object{
        var mapid: Long = 1
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postedMaps!!.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val currentMap = postedMaps?.get(position)



        //  if (!MainActivity.admins){
        //      holder.deleteButton.visibility = View.GONE
        //  }else if(MainActivity.admins){
        //      holder.deleteButton.visibility =View.VISIBLE
        //  }


        //     val currentMapAuthor = users?.get(position)

        // If the url link is longer than 10, then get the image from the url. Else use a default image.
        val bannerImageSfx = currentMap?.map.toString()
        if ((currentMap?.map.toString().endsWith("jpeg")) ||
            (currentMap?.map.toString().endsWith("jpg")) ||
            (currentMap?.map.toString().endsWith("png")) ||
            (currentMap?.map.toString().contains("auto"))
        ) {
            Picasso.get().load(currentMap?.map).into(holder.mapImage)
        }

        val eventPictireSfx = currentMap?.map.toString()
        val uri: Uri = Uri.parse(eventPictireSfx)
        Glide.with(context).load(uri).into(holder.mapImage)
        //if ((currentMap?.event_image.toString().endsWith("jpeg")) ||
        //    (currentMap?.event_image.toString().endsWith("png")) ||
        //    (currentMap?.event_image.toString().endsWith("jpg")) ||
        //    (currentMap?.event_image.toString().contains("auto"))
        //) {
        //    Picasso.get().load(currentMap?.event_image).into(holder.bannerImage)
        //}
        //holder.username?.text = currentMapAuthor?.username

        holder.title?.text = currentMap?.title
        holder.address?.text = currentMap?.address
        // holder.eventDate?.text = currentMap?.created_at.toString()
        val myString = currentMap?.created_at.toString()
        val ms = "" +myString[0] + myString[1] + myString[2] + myString[3] + "/" + myString[4] + myString[5] + "/" + myString[6] + "0"
        holder.date?.text = ms
        holder.username?.text = currentMap?.user?.username.toString()
        holder.city?.text = currentMap?.city.toString()
        holder.id?.text = currentMap?.id.toString()
        MainActivity.MAPID = currentMap?.id.toString().toLong()
        //mapid = currentMap?.id.toString().toLong()
        DetailActivity.IMAGEME = currentMap?.map.toString()

        key = currentMap?.id.toString().toLong()
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra("key", currentMap?.id.toString().toLong())
        intent.putExtra("image", currentMap?.map.toString())

        holder.mapImage?.setOnClickListener {
            mapid = currentMap?.id.toString().toLong()
            intent.putExtra("key", currentMap?.id.toString().toLong())
            context?.startActivity(intent)

        }

        holder.state?.text = currentMap?.state.toString()
        holder.zip?.text = currentMap?.zip.toString()
        holder.comments?.text = currentMap?.comments.toString()
        val profilePic = currentMap?.user?.profilepicture.toString()
        if ((currentMap?.user?.profilepicture.toString().endsWith("jpeg")) ||
            (currentMap?.user?.profilepicture.toString().endsWith("jpg")) ||
            (currentMap?.user?.profilepicture.toString().endsWith("png")) ||
            (currentMap?.user?.profilepicture.toString().contains("auto"))
        ) {
            Picasso.get().load(currentMap?.user?.profilepicture).into(holder.profilepicture)
        }

//        holder.deleteButton.setOnClickListener {
//            val builder = AlertDialog.Builder(context)
//            builder.setTitle("Delete Confirmation")
//            builder.setMessage("Are you sure you want to delete this property?")
//            builder.setPositiveButton("YES") { dialogInterface, _ ->
//                holder.cardViewDeleteOnLongPress(position)
//                if (currentMap != null) {
//                    deleteMap(currentMap.eventid!!)
//                }
//                Toast.makeText(
//                    context,
//                    "Property has been successfully deleted",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//            builder.setNegativeButton("NO"){ dialogInterface, _ ->
//                dialogInterface.dismiss()
//            }
//            builder.show()
//        }




    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id: TextView? = itemView.textview_id
        val mapImage: ImageView? = itemView.imageView_map
        val title: TextView? = itemView.textview_title
        val address: TextView? = itemView.textview_address
        val city: TextView? = itemView.textview_city
        val state: TextView? = itemView.textview_state
        val zip: TextView? = itemView.textview_zip
        val comments: TextView? = itemView.textview_comments
        val profilepicture : CircleImageView? = itemView.textview_profilepicture
        val username: TextView? = itemView.textview_username
        val date: TextView? = itemView.textview_posteddate



        fun cardViewDeleteOnLongPress(itemPosition: Int) {
            postedMaps?.removeAt(itemPosition)
            updateMap(postedMaps)
        }



    }


    fun updateMap(newList: MutableList<PostedMaps>?) {
        postedMaps = newList
        notifyDataSetChanged()
    }


    fun deleteMap(id: Long){
        val call: Call<Void> = ServiceBuilder.create().deleteMap(id)
        call.enqueue(object: Callback<Void> {
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.i("Add Property", "OnFailure ${t.message}")
            }
            //
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){


//
                }
                else{
                }
            }
//
        })
    }

    fun getMapById(){
        val call: Call<PostedMaps> = ServiceBuilder.create().getById(MainActivity.MAPID)

        call.enqueue(object: Callback<PostedMaps> {
            override fun onFailure(call: Call<PostedMaps>, t: Throwable) {
            }


            override fun onResponse(call: Call<PostedMaps>, response: Response<PostedMaps>) {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("key", MainActivity.MAPID )
                context?.startActivity(intent)
            }

        })


    }




}