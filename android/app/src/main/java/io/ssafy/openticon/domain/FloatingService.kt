package io.ssafy.openticon

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
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
import androidx.core.app.NotificationCompat
import android.widget.Button

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

        if (::floatingView.isInitialized) return

        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        layoutParams.gravity = Gravity.TOP or Gravity.LEFT

        floatingView = LayoutInflater.from(this).inflate(R.layout.floating_layout, null)
        floatingView.findViewById<Button>(R.id.closeButton).setOnClickListener {
            windowManager.removeView(floatingView)
            stopSelf()
        }
        floatingView.setOnTouchListener(createDragTouchListener(layoutParams))
        windowManager.addView(floatingView, layoutParams)
    }

    private fun createDragTouchListener(layoutParams: WindowManager.LayoutParams): View.OnTouchListener {
        return View.OnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    layoutParams.x = event.rawX.toInt()
                    layoutParams.y = event.rawY.toInt()
                    windowManager.updateViewLayout(view, layoutParams)
                }
            }
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // WindowManager에 뷰가 연결되어 있는지 확인 후 제거
        if (::floatingView.isInitialized && floatingView.windowToken != null) {
            windowManager.removeView(floatingView)
        }

        // Broadcast 전송하여 isServiceRunning 상태 초기화
        val intent = Intent("io.ssafy.openticon.ACTION_STOP_FLOATING_SERVICE")
        sendBroadcast(intent)
    }


    override fun onBind(intent: Intent?): IBinder? = null
}
