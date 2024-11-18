# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

######################
# Jetpack Compose 관련 규칙
######################

# Composable 함수 보호
-keep class * { @androidx.compose.runtime.Composable *; }

# Jetpack Compose의 모든 클래스 보호
-keep class androidx.compose.** { *; }

# Jetpack Compose Compiler가 생성한 클래스 보호
-keep class androidx.activity.ComponentActivity { *; }
-keep class androidx.lifecycle.ViewModel { *; }

# Jetpack Compose 애니메이션 관련 클래스 보호 (애니메이션을 사용하는 경우에만 필요)
-keep class androidx.compose.animation.** { *; }


######################
# Coroutine 및 Flow 관련 규칙
######################

# Coroutine 및 Flow 사용 시 필요한 규칙
-keepclassmembers class kotlinx.coroutines.** { *; }
-keepclassmembers class kotlin.coroutines.jvm.internal.** { *; }
-keepclassmembers class kotlinx.coroutines.flow.StateFlow { *; }
-keepclassmembers class kotlinx.coroutines.flow.MutableStateFlow { *; }

# 코루틴 관련 경고 무시
-dontwarn kotlinx.coroutines.**


######################
# ViewModel 관련 규칙
######################

# ViewModel 클래스 보호
-keep class * extends androidx.lifecycle.ViewModel { *; }
-keepclassmembers class * extends androidx.lifecycle.ViewModel { *; }
-keep class androidx.lifecycle.** { *; }


######################
# Compose Navigation 관련 규칙
######################

# Compose Navigation 사용 시 필요한 규칙
-keep class androidx.navigation.NavController { *; }
-keep class androidx.navigation.NavHostController { *; }
-keep class androidx.navigation.** { *; }


######################
# Parcelable 객체 보호 규칙
######################

# Parcelable을 구현한 클래스 보호
-keepclassmembers class * implements android.os.Parcelable {
    static final android.os.Parcelable$Creator *;
}


######################
# Android 구성 요소 보호 규칙
######################

# Activity, Service, Application 등 Android 기본 구성 요소 보호
-keep class * extends android.app.Activity
-keep class * extends android.app.Service
-keep class * extends android.app.Application
-keep class * extends android.content.BroadcastReceiver
-keep class * extends android.content.ContentProvider


######################
# Gson 관련 규칙
######################

# Gson에서 JSON 직렬화/역직렬화를 위한 클래스 보호
-keep class com.google.gson.** { *; }
-keep class io.ssafy.openticon.data.model.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Gson을 사용할 때 LinkedTreeMap 관련 경고 무시
-dontwarn com.google.gson.internal.LinkedTreeMap


######################
# 기타
######################

# Serializable 클래스 보호
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 디버깅 및 최적화 방지 규칙 (최종 릴리즈에서 제거 권장)
# -dontoptimize
# -dontobfuscate
