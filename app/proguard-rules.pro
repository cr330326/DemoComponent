# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\android-studio-sdk/tools/proguard/proguard-android.txt
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

#指定代码的压缩级别 0-7,表示对代码进行迭代优化的次数
-optimizationpasses 5

#不使用大小写混合，避免混淆时产生形形色色的类名
-dontusemixedcaseclassnames

#如果应用程序引入的有jar包，并且想混淆jar包里面的class
#-dontskipnonpubliclibraryclasses

#优化，不优化输入的类文件
-dontoptimize

#混淆时不做预校验（可去掉加快混淆速度）
-dontpreverify

#混淆时是否记录日志（混淆后生产映射文件map类名 -> 转化后类名的映射）
-verbose

#混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#忽略警告
-ignorewarning

#不混淆需要根据manifest来识别的类，所有activity的子类不要去混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
#如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment
#如果引用了v4或者v7包
-dontwarn android.support.**

#######################记录生成的日志数据,gradle build时在本项目根目录输出################

##apk 包内所有 class 的内部结构
#-dump class_files.txt
##未混淆的类和成员
#-printseeds seeds.txt
##列出从 apk 中删除的代码
#-printusage unused.txt
##混淆前后的映射
#-printmapping mapping.txt

#######################记录生成的日志数据，gradle build时 在本项目根目录输出-end################

#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#保持自定义控件指定规则的方法不被混淆
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

#保持指定规则的方法不被混淆（Android layout布局文件中为控件配置的onClick方法不能混淆）
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

#对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

#保持 Parcelable不被混淆（aidl文件不能去混淆）
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#需要序列化和反序列化的类不能被混淆（注：Java反射用到的类也不能被混淆）
-keepnames class * implements java.io.Serializable

#保护实现接口Serializable的类中，指定规则的类成员不能被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#保持枚举 enum 类不被混淆 如果混淆报错，建议直接使用上面的 -keepclassmembers class * implements java.io.Serializable即可
-keepclassmembers enum * {
  public static **[] values();
  public static ** valueOf(java.lang.String);
}

#不混淆资源类，保持R文件不被混淆，否则，反射是获取不到资源id的
-keepclassmembers class **.R$* {
    public static <fields>;
}

#避免混淆泛型（不写可能会出现类型转换错误，一般情况把这个加上就是）如果混淆报错建议关掉
-keepattributes Signature

#保护注解，假如项目中有用到注解，应加入这行配置
-keepattributes *Annotation*

## webview + js
#
##保护WebView对HTML页面的API不被混淆
#-keep class **.Webview2JsInterface{*;}
#
#-keepattributes *JavascriptInterface*
#
##如果你的项目中用到了webview的复杂操作 ，最好加入
##-keepclassmembers class * extends android.webkit.WebViewClient {
##     public void *(android.webkit.WebView,java.lang.String,android.graphics.Bitmap);
##     public boolean *(android.webkit.WebView,java.lang.String);
##}
#
##如果你的项目中用到了webview的复杂操作 ，最好加入
##-keepclassmembers class * extends android.webkit.WebChromeClient {
##     public void *(android.webkit.WebView,java.lang.String);
##}
#
## keep 使用 webview 的类
#-keepclassmembers class  com.gxframe5060.qr.views.QRResultActivity {
#   public *;
#}
#-keepclassmembers class  com.gxframe5060.faq.views.FAQActivity {
#   public *;
#}

############################4.对第三方库中的类不进行混淆##############################
#-libraryjars libs/android-support-v4.jar
-dontwarn android.support.v4.**
-keep class android.support.v4.**{*;}
-keep public class * extends android.support.v4.**
-keep interface android.support.v4.app.** {*;}

#-libraryjars libs/android-core-3.2.1.jar
-dontwarn com.google.zxing.client.android.camera.**
-keep class com.google.zxing.client.android.camera.**{*;}

#-libraryjars libs/asmack.jar
-dontwarn com.novell.sasl.client.**
-keep class com.novell.sasl.client.**{*;}
-dontwarn de.measite.smack.**
-keep class de.measite.smack.**{*;}
-dontwarn org.apache.harmony.javax.security.**
-keep class org.apache.harmony.javax.security.**{*;}
-dontwarn org.apache.qpid.management.common.sasl.**
-keep class org.apache.qpid.management.common.sasl.**{*;}
-dontwarn org.jivesoftware.smack.**
-keep class org.jivesoftware.smack.**{*;}

#-libraryjars libs/BundleCloud3.6.0-Release_win.jar
-dontwarn com.apkplug.**
-keep class com.apkplug.**{*;}
-dontwarn org.apkplug.**
-keep class org.apkplug.**{*;}

-dontwarn org.osgi.**
-keep class org.osgi.**{*;}
-dontwarn org.tengxin.sv.**
-keep class org.tengxin.sv.**{*;}

#-libraryjars libs/core-3.2.1.jar
-dontwarn com.google.zxing.**
-keep class com.google.zxing.**{*;}

#-libraryjars libs/core-3.2.1-sources.jar
-dontwarn com.google.zxing.**
-keep class com.google.zxing.**{*;}

#-libraryjars libs/ecs.jar
-dontwarn com.kilo.**
-keep class com.kilo.**{*;}

#-libraryjars libs/gson-2.2.4.jar
-dontwarn com.google.gson.**
-keep class com.google.gson.**{*;}
-keep class com.google.gson.stream.** { *; }

#-libraryjars libs/libammsdk.jar
-dontwarn com.tencent.mm.**
-dontwarn com.tencent.wxop.stat.**
-keep class com.tencent.mm.**{*;}
-keep class com.tencent.wxop.stat.**{*;}

#-libraryjars libs/MCRSDK.jar
-dontwarn com.hik.mcrsdk.**
-keep class com.hik.mcrsdk.**{*;}

#-libraryjars libs/MediaPlayer.jar
-dontwarn com.mobile.**
-keep class com.mobile.**{*;}

#-libraryjars libs/PlayerSDK.jar
-dontwarn org.MediaPlayer.PlayM4.**
-keep class org.MediaPlayer.PlayM4.**{*;}

#-libraryjars libs/uk-co-senab-photoview.jar
-dontwarn uk.co.senab.photoview.**
-keep class uk.co.senab.photoview.**{*;}

#-libraryjars libs/universal-image-loader-1.9.5.jar
-dontwarn com.nostra13.universalimageloader.**
-keep class com.nostra13.universalimageloader.**{*;}

#-libraryjars libs/xUtils.jar
-dontwarn android.backport.webp.**
-keep class android.backport.webp.**{*;}
-dontwarn org.xutils.**
-keep class org.xutils.**{*;}

#-libraryjars libs/xUtils-2.6.14.jar
-dontwarn com.lidroid.xutils.**
-keep class com.lidroid.xutils.**{*;}

#-libraryjars libs/nanohttpd-webserver-2.1.1.-jar-with-dependencies.jar
-dontwarn fi.iki.elonen.**
-keep class fi.iki.elonen.**{*;}

#-libraryjars libs/AMap_3DMap.jar
-dontwarn com.amap.api.**
-dontwarn com.autonavi.**
-keep class com.amap.api.**{*;}
-keep class com.autonavi.**{*;}

#-libraryjars libs/Location.jar
-dontwarn com.amap.api.location.**
-dontwarn com.aps.**
-keep class com.amap.api.location.**{*;}
-keep class com.aps.**{*;}


#tecent api
-keep class com.tencent.** { *; }

-dontwarn com.tencent.**

#-keepattributes Exceptions, Signature, InnerClasses