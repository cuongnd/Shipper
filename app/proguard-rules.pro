# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\ANDROID_SDK\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-ignorewarnings
-libraryjars libs/android-support-v4.jar
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontoptimize
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.idea.fifaalarmclock.entity.***
-keep class com.google.gson.stream.** { *; }

-libraryjars libs/baidumapapi_v3_5_0.jar
-keep class com.baidu.** { *; }
-keep class vi.com.gdi.bgl.android.**{*;}

-keep class com.tencent.upload.network.base.ConnectionImpl
-keep class com.tencent.upload.network.base.ConnectionImpl {*;}
-keep class * extends com.qq.taf.jce.JceStruct { *; }

-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

-libraryjars /libs/tncrash.jar
-dontwarn com.testin.agent.**
-keep class com.testin.agent.** {*;}