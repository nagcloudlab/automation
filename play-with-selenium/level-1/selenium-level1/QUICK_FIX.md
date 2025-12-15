# 🔧 QUICK FIX - Compilation Errors

## Problem You're Seeing

IntelliJ showing errors like:
```
Cannot resolve symbol 'suite'
Cannot resolve symbol 'Suite'
Cannot resolve symbol 'SelectClasses'
```

## Root Cause

❌ **Old test files** mixed with new files  
❌ **Wrong package structure** (old: `com.npci.selenium.level1`)  
✅ **Correct package:** `com.npci.training.level1`

## ✅ SOLUTION: Use This Clean Package

**This package contains ONLY the correct files:**

```
selenium-level1-clean/
├── pom.xml
├── README.md
├── EXERCISES.md
├── QUICK_FIX.md (this file)
└── src/
    ├── main/java/com/npci/training/utils/
    │   └── TestUtils.java                    ✓ CORRECT
    └── test/java/com/npci/training/level1/
        ├── Test01_FirstScript.java           ✓ CORRECT
        ├── Test02_LocatingElements.java      ✓ CORRECT
        ├── Test03_BasicInteractions.java     ✓ CORRECT
        ├── Test04_CompleteLoginTests.java    ✓ CORRECT
        └── Test05_UsingUtilityClass.java     ✓ CORRECT
```

**Total: 5 test files + 1 utility = 6 Java files**

**NO old files!** ✓

---

## 🚀 Fresh Start (2 Minutes)

### Step 1: Close Current Project
```
In IntelliJ:
File → Close Project
```

### Step 2: Delete Old Project
```bash
# Delete the old selenium-level1 folder
# (or just remove from IntelliJ)
```

### Step 3: Import Clean Project
```
1. Extract selenium-level1-clean.zip
2. In IntelliJ: File → Open
3. Select selenium-level1-clean folder
4. Wait for Maven to sync
```

### Step 4: Verify Clean Import
```
Check Project structure:
- Should see ONLY com.npci.training packages
- Should see ONLY Test01-Test05 files
- NO com.npci.selenium packages
- NO LoginTestSuite.java
- NO Script files
```

### Step 5: Run First Test
```bash
mvn test -Dtest=Test01_FirstScript
```

**Should work perfectly!** ✓

---

## 🎯 Quick Verification Checklist

Open IntelliJ and verify:

- [ ] No compilation errors
- [ ] Only 5 test files visible (Test01-Test05)
- [ ] Package is `com.npci.training.level1`
- [ ] TestUtils.java in `com.npci.training.utils`
- [ ] Can run Test01_FirstScript
- [ ] Green checkmarks everywhere!

---

## 📁 File Count Verification

**You should see EXACTLY:**
- 5 test files in `src/test/java/com/npci/training/level1/`
- 1 util file in `src/main/java/com/npci/training/utils/`
- 1 pom.xml
- 3 documentation files (README, EXERCISES, QUICK_FIX)

**If you see more files → WRONG PACKAGE!**

---

## ⚡ Run All Tests

```bash
# All tests should pass
mvn clean test

# Expected output:
Tests run: 17, Failures: 0, Errors: 0, Skipped: 0
```

---

## 🔍 Why This Happened

The first package accidentally included:
- Old test files from a previous session
- Mixed package structures
- Suite files that aren't needed for Level 1

**This clean package has ONLY what you need!**

---

## 💡 If Still Seeing Errors

### 1. Maven Reload
```
In IntelliJ:
Right-click pom.xml → Maven → Reload Project
```

### 2. Invalidate Caches
```
File → Invalidate Caches → Invalidate and Restart
```

### 3. Reimport
```
File → Close Project
Delete .idea folder
Re-open project
```

### 4. Check Java Version
```bash
java -version
# Should be 11 or higher
```

---

## ✅ Success Indicators

**You know it's working when:**

1. ✓ No red underlines in code
2. ✓ Can run Test01 with green checkmark
3. ✓ Browser opens during test
4. ✓ Test completes successfully
5. ✓ Can run `mvn test` from terminal

---

## 🎓 Ready to Learn!

**Once this is working:**
1. Start with Test01_FirstScript
2. Read the code
3. Run it
4. Understand what happened
5. Move to Test02
6. Progress through all 5 tests
7. Complete exercises

---

## 📞 Quick Reference

**Project Structure:**
```
com.npci.training
├── level1/          (5 test files here)
└── utils/           (TestUtils.java here)
```

**Run Commands:**
```bash
mvn test -Dtest=Test01_FirstScript
mvn test -Dtest=Test02_LocatingElements
mvn test -Dtest=Test03_BasicInteractions
mvn test -Dtest=Test04_CompleteLoginTests
mvn test -Dtest=Test05_UsingUtilityClass
mvn test  # Run all
```

---

## 🎉 You're Good to Go!

**This clean package is ready for training!**

Extract → Import → Run → Learn! 🚀
