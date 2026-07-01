# مدير مهام الأسرة — Parent-Child Task Manager

A complete Kotlin Android project for a parent-child task management system with gamification.

## Quick Start

1. Open **Android Studio** (Hedgehog or newer)
2. Choose **Open** → select the `android-app/` folder
3. Wait for Gradle sync to finish (~2 minutes first time)
4. Run on an emulator or device (minSdk 24 / Android 7+)

> The gradle wrapper JAR is fetched automatically by Android Studio on first sync.
> If it prompts "Trust Gradle settings?" — click **Trust**.

---

## Project Structure

```
app/src/main/kotlin/com/parentchild/taskmanager/
├── data/
│   ├── model/          ← Task, GameProgress, Character, Titles, BedtimeState
│   ├── local/          ← SharedPrefsManager (all persistence)
│   └── repository/     ← TaskRepo, ProgressRepo, BedtimeRepo
├── ui/
│   ├── role/           ← RoleSelectionActivity (first launch)
│   ├── mother/         ← MotherDashboardActivity, AddTaskActivity, ChildProgressActivity
│   └── child/          ← ChildHomeActivity, BedtimeActivity
├── viewmodel/          ← MotherViewModel, ChildViewModel (MVVM)
└── utils/              ← Constants, Extensions, AlarmHelper, BedtimeService
```

---

## Features Implemented

### 👩 Mother Side
- [x] Dashboard with task list & live progress stats
- [x] Add task (title + description) → sent to child
- [x] Mark task as completed (awards 1 star)
- [x] Delete tasks
- [x] View child progress: stars, level, progress bar, unlocked characters
- [x] Activate/deactivate Bedtime Mode

### 👦 Child Side
- [x] Home screen with big centered task card
- [x] Task always visible until completed
- [x] Snooze button (10 min, once per task) with live countdown
- [x] Task reappears automatically after snooze

### 🎮 Gamification
- [x] 1 completed task = 1 star ⭐
- [x] Every 5 stars = level up
- [x] Level-up dialog (new character + new Arabic title)
- [x] 6 unlockable characters (SVG vector drawables)
- [x] 10 Arabic motivational titles (levels 1–10)

### 🌙 Bedtime Mode
- [x] First 5 min: soft reminder screen with countdown
- [x] After 5 min: full dark bedtime screen
- [x] Back button blocked during active bedtime
- [x] Mother can dismiss from the reminder screen

---

## Arabic Titles (10 levels)
| Level | Title |
|-------|-------|
| 1 | المبتدئ الشجاع |
| 2 | المتعلم المثابر |
| 3 | النشيط الصغير |
| 4 | الطالب المجتهد |
| 5 | المنجز الماهر |
| 6 | المحارب الذكي |
| 7 | النجم الصاعد |
| 8 | البطل الحقيقي |
| 9 | المتميز دائماً |
| 10 | الأسطورة الخالدة |

---

## Characters (6 unlockable)
| ID | Name (AR) | Emoji | Unlock at Level |
|----|-----------|-------|-----------------|
| 1 | المبتدئ | 🐣 | 1 |
| 2 | المتعلم | 🐥 | 2 |
| 3 | النشيط | 🦊 | 3 |
| 4 | المجتهد | 🦁 | 5 |
| 5 | البطل | 🦅 | 7 |
| 6 | الأسطورة | 🔥 | 10 |

---

## Firebase Migration Guide

The architecture is designed for a clean Firebase swap:

### Step 1 — Add Firebase
1. Go to [console.firebase.google.com](https://console.firebase.google.com)
2. Create project → Add Android app → package `com.parentchild.taskmanager`
3. Download `google-services.json` → place in `app/`
4. Uncomment the Firebase lines in:
   - `gradle/libs.versions.toml`
   - `app/build.gradle.kts`

### Step 2 — Replace repositories
Each repository (`TaskRepository`, `ProgressRepository`, `BedtimeRepository`) has a comment at the top:
```
// Firebase migration path: ...
```
Follow those comments to inject `FirebaseFirestore` and replace SharedPrefs calls with Firestore reads/writes.

### Step 3 — Add Auth
- Inject `FirebaseAuth` into `SharedPrefsManager` or a new `AuthRepository`
- Replace the role-selection screen with proper sign-in (mother gets a PIN/password, child gets a simpler flow)
- User UIDs become the document keys in Firestore: `users/{uid}/tasks`, `users/{uid}/progress`

### Step 4 — Real-time sync
- Replace `List<Task>` returns with `Flow<List<Task>>` using `snapshotListeners`
- Mother marks complete → Firestore updates → child's screen updates instantly via `observeForever`

### Step 5 — Push notifications (FCM)
- Uncomment the `FCMService` entry in `AndroidManifest.xml`
- Create `utils/FCMService.kt` extending `FirebaseMessagingService`
- Server (Cloud Functions) triggers a push when mother sends a task or activates bedtime

---

## Tech Stack
- **Language**: Kotlin
- **Min SDK**: 24 (Android 7.0)
- **Architecture**: MVVM (ViewModel + LiveData)
- **Storage**: SharedPreferences + Gson (swap-ready for Firebase)
- **UI**: Material Components 3, ViewBinding
- **Async**: CountDownTimer, AlarmManager (WorkManager-ready)
- **Build**: Gradle 8.7, AGP 8.4.2, Kotlin 1.9.24
