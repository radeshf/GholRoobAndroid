plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
}

android {
    namespace = "com.radesh.basemodule"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    api ("org.jetbrains.kotlin:kotlin-stdlib-jdk7:2.0.0")
    api ("org.jetbrains.kotlin:kotlin-stdlib:2.0.0")
    api ("androidx.multidex:multidex:2.0.1")
    api ("androidx.core:core-ktx:1.13.1")
    api ("androidx.appcompat:appcompat:1.7.0")
    api ("com.google.android.material:material:1.13.0")
    api ("androidx.constraintlayout:constraintlayout:2.1.4")
    api ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    //Logger
    api ("com.jakewharton.timber:timber:4.7.1")
    api ("com.squareup.okhttp3:logging-interceptor:3.11.0")//don"t update it

    //FONT
    api ("io.github.inflationx:calligraphy3:3.1.1")
    api ("io.github.inflationx:viewpump:2.0.3")
    api ("com.intuit.sdp:sdp-android:1.1.0")

    api ("io.reactivex.rxjava2:rxandroid:2.1.1")
    api ("io.reactivex.rxjava2:rxjava:2.2.10")
    //don"r update it it"s conflict with retrofit in android 21 and below

    api ("com.squareup.retrofit2:retrofit:2.9.0")
    api ("com.squareup.retrofit2:converter-gson:2.9.0")
    api ("com.squareup.retrofit2:adapter-rxjava2:2.3.0")

    api ("org.greenrobot:eventbus:3.3.1")
    api ("com.github.f0ris.sweetalert:library:1.6.2")
    api ("de.hdodenhof:circleimageview:3.1.0")
    api ("com.squareup.picasso:picasso:2.71828")
//
    api ("androidx.room:room-runtime:2.6.1")
    ksp ("androidx.room:room-compiler:2.6.1")
    api ("androidx.room:room-ktx:2.6.1")
    api ("androidx.room:room-rxjava2:2.6.1")

    api("com.github.medyo:dynamicbox:1.2@aar")
}