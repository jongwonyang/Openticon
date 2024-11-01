package io.ssafy.openticon

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import io.ssafy.openticon.data.model.Emoticon
import io.ssafy.openticon.data.model.EmoticonPack
import io.ssafy.openticon.ui.component.EmoticonPackView
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FloatingService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var floatingView: View
    private lateinit var secondFloatingView: View
    private var isSecondViewVisible = false  // 두 번째 뷰의 표시 상태
    private lateinit var secondLayoutParams: WindowManager.LayoutParams


    private var initialTouchX = 0
    private var initialTouchY = 0
    private var initialX = 0
    private var initialY = 0

    override fun onCreate() {
        super.onCreate()
        Log.d("FloatingService", "onCreate called")
        startForegroundServiceWithNotification()
        setupFloatingView()
        //setupSecondFloatingView()  // 두 번째 플로팅 뷰 초기화
        loadInitialData()
        loadLikeDate()
    }

    private fun loadInitialData() {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString("emoticon_data", null)

        // jsonString이 null이 아니면 역직렬화하여 List<ImoticonPack>으로 변환
        val data = jsonString?.let {
            Json.decodeFromString<List<EmoticonPack>>(it)
        } ?: emptyList()

        updateFloatingView(data)
    }

    private fun loadLikeDate() {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString("like_emoticon_data", null)

        // jsonString이 null이 아니면 역직렬화하여 List<ImoticonPack>으로 변환
        val data = jsonString?.let {
            Json.decodeFromString<EmoticonPack>(it)
        }

        val likeView = secondFloatingView.findViewById<EmoticonPackView>(R.id.imageLike)
        val tableLayout = secondFloatingView.findViewById<TableLayout>(R.id.tableLayout)
        data?.let {
            likeView.removeAllViews() // 이미 존재하는 이미지 삭제
            likeView.setupEmoticonPack(it) { images ->
                likeView.displayImagesInTable(tableLayout, images,
                    onImageClick = { emoticon: Emoticon ->
                        insertEmoticonIntoFocusedEditText(emoticon.imageResource)
                    }
                )
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun updateFloatingView(data: List<EmoticonPack>) {
        // WindowManager를 사용하여 floatingView 설정
        secondLayoutParams = WindowManager.LayoutParams(
            dpToPx(380),  // 380dp를 px로 변환하여 넓이 설정
            dpToPx(300),  // 300dp를 px로 변환하여 높이 설정
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        secondLayoutParams.gravity = Gravity.CENTER
        secondFloatingView = LayoutInflater.from(this).inflate(R.layout.new_compose_activity_layout, null)
        // 두 번째 플로팅 뷰 설정
        // 터치 리스너 설정
        secondFloatingView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialTouchX = event.rawX.toInt()
                    initialTouchY = event.rawY.toInt()
                    initialX = secondLayoutParams.x
                    initialY = secondLayoutParams.y
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    secondLayoutParams.x = initialX + (event.rawX - initialTouchX).toInt()
                    secondLayoutParams.y = initialY + (event.rawY - initialTouchY).toInt()
                    windowManager.updateViewLayout(secondFloatingView, secondLayoutParams)
                    true
                }
                else -> false
            }
        }
        val horizontalScrollView = secondFloatingView.findViewById<LinearLayout>(R.id.horizontal_linear)
        val tableLayout = secondFloatingView.findViewById<TableLayout>(R.id.tableLayout)

        // 데이터에 따라 UI 업데이트
        horizontalScrollView.removeAllViews()
        data.forEach { pack ->
            val emoticonPackView = EmoticonPackView(this)
            emoticonPackView.setupEmoticonPack(pack) { images ->
                emoticonPackView.displayImagesInTable(tableLayout, images,
                    onImageClick = { emoticon: Emoticon ->
                        insertEmoticonIntoFocusedEditText(emoticon.imageResource) },
                    onImageLongClick = {
                        emoticon: Emoticon ->
                        lkeEmoticon(emoticon)
                    }
                )
            }
            horizontalScrollView.addView(emoticonPackView)
        }
    }

    private fun lkeEmoticon(emoticon: Emoticon){
//        val alertView = LayoutInflater.from(this).inflate(R.layout.alert_layout, null)
//        val params = WindowManager.LayoutParams(
//            WindowManager.LayoutParams.WRAP_CONTENT,
//            WindowManager.LayoutParams.WRAP_CONTENT,
//            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
//            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
//            PixelFormat.TRANSLUCENT
//        )
//        windowManager.addView(alertView, params)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val past_jsonString = sharedPreferences.getString("like_emoticon_data", null)

        // jsonString이 null이 아니면 역직렬화하여 List<ImoticonPack>으로 변환
        val emoticonPack = past_jsonString?.let {
            Json.decodeFromString<EmoticonPack>(it)
        }


        emoticonPack?.let {
            val mutableImages = it.images.toMutableList()  // MutableList로 변환
            mutableImages.add(emoticon.copy())
            it.images = mutableImages.toList()  // 다시 List로 변환하여 할당
        }

        val editor = sharedPreferences.edit()
        val jsonString = Json.encodeToString(emoticonPack)
        editor.putString("like_emoticon_data", jsonString)
        editor.apply()
        loadLikeDate()
        Log.d("floating", "Maybe.... success,..?")
    }

    private fun copyEmoticon(clickedEmoticon: Emoticon) {
        /***
        val intent = Intent("io.ssafy.openticon.INSERT_EMOTICON").setClassName(/* TODO: provide the application ID. For example: */
            packageName,
        )
        intent.putExtra("emoticonResource", clickedEmoticon.imageResource)
        Log.d("serviceInsert", intent.action.toString())
        sendBroadcast(intent)
        ***/
        // 다른 서비스에서 실행
        val intent = Intent(this, KeyboardAccessibilityService::class.java)
        intent.putExtra("CALL_METHOD", "insertEmoticon")
        intent.putExtra("resourceId", clickedEmoticon.imageResource)
        startService(intent)
    }

    fun insertEmoticonIntoFocusedEditText(resourceId: Int) {
        Log.d("AccessInsert", resourceId.toString())
        val drawable = ContextCompat.getDrawable(applicationContext, resourceId) ?: return
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        // 클립보드에 비트맵 복사
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newUri(contentResolver, "Emoticon", getImageUri(bitmap))
        clipboard.setPrimaryClip(clip)

        Toast.makeText(this, "이모티콘이 클립보드에 복사되었습니다. 붙여넣기를 시도해보세요.", Toast.LENGTH_SHORT).show()
    }

    fun getImageUri(bitmap: Bitmap): Uri? {
        val file = File(cacheDir, "emoticon.png")
        try {
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return FileProvider.getUriForFile(this, "${packageName}.provider", file)
    }



    private fun startForegroundServiceWithNotification() {
        val channelId = "floating_service_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "Floating Service Channel", NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Floating Service")
            .setContentText("Floating window is running in the background")
            .setSmallIcon(R.drawable.ic_notification)
            .build()
        startForeground(1, notification)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupFloatingView() {
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            PixelFormat.TRANSLUCENT
        )
        layoutParams.gravity = Gravity.TOP or Gravity.LEFT

        floatingView = LayoutInflater.from(this).inflate(R.layout.floating_layout, null)
//        floatingView.findViewById<Button>(R.id.closeButton).setOnClickListener {
//            if (floatingView.windowToken != null) {
//                windowManager.removeView(floatingView)
//                if (isSecondViewVisible) windowManager.removeView(secondFloatingView)
//                stopSelf()  // 서비스 종료
//            }
//        }

        // 토글 버튼 설정
//        floatingView.findViewById<Button>(R.id.toggleButton).setOnClickListener {
//            toggleSecondFloatingView()
//        }


        val imageButton = floatingView.findViewById<ShapeableImageView>(R.id.imageButton)
        imageButton.shapeAppearanceModel = imageButton.shapeAppearanceModel.toBuilder()
            .setAllCorners(CornerFamily.ROUNDED, 100f) // 원하는 크기의 반지름 설정
            .build()

        floatingView.findViewById<ShapeableImageView>(R.id.imageButton).setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialTouchX = event.rawX.toInt()
                    initialTouchY = event.rawY.toInt()
                    initialX = layoutParams.x
                    initialY = layoutParams.y
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    layoutParams.x = initialX + (event.rawX - initialTouchX).toInt()
                    layoutParams.y = initialY + (event.rawY - initialTouchY).toInt()
                    windowManager.updateViewLayout(floatingView, layoutParams)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    // 터치 움직임이 작으면 클릭으로 간주
                    if (Math.abs(event.rawX - initialTouchX) < 10 && Math.abs(event.rawY - initialTouchY) < 10) {
                        toggleSecondFloatingView()
                    }
                    true
                }
                else -> false
            }
        }


        windowManager.addView(floatingView, layoutParams)
        Log.d("FloatingService", "Floating view added successfully")
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }
/**
    private fun setupSecondFloatingView() {
        // dp 값을 px로 변환하여 고정된 크기 설정
        secondLayoutParams = WindowManager.LayoutParams(
            dpToPx(380),  // 380dp를 px로 변환하여 넓이 설정
            dpToPx(300),  // 300dp를 px로 변환하여 높이 설정
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        secondLayoutParams.gravity = Gravity.CENTER

        // 두 번째 플로팅 뷰 설정
        secondFloatingView = LayoutInflater.from(this).inflate(R.layout.new_compose_activity_layout, null)

        // 터치 리스너 설정
        secondFloatingView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialTouchX = event.rawX.toInt()
                    initialTouchY = event.rawY.toInt()
                    initialX = secondLayoutParams.x
                    initialY = secondLayoutParams.y
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    secondLayoutParams.x = initialX + (event.rawX - initialTouchX).toInt()
                    secondLayoutParams.y = initialY + (event.rawY - initialTouchY).toInt()
                    windowManager.updateViewLayout(secondFloatingView, secondLayoutParams)
                    true
                }
                else -> false
            }
        }
    }
**/
    private fun toggleSecondFloatingView() {
        if (isSecondViewVisible) {
            windowManager.removeView(secondFloatingView)
            Log.d("FloatingService", "Second Floating View Hidden")
        } else {
            windowManager.addView(secondFloatingView, secondLayoutParams)
            Log.d("FloatingService", "Second Floating View Shown")
        }
        isSecondViewVisible = !isSecondViewVisible
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::floatingView.isInitialized && floatingView.windowToken != null) {
            windowManager.removeView(floatingView)
        }
        if (isSecondViewVisible && ::secondFloatingView.isInitialized && secondFloatingView.windowToken != null) {
            windowManager.removeView(secondFloatingView)
        }

        val intent = Intent("io.ssafy.openticon.ACTION_STOP_FLOATING_SERVICE")
        sendBroadcast(intent)
        Log.d("FloatingService", "FloatingService stopped, broadcast sent")

        getSharedPreferences("ServicePrefs", Context.MODE_PRIVATE)
            .edit().putBoolean("isServiceRunning", false).apply()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}