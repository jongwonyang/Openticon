package io.ssafy.openticon

import android.accessibilityservice.AccessibilityService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.core.content.ContextCompat

class KeyboardAccessibilityService : AccessibilityService() {

    var isServiceRunning = false  // 플래그로 실행 상태 관리

    // FloatingService의 상태를 수신하는 BroadcastReceiver
    private val serviceStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "io.ssafy.openticon.ACTION_STOP_FLOATING_SERVICE") {
                isServiceRunning = false
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        // BroadcastReceiver 등록
        val filter = IntentFilter("io.ssafy.openticon.ACTION_STOP_FLOATING_SERVICE")

        // RECEIVER_NOT_EXPORTED 플래그 사용
        ContextCompat.registerReceiver(this, serviceStateReceiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            if (isKeyboardEvent(event)) {
                startFloatingServiceIfNotRunning()
            }
        }
    }

    private fun isKeyboardEvent(event: AccessibilityEvent): Boolean {
        return (event.eventType == AccessibilityEvent.TYPE_VIEW_FOCUSED ||
                event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) &&
                event.className?.contains("EditText") == true
    }

    private fun startFloatingServiceIfNotRunning() {
        if (!isServiceRunning) {
            Log.d("KeyboardAccessibilityService", "Starting FloatingService")
            val intent = Intent(this, FloatingService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
            isServiceRunning = true
        } else {
            Log.d("KeyboardAccessibilityService", "FloatingService already running")
        }
    }

    override fun onInterrupt() {}

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(serviceStateReceiver)  // 서비스 종료 시 BroadcastReceiver 해제
    }

}
