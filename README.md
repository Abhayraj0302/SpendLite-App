# SpendLite 💸

A personal expense tracker for Android built with Jetpack Compose, backed by Firebase Auth and a local Room database synced to Firestore.

## 📱 Screenshots

<p align="center">
  <img src="https://github.com/user-attachments/assets/c498eeb9-acb8-403f-a742-86dc1e328f51" width="220" />
  <img src="https://github.com/user-attachments/assets/3abe7ff5-a743-4860-8e4e-6121997d71fc" width="220" />
  <img src="https://github.com/user-attachments/assets/a4881cae-0b31-482d-a0c7-de13f218e435" width="220" />
  <img src="https://github.com/user-attachments/assets/b550b641-655d-4aba-8399-c2055bfd4bad" width="220" />
</p>

## Features

- Email/password and Google Sign-In authentication via Firebase Auth
- Add transactions with title, amount, category, date, and type (credit/debit)
- Home screen with running balance and monthly total
- Filter transactions by All / Income / Expense
- Credited/debited color coding (teal for income, red for expenses)
- Offline-first: transactions persist locally in Room and sync to Firestore
- Clean dark UI with teal accent theme

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose |
| Architecture | Clean Architecture (feature-based) + MVVM |
| Dependency Injection | Hilt |
| Local Storage | Room |
| Remote Backend | Firebase Auth + Cloud Firestore |
| Navigation | Jetpack Navigation Compose |
| State Management | `StateFlow`, `Flow` |
| Build | Gradle (Kotlin DSL) |

## Architecture

The app follows a feature-based Clean Architecture, organized as:

- **`core/`** — shared UI components (`AppButton`, `AppTextField`, `TopBar`), utils (validation, date formatting, extensions)
- **`data/`**
  - `local/` — Room `ExpenseEntity`, DAO, and database
  - `remote/` — `FirebaseService` for auth and Firestore reads/writes
  - `repository/` — `ExpenseRepository` mediates between local DB and remote sync
  - `mapper/` — maps between `ExpenseEntity` (DB) and `Expense` (domain model)
- **`features/`** — one package per screen (`auth/signin`, `auth/signup`, `home`, `addexpense`), each with its own `Screen`, `ViewModel`, `State`, and `Event`
- **`di/`** — Hilt modules providing the database, DAO, and repository as singletons
- **`navigation/`** — `AppNavigation` and route definitions

Each screen owns its own `ViewModel` (via `@HiltViewModel`), and all screens share a single `ExpenseRepository` instance injected through Hilt — so state stays consistent across the app without manual instance-passing.
