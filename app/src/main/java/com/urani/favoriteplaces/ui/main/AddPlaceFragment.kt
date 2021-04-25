package com.urani.favoriteplaces.ui.main

import android.Manifest
import android.app.Activity
import android.content.*
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.urani.favoriteplaces.R
import com.urani.favoriteplaces.databinding.FragmentAddPlaceBinding
import com.urani.favoriteplaces.extension.visible
import com.urani.favoriteplaces.utils.BitmapUtil
import com.urani.favoriteplaces.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


@AndroidEntryPoint
class AddPlaceFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    private lateinit var binding: FragmentAddPlaceBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var mContext: Context
    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    companion object {
        private const val REQUEST_STORAGE_PERMISSION = 100
        private const val PICK_IMAGE_CAMERA = 1
    }

    private lateinit var googleMap: GoogleMap
    val zoomLevel = 13.0f
    private lateinit var mapFragment: SupportMapFragment

    private lateinit var values: ContentValues
    private var imageUri: Uri? = null
    private var imageBitmap: Bitmap? = null
    private lateinit var clipboard:ClipboardManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_place, container, false)
        binding.fragment = this

        clipboard = mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager


        return binding.root
    }

    fun onBackButtonClick(view: View?) {
        activity?.onBackPressed()
    }

    fun onEditPlaceButtonClick(view: View?) {

    }

    fun onTakePhotoClicked(view: View?) {
        if (Utils.checkCameraAndStoragePermissions(mContext)) {
            pickImage()
        } else
            requestPermissions(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), REQUEST_STORAGE_PERMISSION
            )
    }

    fun onPasteLinkClicked(view: View?) {
        clipboard.primaryClipDescription?.let { it->
            if (it.hasMimeType(MIMETYPE_TEXT_PLAIN)){
                val mediaURL = clipboard.primaryClip?.getItemAt(0)?.text.toString()

                if (URLUtil.isValidUrl(mediaURL)){
                    binding.buttonPasteLink.isEnabled = false
                    binding.linkTextView.visible()
                    binding.linkTextView.text = clipboard.primaryClip?.getItemAt(0)?.text

                    Glide.with(mContext)
                        .load(mediaURL)
                        .timeout(10000)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding.buttonPasteLink.isEnabled = true
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: com.bumptech.glide.load.DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {

                                binding.buttonPasteLink.isEnabled = true
                                imageBitmap = (resource as BitmapDrawable).bitmap
                                imageUri = null
                                return false
                            }
                        })
                        .into(binding.placeImage)
                }

            }
        }


//        binding.linkTextView.visible()
//        binding.linkTextView.text = clipboard.primaryClip?.getItemAt(0)?.text

    }

    fun onDeleteButtonClick(view: View?) {

    }

    override fun onMapReady(map: GoogleMap?) {
        binding.mapProgressBar.visibility = View.GONE
        map?.let {
            googleMap = map
            googleMap.setOnMapLongClickListener(this)
            googleMap.uiSettings.isZoomControlsEnabled = true

            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(42.6697152, 21.1440927),
                    zoomLevel
                )
            )


        }
    }


    override fun onMapLongClick(latLng: LatLng?) {
        googleMap.clear()
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.addMarker(MarkerOptions().position(latLng!!))
        zoomMapsToInitialState(latLng)
    }

    private fun zoomMapsToInitialState(latLng: LatLng) {
        googleMap.clear()
        googleMap.addMarker(MarkerOptions().position(latLng))
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng,
                zoomLevel
            )
        )
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mapFragment = (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!
        mapFragment.getMapAsync(this)
    }

    private fun pickImage() {
        try {

            val options = arrayOf<CharSequence>(
                getString(R.string.take_photo),
                getString(R.string.cancel)
            )
            val builder = androidx.appcompat.app.AlertDialog.Builder(mContext)
            builder.setTitle(getString(R.string.select_option))
            builder.setItems(options) { dialog, item ->
                when {

                    options[item] == getString(R.string.take_photo) -> {
                        dialog.dismiss()
                        values = ContentValues()
                        imageUri = mContext.contentResolver.insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            values
                        )!!
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                        startActivityForResult(intent, PICK_IMAGE_CAMERA)
                    }

                    options[item] == getString(R.string.cancel)-> dialog.dismiss()
                }
            }
            builder.show()

        } catch (e: Exception) {
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try {
            when (requestCode) {

                PICK_IMAGE_CAMERA -> {
                    if (resultCode == Activity.RESULT_OK) {
                        imageUri?.let {
                            loadImageFromCamera(it)
                            imageBitmap = null
                        }
                    }
                }
            }

        } catch (e: Exception) {
        }
    }

    private fun loadImageFromCamera(uri: Uri){
        Glide.with(mContext)
            .load(uri)
            .timeout(10000)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    val bitmap = (resource as BitmapDrawable).bitmap
                    return false
                }
            })
            .into(binding.placeImage)
    }

    private fun saveImage(imageUri: Uri?, imageBitmap: Bitmap?){
        val path: String = Environment.getExternalStorageDirectory().toString()
        val file = File(path, "UniqueFileName" + ".jpg")

        try {
            var stream: OutputStream? = null
            stream = FileOutputStream(file)

            if (imageBitmap != null){
                imageBitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    100,
                    stream
                )
            }else if (imageUri!= null){
                BitmapUtil.getBitmapFromURi(mContext, imageUri)?.compress(
                    Bitmap.CompressFormat.JPEG,
                    100,
                    stream
                )
            }else{
                return
            }

            stream.flush()
            stream.close()

        } catch (e: IOException) // Catch the exception
        {
            e.printStackTrace()
        }

        // Parse the saved image path to uri
        val savedImageURI = Uri.parse(file.absolutePath)

        // Display the saved image to ImageView

        // Display the saved image to ImageView
        binding.placeImage.setImageURI(savedImageURI)

        //todo save place image file.absolutePath in local db
    }


}