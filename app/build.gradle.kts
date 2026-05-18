plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
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
        versionCode = 1
        versionName = "1.0"
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

//        create("witk2") {
//            dimension = "application"
//            applicationId = "com.mrprojects.witk2"
//
//            buildConfigField("String", "APP_ID", "\"com.mrprojects.wasticker\"")
//            resValue("string", "app_name", "What Stickers")
//            resValue("string", "app_name_farsi", "قاتل کیه؟")
//            buildConfigField("String", "USERNAME_PREFIX", "\"witk\"")
//        }

        create("bazaar") {
            dimension = "market"
            buildConfigField("String", "MARKET", "\"BAZAAR\"")
            versionNameSuffix = ".0"
        }

        create("myket") {
            dimension = "market"
            buildConfigField("String", "MARKET", "\"MYKET\"")
            versionNameSuffix = ".1"

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
                buildConfigField("String", "PAYMENT_KEY", "\"MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwCiuj0N3aazp8zHfPgy/Co4nMHS01apObFKMhbyvPRRd8ybydXkCTEk/V95c1vi7u7mgp8wfog2TENjJLm/bnSSUSAL0xLR1FuT5lFKdPYDBhyCkjgM9pzH8A9q63THhFpQQTwP27P5hWiNrGSCwfG0CPV0rvX+y8pcqhzEDTWX0NOGYhgSNVAJSmuUvCsHEoaFKDY/FmiowFfDnsBwUcmTPqPN7NaGSpXh/J6/F10CAwEAAQ==\"")
            }
            appFlavor == "gholroob" && marketFlavor == "myket" -> {
                buildConfigField("String", "PAYMENT_KEY", "\"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQD5CP7I6cxxSL7OjsnZP7gm7yOnlnNgkVn+CGVde7TDuu3tk0c85aGAJjBeHRhy+ir0YJ33CjiLPwDi0rh8hczez8WYu2slHbWTyLXXkls7v3iTPvpJzKBuajcW51dCvTFIgBO1vGJKhRTRMc4lpySC1e/pQjZ5r7sO86CBaRDWhQIDAQAB\"")
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

    // Hilt
//    implementation("com.google.dagger:hilt-android:2.47")
//    kapt("com.google.dagger:hilt-android-compiler:2.47")
    implementation("com.airbnb.android:lottie:6.1.0")
    implementation(project(mapOf("path" to ":basemodule")))
}

configurations.all {
    resolutionStrategy {
        force("com.google.android.gms:play-services-ads-identifier:17.0.0")
    }
}