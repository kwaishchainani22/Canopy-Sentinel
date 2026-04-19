# 🛰️ Canopy Sentinel 2.0: Satellite-Based Deforestation Monitoring & Carbon Analysis

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Android-API_24+-green.svg)](https://developer.android.com/)
[![License](https://img.shields.io/badge/License-Apache_2.0-orange.svg)](https://opensource.org/licenses/Apache-2.0)

**Canopy Sentinel** is an Android-based environmental intelligence platform built to monitor global deforestation using satellite data. The application not only detects forest degradation but also estimates the **carbon impact** caused due to deforestation.

With an integrated **Carbon Analysis System**, users can visualize forest conditions, track environmental changes over time, and understand the economic and ecological significance of forest resources.

---

## 🚀 Key Features

### 🌍 Live Forest Monitoring

Interactive map-based visualization of forest regions highlighting deforested areas using satellite data overlays.

### 💰 Carbon Impact Estimation

Calculates approximate carbon loss based on forest degradation and provides insight into environmental impact.

### 🕒 Historical Data Analysis

Allows users to compare current forest conditions with past data to observe trends and changes over time.

### ⚡ Real-Time Alerts

Displays alerts for newly detected deforestation zones with severity indicators for better decision-making.

### 📊 Data Visualization & Reports

Generates visual insights such as graphs and reports to help understand forest health and carbon metrics.

---

## 🛠️ Tech Stack & Tools

### 🔹 Kotlin

Used as the primary programming language for building a modern, efficient, and safe Android application.

### 🔹 MVVM Architecture

Ensures clean separation between UI and business logic, making the app scalable and maintainable.

### 🔹 osmdroid

Provides map rendering and supports integration of satellite imagery for location-based visualization.

### 🔹 MPAndroidChart

Used to create interactive graphs and charts for representing environmental data clearly.

### 🔹 Android DataStore

Handles local data storage securely for user preferences and session data.

---

## 📦 Installation & Setup

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/kwaishchainani22/Canopy-Sentinel.git
```

### 2️⃣ Open in Android Studio

* Launch Android Studio
* Select **Open Project**
* Choose the project folder

### 3️⃣ Sync Dependencies

* Allow Gradle to sync automatically
* Ensure stable internet connection

### 4️⃣ Run the Application

* Connect an Android device or use emulator (API 24+)
* Click **Run ▶️** or press `Shift + F10`

---

## 👥 Team Members

This project was developed as part of a collaborative team effort:

* **Kwaish Chainani** – Maintainer / Repository Owner
* Parv Kajla 
* Pari Rastogi 
* Vayu Nandan Tripathi 
* Anshul Pandey

---

## 📂 Project Structure

```text
app/src/main/java/com/erc/canopysentinalg/
├── data/
│   ├── model/
│   └── repository/
├── ui/
│   ├── adapter/
│   ├── map/
│   ├── analytics/
│   └── viewmodel/
└── MainActivity.kt
```

---

