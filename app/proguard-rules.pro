# Proguard rules for Nexus Quiz App
-keep class com.nexus.quiz.data.model.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
