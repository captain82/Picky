package com.captain.picky.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.captain.picky.models.ImagesResponseModel
import com.captain.picky.viewModels.ImagesViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.single_image_layout.view.*
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.single_image_layout.view.author


class MainActivity : AppCompatActivity(), Callback.onBindviewHolderCallback {

    private var imageList: List<ImagesResponseModel> = arrayListOf()

    private var imageList1: MutableList<ImagesResponseModel> = mutableListOf()

    private val mAdapter by lazy { ImageRecyclerAdapter(this) }

    var index: Int? = null

    var clickPosition: Int? = null


    override fun onBindViewHolder(p0: ImageRecyclerAdapter.ViewHolder, position: Int) {



        val image = imageList1[position]

        Log.i("Visibility", imageList1[position].visisbility.toString())

        Log.i("POsition", p0.adapterPosition.toString().plus("  ").plus(position.toString()))

        Log.i("Check", imageList1[position].author.toString())
        if (position % 3 != 0 && position != 0) {
            p0.itemView.imageView.visibility = View.VISIBLE
            Picasso.get().load(image.download_url).resize(350, 350).centerCrop()
                .into(p0.itemView.imageView)
            p0.itemView.author.text = image.author
            p0.itemView.download_url.text = "Image Url  :  ${image.download_url}"
            p0.itemView.url.text = "Website  :  ${image.url}"

            p0.itemView.detailsCardView.visibility = View.GONE
            p0.itemView.triangle_marker.visibility = View.GONE


        } else {
            if (image.visisbility == false) {
                //p0.itemView.visibility =View.GONE
                p0.itemView.detailsCardView.visibility = View.GONE
                p0.itemView.triangle_marker.visibility = View.GONE
                p0.itemView.setPadding(0,0,0,0)


            } else {
                p0.itemView.detailsCardView.visibility = View.VISIBLE
                p0.itemView.triangle_marker.visibility = View.VISIBLE

                clickPosition?.let {
                    if ((it + 2) % 3 == 0) {
                        p0.itemView.triangle_marker.translationX = -250F


                    } else if ((it + 1) % 3 == 0) {
                        p0.itemView.triangle_marker.translationX = 250F


                    }
                }


                // p0.itemView.detailText.visibility = View.VISIBLE

            }
            p0.itemView.author.text = image.author
            p0.itemView.imageView.visibility = View.GONE
            //p0.itemView.triangle_marker.visibility = View.GONE

        }


        p0.itemView.imageView.setOnClickListener {

            clickPosition = p0.adapterPosition


            var author: String? = ""
            var download_url:String? = ""
            var url:String? = ""


            if ((position + 2) % 3 == 0) {
                author = imageList1[position].author
                download_url = imageList1[position].download_url
                url = imageList1[position].url

                index = position + 2

            } else if ((position + 1) % 3 == 0) {
                author = imageList1[position].author
                download_url = imageList1[position].download_url
                url = imageList1[position].url

                index = position + 1
            }
            Log.i("Position clicked", index.toString())

            index?.let {
                imageList1[it].author = author
                imageList1[it].download_url = download_url
                imageList1[it].url = url

                if (!imageList1[it].visisbility)
                    imageList1[it].visisbility = true
                else if (imageList1[it].visisbility)
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
        setContentView(com.captain.picky.R.layout.activity_main)

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.statusBarColor = resources.getColor(com.captain.picky.R.color.white)

        viewModel.getAllImages().observe(this, Observer {

            if (it != null && !it.isEmpty()) {
                parentShimmerLayout.visibility = View.GONE
                parentShimmerLayout.stopShimmerAnimation()
                imageList = it

                var count = 0

                imageList.forEachIndexed { index, imagesResponseModel ->

                    if (count % 3 == 0) {
                        imageList1.add(ImagesResponseModel(index, 0, "null", "", "", "", false))
                        imageList1.add(imagesResponseModel)
                        count++
                    } else {
                        imageList1.add(imagesResponseModel)
                    }
                    count++


                }
                imageList1.add(ImagesResponseModel(index, 0, "null", "", "", "", false))

                imageList1.forEach {
                    Log.i("list check", it.author.toString())
                }

                mAdapter.showAllImages(imageList1)


            } else {
                imageList = arrayListOf()
                mAdapter.showAllImages(imageList)
                Toast.makeText(this, "Try again", Toast.LENGTH_LONG).show()

            }
        })
        val gridManager = GridLayoutManager(this, 2)
        val itemDecoration = ItemOffsetDecoration(this, com.captain.picky.R.dimen.padding5)
        recyclerView.addItemDecoration(itemDecoration)
        gridManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {


                when (position % 3) {
                    0 -> return 2
                    else -> return 1
                }

            }

        }


        recyclerView.layoutManager = gridManager
        //recyclerView.adapter = mAdapter
        //recyclerView.scheduleLayoutAnimation()

        //recyclerView.adapter = mAdapter

        /* val resId = R.anim.layout_animation_fall_down
         val animation = AnimationUtils.loadLayoutAnimation(this, resId)
         recyclerView.layoutAnimation = animation*/
        mAdapter.notifyDataSetChanged()
        //recyclerView.scheduleLayoutAnimation()
        recyclerView.adapter = mAdapter

        viewModel.getImagesFromNetwork()
        // recyclerView.scheduleLayoutAnimation()


    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()


    }

    override fun onResume() {
        super.onResume()
        parentShimmerLayout.startShimmerAnimation()


    }

    override fun onStop() {
        super.onStop()
        parentShimmerLayout.stopShimmerAnimation()
    }



}
