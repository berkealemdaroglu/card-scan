package com.getbouncer.scan.payment

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ConfigurationInfo
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.util.Size
import androidx.annotation.CheckResult
import com.getbouncer.scan.framework.Config
import com.getbouncer.scan.framework.image.crop
import com.getbouncer.scan.framework.image.size
import com.getbouncer.scan.framework.util.centerOn
import com.getbouncer.scan.framework.util.intersectionWith
import com.getbouncer.scan.framework.util.maxAspectRatioInSize
import com.getbouncer.scan.framework.util.projectRegionOfInterest
import com.getbouncer.scan.framework.util.toRect

/**
 * Get a rect indicating what part of the preview is actually visible on screen. This assumes that the preview
 * is the same size or larger than the screen in both dimensions.
 */
private fun getVisiblePreview(previewBounds: Rect) = Size(
    previewBounds.right + previewBounds.left,
    previewBounds.bottom + previewBounds.top,
)

/**
 * Crop the preview image from the camera based on the view finder's position in the preview bounds.
 *
 * Note: This algorithm makes some assumptions:
 * 1. the previewBounds and the cameraPreviewImage are centered relative to each other.
 * 2. the previewBounds circumscribes the cameraPreviewImage. I.E. they share at least one field of view, and the
 *    cameraPreviewImage's fields of view are smaller than or the same size as the previewBounds's
 * 3. the previewBounds and the cameraPreviewImage have the same orientation
 */
@Deprecated(
    message = "Replaced by stripe card scan. See https://github.com/stripe/stripe-android/tree/master/stripecardscan",
    replaceWith = ReplaceWith("StripeCardScan"),
)
fun cropCameraPreviewToViewFinder(
    cameraPreviewImage: Bitmap,
    previewBounds: Rect,
    viewFinder: Rect,
): Bitmap {
    require(
        viewFinder.left >= previewBounds.left &&
            viewFinder.right <= previewBounds.right &&
            viewFinder.top >= previewBounds.top &&
            viewFinder.bottom <= previewBounds.bottom
    ) { "View finder $viewFinder is outside preview image bounds $previewBounds" }

    // Scale the cardFinder to match the full image
    val projectedViewFinder = previewBounds
        .projectRegionOfInterest(
            toSize = cameraPreviewImage.size(),
            regionOfInterest = viewFinder
        )
        .intersectionWith(cameraPreviewImage.size().toRect())

    return cameraPreviewImage.crop(projectedViewFinder)
}

/**
 * Crop the preview image from the camera based on a square surrounding the view finder's position in the preview
 * bounds.
 *
 * Note: This algorithm makes some assumptions:
 * 1. the previewBounds and the cameraPreviewImage are centered relative to each other.
 * 2. the previewBounds circumscribes the cameraPreviewImage. I.E. they share at least one field of view, and the
 *    cameraPreviewImage's fields of view are smaller than or the same size as the previewBounds's
 * 3. the previewBounds and the cameraPreviewImage have the same orientation
 */
@Deprecated(
    message = "Replaced by stripe card scan. See https://github.com/stripe/stripe-android/tree/master/stripecardscan",
    replaceWith = ReplaceWith("StripeCardScan"),
)
fun cropCameraPreviewToSquare(
    cameraPreviewImage: Bitmap,
    previewBounds: Rect,
    viewFinder: Rect,
): Bitmap {
    require(
        viewFinder.left >= previewBounds.left &&
            viewFinder.right <= previewBounds.right &&
            viewFinder.top >= previewBounds.top &&
            viewFinder.bottom <= previewBounds.bottom
    ) { "Card finder is outside preview image bounds" }

    val visiblePreview = getVisiblePreview(previewBounds)
    val squareViewFinder = maxAspectRatioInSize(visiblePreview, 1F).centerOn(viewFinder)

    // calculate the projected squareViewFinder
    val projectedSquare = previewBounds
        .projectRegionOfInterest(cameraPreviewImage.size(), squareViewFinder)
        .intersectionWith(cameraPreviewImage.size().toRect())

    return cameraPreviewImage.crop(projectedSquare)
}

/**
 * Determine if the device supports OpenGL version 3.1.
 */
@CheckResult
@Deprecated(
    message = "Replaced by stripe card scan. See https://github.com/stripe/stripe-android/tree/master/stripecardscan",
    replaceWith = ReplaceWith("StripeCardScan"),
)
fun hasOpenGl31(context: Context): Boolean {
    val openGlVersion = 0x00030001
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val configInfo = activityManager.deviceConfigurationInfo

    val isSupported = if (configInfo.reqGlEsVersion != ConfigurationInfo.GL_ES_VERSION_UNDEFINED) {
        configInfo.reqGlEsVersion >= openGlVersion && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
    } else {
        false
    }

    Log.d(Config.logTag, "OpenGL is supported? $isSupported")
    return isSupported
}
