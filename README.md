# Random Quote Generator

A simple native Android application developed in Kotlin that displays random quotes with their authors. The app provides a clean, minimal, and user-friendly interface with options to generate and share quotes.

## Features

* Displays a random quote when the app launches.
* Generates a new quote with the "New Quote" button.
* Displays the quote text and author clearly.
* Prevents quotes from repeating during the current app session.
* Fetches fresh quotes from an online API when an internet connection is available.
* Provides an offline quote collection when the internet is unavailable.
* Allows users to share quotes through apps such as WhatsApp, Instagram, and other supported applications.
* Includes a custom purple and indigo themed user interface.
* Supports a custom adaptive application icon.

## Technologies Used

* Kotlin
* Android Studio
* XML
* Android SDK
* Material Components
* REST API

## Project Structure

```text
RandomQuoteGenerator/
├── app/
│   ├── build.gradle
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/codealpha/randomquotegenerator/
│       │   └── MainActivity.kt
│       └── res/
│           ├── layout/
│           │   └── activity_main.xml
│           ├── values/
│           └── drawable/
├── build.gradle
├── settings.gradle
└── gradle.properties
```

## Application

The Random Quote Generator provides a simple way to discover and share inspirational and interesting quotes through a clean Android interface.
