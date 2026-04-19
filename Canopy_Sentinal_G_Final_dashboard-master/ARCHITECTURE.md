# Canopy Sentinel - Architecture Documentation

## Overview
This document provides a comprehensive overview of the Canopy Sentinel deforestation tracking app architecture, improvements, and implementation details.

## Architecture Pattern: MVVM (Model-View-ViewModel)

The app follows the **MVVM (Model-View-ViewModel)** architecture pattern, which provides:
- **Separation of Concerns**: Business logic separated from UI
- **Testability**: ViewModels can be unit tested independently
- **Lifecycle Awareness**: ViewModels survive configuration changes
- **Reactive UI**: LiveData/Flow automatically update UI when data changes

### Architecture Layers

```
┌─────────────────────────────────────┐
│         UI Layer (Fragments)        │
│  HomeFragment, MapFragment, etc.    │
└──────────────┬──────────────────────┘
               │ Observes
               ▼
┌─────────────────────────────────────┐
│      ViewModel Layer                 │
│  ForestViewModel, AuthViewModel      │
└──────────────┬──────────────────────┘
               │ Uses
               ▼
┌─────────────────────────────────────┐
│      Repository Layer                │
│  ForestDataRepository, AuthRepository│
└──────────────┬──────────────────────┘
               │ Uses
               ▼
┌─────────────────────────────────────┐
│      Data Layer                     │
│  Models, Data Sources, Cache        │
└─────────────────────────────────────┘
```

## Key Components

### 1. Data Models (`data/model/`)

#### `DeforestationAlert.kt`
- Represents a deforestation alert with location, area, severity, and NDVI values
- Parcelable for passing between activities/fragments
- Includes severity levels: LOW, MEDIUM, HIGH, CRITICAL

#### `ForestStats.kt`
- Aggregated forest statistics
- Calculated properties: healthyArea, deforestationRate
- Used for dashboard display

#### `Country.kt`
- Country information with forest statistics
- Parcelable for navigation
- Includes coordinates for map centering

#### `User.kt`
- User model supporting both guest and authenticated users
- Stores name, email, photo URL

#### `TimeRange.kt`
- Enum for historical data time ranges
- Supports 1 month to 5 years, plus all-time

### 2. Repository Layer (`data/repository/`)

#### `ForestDataRepository.kt`
- Single source of truth for forest data
- Uses Flow for reactive data streams
- Methods:
  - `getForestStats(countryCode)`: Get aggregated statistics
  - `getAlerts(countryCode, limit)`: Get deforestation alerts
  - `getCountries()`: Get list of tracked countries
  - `getHistoricalData(countryCode, timeRange)`: Get historical trends

#### `AuthRepository.kt`
- Manages authentication state
- Uses DataStore for persistent storage
- Methods:
  - `signInAsGuest()`: Create guest user session
  - `signInWithGoogle(user)`: Authenticate with Google
  - `signOut()`: Clear session
  - `getCurrentUserFlow()`: Observe current user state

### 3. ViewModel Layer (`ui/viewmodel/`)

#### `ForestViewModel.kt`
- Manages forest data state
- Exposes LiveData for UI observation
- Handles country selection and time range filtering
- Provides refresh functionality

#### `AuthViewModel.kt`
- Manages authentication state
- Handles guest and Google sign-in
- Observes user state changes

### 4. UI Layer (`ui/`)

#### Fragments
- **HomeFragment**: Dashboard with stats cards and recent alerts
- **MapFragment**: Interactive satellite map with markers
- **AnalyticsFragment**: Charts and insights (placeholder)
- **AlertsFragment**: List of all deforestation alerts
- **ProfileFragment**: User profile and settings

#### Activities
- **AuthActivity**: Authentication screen (launcher activity)
- **MainActivity**: Main app container with bottom navigation

## Key Features Implemented

### 1. Authentication System

#### Guest Mode
- Users can explore app without signing in
- Limited features (can be extended)
- Session persists using DataStore

#### Google Sign-In
- Optional Google authentication
- Stores user profile information
- Uses Google Sign-In API

### 2. Navigation

#### Bottom Navigation
- 5 main sections: Home, Map, Analytics, Alerts, Profile
- Uses Navigation Component for fragment management
- Smooth transitions between screens

#### Navigation Graph
- Centralized navigation configuration
- Type-safe navigation with arguments
- Deep linking support ready

### 3. Material Design 3

#### Theming
- Dynamic color support ready
- Light/Dark mode support
- Consistent shape and elevation

#### Components
- MaterialCardView with elevation
- MaterialButton with rounded corners
- BottomNavigationView with proper theming

### 4. Satellite Map Integration

#### Google Maps
- Satellite view mode
- Custom markers for alerts
- Camera animations
- Time slider for historical data (UI ready)

#### Map Features
- Heatmap overlay support (ready for implementation)
- Country-based filtering
- Alert clustering (can be added)

### 5. Multi-Country Tracking

#### Country Selector
- List of 10 major forest countries
- Country-specific statistics
- Map centering on selection

#### Data Filtering
- Filter alerts by country
- Country-specific forest stats
- Historical data by country

### 6. Performance Optimizations

#### Caching Strategy
- Room database ready for implementation
- DataStore for preferences
- Image caching with Glide

#### API Optimization
- Flow-based reactive streams
- Lazy loading of data
- Efficient data structures

### 7. Accessibility Features

#### Dark Mode
- Theme supports DayNight mode
- Automatic switching based on system settings

#### Large Text Support
- Scalable text sizes
- Proper text contrast ratios

#### Screen Reader Friendly
- Content descriptions on all images
- Semantic labels
- Proper focus management

## Code Structure

```
app/src/main/java/com/erc/canopysentinalg/
├── data/
│   ├── model/              # Data models
│   ├── repository/          # Repository implementations
│   └── MockSatelliteDataProvider.kt
├── ui/
│   ├── auth/               # Authentication UI
│   ├── home/               # Home screen
│   ├── map/                # Map screen
│   ├── analytics/          # Analytics screen
│   ├── alerts/             # Alerts screen
│   ├── profile/            # Profile screen
│   └── viewmodel/          # ViewModels
└── MainActivity.kt         # Main activity
```

## Dependencies

### Core
- **Kotlin**: 1.9.22
- **AndroidX Core**: 1.12.0
- **Material Components**: 1.11.0

### Architecture Components
- **Lifecycle**: 2.7.0
- **Navigation**: 2.7.6
- **Room**: 2.6.1 (ready for caching)
- **DataStore**: 1.0.0

### Maps & Location
- **Google Maps**: 18.2.0
- **Google Location Services**: 21.1.0

### Authentication
- **Google Sign-In**: 20.7.0

### Networking
- **Retrofit**: 2.9.0
- **OkHttp**: 4.12.0

### Image Loading
- **Glide**: 4.16.0

## Future Enhancements

### 1. Real API Integration
- Replace MockSatelliteDataProvider with real API calls
- Implement Retrofit interfaces
- Add error handling and retry logic

### 2. Offline Support
- Implement Room database for caching
- Sync data when online
- Show cached data when offline

### 3. Advanced Map Features
- Heatmap overlays for deforestation density
- Time slider for historical visualization
- Custom map styling
- Route navigation for field users

### 4. Analytics Dashboard
- Charts using MPAndroidChart or similar
- Trend analysis
- Export functionality

### 5. Push Notifications
- Real-time alert notifications
- Country-specific alerts
- Customizable notification preferences

### 6. User Features
- Save favorite locations
- Share alerts
- Report false positives
- Export data

## Testing Strategy

### Unit Tests
- ViewModel logic
- Repository data transformations
- Model calculations

### Integration Tests
- Repository + DataSource
- ViewModel + Repository

### UI Tests
- Fragment navigation
- User interactions
- Authentication flows

## Best Practices Implemented

1. **Single Responsibility**: Each class has one clear purpose
2. **Dependency Injection**: Ready for Hilt/Koin integration
3. **Reactive Programming**: Flow/LiveData for data streams
4. **Error Handling**: Try-catch blocks with user-friendly messages
5. **Resource Management**: Proper lifecycle management
6. **Code Organization**: Clear package structure
7. **Documentation**: Inline comments and this documentation

## Setup Instructions

1. **Get Google Maps API Key**
   - Add your API key to `strings.xml`: `maps_api_key`
   - Enable Maps SDK for Android in Google Cloud Console

2. **Configure Google Sign-In**
   - Add SHA-1 fingerprint to Firebase Console
   - Configure OAuth consent screen

3. **Build and Run**
   - Sync Gradle files
   - Build project
   - Run on device or emulator

## Notes

- The app currently uses mock data for demonstration
- Replace `MockSatelliteDataProvider` with real API integration
- Add proper error handling for network failures
- Implement Room database for offline support
- Add comprehensive unit and UI tests
