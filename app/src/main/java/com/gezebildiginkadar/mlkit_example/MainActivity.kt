package com.gezebildiginkadar.mlkit_example

import android.Manifest
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ai.cardscan.insurance.CardScanActivity
import ai.cardscan.insurance.data.CardScanHelper
import ai.cardscan.insurance.data.CardScanConfig
import ai.cardscan.insurance.data.ScannedCard
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    //private lateinit var cameraExecutor: ExecutorService
    //private lateinit var overlay: CustomOverlay

    //private lateinit var previewView: PreviewView
    //lateinit var tv_card_number: TextView

    lateinit var text: TextView
    lateinit var button: Button
    private var token: String = ""

    // Permission launcher, displays dialog asking for permission
    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted, you can launch Cardscan activity safely
                launchCardScanActivity()
            } else {
                // Permission denied or dismissed.
                // Explain to user that feature is unavailable, because permission
                // is needed to use it.
            }
        }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //tv_card_number = findViewById<TextView>(R.id.tv_card_number)
        //previewView = findViewById<PreviewView>(R.id.previewView)
        //overlay = findViewById(R.id.customOverlay)

        text = findViewById(R.id.text_item)
        button = findViewById(R.id.button_item)
        lifecycleScope.launch {
        getSessionToken()
        }
        // Kamera izni kontrolü
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            REQUEST_CAMERA_PERMISSION
        )


        button.setOnClickListener {
            val intent = Intent(this, SingleActivityDemo::class.java)
            startActivity(intent)
            //setUpCardScanHelper()
            //launchCardScanActivity()
        }

        ///cameraExecutor = Executors.newSingleThreadExecutor()
        //handleScannerOpen()
        //startCamera()
    }

    private suspend fun getSessionToken(): String {
        val apiService = Retrofit.Builder()
            .baseUrl("https://sandbox.cardscan.ai/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CardScanApiService::class.java)

        val response = apiService.getSessionToken("Bearer secret_test_eI0ZVbTJNc1Ua2IX")
        token = response.Token.toString()
        Log.e("ersin", token)
        return response?.Token ?: throw Exception("tet")
    }

    private suspend fun getCards(cardid: String) {
        val apiService = Retrofit.Builder()
            .baseUrl("https://sandbox.cardscan.ai/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CardScanApiService::class.java)

        val response = apiService.getCard("Bearer secret_test_eI0ZVbTJNc1Ua2IX", cardid)
        val gson = Gson()
        val jsonString = gson.toJson(response)
        Log.d("CardResponse", jsonString)

        Log.e("ersin", token)
    }


    // Check permission status and act on it
    private fun handleScannerOpen() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission is granted, you can start
                // Cardscan activity safely.
                launchCardScanActivity()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> {
                // Show UI to app user to tell them why you need the permission.
                // This is a good place to take them to settings too
                // so they can grant permission there.
            }

            else -> {
                // Here you can directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun launchCardScanActivity() {
        CardScanActivity.launch(
            // Pass an activity to the launcher
            this@MainActivity,
            // A CardScanConfig instance to connect to the server and customize UI
            CardScanConfig(
                // your token goes here
                sessionToken = token,
            ),
            // And other optional parameters to alter behavior
            closeAfterSuccess = true, // Close the scanner after successful scan?
        )
    }


    private fun setUpCardScanHelper() {
        CardScanHelper(this).apply {
            onSuccess { card ->
                val cardJson = Json.encodeToString(ScannedCard.serializer(), card)
                text.text = cardJson
                lifecycleScope.launch {
                getCards(card.cardId.toString())
                }
                Log.e("ersin", cardJson)
            }
            onError { error ->
                print(error?.message)
            }
            onRetry {
                print("retry")
            }
            onCancel {
                print("cancel")
            }
        }
    }

/*    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(
            {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                val imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(
                            cameraExecutor,
                            { imageProxy ->
                                processImageProxy(imageProxy)
                            })
                    }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        this,
                        cameraSelector,
                        preview,
                        imageAnalyzer
                    )
                } catch (exc: Exception) {
                    Log.e(
                        "MainActivity",
                        "Kamera başlatılamadı.",
                        exc
                    )
                }

            },
            ContextCompat.getMainExecutor(this)
        )
    }

    @OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {

            // ImageProxy'yi Bitmap'e dönüştür
            val bitmap = imageProxyToBitmap(imageProxy)
            if (bitmap != null) {
                // Bitmap'i gri tonlamaya çevir
                val grayBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(grayBitmap)
                val paint = Paint()
                val colorMatrix = ColorMatrix().apply { setSaturation(0f) }
                val filter = ColorMatrixColorFilter(colorMatrix)
                paint.colorFilter = filter
                canvas.drawBitmap(bitmap, 0f, 0f, paint)

                // Gri tonlamalı Bitmap'ten InputImage oluştur
                val inputImage = InputImage.fromBitmap(grayBitmap, 0)

                // Metin tanıma işlemini başlat
                val recognizer = TextRecognition.getClient(TextRecognizerOptions.Builder().build())
                recognizer.process(inputImage)
                    .addOnSuccessListener { visionText ->
                        val cardNumber = extractCardNumber(visionText.text)
                        tv_card_number.text = "Card Number: ${cardNumber ?: "Not detected"}"
                    }
                    .addOnFailureListener { e ->
                        Log.e("MainActivity", "Text recognition error: ${e.message}")
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                    }
            } else {
                imageProxy.close()
            }
        } else {
            imageProxy.close()
        }
    }


    @OptIn(ExperimentalGetImage::class)
    fun imageProxyToBitmap(imageProxy: ImageProxy): Bitmap? {
        val image = imageProxy.image ?: return null

        // YUV formatındaki görüntüyü dönüştürmek için gerekli bilgileri alıyoruz
        val yBuffer = image.planes[0].buffer // Y verisi
        val uBuffer = image.planes[1].buffer // U verisi
        val vBuffer = image.planes[2].buffer // V verisi

        // YUV verilerinin boyutları
        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        // YUV verilerini byte dizisi halinde birleştiriyoruz
        val nv21 = ByteArray(ySize + uSize + vSize)

        // Y verisini byte dizisine kopyala
        yBuffer.get(nv21, 0, ySize)

        // UV verilerini interleave (birbirine geçecek şekilde) kopyala
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        // ImageProxy'nin dönüş derecesini alıyoruz
        val rotationDegrees = imageProxy.imageInfo.rotationDegrees

        // YUV verisinden YuvImage oluşturuyoruz
        val yuvImage = YuvImage(nv21, ImageFormat.NV21, imageProxy.width, imageProxy.height, null)

        // ByteArrayOutputStream kullanarak JPEG formatında bir çıktı alıyoruz
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(android.graphics.Rect(0, 0, yuvImage.width, yuvImage.height), 100, out)
        val jpegBytes = out.toByteArray()

        // Byte dizisinden Bitmap oluşturuyoruz
        val bitmap = BitmapFactory.decodeByteArray(jpegBytes, 0, jpegBytes.size)

        // Eğer gerekliyse, Bitmap'i döndürüyoruz
        return if (rotationDegrees != 0) {
            val matrix = android.graphics.Matrix()
            matrix.postRotate(rotationDegrees.toFloat())
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } else {
            bitmap
        }
    }*/


    private fun extractCardNumber(text: String): String? {
        val cardNumberRegex = Regex("\\b\\d{4}\\s?\\d{4}\\s?\\d{4}\\s?\\d{4}\\b")
        val matchResult = cardNumberRegex.find(text)
        return matchResult?.value
    }

    override fun onDestroy() {
        super.onDestroy()
        //cameraExecutor.shutdown()
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 10
    }


}