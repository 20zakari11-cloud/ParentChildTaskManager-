# Add project specific ProGuard rules here.

# Keep data models for Gson serialization
-keep class com.parentchild.taskmanager.data.model.** { *; }

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Keep enum values
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Firebase — uncomment when integrating
# -keep class com.google.firebase.** { *; }
# -keep class com.google.android.gms.** { *; }
