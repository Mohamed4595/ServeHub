# 🍽️ ServeHub

**ServeHub** is a modern mobile application that connects customers and restaurant owners in one seamless platform.
It allows users to browse restaurant menus, add items to a cart, and contact restaurants directly — while enabling owners to manage their menus easily and efficiently.

---

## 🚀 Features

### 👤 Customer Features

* Browse restaurants list
* View restaurant details and menu
* Add items to cart
* Smart cart (single restaurant only)
* Call restaurant directly

### 👨‍🍳 Owner Features

* Create and manage restaurant
* Add / edit / delete menu items
* Control pricing and content

---

## 🧠 Architecture

The project is built using:

* **Kotlin Compose Multiplatform**
* **MVI (Model–View–Intent)**
* **Clean Architecture**
* **Decompose (Navigation)**

### 📦 Project Structure

```text
project-root/
 ├── shared/
 │    ├── domain/
 │    ├── data/
 │    ├── presentation/
 │
 ├── androidApp/
 ├── iosApp/
```

---

## 🔐 Authentication

* Email & Password login (initial)
* Structured to support OTP (Firebase) later

---

## 🛒 Cart System

* Cart is tied to **one restaurant only**
* If user adds item from another restaurant:

  * Confirmation dialog appears
* Supports:

  * Quantity update
  * Item removal
  * Total calculation

---

## 🧭 Navigation Flow

```text
Splash
 → Login
 → (Based on role)
    → Customer Flow
    → Owner Dashboard
```

---

## 🎨 UI/UX Design

* Minimal & modern design
* Clean layout with intuitive navigation
* Responsive components

### 🎨 Color Palette

* Primary: `#FF6B35`
* Secondary: `#1E1E1E`
* Background: `#F8F8F8`
* Accent: `#4CAF50`
* Error: `#E53935`

---

## 🧩 Tech Stack

* Kotlin Multiplatform
* Compose Multiplatform UI
* Firebase (planned)

  * Authentication
  * Firestore
* Decompose Navigation

---

## 📞 Call Feature

* Android → `Intent.ACTION_DIAL`
* iOS → `tel://`

---

## 🛠️ Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/your-username/servehub.git
cd servehub
```

---

### 2. Run Android App

```bash
./gradlew :androidApp:installDebug
```

---

### 3. Run iOS App

Open the `iosApp` in Xcode and run the project.

---

## 🧪 Development Strategy

The project is built incrementally:

1. Project setup & navigation
2. Authentication (mock → real)
3. Restaurant listing
4. Cart system
5. Owner features
6. Firebase integration

---

## 🔒 Permissions

* Only restaurant owners can modify their menus
* Ownership is verified via `ownerId`

---

## 💡 Future Improvements

* 🔍 Search & filters
* 🌙 Dark mode
* 💳 Online payments
* 🔔 Notifications
* 📍 Location-based restaurants (GPS)

---

## ✨ Vision

**ServeHub aims to simplify how restaurants present their menus and how customers interact with them — all in one seamless mobile experience.**
## Kotlin Multiplatform Architecture (Android + iOS)

- The project uses Kotlin Multiplatform to share code between Android and iOS.
- Shared code resides in /composeApp with commonMain, androidMain, and iosMain source sets, while /iosApp hosts the iOS app entry point (SwiftUI/Swift).
- Android-specific code continues to live under the androidMain source set of the shared module, and resource/manifest concerns are aligned accordingly.
