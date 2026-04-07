import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.android)
}

android {

    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localProperties.load(localPropertiesFile.inputStream())
    }

    val mockoonIp = localProperties.getProperty("MOCKOON_IP") ?: "ERROR"

    namespace = "com.iberdrola.practicas2026.alejandroLO"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.iberdrola.practicas2026.alejandroLO"
        minSdk = 33
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "MOCKOON_URL", "\"http://$mockoonIp:3000/\"")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true // para poder coger MOCKOON_URL
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    //noinspection WrongGradleMethod
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)

    implementation("androidx.compose.material:material-icons-extended")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //implementation(libs.skeletonlayout) // dependecia que convierte la vista de una pantalla en un skeleton de carga
    implementation("com.valentinilk.shimmer:compose-shimmer:1.2.0")
    implementation("androidx.compose.foundation:foundation") // para cambiar de pantalla arrasatrando hacia los lados
//    implementation(libs.androidx.room3.common.jvm)v
//    implementation(libs.androidx.room3.runtime)

    // DEPENDENCIAS PARA LOS TEST
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.junit)
    testImplementation(libs.turbine)


    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}