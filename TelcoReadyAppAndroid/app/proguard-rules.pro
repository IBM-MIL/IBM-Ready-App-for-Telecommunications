# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/tannerpreiss1/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:
-keep class org.apache.http.** { *; }
-dontwarn butterknife.internal.ButterKnifeProcessor

-keep class android.support.design.widget.** { *; }
-keep interface android.support.design.widget.** { *; }
-dontwarn android.support.design.**

# Hide warnings about references to newer platforms in the library
-dontwarn android.support.v7.**
# don't process support library
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

# Hide warnings about references to newer platforms in the library
-dontwarn android.support.v4.**
# don't process support library
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }

-keep class com.ibm.mil.readyapps.** { *; }
-keep interface com.ibm.mil.readyapps.** { *; }

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-keepnames class * { @butterknife.Bind *; }

# MQA
-keep class ext.** { *; }
-keep class com.applause.** { *; }
-keepattributes
-keep class ext.com.google.inject.** { *; }
-keepclassmembers class * { @ext.com.google.inject.Inject <init>(...); }

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
