# Keep ML Kit classes
-keep class com.google.mlkit.** { *; }
-keep class com.google.android.gms.** { *; }

# Keep Hilt generated code
-keep class dagger.hilt.internal.aggregatedroot.codegen.* { *; }
-keep class dagger.hilt.internal.processedrootsentinel.codegen.* { *; }
-keep class dagger.hilt.android.internal.managers.* { *; }

-dontwarn javax.annotation.**
-dontwarn dagger.hilt.internal.**
