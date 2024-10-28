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

    override fun onCreate() {
        super.onCreate()
        Log.d("FloatingService", "onCreate called")
        startForegroundServiceWithNotification()
        setupFloatingView()
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
                stopSelf()  // 서비스 종료
            }
        }

        floatingView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_MOVE) {
                layoutParams.x = event.rawX.toInt()
                layoutParams.y = event.rawY.toInt()
                windowManager.updateViewLayout(floatingView, layoutParams)
            }
            true
        }
        windowManager.addView(floatingView, layoutParams)
        Log.d("FloatingService", "Floating view added successfully")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::floatingView.isInitialized && floatingView.windowToken != null) {
            windowManager.removeView(floatingView)
        }

        // Broadcast 전송하여 isServiceRunning 상태 초기화
        val intent = Intent("io.ssafy.openticon.ACTION_STOP_FLOATING_SERVICE")
        sendBroadcast(intent)
        Log.d("FloatingService", "FloatingService stopped, broadcast sent")

        // 서비스 종료 시 상태를 SharedPreferences에 반영
        getSharedPreferences("ServicePrefs", Context.MODE_PRIVATE)
            .edit().putBoolean("isServiceRunning", false).apply()
    }


    override fun onBind(intent: Intent?): IBinder? = null
}
