package io.ssafy.openticon

import android.accessibilityservice.AccessibilityService
import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class KeyboardAccessibilityService : AccessibilityService() {

    private val sharedPreferences by lazy {
        getSharedPreferences("ServicePrefs", Context.MODE_PRIVATE)
    }



    override fun onCreate() {
        super.onCreate()
        // 서비스 실행 상태를 초기화하여 앱 시작 시 기본적으로 false가 되도록 설정
        sharedPreferences.edit().putBoolean("isServiceRunning", false).apply()
    }


    override fun onServiceConnected() {
        super.onServiceConnected()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.getStringExtra("CALL_METHOD")) {
            "insertEmoticon" -> {
                val resourceId = intent.getIntExtra("resourceId", -1)
                if (resourceId != -1) {
                    insertEmoticonIntoFocusedEditText(resourceId)
                }
            }
            // 다른 호출도 필요하면 추가
        }
        return super.onStartCommand(intent, flags, startId)
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

    // 비트맵을 Uri로 변환하는 메서드
    private fun getImageUri(bitmap: Bitmap): Uri {
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Emoticon", null)
        return Uri.parse(path)
    }


    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            if (event.eventType == AccessibilityEvent.TYPE_VIEW_FOCUSED &&
                event.className?.contains("EditText") == true) {
                Log.d("KeyboardAccessibilityService", "EditText focused - possible keyboard activation")
                startFloatingServiceIfNotRunning()
            }

            // 키보드 닫힘을 감지하여 플로팅 윈도우 닫기
            if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED &&
                event.className != "android.inputmethodservice.InputMethodService") {
                Log.d("KeyboardAccessibilityService", "Non-keyboard window detected - possible keyboard close")
                stopFloatingServiceIfRunning()
            }
        }
    }

    private fun startFloatingServiceIfNotRunning() {
        val isServiceRunning = sharedPreferences.getBoolean("isServiceRunning", false)
        if (!isServiceRunning) {
            Log.d("KeyboardAccessibilityService", "Starting FloatingService")
            val intent = Intent(this, FloatingService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
            // 서비스 시작 후 실행 상태를 업데이트
            sharedPreferences.edit().putBoolean("isServiceRunning", true).apply()
        } else {
            Log.d("KeyboardAccessibilityService", "FloatingService already running")
        }
    }

    private fun stopFloatingServiceIfRunning() {
        val isServiceRunning = sharedPreferences.getBoolean("isServiceRunning", false)
        if (isServiceRunning) {
            Log.d("KeyboardAccessibilityService", "Stopping FloatingService")
            val intent = Intent(this, FloatingService::class.java)
            stopService(intent)
            sharedPreferences.edit().putBoolean("isServiceRunning", false).apply()
        }
    }

    override fun onInterrupt() {}

    override fun onDestroy() {
        super.onDestroy()
    }
}
