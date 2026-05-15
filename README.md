# SpendLite 💸

A minimal personal expense tracker for Android built with Jetpack Compose.

## Screenshots

<img width="310" height="681" alt="Screenshot 2026-05-15 at 21 31 55" src="https://github.com/user-attachments/assets/a4881cae-0b31-482d-a0c7-de13f218e435" />
<img width="313" height="688" alt="Screenshot 2026-05-15 at 21 32 15" src="https://github.com/user-attachments/assets/b550b641-655d-4aba-8399-c2055bfd4bad" />


## Features

- Add expenses with title, amount, category, and date
- View all expenses on the home screen with monthly total
- Filter by All / Income / Expense / Pending
- Swipe gesture to navigate back from Add screen
- Clean dark UI with teal accent theme

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose |
| Architecture | MVVM (Model-View-ViewModel) |
| Navigation | Jetpack Navigation Compose |
| State Management | `mutableStateOf`, `mutableStateListOf` |
| Build | Gradle (Kotlin DSL) |

## Architecture

The app follows **MVVM** architecture:

- **Model** — `Expense` data class holds expense data
- **ViewModel** — `ViewModelApp` manages all UI state and business logic; shared across screens via a single instance in `MainActivity`
- **View** — `MainScreen` and `AddScreen` composables observe ViewModel state and react to changes

