package com.getbouncer.scan.framework.util

import android.content.Context

@Deprecated(
    message = "Replaced by stripe card scan. See https://github.com/stripe/stripe-android/tree/master/stripecardscan",
    replaceWith = ReplaceWith("StripeCardScan"),
)
data class AppDetails(
    val appPackageName: String?,
    val applicationId: String,
    val libraryPackageName: String,
    val sdkVersion: String,
    val sdkVersionCode: Int,
    val sdkFlavor: String,
    val isDebugBuild: Boolean
) {
    companion object {
        @JvmStatic
        fun fromContext(context: Context) = AppDetails(
            appPackageName = getAppPackageName(context),
            applicationId = getApplicationId(),
            libraryPackageName = getLibraryPackageName(),
            sdkVersion = getSdkVersion(),
            sdkVersionCode = getSdkVersionCode(),
            sdkFlavor = getSdkFlavor(),
            isDebugBuild = isDebugBuild()
        )
    }
}

@Deprecated(
    message = "Replaced by stripe card scan. See https://github.com/stripe/stripe-android/tree/master/stripecardscan",
    replaceWith = ReplaceWith("StripeCardScan"),
)
fun getAppPackageName(context: Context): String? = context.applicationContext.packageName

private fun getApplicationId(): String = "" // no longer available in later versions of gradle.

@Deprecated(
    message = "Replaced by stripe card scan. See https://github.com/stripe/stripe-android/tree/master/stripecardscan",
    replaceWith = ReplaceWith("StripeCardScan"),
)
fun getLibraryPackageName(): String = "BuildConfig.LIBRARY_PACKAGE_NAME"

@Deprecated(
    message = "Replaced by stripe card scan. See https://github.com/stripe/stripe-android/tree/master/stripecardscan",
    replaceWith = ReplaceWith("StripeCardScan"),
)
fun getSdkVersion(): String = "BuildConfig.SDK_VERSION_STRING"

private fun getSdkVersionCode(): Int = -1 // no longer available in later versions of gradle.

@Deprecated(
    message = "Replaced by stripe card scan. See https://github.com/stripe/stripe-android/tree/master/stripecardscan",
    replaceWith = ReplaceWith("StripeCardScan"),
)
fun getSdkFlavor(): String = "BuildConfig.BUILD_TYPE"

private fun isDebugBuild(): Boolean = false
