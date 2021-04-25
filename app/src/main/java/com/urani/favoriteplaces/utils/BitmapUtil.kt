package com.urani.favoriteplaces.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.exifinterface.media.ExifInterface
import java.io.IOException


object BitmapUtil {
    private fun rotateBitmap(source: Bitmap, angle: Float): Bitmap? {
        return try {
            val matrix = Matrix()
            matrix.postRotate(angle)
            Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
        } catch (e: Exception) {
            null
        }
    }

    private fun modifyOrientation(context: Context, bitmap: Bitmap, imageUri: Uri): Bitmap? {
        try {
            val inStream = context.contentResolver.openInputStream(imageUri) ?: return bitmap
            val ei = ExifInterface(inStream)

            return when (ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)

                ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)

                ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)

                else -> bitmap
            }
        } catch (e: IOException) {
            return null
        }
    }

    fun getBitmapFromURi(mContext: Context, uri: Uri): Bitmap? {
        var thumbnail: Bitmap?
        try {
            thumbnail = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val source = ImageDecoder.createSource(mContext.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(mContext.contentResolver, uri)
            }

            thumbnail = modifyOrientation(mContext, thumbnail!!, uri)

            return thumbnail
        } catch (e: Exception) {

        }

        return null
    }
}

