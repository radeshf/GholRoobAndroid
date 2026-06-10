plugins {
    id("com.android.application")
    id("kotlin-android")
//    id("kotlin-kapt")
//    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"


    // id("com.google.gms.google-services")
}


android {
    namespace = "com.mrprojects.gholrob"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mrprojects.gholrob"
        minSdk = 21
        targetSdk = 34
        versionCode = 2
        versionName = "1.1.2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true

        val contentProviderAuthority = "${applicationId}.stickercontentprovider"

        manifestPlaceholders["contentProviderAuthority"] = contentProviderAuthority

        buildConfigField(
            "String",
            "CONTENT_PROVIDER_AUTHORITY",
            "\"${contentProviderAuthority}\""
        )
    }

    buildTypes {
        getByName("release") {
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

    aaptOptions {
        noCompress += "webp"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }

    flavorDimensions += listOf("application", "market")

    productFlavors {
        create("gholroob") {
            dimension = "application"
            applicationId = "com.mrprojects.gholrob"

            buildConfigField("String", "APP_ID", "\"com.mrprojects.gholrob\"")
            resValue("string", "app_name", "GholRoob")
            resValue("string", "app_name_farsi", "غول روب")
            buildConfigField("String", "USERNAME_PREFIX", "\"gholroob\"")
        }

        create("bazaar") {
            dimension = "market"
            buildConfigField("String", "MARKET", "\"BAZAAR\"")
        }

        create("myket") {
            dimension = "market"
            buildConfigField("String", "MARKET", "\"MYKET\"")

            manifestPlaceholders["marketApplicationId"] = "ir.mservices.market"
            manifestPlaceholders["marketBindAddress"] = "ir.mservices.market.InAppBillingService.BIND"
            manifestPlaceholders["marketPermission"] = "ir.mservices.market.BILLING"
        }
    }

    applicationVariants.all {
        val appFlavor = productFlavors.find { it.dimension == "application" }?.name
        val marketFlavor = productFlavors.find { it.dimension == "market" }?.name

        when {
            appFlavor == "gholroob" && marketFlavor == "bazaar" -> {
                buildConfigField("String", "PAYMENT_KEY", "\"MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwD9tIZ4VMr76BMBFY4WQXRkAV1ZaRROjV9DRdiBT7kmHyTT2jMtLB2QaZUcXtk4+MaV0JfY2N5QQGrdn8ver5MJzu5+IqdcCIjDvDODKpE4aO4MUnOb6maqJEc6urooDT5wLfcC5oip+X9NdY0HzOfnzO5dgf/Avy0D16KbSIeMtspmJl5SDGetvQ4PmM9IZThA+mXqoDsewefAAQogvby26RrMHDjQn81WAMVPGi0CAwEAAQ==\"")
            }
            appFlavor == "gholroob" && marketFlavor == "myket" -> {
                buildConfigField("String", "PAYMENT_KEY", "\"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCa2z94NAUZZju5667WK+lO3eGEH7EnBMETMbPYnMXLU9Rx4Vd66vkZa2V6Rm1m4ep/k5humdCDh6mu2ijFg4RxaB3+y+YWOwNilbFX2vgGsSMs+yVbTyZuB0/wvTWLROdDIlAVxVaoE4w/TumRwX1a8rRBWBmzvxupgs2ukiUPVwIDAQAB\"")
            }
            appFlavor == "witk2" && marketFlavor == "bazaar" -> {
                buildConfigField("String", "PAYMENT_KEY", "\"YOUR_KEY_FOR_WITK2_BAZAAR\"")
            }
            appFlavor == "witk2" && marketFlavor == "myket" -> {
                buildConfigField("String", "PAYMENT_KEY", "\"YOUR_KEY_FOR_WITK2_MYKET\"")
            }
        }
    }


}


dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    val fresco_version = "3.1.3"
    implementation("com.facebook.fresco:fresco:$fresco_version")
    implementation("com.facebook.fresco:webpsupport:$fresco_version")
    implementation("com.facebook.fresco:animated-webp:$fresco_version")
    implementation("com.facebook.fresco:animated-base:$fresco_version")
//    implementation("com.google.android.flexbox:flexbox:3.0.0")

    add("bazaarImplementation", "com.github.cafebazaar.Poolakey:poolakey:2.2.0")
    add("bazaarImplementation", "com.github.cafebazaar.Poolakey:poolakey-rx:2.2.0")

    add("myketImplementation", "com.github.myketstore:myket-billing-client:1.18")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.core:core-splashscreen:1.0.1")

    implementation("ir.tapsell.plus:tapsell-plus-sdk-android:2.1.7")

    ksp("androidx.room:room-compiler:2.6.1")

    implementation("com.airbnb.android:lottie:6.1.0")
    implementation(project(mapOf("path" to ":basemodule")))
}

configurations.all {
    resolutionStrategy {
        force("com.google.android.gms:play-services-ads-identifier:17.0.0")
    }
}