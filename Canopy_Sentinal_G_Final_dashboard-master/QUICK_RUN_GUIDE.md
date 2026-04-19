# Quick Run Guide - Canopy Sentinel App

## 🚀 Running the App - Step by Step

### Step 1: Check Prerequisites ✅

- [ ] Android Studio is open
- [ ] Project is synced (no Gradle errors)
- [ ] Device/Emulator is connected

### Step 2: Set Up Google Maps API Key (Optional but Recommended)

**The app will run without this, but maps won't work.**

1. Open `app/src/main/res/values/strings.xml` (you're already here!)
2. Find line 16: `<string name="maps_api_key">YOUR_MAPS_API_KEY_HERE</string>`
3. Replace `YOUR_MAPS_API_KEY_HERE` with your actual Google Maps API key
4. If you don't have one yet, you can run the app anyway - just maps won't display

**To get a Google Maps API Key:**
- Go to [Google Cloud Console](https://console.cloud.google.com/)
- Create/Select project
- Enable "Maps SDK for Android"
- Create API Key
- Copy and paste it in strings.xml

### Step 3: Build the Project

1. **Clean Project** (recommended):
   - Menu: `Build → Clean Project`
   - Wait for it to complete

2. **Rebuild Project**:
   - Menu: `Build → Rebuild Project`
   - This ensures everything compiles correctly

### Step 4: Connect Device/Emulator

**Option A: Physical Device**
- Enable USB Debugging on your Android device
- Connect via USB
- Allow USB debugging when prompted

**Option B: Emulator**
- Tools → Device Manager
- Create Virtual Device (if needed)
- Select device with API 26+ (Android 8.0+)
- Click ▶️ to start emulator

### Step 5: Run the App

1. **Select Run Configuration:**
   - Top toolbar: Ensure "app" is selected
   - Build variant: "debug" (default)

2. **Click Run Button:**
   - Click the green ▶️ Run button (top toolbar)
   - OR Press `Shift + F10`
   - OR Right-click `app` → Run 'app'

3. **Select Target:**
   - Choose your connected device/emulator
   - Click OK

### Step 6: What to Expect

**First Launch:**
- App installs on device
- **AuthActivity** opens first (login screen)
- You'll see:
  - App logo/icon
  - "Canopy Sentinel" title
  - "Continue as Guest" button
  - "Sign in with Google" button

**After Tapping "Continue as Guest":**
- MainActivity opens
- Bottom navigation appears
- Home screen shows:
  - Forest statistics cards
  - Recent alerts list
  - Country selector

**Navigation:**
- Tap bottom nav items to switch screens:
  - 🏠 Home
  - 🗺️ Map (will show error if no API key)
  - 📊 Analytics
  - 🚨 Alerts
  - 👤 Profile

## ⚠️ Common Issues & Solutions

### Issue: "Build Failed"
**Solution:**
```
1. Build → Clean Project
2. Build → Rebuild Project
3. File → Sync Project with Gradle Files
```

### Issue: "Installation Failed"
**Solution:**
```
1. Uninstall old version: Settings → Apps → Canopy Sentinel → Uninstall
2. Try running again
```

### Issue: "Maps Not Showing"
**Solution:**
```
1. Check API key in strings.xml
2. Verify API key is correct
3. Check Google Cloud Console - Maps SDK enabled
4. Check device internet connection
```

### Issue: "App Crashes on Launch"
**Solution:**
```
1. Check Logcat for error messages
2. Verify all dependencies synced
3. Clean and rebuild
4. Check AndroidManifest.xml
```

### Issue: "Cannot resolve MainActivity"
**Solution:**
```
- There are two MainActivity files (Java and Kotlin)
- The Kotlin version should be used
- If issues persist, delete MainActivity.java
```

## 📱 Testing the App

### Test Guest Mode:
1. Launch app
2. Tap "Continue as Guest"
3. Should navigate to main screen
4. Check all bottom nav items work

### Test Navigation:
- Tap each bottom nav item
- Verify screens switch correctly
- Check back button behavior

### Test Features:
- **Home**: View stats cards
- **Map**: View satellite map (if API key set)
- **Alerts**: Scroll through alerts list
- **Profile**: View user info

## 🐛 Debugging Tips

### View Logs:
- Bottom panel → Logcat tab
- Filter by "Canopy" or "MainActivity"
- Look for red errors

### Check Build Output:
- Bottom panel → Build tab
- Look for warnings/errors

### Verify Installation:
- Device → Settings → Apps → Canopy Sentinel
- Check version and permissions

## ✅ Success Indicators

You'll know it's working when:
- ✅ App installs successfully
- ✅ AuthActivity opens on launch
- ✅ Can tap "Continue as Guest"
- ✅ Main screen loads with stats
- ✅ Bottom navigation works
- ✅ Can switch between screens
- ✅ No crashes

## 🎯 Quick Checklist

Before Running:
- [ ] Gradle sync completed
- [ ] No red errors in Project view
- [ ] Device/Emulator connected
- [ ] Build → Make Project succeeds

After Running:
- [ ] App installs
- [ ] AuthActivity opens
- [ ] Can navigate to main screen
- [ ] Bottom nav works
- [ ] No crashes

---

**Ready to Run!** 🚀

Just click the Run button (▶️) or press `Shift + F10`!
