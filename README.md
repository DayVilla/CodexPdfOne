# CodexPdfOne

This project demonstrates integrating an open-source PDF engine in an Android application. It uses Android's built-in `PdfRenderer` displayed through [PhotoView](https://github.com/chrisbanes/PhotoView) for smooth pinch‑to‑zoom and [PdfBox-Android](https://github.com/TomRoush/PdfBox-Android) for creating and manipulating PDF content.

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

## Licensing

PdfBox-Android and PhotoView are Apache‑2.0 licensed, which allows commercial usage.
