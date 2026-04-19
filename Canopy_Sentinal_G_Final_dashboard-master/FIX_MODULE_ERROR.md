# Fix "Module Not Specified" Error

## Quick Fix Steps

### Method 1: Sync Gradle (Most Common Fix)

1. **File → Sync Project with Gradle Files**
   - Wait for sync to complete
   - Check bottom status bar for "Gradle sync finished"

2. **If that doesn't work:**
   - File → Invalidate Caches → Invalidate and Restart
   - Wait for Android Studio to restart
   - It will re-index the project

### Method 2: Check Run Configuration

1. **Click the Run Configuration dropdown** (top toolbar, next to Run button)
2. **Select "Edit Configurations..."**
3. **Check/Set:**
   - Name: `app`
   - Module: Select `app` from dropdown
   - If "app" is not in dropdown, see Method 3

### Method 3: Reimport Project

1. **Close Android Studio**
2. **Delete these folders** (if they exist):
   - `.idea` folder
   - `.gradle` folder (in project root)
   - `build` folders
3. **Reopen Android Studio**
4. **File → Open** → Select project folder
5. **Select "Import Gradle Project"** when prompted
6. **Wait for sync**

### Method 4: Manual Module Selection

1. **File → Project Structure** (or `Ctrl+Alt+Shift+S`)
2. **Check Modules:**
   - Should see `app` module listed
   - If not, click `+` → Import Module → Select `app` folder
3. **Click OK**

### Method 5: Check settings.gradle.kts

Verify the file contains:
```kotlin
rootProject.name = "Canopy Sentinal G"
include(":app")
```

## Step-by-Step Solution (Recommended)

**Follow these steps in order:**

### Step 1: Clean Everything
```
1. Build → Clean Project
2. File → Invalidate Caches → Invalidate and Restart
```

### Step 2: Sync Gradle
```
1. File → Sync Project with Gradle Files
2. Wait for completion (check bottom status bar)
```

### Step 3: Verify Module
```
1. File → Project Structure
2. Modules → Should see "app"
3. If missing, click "+" → Import Module → app folder
```

### Step 4: Set Run Configuration
```
1. Top toolbar → Click dropdown next to Run button
2. Select "Edit Configurations..."
3. Under "General" tab:
   - Module: Select "app" from dropdown
   - If "app" not listed, click "+" → Android App → Name: app → Module: app
4. Click OK
```

### Step 5: Try Running Again
```
1. Click Run button (▶️)
2. Should now work!
```

## Common Causes

1. **Gradle sync incomplete** - Most common
2. **Project not properly imported** - Need to reimport
3. **Run configuration missing module** - Need to set manually
4. **Corrupted .idea folder** - Need to delete and restart

## Verification Checklist

After following steps, verify:

- [ ] Gradle sync completed successfully
- [ ] Project Structure shows "app" module
- [ ] Run Configuration has "app" selected
- [ ] No red errors in Project view
- [ ] Can see `app` folder in Project view

## Still Not Working?

Try this nuclear option:

1. **Close Android Studio**
2. **Delete these folders:**
   ```
   .idea/
   .gradle/
   app/build/
   build/
   ```
3. **Reopen Android Studio**
4. **File → Open** → Select project root folder
5. **Select "Open as Gradle Project"**
6. **Wait for sync**
7. **File → Sync Project with Gradle Files**
8. **Try running again**
