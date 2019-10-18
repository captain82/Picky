package com.captain.picky.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.marginLeft
import androidx.core.view.marginStart
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.captain.picky.R
import com.captain.picky.models.ImagesResponseModel
import com.captain.picky.viewModels.ImagesViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.single_image_layout.view.*
import androidx.recyclerview.widget.GridLayoutManager


class MainActivity : AppCompatActivity(),Callback.onBindviewHolderCallback {

    private var imageList: List<ImagesResponseModel> = arrayListOf()

    private var imageList1:MutableList<ImagesResponseModel> = mutableListOf()

    private val mAdapter by lazy { ImageRecyclerAdapter(this) }

    var index: Int? = null

    var clickPosition:Int?=null


    override fun onBindViewHolder(p0: ImageRecyclerAdapter.ViewHolder, position: Int) {
        val image = imageList1[position]

        Log.i("Visibility" , imageList1[position].visisbility.toString())

        Log.i("POsition",p0.adapterPosition.toString().plus("  ").plus(position.toString()))

            Log.i("Check" , imageList1[position].author.toString())
            if (position%3!=0 && position!=0) {
                p0.itemView.imageView.visibility = View.VISIBLE
                Picasso.get().load(image.download_url).resize(500,500).centerCrop()
                    .into(p0.itemView.imageView)
                p0.itemView.detailText.text = image.author
                p0.itemView.detailsCardView.visibility = View.GONE
                p0.itemView.triangle_marker.visibility = View.GONE

            }
            else
            {
                if(image.visisbility==false)
                {
                    //p0.itemView.visibility =View.GONE
                    p0.itemView.detailsCardView.visibility = View.GONE
                    p0.itemView.triangle_marker.visibility = View.GONE


                }
                else
                {
                    //p0.itemView.visibility=View.VISIBLE
                    p0.itemView.detailsCardView.visibility =View.VISIBLE
                    p0.itemView.triangle_marker.visibility = View.VISIBLE

                    clickPosition?.let {
                        if ((it+2)%3==0) {
                            p0.itemView.triangle_marker.translationX = -250F


                        } else if((it+1)%3==0)
                        {
                            p0.itemView.triangle_marker.translationX = 250F


                        }
                    }




                    // p0.itemView.detailText.visibility = View.VISIBLE

                }
                p0.itemView.detailText.text = image.author
                p0.itemView.imageView.visibility = View.GONE
                //p0.itemView.triangle_marker.visibility = View.GONE


            }


        p0.itemView.imageView.setOnClickListener {

            clickPosition=p0.adapterPosition



            var detail:String? = ""

            if ((position+2)%3==0) {
                detail = imageList1[position].author
                index = position + 2

            }
            else if((position+1)%3==0)
            {
                detail = imageList1[position].author
                index = position + 1
            }
            Log.i("Position clicked" , index.toString())

            index?.let{
                imageList1[it].author = detail
                if (!imageList1[it].visisbility)
                    imageList1[it].visisbility = true
                else if(imageList1[it].visisbility)
                    imageList1[it].visisbility = false



                /*if (p0.itemView.triangle_marker.visibility == View.VISIBLE) {

                    //p0.itemView.triangle_marker.visibility = View.GONE
                }else
                {
                    //p0.itemView.triangle_marker.visibility = View.VISIBLE

                }*/
                mAdapter.notifyItemChanged(it)

            }





/*
            if (!image.visisbility)
            {
                p0.itemView.detailText.visibility = View.GONE
            }
            else
            {
                p0.itemView.detailText.visibility = View.VISIBLE

            }*/

        }
    }

    private val viewModel by lazy {

        ViewModelProviders.of(this).get(ImagesViewModel::class.java)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        viewModel.getAllImages().observe(this, Observer {

            if (it != null && !it.isEmpty()) {
                imageList = it

                var count = 0
                imageList.forEachIndexed { index, imagesResponseModel ->

                    if(count%3==0 )
                    {
                        imageList1.add(ImagesResponseModel(index,0,"null","","","",false))
                        imageList1.add(imagesResponseModel)
                        count++
                    }else
                    {
                        imageList1.add(imagesResponseModel)
                    }
                    count++


                }
                imageList1.add(ImagesResponseModel(index,0,"null","","","",false))

                imageList1.forEach {
                    Log.i("list check" , it.author.toString())
                }

                mAdapter.showAllImages(imageList1)


            } else {
                imageList = arrayListOf()
                mAdapter.showAllImages(imageList)
                Toast.makeText(this,"Try again",Toast.LENGTH_LONG).show()

            }
        })
        val gridManager = GridLayoutManager(this, 2)
        gridManager.spanSizeLookup=object :GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {


                when (position % 3) {
                   0->return 2
                    else->return 1
                }

            }

        }

        recyclerView.layoutManager = gridManager

        recyclerView.adapter = mAdapter

        viewModel.getImagesFromNetwork()

    }
}
