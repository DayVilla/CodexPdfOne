# CodexPdfOne

This project demonstrates integrating an open-source PDF engine in an Android application. It uses [AndroidPdfViewer](https://github.com/barteksc/AndroidPdfViewer) for rendering and pinch‑to‑zoom support and [PdfBox-Android](https://github.com/TomRoush/PdfBox-Android) for creating and manipulating PDF content.

## Features

- View PDF documents with smooth zooming and panning.
- Freehand annotation overlay using `AnnotationView`.
- Sample PDF bundled in the `assets` folder.
- Ready to extend with form filling, signatures and image placement.

## Running

Build the project with Android Studio or the command line:

```bash
./gradlew assembleDebug
```

Then install the APK on a device or emulator.

This project uses the JitPack repository for the AndroidPdfViewer library. If
you copy this setup to another project ensure the following entry exists in
`settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
```

## Licensing

Both AndroidPdfViewer and PdfBox-Android are Apache‑2.0 licensed, which allows commercial usage.
