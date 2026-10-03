# GitHub User Search

An Android app for searching GitHub users, viewing profile details, and saving favorite users to local storage. Built as part of the **HCS-IDN Android Assessment**.

>**Download the ready-to-use APK:** [APK_LINK](https://drive.google.com/drive/folders/1alj8WvEpoZR2CXjt60fXv6808mB1KOJp?usp=drive_link)

---

## Table of Contents
1. [Project Overview](#-project-overview)
2. [Architecture & Tech Stack](#-architecture--tech-stack)
3. [How to Build & Run](#-how-to-build--run)
4. [Features & Nice to Have](#-features--nice-to-have)
5. [Challenges & Trade-offs](#-challenges--trade-offs)

---

## Project Overview

GitHub User Search lets users:
- Search GitHub users by username through the GitHub REST API.
- View user profile details.
- Mark users as **favorites** and store them locally (available offline).
- Switch between **dark/light theme** and exit the app from the **Settings** page.

---

## Architecture & Tech Stack

### Architecture: MVVM (Model–View–ViewModel)

The app follows **MVVM** with the following layer separation:

| Layer | Responsibility |
|-------|----------------|
| **View** (Activity/Fragment) | Renders the UI and observes state from the ViewModel. Contains no business logic. |
| **ViewModel** | Holds and manages UI state, calls the Repository, and survives configuration changes (e.g. screen rotation). |
| **Repository** | Single source of truth: coordinates remote (Retrofit) and local (Room) data sources. |
| **Data Source** | Remote (GitHub API via Retrofit + Moshi) and Local (Room Database). |

**Why MVVM:**
1. **Officially recommended by Google** for Android, with first-class Jetpack support (ViewModel, LiveData/Flow, Room, Hilt).
2. **Separation of concerns**: UI, logic, and data are decoupled, so the code is cleaner and easier to maintain.
3. **Lifecycle-aware**: the ViewModel preserves state across rotation, so data doesn't need to be reloaded.
4. **Testable**: ViewModels and Repositories can be unit-tested without the Android framework.
5. **Right-sized for the project**: MVVM is sufficient for this scope, without the over-engineering of full Clean Architecture.

### Tech Stack
 
| Category | Library / Tool |
|----------|----------------|
| Language | Kotlin 1.9.24 |
| UI | XML + ViewBinding, Material Components 1.12.0, RecyclerView |
| Architecture | MVVM, Repository Pattern |
| Jetpack | ViewModel + LiveData (Lifecycle 2.8.4), Navigation Component 2.7.7 |
| Dependency Injection | Hilt 2.51.1 (with KSP 1.9.24-1.0.20) |
| Networking | Retrofit 2.11.0, OkHttp 4.12.0 |
| JSON Parsing | Moshi 1.15.1 (with KSP codegen) |
| Network Debugging | Chucker 4.0.0 (`no-op` variant in release builds) |
| Local Database | Room 2.6.1 |
| Asynchronous | Kotlin Coroutines 1.8.1 |
| Image Loading | Glide 4.16.0 |
| Dependency Management | Gradle Version Catalog (`gradle/libs.versions.toml`) |
| Unit Testing | JUnit 4.13.2, MockK 1.13.12, Coroutines Test, Arch Core Testing |
| UI Testing | AndroidX Test (JUnit ext 1.2.1), Espresso 3.6.1 |
 
---

## How to Build & Run

### Requirements

| Component | Version |
|-----------|---------|
| Android Studio | Rabbit 1 |
| JDK | 17 |
| Gradle | 9.6.0  |
| Android Gradle Plugin | 8.5.2 |
| Kotlin | 1.9.24 |
| KSP | 1.9.24-1.0.20 |
| Min SDK / Target SDK | 24 / 34 (compileSdk 34) |

### Option 1: Install the APK directly
Download the ready-to-use app here: [APK_LINK](https://drive.google.com/drive/folders/1alj8WvEpoZR2CXjt60fXv6808mB1KOJp?usp=drive_link), then install it on your Android device (enable *Install from unknown sources* if prompted).

### Option 2: Build from source

**1. Clone the repository**
```bash
git clone https://github.com/Adam-Nurwahid/GithubUser.git
cd GithubUser
```
Alternatively, download the ZIP via **Code → Download ZIP** on GitHub and extract it.

**2. Set up `local.properties` (REQUIRED)**

The app uses a **GitHub Personal Access Token** to call the GitHub API (to avoid rate limiting). The token is read from `local.properties` in the project root, so it is **never committed to the repository**.

- Download `local.properties` from this link: [LOCAL_PROPERTIES_LINK](https://drive.google.com/drive/folders/1alj8WvEpoZR2CXjt60fXv6808mB1KOJp?usp=drive_link)
- Place it in the **project root folder** (next to `build.gradle.kts` and `settings.gradle.kts`).
- Or create it manually:

```properties
sdk.dir=/path/to/Android/Sdk
github.token=ghp_xxxxxxxxxxxxxxxxxxxx
```

> ✏️ Make sure the key name (`github.token`) exactly matches what the app module's `build.gradle.kts` reads.

> 🔐 **Creating your own token:** GitHub → Settings → Developer settings → Personal access tokens. No extra scopes are needed for public data.

**3. Open in Android Studio**
- `File → Open` and select the project folder.
- Wait for **Gradle Sync** to finish.
- Make sure the **Gradle JDK** matches the requirements (`Settings → Build, Execution, Deployment → Build Tools → Gradle`).

**4. Build & Run**
- Select an emulator or physical device and click ▶️ **Run 'app'**, or use the terminal:
```bash
./gradlew assembleDebug
./gradlew installDebug
```

**5. Run tests**
```bash
./gradlew test                    # Unit tests
./gradlew connectedAndroidTest    # UI tests (requires an emulator/device)
```

---

## Features & Nice to Have

### Core Features
- 🔍 **Search** GitHub users by username
- 👤 **Detail** screen for user profiles
- 💾 **Local caching / favorites** using Room

### Additional Features
- ⭐ **Favorites**: save and browse favorite users offline
- 🌗 **Dark/Light theme**: switch themes from the Settings page
- 🚪 **Exit button** on the Settings page

### Improvements
- 🎨 **UI/UX**: refined look and feel with a consistent theme and color palette

### Nice-to-Haves Implemented
 
| Item | Description |
|------|-------------|
| **Chucker** | Lets you inspect HTTP requests and responses directly on the device via a notification, with no Logcat needed. Added with `debugImplementation(libs.chucker)` and `releaseImplementation(libs.chucker.no.op)`, so the `no-op` variant is used in release builds and nothing leaks to production. Very useful for debugging endpoints and responses. |
| **Version Catalog** | All dependencies and versions are centralized in `gradle/libs.versions.toml`. This keeps versions consistent, makes updates easy, and gives type-safe accessors (`libs.xxx`) across modules. |
| **Moshi** | A lightweight, Kotlin-friendly JSON library. Used as the Retrofit converter (`converter-moshi`) with **KSP codegen** (`moshi.generateAdapter`), so adapters are generated at compile time instead of using reflection. |
| **Unit Testing** | Tests ViewModel/Repository logic in isolation using JUnit, MockK, Coroutines Test, and Arch Core Testing (`InstantTaskExecutorRule`). Located in `app/src/test`. |
| **UI Testing** | Verifies screen flows and user interactions (e.g. search and navigation) with Espresso and AndroidX Test. Located in `app/src/androidTest`. |
 
 
---

 
## 🧗 Challenges & Trade-offs
 
### 1. Hilt, KSP, and Gradle/Java version compatibility
- **Challenge:** Version conflicts between Hilt, KSP, Kotlin, the Android Gradle Plugin, and the Java/JDK version caused sync and build errors, as well as annotation processing failures.
- **Solution:** Aligned all component versions: KSP `1.9.24-1.0.20` matches Kotlin `1.9.24`, Hilt `2.51.1` runs on KSP, and Java/Kotlin target is set to 17 to match the JDK. I relied on the official documentation, Stack Overflow, and AI assistance to analyze the errors and adjust the configuration. All versions were then centralized in the Version Catalog to keep them consistent.
### 2. UI elements too large or not visible
- **Challenge:** Some UI elements were oversized or hidden on certain screen sizes.
- **Solution:** Adjusted the layouts (sizes, constraints, margins/padding) and tested them on multiple screen sizes/emulators.
### Trade-offs
- Chose **MVVM** over full Clean Architecture to keep the codebase simple and proportional to the app's scale.
- _(Add other trade-offs here, e.g. features that were out of scope.)_
---

## Author

**Adam Nurwahid** — [GitHub](https://github.com/Adam-Nurwahid)
