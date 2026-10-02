# GithubUser

An Android app for searching GitHub users and viewing their profiles, built with Kotlin and the Gradle Kotlin DSL.

> ⚠️ **Draft README.** Items marked with `TODO` should be checked against the actual code and adjusted before submission.

---

## Features

- 🔍 Search GitHub users by username
- 👤 User detail page (avatar, name, username, followers, following, repositories)
- 👥 Followers and Following lists (tabbed)
- ⭐ Favorite users stored locally (TODO: remove if not implemented)
- 🌙 Dark / Light theme setting (TODO: remove if not implemented)
- ⏳ Loading state and error handling for network requests

## Tech Stack

| Area | Technology |
| --- | --- |
| Language | Kotlin |
| Build system | Gradle (Kotlin DSL) |
| Architecture | MVVM (TODO: confirm) |
| Networking | Retrofit + OkHttp + Gson/Moshi (TODO: confirm) |
| Async | Kotlin Coroutines / LiveData / Flow (TODO: confirm) |
| Local storage | Room, DataStore (TODO: confirm) |
| Image loading | Glide / Coil (TODO: confirm) |
| UI | XML + ViewBinding (TODO: or Jetpack Compose) |

## Getting Started

### Prerequisites

- Android Studio (latest stable)
- JDK 17 or newer
- Android SDK with a device or emulator running API 24+ (TODO: match `minSdk` in `app/build.gradle.kts`)

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/Adam-Nurwahid/GithubUser.git
   ```
2. Open the project in Android Studio and let Gradle sync.
3. Get a GitHub Personal Access Token from **GitHub → Settings → Developer settings → Personal access tokens**. No special scopes are needed for public data.
4. Add the token to `local.properties` (this file is git-ignored):
   ```properties
   API_KEY=your_github_token_here
   ```
   (TODO: adjust the key name to match how the token is read in `build.gradle.kts`.)
5. Run the app on an emulator or a physical device.

## Architecture

The app follows the **MVVM (Model–View–ViewModel)** pattern with a **Repository** layer.

```
UI (Activity / Fragment)
        │  observes state
        ▼
   ViewModel
        │  calls
        ▼
   Repository
     ├── Remote: Retrofit (GitHub REST API)
     └── Local:  Room / DataStore
```

### Why MVVM?

- **Separation of concerns**: UI code only renders state; business logic lives in the ViewModel and Repository.
- **Lifecycle awareness**: ViewModels survive configuration changes such as screen rotation, so search results are not reloaded needlessly.
- **Testability**: ViewModels and Repositories have no direct dependency on Android views, so they can be unit tested.
- **Official recommendation**: it is the architecture recommended by Google for modern Android apps, and it works naturally with LiveData / StateFlow, Room, and Retrofit.
- **Scalability**: adding features such as favorites or settings only adds new ViewModels and Repository methods without touching existing UI code.

## Additional Features and Improvements

*(Edit to match what you actually built.)*

- Dedicated loading indicator and error messages for network failures or empty results
- Local favorite list so users can revisit profiles without searching again
- Theme switching persisted with DataStore
- Followers / Following shown in tabs with ViewPager2
- Handling of configuration changes without losing state

## Challenges Encountered

*(Replace the examples below with your real experience; reviewers value honest detail.)*

1. **Handling API rate limits**: GitHub limits unauthenticated requests, so a personal access token is used and kept out of version control.
2. **State management on rotation**: moving state into the ViewModel prevented duplicate API calls when the screen was recreated.
3. **Displaying loading and error states correctly**: a single UI state model per screen made transitions between loading, success, and error predictable.
4. **Keeping local and remote data in sync**: favorite status had to be checked against the local database whenever a detail page was opened.

## Project Structure

```
GithubUser/
├── app/
│   └── src/main/
│       ├── java/.../
│       │   ├── data/       # remote (Retrofit), local (Room), repository
│       │   ├── ui/         # activities, fragments, adapters, viewmodels
│       │   └── utils/
│       └── res/
├── gradle/
├── build.gradle.kts
└── settings.gradle.kts
```

*(TODO: update the package layout to match the real folders.)*

## Screenshots

| Home / Search | Detail | Favorites |
| --- | --- | --- |
| *(add image)* | *(add image)* | *(add image)* |

## Author

**Adam Nurwahid**
GitHub: [@Adam-Nurwahid](https://github.com/Adam-Nurwahid)
