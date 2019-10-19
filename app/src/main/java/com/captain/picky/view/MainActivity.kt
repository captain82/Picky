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

    private var mutableImageList: MutableList<ImagesResponseModel> = mutableListOf()

    private val mAdapter by lazy { ImageRecyclerAdapter(this) }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ImagesViewModel::class.java)
    }

    var index: Int? = null

    var clickPosition: Int? = null

    var author: String? = ""

    var download_url: String? = ""

    var url: String? = ""




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.captain.picky.R.layout.activity_main)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = resources.getColor(com.captain.picky.R.color.white)

        viewModel.getAllImages().observe(this, Observer {

            if (it != null && it.isNotEmpty()) {

                parentShimmerLayout.visibility = View.GONE
                parentShimmerLayout.stopShimmerAnimation()

                //Adding a dummy ImageResponseModel with visibility false at every 3rd position

                var count = 0

                it.forEachIndexed { index, imagesResponseModel ->

                    if (count % 3 == 0) {
                        mutableImageList.add(ImagesResponseModel(index, 0, "null", "", "", "", false))
                        mutableImageList.add(imagesResponseModel)
                        count++
                    } else {
                        mutableImageList.add(imagesResponseModel)
                    }
                    count++
                }

                mutableImageList.add(ImagesResponseModel(index, 0, "null", "", "", "", false))


            } else {
                mutableImageList = arrayListOf()
                Toast.makeText(this, "Network Error", Toast.LENGTH_LONG).show()

            }

            mAdapter.showAllImages(mutableImageList)

        })


        val gridManager = GridLayoutManager(this, 2)

        //Setting equal padding for grid layout
        val itemDecoration = ItemOffsetDecoration(this, com.captain.picky.R.dimen.padding5)
        recyclerView.addItemDecoration(itemDecoration)

        //Setting the column length of every 3rd element to 2
        gridManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {

            override fun getSpanSize(position: Int): Int {
                when (position % 3) {
                    0 -> return 2
                    else -> return 1
                }

            }

        }


        recyclerView.layoutManager = gridManager
        mAdapter.notifyDataSetChanged()
        recyclerView.adapter = mAdapter
        viewModel.getImagesFromNetwork()


    }


    override fun onResume() {
        super.onResume()
        parentShimmerLayout.startShimmerAnimation()


    }

    override fun onStop() {
        super.onStop()
        parentShimmerLayout.stopShimmerAnimation()
    }

    override fun onBindViewHolder(p0: ImageRecyclerAdapter.ViewHolder, position: Int) {


        val image = mutableImageList[position]

        //If the position is multiple of 3 we make their visibility GONE otherwise VISIBLE

        if (position % 3 != 0 && position != 0) {
            p0.itemView.imageView.visibility = View.VISIBLE
            Picasso.get().load(image.download_url).resize(450, 450).centerCrop()
                .into(p0.itemView.imageView)
            p0.itemView.author.text = image.author
            p0.itemView.download_url.text = "Image Url  :  ${image.download_url}"
            p0.itemView.url.text = "Website  :  ${image.url}"
            p0.itemView.detailsCardView.visibility = View.GONE
            p0.itemView.triangle_marker.visibility = View.GONE


        } else {
            if (!image.visisbility) {

                p0.itemView.detailsCardView.visibility = View.GONE
                p0.itemView.triangle_marker.visibility = View.GONE

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

            }
            p0.itemView.author.text = image.author
            p0.itemView.download_url.text = image.download_url
            p0.itemView.url.text = image.url
            p0.itemView.imageView.visibility = View.GONE

        }


        p0.itemView.imageView.setOnClickListener {

            clickPosition = p0.adapterPosition

            //Detecting if the left or right element is clicked

            if ((position + 2) % 3 == 0) {
                author = image.author
                download_url = image.download_url
                url = image.url
                index = position + 2

            } else if ((position + 1) % 3 == 0) {
                author = image.author
                download_url = image.download_url
                url = image.url
                index = position + 1
            }

            //changing the visibility all other tootips except the one clicked to false
            index?.let {
                mutableImageList.forEachIndexed { index, imagesResponseModel ->
                    if (index != it && imagesResponseModel.visisbility) {
                        imagesResponseModel.visisbility = false
                        mAdapter.notifyItemChanged(index)
                    }
                }
                mutableImageList[it].author = author
                mutableImageList[it].download_url = download_url
                mutableImageList[it].url = url

                if (!mutableImageList[it].visisbility)
                    mutableImageList[it].visisbility = true
                else if (mutableImageList[it].visisbility)
                    mutableImageList[it].visisbility = false

                mAdapter.notifyItemChanged(it)

            }
        }
    }


}
