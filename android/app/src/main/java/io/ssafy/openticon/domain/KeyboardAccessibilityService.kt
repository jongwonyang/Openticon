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

    private val sharedPreferences by lazy {
        getSharedPreferences("ServicePrefs", Context.MODE_PRIVATE)
    }

    // 서비스 종료 시 실행 상태 초기화를 위한 BroadcastReceiver
    private val serviceStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "io.ssafy.openticon.ACTION_STOP_FLOATING_SERVICE") {
                sharedPreferences.edit().putBoolean("isServiceRunning", false).apply()
                Log.d("KeyboardAccessibilityService", "FloatingService stopped, isServiceRunning reset to false")
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        val filter = IntentFilter("io.ssafy.openticon.ACTION_STOP_FLOATING_SERVICE")
        ContextCompat.registerReceiver(this, serviceStateReceiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            // EditText에 포커스가 설정된 경우에만 플로팅 서비스 실행
            if (event.eventType == AccessibilityEvent.TYPE_VIEW_FOCUSED &&
                event.className?.contains("EditText") == true) {
                Log.d("KeyboardAccessibilityService", "EditText focused - possible keyboard activation")
                startFloatingServiceIfNotRunning()
            }

            // 키보드 닫힘 추정 이벤트 감지하여 플로팅 윈도우 닫기
            if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED &&
                event.className != "android.inputmethodservice.InputMethodService") {
                Log.d("KeyboardAccessibilityService", "Non-keyboard window detected - possible keyboard close")
                stopFloatingServiceIfRunning()
            }
        }
    }

    private fun isKeyboardEvent(event: AccessibilityEvent): Boolean {
        return (event.eventType == AccessibilityEvent.TYPE_VIEW_FOCUSED ||
                event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) &&
                event.className?.contains("EditText") == true
    }

    private fun startFloatingServiceIfNotRunning() {
        if (!sharedPreferences.getBoolean("isServiceRunning", false)) {
            Log.d("KeyboardAccessibilityService", "Starting FloatingService")
            val intent = Intent(this, FloatingService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
            sharedPreferences.edit().putBoolean("isServiceRunning", true).apply()
        } else {
            Log.d("KeyboardAccessibilityService", "FloatingService already running")
        }
    }

    private fun stopFloatingServiceIfRunning() {
        if (sharedPreferences.getBoolean("isServiceRunning", false)) {
            Log.d("KeyboardAccessibilityService", "Stopping FloatingService")
            val intent = Intent(this, FloatingService::class.java)
            stopService(intent)
            sharedPreferences.edit().putBoolean("isServiceRunning", false).apply()
        }
    }

    override fun onInterrupt() {}

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(serviceStateReceiver)
    }
}
