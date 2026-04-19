# Canopy Sentinel - Setup Guide

## Prerequisites

- Android Studio Hedgehog or later
- JDK 11 or higher
- Android SDK 26+ (Android 8.0+)
- Google Maps API Key
- Google Sign-In configured (optional)

## Step 1: Clone and Open Project

1. Open Android Studio
2. Select "Open an Existing Project"
3. Navigate to the project directory
4. Wait for Gradle sync to complete

## Step 2: Configure Google Maps API

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing one
3. Enable "Maps SDK for Android"
4. Create credentials (API Key)
5. Restrict the API key to Android apps (recommended)
6. Open `app/src/main/res/values/strings.xml`
7. Replace `YOUR_MAPS_API_KEY_HERE` with your actual API key:

```xml
<string name="maps_api_key">YOUR_ACTUAL_API_KEY</string>
```

## Step 3: Configure Google Sign-In (Optional)

1. In Google Cloud Console, enable "Google Sign-In API"
2. Add your app's SHA-1 fingerprint:
   ```bash
   # For debug keystore
   keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
   ```
3. Add the SHA-1 to Firebase Console or Google Cloud Console
4. Download `google-services.json` if using Firebase
5. Place it in `app/` directory

## Step 4: Build and Run

1. Sync Gradle files (File → Sync Project with Gradle Files)
2. Connect an Android device or start an emulator
3. Click "Run" button or press Shift+F10
4. The app should launch with AuthActivity first

## Project Structure

```
app/src/main/
├── java/com/erc/canopysentinalg/
│   ├── data/
│   │   ├── model/          # Data models
│   │   ├── repository/     # Repository implementations
│   │   └── MockSatelliteDataProvider.kt
│   ├── ui/
│   │   ├── auth/           # Authentication
│   │   ├── home/           # Home screen
│   │   ├── map/            # Map screen
│   │   ├── analytics/      # Analytics screen
│   │   ├── alerts/         # Alerts screen
│   │   ├── profile/        # Profile screen
│   │   ├── viewmodel/      # ViewModels
│   │   └── adapter/        # RecyclerView adapters
│   └── MainActivity.kt
└── res/
    ├── layout/             # XML layouts
    ├── values/             # Strings, colors, themes
    ├── drawable/           # Icons and drawables
    ├── menu/               # Menu resources
    └── navigation/         # Navigation graph
```

## Key Features

### Authentication
- **Guest Mode**: Tap "Continue as Guest" to explore without login
- **Google Sign-In**: Tap "Sign in with Google" for full features

### Navigation
- **Bottom Navigation**: 5 main sections
  - Home: Dashboard with stats
  - Map: Satellite view with alerts
  - Analytics: Charts and insights
  - Alerts: List of all alerts
  - Profile: User settings

### Dashboard
- **Forest Statistics**: Total area, deforested area, health percentage
- **Recent Alerts**: Latest 5 deforestation alerts
- **Country Selector**: Filter by country

### Map Features
- **Satellite View**: Google Maps satellite imagery
- **Alert Markers**: Clickable markers for deforestation alerts
- **Time Slider**: UI ready for historical data (needs backend)

## Troubleshooting

### Maps Not Showing
- Verify API key is correct in `strings.xml`
- Check API key restrictions in Google Cloud Console
- Ensure Maps SDK is enabled

### Google Sign-In Not Working
- Verify SHA-1 fingerprint is added
- Check OAuth consent screen is configured
- Ensure Google Sign-In API is enabled

### Build Errors
- Clean project: Build → Clean Project
- Rebuild: Build → Rebuild Project
- Invalidate caches: File → Invalidate Caches

### Gradle Sync Issues
- Check internet connection
- Verify Gradle version compatibility
- Try: File → Sync Project with Gradle Files

## Development Notes

### Current Data Source
The app uses `MockSatelliteDataProvider` for demonstration. To integrate real API:

1. Create Retrofit interfaces in `data/api/`
2. Replace `MockSatelliteDataProvider` calls in `ForestDataRepository`
3. Add error handling and retry logic

### Adding New Features

#### Add New Screen
1. Create Fragment in `ui/[feature]/`
2. Create layout in `res/layout/fragment_[feature].xml`
3. Add to navigation graph
4. Add menu item if needed

#### Add New Data Model
1. Create data class in `data/model/`
2. Make Parcelable if needed for navigation
3. Update Repository if needed

#### Add New ViewModel
1. Extend `AndroidViewModel`
2. Use Repository for data
3. Expose LiveData/Flow for UI

## Testing

### Unit Tests
```bash
./gradlew test
```

### Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

## Dependencies

Key dependencies used:
- **Material Components**: 1.11.0
- **Navigation Component**: 2.7.6
- **Lifecycle**: 2.7.0
- **Room**: 2.6.1
- **Retrofit**: 2.9.0
- **Google Maps**: 18.2.0
- **Google Sign-In**: 20.7.0

## Performance Tips

1. **Image Loading**: Glide is configured for image caching
2. **Database**: Room is ready for offline caching
3. **Networking**: OkHttp with logging interceptor
4. **Memory**: Proper lifecycle management

## Security Notes

- Never commit API keys to version control
- Use `local.properties` for sensitive keys (not included)
- Restrict API keys in Google Cloud Console
- Use ProGuard/R8 for release builds

## Support

For issues or questions:
1. Check `ARCHITECTURE.md` for code structure
2. Review `IMPROVEMENTS_SUMMARY.md` for features
3. Check Android Studio logs for errors

## License

See LICENSE file for details.
