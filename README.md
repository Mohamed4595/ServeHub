# ServeHub

ServeHub is a Kotlin Multiplatform food ordering app prototype built with Compose for Android and a shared KMP module for app state, navigation, auth flow, and restaurant/cart logic.

## Features

- Splash and login flow
- Customer home, restaurant details, and cart screens
- Owner dashboard, restaurant creation, and menu management
- Fake auth with role-based routing
- Shared cart state with cross-restaurant replacement confirmation
- Android-specific direct call integration

## Tech Stack

- Kotlin Multiplatform
- Jetpack Compose
- Decompose
- Gradle Kotlin DSL

## Project Structure

- `shared`: shared models, repositories, login MVI, root navigation/state
- `androidApp`: Android Compose UI and platform integrations
- `iosApp`: iOS host project scaffold

## Build

Use the Gradle wrapper from the project root:

```bash
./gradlew build
```
