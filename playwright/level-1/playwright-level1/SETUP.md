# 🛠️ Playwright Setup Guide

## Complete Installation Instructions for NPCI Team

---

## 📋 Prerequisites

### **1. Java Development Kit (JDK)**
**Required:** Java 11 or higher

**Check Java version:**
```bash
java -version
```

**If not installed:**
- **Windows:** Download from https://adoptium.net/
- **macOS:** `brew install openjdk@11`
- **Linux:** `sudo apt install openjdk-11-jdk`

### **2. Maven**
**Required:** Maven 3.6+

**Check Maven version:**
```bash
mvn -version
```

**If not installed:**
- **Windows:** Download from https://maven.apache.org/download.cgi
- **macOS:** `brew install maven`
- **Linux:** `sudo apt install maven`

### **3. IDE (Recommended)**
- IntelliJ IDEA (Community Edition is free)
- Eclipse
- VS Code with Java extensions

---

## 🚀 Quick Setup (5 minutes)

### **Step 1: Extract Package**
```bash
unzip playwright-level1.zip
cd playwright-level1
```

### **Step 2: Install Playwright Browsers**
```bash
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
```

This command downloads:
- ✅ Chromium (~100MB)
- ✅ Firefox (~80MB)
- ✅ WebKit (~60MB)

**Note:** This is one-time setup. Browsers are installed globally.

### **Step 3: Verify Installation**
```bash
mvn test -Dtest=Test01_FirstPlaywrightScript#test01_LaunchBrowserAndNavigate
```

**Expected:** Browser launches, navigates to Google, test passes! ✅

---

## 📂 Import in IDE

### **IntelliJ IDEA**
1. Open IntelliJ IDEA
2. File → Open
3. Select `playwright-level1` folder
4. Wait for Maven import (downloads dependencies)
5. Right-click on any test → Run

### **Eclipse**
1. Open Eclipse
2. File → Import → Existing Maven Projects
3. Browse to `playwright-level1` folder
4. Finish
5. Right-click on test → Run As → JUnit Test

### **VS Code**
1. Open VS Code
2. File → Open Folder → Select `playwright-level1`
3. Install Java Extension Pack
4. Open test file → Click "Run Test" above test method

---

## 🌐 Browser Installation Details

### **Where are browsers installed?**
```
Windows: C:\Users\<username>\AppData\Local\ms-playwright
macOS: ~/Library/Caches/ms-playwright
Linux: ~/.cache/ms-playwright
```

### **Install specific browser:**
```bash
# Chromium only
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install chromium"

# Firefox only
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install firefox"

# WebKit only
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install webkit"
```

### **Verify browsers installed:**
```bash
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install --dry-run"
```

---

## 🔧 Configuration

### **pom.xml Dependencies**
Already configured in the package:
```xml
<!-- Playwright -->
<dependency>
    <groupId>com.microsoft.playwright</groupId>
    <artifactId>playwright</artifactId>
    <version>1.40.0</version>
</dependency>

<!-- JUnit 5 -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.10.1</version>
    <scope>test</scope>
</dependency>
```

### **Maven Surefire Plugin**
For running tests:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.2.2</version>
</plugin>
```

---

## 🧪 Running Tests

### **All tests in Level 1:**
```bash
mvn test
```

### **Specific test class:**
```bash
mvn test -Dtest=Test01_FirstPlaywrightScript
mvn test -Dtest=Test02_MultipleBrowsers
mvn test -Dtest=Test03_BrowserContext
mvn test -Dtest=Test04_BasicInteractions
mvn test -Dtest=Test05_UsingBaseTest
```

### **Specific test method:**
```bash
mvn test -Dtest=Test01_FirstPlaywrightScript#test01_LaunchBrowserAndNavigate
```

### **Multiple test classes:**
```bash
mvn test -Dtest=Test01*,Test02*
```

---

## 🐛 Troubleshooting

### **Problem 1: "Cannot resolve com.microsoft.playwright"**
**Solution:**
```bash
# Reload Maven dependencies
mvn clean install

# In IntelliJ: Right-click pom.xml → Maven → Reload Project
# In Eclipse: Right-click project → Maven → Update Project
```

### **Problem 2: "Browser not found"**
**Solution:**
```bash
# Reinstall browsers
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install --force"
```

### **Problem 3: "Java version error"**
**Solution:**
```bash
# Check Java version (needs 11+)
java -version

# Set JAVA_HOME
export JAVA_HOME=/path/to/jdk11
```

### **Problem 4: "Tests not visible in IDE"**
**Solution:**
- Make sure JUnit 5 is in classpath
- In IntelliJ: File → Invalidate Caches → Restart
- In Eclipse: Clean and rebuild project

### **Problem 5: "Browser launches but test fails"**
**Solution:**
- Check internet connection (some tests use real websites)
- Increase timeout if network is slow
- Check firewall/antivirus settings

---

## 🌍 Network & Proxy Settings

### **If behind corporate proxy:**

**Option 1: Maven settings.xml**
```xml
<!-- ~/.m2/settings.xml -->
<settings>
  <proxies>
    <proxy>
      <host>proxy.company.com</host>
      <port>8080</port>
      <username>user</username>
      <password>password</password>
    </proxy>
  </proxies>
</settings>
```

**Option 2: Environment variables**
```bash
export HTTP_PROXY=http://proxy.company.com:8080
export HTTPS_PROXY=http://proxy.company.com:8080
```

---

## 💻 System Requirements

### **Minimum:**
- **RAM:** 4GB
- **Disk Space:** 2GB (for browsers)
- **OS:** Windows 10+, macOS 10.15+, Linux (any recent distro)
- **Internet:** Required for initial browser download

### **Recommended:**
- **RAM:** 8GB+
- **Disk Space:** 5GB+
- **SSD:** For faster test execution
- **Internet:** Stable connection for web tests

---

## 📊 Project Structure

```
playwright-level1/
├── pom.xml                      # Maven configuration
├── README.md                    # Main documentation
├── SETUP.md                     # This file
└── src/
    ├── main/
    │   ├── java/                # (Empty for Level 1)
    │   └── resources/           # (Empty for Level 1)
    └── test/
        ├── java/com/npci/training/
        │   ├── tests/
        │   │   └── BaseTest.java        # Base test class
        │   └── level1/
        │       ├── Test01_FirstPlaywrightScript.java
        │       ├── Test02_MultipleBrowsers.java
        │       ├── Test03_BrowserContext.java
        │       ├── Test04_BasicInteractions.java
        │       └── Test05_UsingBaseTest.java
        └── resources/           # (Empty for Level 1)
```

---

## 🎯 Verification Checklist

After setup, verify everything works:

- [ ] Java 11+ installed and in PATH
- [ ] Maven 3.6+ installed and in PATH
- [ ] Project opens in IDE without errors
- [ ] Maven dependencies downloaded successfully
- [ ] Playwright browsers installed
- [ ] First test runs successfully
- [ ] Browser launches visibly

**Run this command to verify all:**
```bash
java -version && mvn -version && mvn test -Dtest=Test01_FirstPlaywrightScript#test01_LaunchBrowserAndNavigate
```

---

## 🚀 Quick Test Commands

```bash
# 1. Verify Java & Maven
java -version
mvn -version

# 2. Install browsers
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"

# 3. Run first test
mvn test -Dtest=Test01_FirstPlaywrightScript#test01_LaunchBrowserAndNavigate

# 4. Run all Level 1 tests
mvn clean test
```

---

## 🎓 Training Setup (for Trainers)

### **Before Training Session:**
1. Ensure all participants have Java 11+ and Maven installed
2. Share playwright-level1.zip with participants
3. Walk through setup together
4. Verify everyone can run first test
5. Troubleshoot any issues before starting training

### **Training Environment:**
- Projector/screen sharing for demos
- All participants should run tests locally
- Hands-on practice after each topic
- Q&A after each test class

---

## 📞 Support

### **If you encounter issues:**
1. Check this troubleshooting guide
2. Search Playwright documentation: https://playwright.dev/java/
3. Check GitHub issues: https://github.com/microsoft/playwright-java/issues
4. Ask in team Slack channel
5. Contact training instructor

---

## ✅ Setup Complete!

If you've completed all steps and can run tests successfully:

**🎉 Congratulations! You're ready to start learning Playwright!**

Proceed to **README.md** for training content.

---

**Happy Setup!** 🛠️
