# 📚 LEVEL 1 TEACHING NOTES - Selenium Basics

## TRAINER: NPCI Training Program
## DURATION: 3-4 hours (1 day session)
## AUDIENCE: Fresh employees, No prior Selenium experience

---

## 🎯 LEARNING OBJECTIVES

By the end of Level 1, students will be able to:
1. Set up Selenium project with Maven
2. Write their first automated test
3. Use basic locators (ID, Name, Class, Tag, Link Text)
4. Interact with web elements (click, sendKeys, getText)
5. Handle basic alerts
6. Run tests with JUnit 5
7. Debug common issues

---

## 📋 PRE-SESSION CHECKLIST

**One Week Before:**
- [ ] Send installation guide to students
- [ ] Verify all students have Java 11+ installed
- [ ] Verify Maven installation
- [ ] Share Banking Portal zip file
- [ ] Test that portal runs on localhost:8000

**Day Before:**
- [ ] Test all demos on your machine
- [ ] Prepare backup laptops (2-3)
- [ ] Print quick reference cards
- [ ] Set up projector/screen sharing

**Morning Of:**
- [ ] Start Banking Portal on localhost:8000
- [ ] Open IntelliJ IDEA
- [ ] Have selenium-level1.zip ready
- [ ] Test internet connection (for Maven downloads)

---

## 📖 SESSION STRUCTURE (240 minutes)

### **PART 1: Introduction (45 minutes)**

**09:00 - 09:15 | Welcome & Overview (15 min)**
```
Script:
"Good morning! Welcome to NPCI Selenium Training. Over the next few weeks,
you'll learn professional test automation. Today is Level 1 - the foundation.

By the end of today, you'll write your first automated test. It will open
a browser, login to our banking portal, and verify success - all automatically!"

Show:
1. Demo of final working test (run Test05)
2. Show browser automating login
3. "This is what you'll build today"
```

**Key Points to Emphasize:**
- ✅ Automation saves time (100 manual tests → 5 minutes automated)
- ✅ More reliable than humans (no fatigue, no mistakes)
- ✅ Run tests 24/7 (overnight, weekends)
- ✅ NPCI specific: Critical for UPI payment testing

**09:15 - 09:30 | What is Selenium? (15 min)**
```
Whiteboard:
Draw this diagram:

    [Your Test Code]
           ↓
    [Selenium WebDriver]
           ↓
    [Browser Driver (ChromeDriver)]
           ↓
    [Chrome Browser]
           ↓
    [Banking Portal Website]
```

**Explain:**
- Selenium = Tool to control browsers programmatically
- WebDriver = API that controls browsers
- ChromeDriver = Translator between Selenium and Chrome
- We write Java code, Selenium executes it in browser

**Common Questions:**
Q: "Can we use Selenium with any browser?"
A: "Yes! Chrome, Firefox, Edge, Safari - all supported."

Q: "Is Selenium only for testing?"
A: "Mainly testing, but also for web scraping, automation tasks."

**09:30 - 09:45 | Setup Verification (15 min)**
```
Hands-On Activity:

"Let's verify everyone's setup. Open terminal/command prompt:"

Windows:
  java -version    (should show 11 or higher)
  mvn -version     (should show Maven 3.x)

Mac:
  java -version
  mvn -version

Troubleshooting:
- Java not found → Install JDK 11
- Maven not found → Install Maven, set PATH
- Wrong version → Upgrade
```

**Checkpoint:** Everyone should see Java and Maven versions

---

### **PART 2: First Test (60 minutes)**

**09:45 - 10:00 | Project Setup (15 min)**
```
Live Demo (students follow along):

1. Extract selenium-level1.zip
2. Import into IntelliJ:
   File → Open → Select selenium-level1 folder
   Trust the project
   Wait for Maven to download dependencies (2-3 minutes)

3. Show project structure:
   src/test/java/com/npci/training/level1/
   ├── Test01_FirstSeleniumTest.java
   ├── Test02_BasicLocators.java
   └── ...

4. Open Test01_FirstSeleniumTest.java
```

**Common Issues:**
- Maven not importing → Reimport (right-click pom.xml → Maven → Reimport)
- Red squiggly lines → Dependencies still downloading, wait
- Can't find main class → Mark src/test/java as Test Sources Root

**10:00 - 10:15 | Understanding Test Structure (15 min)**
```
Open Test01_FirstSeleniumTest.java

Explain line by line:

@BeforeEach
public void setup() {
    // This runs BEFORE each test
    // Sets up browser
}

@Test
public void testOpenBrowser() {
    // This is actual test
    // One test = one scenario
}

@AfterEach
public void teardown() {
    // This runs AFTER each test
    // Closes browser
}
```

**Analogy:**
"Think of @BeforeEach as 'preparations before cooking'
@Test as 'cooking the meal'
@AfterEach as 'cleaning up the kitchen'"

**10:15 - 10:30 | Run First Test (15 min)**
```
Live Demo:

1. Right-click on testOpenBrowser()
2. Click "Run 'testOpenBrowser()'"
3. Watch:
   - Chrome browser opens
   - Navigates to login page
   - Browser closes
4. Check output:
   "Tests run: 1, Failures: 0" ✅

Student Activity:
"Now you try! Run the test on your machine."

Walk around, help students individually.
```

**Expected Issues:**
- "Browser doesn't open" → WebDriverManager downloading driver, wait
- "Port 8000 not available" → Banking portal not running
- "Chrome version mismatch" → WebDriverManager will handle it

**10:30 - 10:45 | Break (15 min)**
☕ Coffee break

---

### **PART 3: Locators & Interactions (75 minutes)**

**10:45 - 11:15 | Understanding Locators (30 min)**
```
Open Test02_BasicLocators.java

Whiteboard:
"How does Selenium find elements on a page?"

Show login page in browser:
- Username field has id="username"
- Password field has id="password"
- Login button has id="loginBtn"

Explain:
By.id("username")  ← Most reliable, use whenever possible
By.name("username")  ← Good for form fields
By.className("btn-primary")  ← For elements with CSS classes
By.tagName("button")  ← Too generic, avoid
By.linkText("Register")  ← Perfect for links
```

**Live Demo:**
```java
// Show in browser DevTools (F12):
<input type="text" id="username" name="username">

// In Selenium:
driver.findElement(By.id("username"))
driver.findElement(By.name("username"))
// Both find same element!
```

**Hands-On Exercise (10 min):**
"Find the password field using:"
1. By.id
2. By.name
3. By.tagName

**11:15 - 11:45 | Element Interactions (30 min)**
```
Open Test03_BasicInteractions.java

Demo each interaction:

1. sendKeys() - Type text
   driver.findElement(By.id("username")).sendKeys("admin");
   
2. click() - Click element
   driver.findElement(By.id("loginBtn")).click();
   
3. clear() - Clear text
   element.clear();
   
4. getText() - Get text
   String text = element.getText();
   
5. getAttribute() - Get attribute
   String value = element.getAttribute("value");

Run test, show each interaction happening.
```

**Important Teaching Point:**
"Notice the pattern:
1. Find element: driver.findElement(By.id("username"))
2. Do something: .sendKeys("admin")

Always two steps!"

**11:45 - 12:00 | Complete Login (15 min)**
```
Open Test04_CompleteLoginTests.java

Live Code (students type along):

@Test
public void testValidLogin() throws InterruptedException {
    // Step 1: Enter username
    driver.findElement(By.id("username")).sendKeys("admin");
    System.out.println("✓ Username entered");
    
    // Step 2: Enter password
    driver.findElement(By.id("password")).sendKeys("admin123");
    System.out.println("✓ Password entered");
    
    // Step 3: Select user type
    driver.findElement(By.id("userType")).click();
    driver.findElement(By.cssSelector("option[value='customer']")).click();
    System.out.println("✓ User type selected");
    
    // Step 4: Accept terms
    driver.findElement(By.id("terms")).click();
    System.out.println("✓ Terms accepted");
    
    // Step 5: Click login
    driver.findElement(By.id("loginBtn")).click();
    System.out.println("✓ Login clicked");
    
    Thread.sleep(2000);
    
    // Step 6: Handle alert
    Alert alert = driver.switchTo().alert();
    System.out.println("Alert text: " + alert.getText());
    alert.accept();
    
    // Step 7: Verify
    assertTrue(driver.getCurrentUrl().contains("dashboard.html"));
    System.out.println("✓ Login successful!");
}
```

Run test together, celebrate when it works! 🎉

---

### **PART 4: Lunch Break (60 minutes)**
🍽️ 12:00 - 13:00

---

### **PART 5: Practice & Troubleshooting (60 minutes)**

**13:00 - 13:30 | Guided Practice (30 min)**
```
Student Exercise:

"Create a test that:"
1. Opens the login page
2. Enters WRONG credentials
3. Clicks login
4. Verifies we're still on login page (not dashboard)

Hints:
- Use Test04 as reference
- Username: "wrong"
- Password: "wrong"
- Expected: Stay on login.html
```

**Walk around, provide help:**
- Check their code
- Run their tests
- Debug issues together

**13:30 - 14:00 | Common Issues & Debugging (30 min)**
```
Whiteboard: "Top 10 Selenium Mistakes"

1. NoSuchElementException
   Cause: Element not found
   Fix: Check locator, add wait, inspect element
   
2. ElementNotInteractableException
   Cause: Element not visible/enabled
   Fix: Wait for element, check if hidden
   
3. StaleElementReferenceException
   Cause: Page refreshed, element changed
   Fix: Re-find element after page change
   
4. TimeoutException
   Cause: Element didn't appear in time
   Fix: Increase wait time, check if element exists
   
5. Alert not found
   Cause: No alert present
   Fix: Check if alert appears, add try-catch

Live Demo: Cause each error, show how to fix.
```

**Debugging Tips:**
```
Add this to every test:

Thread.sleep(5000);  // Pause to see what's happening

System.out.println("Current URL: " + driver.getCurrentUrl());
System.out.println("Page title: " + driver.getTitle());
```

---

### **PART 6: Wrap-up & Assessment (30 minutes)**

**14:00 - 14:20 | Quick Assessment (20 min)**
```
Mini Quiz (verbal, interactive):

1. "What does WebDriver do?"
   Answer: Controls the browser

2. "What's the best locator strategy?"
   Answer: ID (most reliable)

3. "How do you click an element?"
   Answer: element.click()

4. "How do you type text?"
   Answer: element.sendKeys("text")

5. "What does @BeforeEach do?"
   Answer: Runs before each test

Hands-On Check:
"Show me you can:"
1. Open browser ✓
2. Navigate to page ✓
3. Find element by ID ✓
4. Click button ✓
5. Get element text ✓
```

**14:20 - 14:30 | Homework & Next Steps (10 min)**
```
Homework Assignment:

"Complete the exercises in EXERCISES.md"

Easy Exercises (must complete):
1. Test login with different credentials
2. Test forgot password link
3. Test clear button

Challenge Exercises (optional):
4. Test all validation errors
5. Navigate through multiple pages

Due: Before Level 2 session

Next Session Preview:
"In Level 2, we'll learn:"
- CSS Selectors (powerful locators)
- XPath (even more powerful!)
- Multiple windows
- Working with tables

Questions?
```

---

## 🎯 KEY TEACHING POINTS

### **Must Emphasize:**

1. **Locator Priority:**
   ```
   ID > Name > CSS > XPath > LinkText > Tag
   Always use ID if available!
   ```

2. **Element Lifecycle:**
   ```
   1. Find element: driver.findElement(By.id("..."))
   2. Interact: element.click() or element.sendKeys()
   Always two steps!
   ```

3. **Wait for Elements:**
   ```
   Thread.sleep() is temporary
   Level 3 will teach proper waits
   ```

4. **Debug Early, Debug Often:**
   ```
   Add System.out.println() everywhere
   Use Thread.sleep() to see what's happening
   ```

---

## ⚠️ COMMON STUDENT MISTAKES

### **Mistake 1: Forgetting to start Banking Portal**
```
Error: "Connection refused"
Fix: "Did you start the portal? Run: python -m http.server 8000"
```

### **Mistake 2: Wrong locator**
```
Error: "NoSuchElementException"
Fix: "Open DevTools, inspect element, check the ID/name"
```

### **Mistake 3: Not importing classes**
```
Error: "Cannot resolve symbol"
Fix: "Alt+Enter to import, or add import statement"
```

### **Mistake 4: Copy-paste without understanding**
```
Symptom: Code works but student can't explain it
Fix: "Explain each line to me. What does this do?"
```

### **Mistake 5: Not reading error messages**
```
Student: "It doesn't work"
Trainer: "What does the error say?"
Student: "I don't know"
Fix: "Always read the error message! It tells you exactly what's wrong."
```

---

## 💡 TEACHING TIPS

### **For Different Learning Speeds:**

**Fast Learners:**
- Give challenge exercises early
- Ask them to help slower students
- "Try implementing Test06 yourself"

**Slow Learners:**
- Pair with fast learner
- Extra 1-on-1 time during breaks
- Simplify: "Just get the browser to open first"

**Struggling Students:**
- Check basic setup (Java, Maven)
- Start from Test01 only
- "Let's just run one test together"

### **Engagement Techniques:**

1. **Live Coding:** Type code together, not just showing slides
2. **Frequent Questions:** "What do you think this does?"
3. **Pair Programming:** Students work in pairs
4. **Celebrate Wins:** "Great! Your test passed! 🎉"
5. **Real Examples:** "This is how we test UPI payments at NPCI"

### **Time Management:**

- **Behind schedule?** Skip some demos, focus on hands-on
- **Ahead of schedule?** Do extra exercises, Q&A
- **Lost students?** Pause, recap, demo again

---

## 📊 SUCCESS CRITERIA

### **Student MUST be able to:**
- [ ] Set up Selenium project
- [ ] Write a basic test with @Test, @BeforeEach, @AfterEach
- [ ] Open browser and navigate to page
- [ ] Find elements using By.id() and By.name()
- [ ] Use sendKeys() and click()
- [ ] Run tests and read results

### **Student SHOULD be able to:**
- [ ] Use multiple locator types
- [ ] Handle alerts
- [ ] Complete login flow
- [ ] Debug common errors
- [ ] Explain what their code does

### **Nice to Have:**
- [ ] Use By.cssSelector() basics
- [ ] Create custom tests
- [ ] Help other students

---

## 📝 POST-SESSION CHECKLIST

**Immediately After:**
- [ ] Collect feedback (quick survey)
- [ ] Note which topics took longer
- [ ] Identify students who need extra help

**Same Day:**
- [ ] Upload session recording (if recorded)
- [ ] Share completed code examples
- [ ] Send reminder about homework

**Before Next Session:**
- [ ] Review homework submissions
- [ ] Prepare additional examples for weak topics
- [ ] Update teaching notes based on experience

---

## 🎤 SAMPLE INSTRUCTOR DIALOGUE

**Opening:**
> "Good morning everyone! Who here has tested software manually? [Show of hands]
> Great! Now imagine if you could make the computer do all that testing for you.
> That's what we're learning today. By the end of this session, you'll write code
> that opens a browser, fills in forms, clicks buttons - all automatically.
> Sound good? Let's start!"

**During Demo:**
> "Watch my screen. I'm going to type this code, and I want you to understand
> each line before you type it. Don't copy-paste. Type it yourself. 
> Ready? Let's go line by line..."

**When Students Struggle:**
> "It's okay if it doesn't work the first time. Everyone makes mistakes.
> Let's look at the error message together. What does it say?
> [Read error] Ah! It says 'element not found'. What does that mean?
> Right - our locator is wrong. Let's inspect the element in DevTools..."

**Celebrating Success:**
> "Excellent! Your test passed! Did you see the browser open automatically?
> That's the power of automation. Now imagine running 100 tests like this
> overnight while you sleep. That's what we do at NPCI!"

---

## 📚 ADDITIONAL RESOURCES

**For Students:**
- Selenium documentation: https://www.selenium.dev/documentation/
- Java basics refresher
- Banking Portal user guide
- Quick reference card (print and distribute)

**For Trainer:**
- Backup USB with all materials
- Troubleshooting guide (common errors)
- Extra exercises
- Assessment rubric

---

## ✅ FINAL CHECKLIST

**Before You Leave:**
- [ ] All students can run at least one test
- [ ] Everyone has homework assignment
- [ ] Level 2 date/time announced
- [ ] Questions answered
- [ ] Feedback collected

---

**Remember:** Your goal is not to make experts in one day. 
Your goal is to make them comfortable with Selenium basics and excited to learn more!

**Good luck with your training! 🚀**