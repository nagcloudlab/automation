# 📚 LEVEL 2 TEACHING NOTES - Advanced Locators & Navigation

## TRAINER: NPCI Training Program
## DURATION: 3-4 hours (1 day session)
## PREREQUISITE: Level 1 completed
## AUDIENCE: Employees comfortable with basic Selenium

---

## 🎯 LEARNING OBJECTIVES

By the end of Level 2, students will be able to:
1. Write CSS Selectors for any element
2. Write XPath expressions for complex scenarios
3. Choose the appropriate locator strategy
4. Navigate between pages using browser commands
5. Handle multiple windows and tabs
6. Extract data from HTML tables
7. Apply locators to real-world scenarios

---

## 📋 PRE-SESSION CHECKLIST

**Before Session:**
- [ ] Review Level 1 homework submissions
- [ ] Identify students who struggled with Level 1
- [ ] Prepare extra examples for weak areas
- [ ] Test all Level 2 demos
- [ ] Print CSS/XPath cheat sheets

**Setup:**
- [ ] Banking Portal running on localhost:8000
- [ ] selenium-level2-FIXED.zip ready (use FIXED version!)
- [ ] Browser DevTools tutorial ready
- [ ] LOCATOR_GUIDE.md printed for each student

---

## 📖 SESSION STRUCTURE (240 minutes)

### **PART 1: Level 1 Recap & Introduction (30 minutes)**

**09:00 - 09:15 | Level 1 Quick Recap (15 min)**
```
Interactive Quiz:

Q1: "Show me By.id() on whiteboard"
Student writes: driver.findElement(By.id("username"))

Q2: "What's the problem with this code?"
driver.findElement(By.tagName("button")).click();
Answer: Too generic, might click wrong button!

Q3: "Show me complete login test structure"
Student should mention: @BeforeEach, @Test, @AfterEach

Review homework:
"Who completed all exercises? Great!
Who struggled? That's fine - we'll reinforce today."
```

**09:15 - 09:30 | Why Advanced Locators? (15 min)**
```
Demo Problem:

Show element in DevTools:
<button class="btn btn-primary btn-lg submit-button" data-action="submit">
  Submit Payment
</button>

Ask: "How would you find this with Level 1 knowledge?"
- By.id? No ID!
- By.name? No name!
- By.tagName? Too many buttons!
- By.className? Which class?

"This is why we need CSS Selectors and XPath!"

Show solution:
By.cssSelector(".btn-primary")  // CSS
By.xpath("//button[contains(@class, 'btn-primary')]")  // XPath

"Both work! But which is better? We'll learn today."
```

---

### **PART 2: CSS Selectors (75 minutes)**

**09:30 - 10:00 | CSS Selector Basics (30 min)**
```
Open Test01_CSSSelectors.java

Whiteboard:
"CSS Selector Patterns"

1. Tag selector
   button  → All buttons
   input   → All inputs
   
2. ID selector (#)
   #username  → Element with id="username"
   
3. Class selector (.)
   .btn-primary  → Elements with class="btn-primary"
   
4. Attribute selector []
   [type='password']  → Elements with type="password"

Live Demo in Browser DevTools:
1. Open login page
2. Press F12
3. Go to Console tab
4. Type: document.querySelectorAll('.btn-primary')
5. Show results
6. "This is what CSS selector does!"
```

**Teaching Tip:**
Use browser console to TEST selectors before writing Selenium code!

**10:00 - 10:30 | Advanced CSS Patterns (30 min)**
```
Hands-On Exercise:

"Open DevTools. Find the password field."

<input type="password" id="password" name="password" 
       placeholder="Enter password" class="form-control">

"Write 5 different CSS selectors for this:"

Student answers (on whiteboard):
1. #password                    // ID
2. [type='password']           // Attribute
3. input[type='password']      // Tag + Attribute
4. .form-control               // Class
5. input#password              // Tag + ID

"All correct! But which is BEST?"
Answer: #password (shortest, most specific)

Advanced Patterns:
6. [id^='pass']               // Starts with 'pass'
7. [id$='word']               // Ends with 'word'
8. [id*='ssw']                // Contains 'ssw'
9. input[placeholder*='password']  // Contains in attribute

Run Test01, show each selector working.
```

**10:30 - 10:45 | CSS Selector Challenge (15 min)**
```
Student Exercise (pairs):

"Find elements on the Banking Portal using CSS:"

1. Find all buttons → "button"
2. Find login button → "#loginBtn"
3. Find all inputs → "input"
4. Find password field → "[type='password']"
5. Find elements with 'form-group' class → ".form-group"
6. Find button with 'btn-primary' class → ".btn-primary"

Bonus Challenge:
7. Find second form-group → ".form-group:nth-child(2)"
8. Find first input inside form → "form > input:first-child"

Check answers:
Open DevTools Console:
$$("button")  // Should return array of buttons
$$(".btn-primary")  // Should return login button
```

**10:45 - 11:00 | Break (15 min)**

---

### **PART 3: XPath Mastery (75 minutes)**

**11:00 - 11:30 | XPath Fundamentals (30 min)**
```
Whiteboard:
"CSS vs XPath"

CSS: Fast, simple, modern
XPath: Powerful, can navigate parent, text-based

When to use XPath:
1. Need to go to PARENT element
2. Need to find by TEXT
3. Complex navigation needed

Basic XPath Syntax:
//tag[@attribute='value']

Examples:
//button[@id='loginBtn']
//input[@type='password']
//h1[text()='Banking Portal']

Live Demo in DevTools Console:
$x("//button[@id='loginBtn']")  // XPath in console
```

**11:30 - 12:00 | XPath Functions (30 min)**
```
Open Test02_XPathSelectors.java

Demonstrate each function:

1. text()
   //button[text()='Login']
   "Finds button with EXACT text 'Login'"
   
2. contains()
   //button[contains(@id, 'login')]
   //h1[contains(text(), 'Banking')]
   "Finds elements CONTAINING text/attribute"
   
3. starts-with()
   //input[starts-with(@id, 'user')]
   "Finds elements STARTING with value"

4. Parent axis
   //input[@id='username']/parent::div
   "Goes UP to parent element"
   
5. Following-sibling
   //div[1]/following-sibling::div[1]
   "Finds NEXT sibling"

Run each test, pause to show browser action.
```

**Common Student Question:**
Q: "When do I use CSS vs XPath?"
A: "Use CSS for 90% of cases (faster). Use XPath when you need parent navigation or text-based finding."

**12:00 - 12:15 | XPath Axes Deep Dive (15 min)**
```
Whiteboard:
Draw DOM tree:

<form>
  <div class="form-group">        ← parent
    <label>Username</label>       ← preceding-sibling
    <input id="username">         ← CURRENT
  </div>
  <div class="form-group">        ← following-sibling
    <input id="password">
  </div>
</form>

Show XPath for each relationship:
//input[@id='username']/parent::div
//input[@id='username']/parent::div/preceding-sibling::label
//input[@id='username']/parent::div/following-sibling::div

Student Exercise:
"Write XPath to find password field starting from username field"
Answer: //input[@id='username']/parent::div/following-sibling::div//input
```

---

### **PART 4: Lunch Break (60 minutes)**
🍽️ 12:15 - 13:15

---

### **PART 5: Navigation & Windows (60 minutes)**

**13:15 - 13:45 | Browser Navigation Commands (30 min)**
```
Open Test04_NavigationCommands.java

Demo each command:

1. navigate().to()
   driver.navigate().to("http://localhost:8000/login.html");
   "Navigate to URL - maintains history"
   
2. navigate().back()
   driver.navigate().back();
   "Click browser back button"
   
3. navigate().forward()
   driver.navigate().forward();
   "Click browser forward button"
   
4. navigate().refresh()
   driver.navigate().refresh();
   "Refresh page (F5)"

Live Demo:
1. Navigate to login
2. Navigate to register
3. Go back
4. Go forward
5. Refresh

"Just like you clicking browser buttons!"

get() vs navigate().to():
driver.get(url)           // Simple, direct
driver.navigate().to(url) // With history, can go back
```

**13:45 - 14:15 | Multiple Windows (30 min)**
```
Open Test05_MultipleWindows.java

Concept:
"Windows have unique IDs called 'handles'"

Show code:
// Get main window handle
String mainWindow = driver.getWindowHandle();

// Open new window (click link)
driver.findElement(By.linkText("Open New")).click();

// Get all window handles
Set<String> allWindows = driver.getWindowHandles();

// Switch to new window
for (String window : allWindows) {
    if (!window.equals(mainWindow)) {
        driver.switchTo().window(window);
        break;
    }
}

// Close new window
driver.close();

// Switch back to main
driver.switchTo().window(mainWindow);

Demo this live - students should see windows opening/closing.

Best Practice Pattern:
ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
driver.switchTo().window(tabs.get(1));  // Switch to 2nd tab
```

**Common Issue:**
Students forget to switch back to main window!
Symptom: NoSuchWindowException
Fix: Always switch back: driver.switchTo().window(mainWindow)

---

### **PART 6: Working with Tables (45 minutes)**

**14:15 - 15:00 | Table Operations (45 min)**
```
Open Test06_WorkingWithTables.java

Show transaction table on dashboard:
<table id="transactionTable">
  <thead>
    <tr><th>ID</th><th>Date</th><th>Amount</th></tr>
  </thead>
  <tbody>
    <tr><td>TXN001</td><td>2024-01-15</td><td>₹500</td></tr>
    <tr><td>TXN002</td><td>2024-01-16</td><td>₹750</td></tr>
  </tbody>
</table>

Operations:

1. Get row count
   List<WebElement> rows = driver.findElements(
       By.cssSelector("#transactionTable tbody tr")
   );
   int count = rows.size();
   
2. Get specific cell
   WebElement cell = driver.findElement(
       By.cssSelector("#transactionTable tbody tr:nth-child(1) td:nth-child(2)")
   );
   String date = cell.getText();
   
3. Iterate all rows
   for (WebElement row : rows) {
       List<WebElement> cells = row.findElements(By.tagName("td"));
       String id = cells.get(0).getText();
       String date = cells.get(1).getText();
       String amount = cells.get(2).getText();
       System.out.println(id + " | " + date + " | " + amount);
   }

4. Find row by text (XPath!)
   WebElement row = driver.findElement(
       By.xpath("//td[contains(text(), 'TXN001')]/parent::tr")
   );

5. Click button in row
   driver.findElement(
       By.cssSelector("#transactionTable tbody tr:nth-child(1) button")
   ).click();

Run test, show each operation.

Student Exercise:
"Calculate total amount from all transactions"
```

**Solution:**
```java
double total = 0.0;
for (WebElement row : rows) {
    List<WebElement> cells = row.findElements(By.tagName("td"));
    String amountText = cells.get(3).getText()
                             .replace("₹", "")
                             .replace(",", "");
    total += Double.parseDouble(amountText);
}
System.out.println("Total: ₹" + total);
```

---

### **PART 7: Practice & Assessment (30 minutes)**

**15:00 - 15:20 | Comprehensive Exercise (20 min)**
```
Final Challenge:

"Write a test that:"
1. Opens login page
2. Logs in using CSS selectors ONLY
3. Navigates to Transactions using linkText
4. Counts transactions using table operations
5. Goes back to dashboard using navigation
6. Logs out

Constraints:
- Use CSS selectors for login form
- Use linkText for navigation
- Use table CSS for counting

Give students 20 minutes.
Walk around, provide hints.
```

**15:20 - 15:30 | Quick Assessment (10 min)**
```
Rapid Fire Questions:

1. "CSS for element with id='test'" → #test
2. "CSS for class 'button'" → .button
3. "XPath for button with text 'Submit'" → //button[text()='Submit']
4. "XPath with contains()" → //div[contains(@class, 'error')]
5. "Navigate back in browser" → driver.navigate().back()
6. "Get all window handles" → driver.getWindowHandles()
7. "Get table row count" → driver.findElements(By.cssSelector("tbody tr")).size()

Hands-On Check:
"Show me you can write CSS selector for any element I point to"
[Point to various elements on page]
```

---

## 🎯 KEY TEACHING POINTS

### **CSS Selector Priority:**
```
1. ID: #id                          ⭐⭐⭐ Best
2. Class: .class                    ⭐⭐⭐ Good
3. Attribute: [type='password']     ⭐⭐⭐ Good
4. Combination: button.btn-primary  ⭐⭐ OK
5. Tag: button                      ⭐ Avoid
```

### **XPath Priority:**
```
1. Attribute: //button[@id='login']       ⭐⭐⭐ Good
2. Text: //button[text()='Submit']        ⭐⭐⭐ Good
3. Contains: //div[contains(@class, 'x')] ⭐⭐ OK
4. Complex axes: //div/parent::form       ⭐ Use carefully
```

### **When to Use What:**
```
Simple element with ID → CSS (#id)
Simple element with class → CSS (.class)
Find by text → XPath (text())
Navigate to parent → XPath (parent::)
Most other cases → CSS (faster)
```

---

## ⚠️ COMMON MISTAKES

### **Mistake 1: Using absolute XPath**
```
❌ /html/body/div/form/input  // Fragile!
✅ //input[@id='username']    // Better
```

### **Mistake 2: Forgetting to switch windows**
```
❌ 
driver.findElement(By.id("newWindowButton")).click();
driver.findElement(By.id("elementInNewWindow"));  // Fails!

✅ 
driver.findElement(By.id("newWindowButton")).click();
driver.switchTo().window(newWindowHandle);  // Must switch!
driver.findElement(By.id("elementInNewWindow"));
```

### **Mistake 3: Wrong table indexing**
```
❌ tr:nth-child(0)  // CSS is 1-indexed!
✅ tr:nth-child(1)  // First row
```

### **Mistake 4: Not using DevTools**
```
Student: "My selector doesn't work"
Trainer: "Did you test it in DevTools Console?"
Student: "No..."
Trainer: "ALWAYS test in console first!"
```

---

## 💡 TEACHING TIPS

### **DevTools is Your Best Friend:**
```
Teach students to:
1. Right-click element → Inspect
2. Look at HTML structure
3. Test CSS in Console: $$('css-selector')
4. Test XPath in Console: $x('xpath')
5. THEN write Selenium code
```

### **Live Debugging:**
When student's code doesn't work:
1. "What error do you see?" [Read error]
2. "Let's test the selector in DevTools" [Open console]
3. "Try this selector" [Test together]
4. "Now update your code" [Fix code]

### **Pair Programming:**
- One student writes CSS selector
- Partner writes equivalent XPath
- Compare which is simpler

---

## 📊 SUCCESS CRITERIA

### **Student MUST be able to:**
- [ ] Write CSS selectors for basic elements (#, ., [])
- [ ] Write XPath with @attribute
- [ ] Use text() and contains() in XPath
- [ ] Navigate using back/forward
- [ ] Switch between windows
- [ ] Read data from tables

### **Student SHOULD be able to:**
- [ ] Use advanced CSS (nth-child, attribute patterns)
- [ ] Use XPath axes (parent, sibling)
- [ ] Choose CSS vs XPath appropriately
- [ ] Handle multiple windows properly
- [ ] Extract all data from table

### **Bonus Skills:**
- [ ] Debug selectors in DevTools
- [ ] Write complex XPath
- [ ] Optimize selector performance

---

## 📝 HOMEWORK ASSIGNMENT

**Mandatory:**
1. Rewrite Level 1 login test using ONLY CSS selectors
2. Rewrite same test using ONLY XPath
3. Compare: Which was easier?

**Practice:**
4. Create test that navigates through all pages (Dashboard → Transactions → Accounts → Reports → Dashboard)
5. Extract all transaction IDs from transaction table

**Challenge:**
6. Find sum of all account balances
7. Handle popup window and close it

**Due:** Before Level 3 session

---

## 🎤 SAMPLE DIALOGUE

**When teaching CSS:**
> "Think of CSS like giving directions. You can say:
> 'The house with number 42' → #42
> 'The blue house' → .blue
> 'The house on Main Street' → [street='Main']
> All get you to the same place, but some are clearer!"

**When teaching XPath:**
> "XPath is like a GPS. It can:
> - Go forward: //button
> - Go backward: /parent::div
> - Check labels: [text()='Submit']
> - Search contents: [contains(@class, 'btn')]
> More powerful, but more complex!"

**When student struggles:**
> "I see you're stuck. Let's debug together:
> 1. Open DevTools
> 2. Find the element
> 3. Look at its attributes
> 4. Test selector in console
> 5. Copy to code
> Always this process, okay?"

---

## 🔍 TROUBLESHOOTING GUIDE

**Issue: "My CSS selector doesn't work"**
1. Test in DevTools Console: `$$('your-selector')`
2. Check if element is in iframe
3. Check if element is dynamically loaded
4. Verify spelling/case-sensitivity

**Issue: "XPath returns multiple elements"**
1. Make it more specific: add more attributes
2. Use indexing: `(//button)[1]`
3. Combine conditions: `//button[@id='x' and @class='y']`

**Issue: "Can't switch to new window"**
1. Verify window actually opened
2. Print all handles: `System.out.println(driver.getWindowHandles())`
3. Check if you're already on that window
4. Add wait after click

---

## ✅ POST-SESSION CHECKLIST

- [ ] All students can write basic CSS selectors
- [ ] Everyone can use text() in XPath
- [ ] Students know when to use CSS vs XPath
- [ ] Homework assigned and understood
- [ ] Questions answered
- [ ] Feedback collected
- [ ] Level 3 preview given

---

**Next Session:** Level 3 - Waits & Synchronization
**Preview:** "We'll learn how to make tests wait SMART, not just sleep()"

**Great job reaching Level 2! The hard part is over - locators are mastered! 🎉**