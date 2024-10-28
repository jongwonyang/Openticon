package io.ssafy.openticon

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.core.app.NotificationCompat

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
        setupSecondFloatingView()  // 두 번째 플로팅 뷰 초기화
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
        floatingView.findViewById<Button>(R.id.closeButton).setOnClickListener {
            if (floatingView.windowToken != null) {
                windowManager.removeView(floatingView)
                if (isSecondViewVisible) windowManager.removeView(secondFloatingView)
                stopSelf()  // 서비스 종료
            }
        }

        // 토글 버튼 설정
        floatingView.findViewById<Button>(R.id.toggleButton).setOnClickListener {
            toggleSecondFloatingView()
        }

        floatingView.setOnTouchListener { _, event ->
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
