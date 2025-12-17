# 🔧 Level 7 - Compilation Fix Guide

## Issue Identified

The `TestListener.java` file had a **missing import** that caused compilation errors.

---

## ❌ Problem

```java
// Missing import!
public class TestListener implements TestWatcher, BeforeEachCallback, AfterEachCallback {
    
    @Override
    public void testDisabled(ExtensionContext context, java.util.Optional<String> reason) {
        // Using Optional without import
    }
}
```

**Error:** `Cannot resolve symbol 'Optional'`

---

## ✅ Solution

Add the missing import at the top of `TestListener.java`:

```java
package com.npci.training.listeners;

import com.npci.training.reporting.ExtentReportsManager;
import org.junit.jupiter.api.extension.*;

// ADD THIS LINE:
import java.util.Optional;

public class TestListener implements TestWatcher, BeforeEachCallback, AfterEachCallback {
    // ... rest of the code
}
```

---

## 🎯 What Was Fixed

### **TestListener.java:**

**Before (Missing Import):**
```java
package com.npci.training.listeners;

import com.npci.training.reporting.ExtentReportsManager;
import com.npci.training.utils.ScreenshotUtils;  // This was unused
import org.junit.jupiter.api.extension.*;

// Missing: import java.util.Optional;
```

**After (Fixed):**
```java
package com.npci.training.listeners;

import com.npci.training.reporting.ExtentReportsManager;
import org.junit.jupiter.api.extension.*;

import java.util.Optional;  // ADDED ✅
```

**Changes:**
1. ✅ Added `import java.util.Optional;`
2. ✅ Removed unused `import com.npci.training.utils.ScreenshotUtils;`

---

## 🚀 How to Use Fixed Version

### **Option 1: Download Fixed Package**
```bash
# Extract the fixed version
unzip playwright-level7-fixed.zip

# Build
cd playwright-level7-fixed
mvn clean compile

# Run tests
mvn test
```

### **Option 2: Manual Fix in IntelliJ**

1. Open `TestListener.java`
2. Add this line after other imports:
   ```java
   import java.util.Optional;
   ```
3. Save file
4. IntelliJ will auto-compile ✅

---

## 📝 Complete Fixed TestListener.java

```java
package com.npci.training.listeners;

import com.npci.training.reporting.ExtentReportsManager;
import org.junit.jupiter.api.extension.*;

import java.util.Optional;

/**
 * TestListener - JUnit 5 Extension for test lifecycle
 */
public class TestListener implements TestWatcher, BeforeEachCallback, AfterEachCallback {
    
    private static ThreadLocal<Long> startTime = new ThreadLocal<>();
    
    @Override
    public void beforeEach(ExtensionContext context) {
        startTime.set(System.currentTimeMillis());
        
        String testName = context.getDisplayName();
        String className = context.getTestClass().map(Class::getSimpleName).orElse("Unknown");
        
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║  STARTING: " + testName);
        System.out.println("╚════════════════════════════════════════════╝");
        
        ExtentReportsManager.createTest(
            className + " - " + testName,
            "Test: " + testName
        );
        
        ExtentReportsManager.logInfo("Test started: " + testName);
    }
    
    @Override
    public void afterEach(ExtensionContext context) {
        long duration = System.currentTimeMillis() - startTime.get();
        String testName = context.getDisplayName();
        
        System.out.println("Duration: " + duration + "ms");
        System.out.println("╚════════════════════════════════════════════╝\n");
        
        ExtentReportsManager.logInfo("Test completed in " + duration + "ms");
    }
    
    @Override
    public void testSuccessful(ExtensionContext context) {
        String testName = context.getDisplayName();
        System.out.println("✅ PASSED: " + testName);
        
        ExtentReportsManager.logPass("Test passed successfully");
    }
    
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        String testName = context.getDisplayName();
        System.out.println("❌ FAILED: " + testName);
        System.out.println("Reason: " + cause.getMessage());
        
        ExtentReportsManager.logFail("Test failed: " + cause.getMessage());
    }
    
    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        String testName = context.getDisplayName();
        System.out.println("⚠️  ABORTED: " + testName);
        
        ExtentReportsManager.logWarning("Test aborted: " + cause.getMessage());
    }
    
    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        String testName = context.getDisplayName();
        System.out.println("⏭️  SKIPPED: " + testName);
        
        ExtentReportsManager.logSkip("Test disabled: " + reason.orElse("No reason"));
    }
}
```

---

## ✅ Verify Fix

After applying the fix, verify compilation:

```bash
# Clean and compile
mvn clean compile

# Should see:
# [INFO] BUILD SUCCESS
```

If still getting errors, make sure:
1. ✅ All imports are present
2. ✅ No typos in package names
3. ✅ Maven dependencies downloaded (`mvn dependency:resolve`)

---

## 🎉 Fixed!

Your Level 7 project should now compile successfully!

Run tests with:
```bash
mvn test
```

View report:
```bash
open reports/ExtentReport_*.html
```

---

**Happy Testing!** 🚀
