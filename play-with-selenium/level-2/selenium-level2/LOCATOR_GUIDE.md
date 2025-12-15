# 🎯 Locator Strategy Quick Reference Guide

## Complete Selenium Locator Cheat Sheet

---

## 📌 **8 Locator Strategies in Selenium**

### **1. By.id() - BEST**
```java
// Most reliable and fastest
driver.findElement(By.id("username"))
driver.findElement(By.id("loginBtn"))
```

**When to use:** Always, if element has a unique ID  
**Speed:** ⚡⚡⚡ Fastest  
**Reliability:** ⭐⭐⭐ Most reliable

---

### **2. By.name()**
```java
// Good for form elements
driver.findElement(By.name("username"))
driver.findElement(By.name("password"))
```

**When to use:** Form fields with name attributes  
**Speed:** ⚡⚡⚡ Fast  
**Reliability:** ⭐⭐⭐ Reliable

---

### **3. By.className()**
```java
// First element with class
driver.findElement(By.className("btn-primary"))
driver.findElement(By.className("form-control"))

// All elements with class
driver.findElements(By.className("form-group"))
```

**When to use:** When class is unique or getting multiple elements  
**Speed:** ⚡⚡ Fast  
**Reliability:** ⭐⭐ Can change

---

### **4. By.tagName()**
```java
// All elements of type
driver.findElement(By.tagName("h1"))
driver.findElements(By.tagName("input"))
```

**When to use:** Rarely - too generic  
**Speed:** ⚡⚡ Fast  
**Reliability:** ⭐ Not reliable alone

---

### **5. By.linkText() - For Links**
```java
// Exact text match
driver.findElement(By.linkText("Register here"))
driver.findElement(By.linkText("Forgot Password?"))
```

**When to use:** Navigation links with stable text  
**Speed:** ⚡⚡ Fast  
**Reliability:** ⭐⭐ Good for links

---

### **6. By.partialLinkText() - For Links**
```java
// Partial text match
driver.findElement(By.partialLinkText("Register"))
driver.findElement(By.partialLinkText("Forgot"))
```

**When to use:** Long link text or dynamic text  
**Speed:** ⚡⚡ Fast  
**Reliability:** ⭐⭐ Good for links

---

### **7. By.cssSelector() - POWERFUL**
```java
// ID
By.cssSelector("#username")

// Class
By.cssSelector(".btn-primary")

// Tag
By.cssSelector("input")

// Attribute
By.cssSelector("[type='password']")
By.cssSelector("[placeholder='Enter username']")

// Starts with
By.cssSelector("[id^='user']")  // id starts with 'user'

// Ends with
By.cssSelector("[id$='Btn']")  // id ends with 'Btn'

// Contains
By.cssSelector("[id*='login']")  // id contains 'login'

// Combinations
By.cssSelector("input#username")
By.cssSelector("button.btn.btn-primary")
By.cssSelector("input[type='text']")

// Child (direct)
By.cssSelector("form > div")

// Descendant (any level)
By.cssSelector("form input")

// Multiple classes
By.cssSelector(".btn.btn-primary.btn-large")

// Nth child
By.cssSelector("div:first-child")
By.cssSelector("div:nth-child(2)")
By.cssSelector("div:last-child")
```

**When to use:** Complex selections, modern approach  
**Speed:** ⚡⚡⚡ Very fast  
**Reliability:** ⭐⭐⭐ Very reliable

---

### **8. By.xpath() - FLEXIBLE**
```java
// Basic
By.xpath("//input[@id='username']")

// Text
By.xpath("//button[text()='Login']")
By.xpath("//label[text()='Username']")

// Contains attribute
By.xpath("//button[contains(@id, 'login')]")
By.xpath("//input[contains(@class, 'form-control')]")

// Contains text
By.xpath("//h1[contains(text(), 'Banking')]")

// Starts with
By.xpath("//input[starts-with(@id, 'user')]")

// Multiple conditions (AND)
By.xpath("//input[@id='username' and @type='text']")

// Multiple conditions (OR)
By.xpath("//input[@id='terms' or @id='rememberMe']")

// Parent
By.xpath("//input[@id='username']/parent::div")

// Following sibling
By.xpath("//input[@id='username']/parent::div/following-sibling::div[1]")

// Preceding sibling
By.xpath("//input[@id='password']/parent::div/preceding-sibling::div[1]")

// Index
By.xpath("(//input)[1]")  // First input
By.xpath("(//input)[2]")  // Second input
By.xpath("(//input)[last()]")  // Last input
By.xpath("(//input)[position()=3]")  // Third input

// Find by label
By.xpath("//label[text()='Password']/following-sibling::input")

// Complex
By.xpath("//form//button[contains(@class, 'btn-primary') and contains(text(), 'Login')]")

// Table cell
By.xpath("//table[@id='dataTable']//tr[2]/td[3]")

// Row by text
By.xpath("//td[contains(text(), 'UPI')]/parent::tr")
```

**When to use:** Complex navigation, parent selection, text-based  
**Speed:** ⚡ Slower  
**Reliability:** ⭐⭐⭐ Very flexible

---

## 🎯 **Decision Tree: Which Locator to Use?**

```
Does element have unique ID?
├─ YES → Use By.id()
└─ NO ↓

Is it a link (<a> tag)?
├─ YES → Use By.linkText() or By.partialLinkText()
└─ NO ↓

Does element have unique name?
├─ YES → Use By.name()
└─ NO ↓

Can you select with simple CSS?
├─ YES → Use By.cssSelector()
└─ NO ↓

Need to navigate up DOM or use text?
├─ YES → Use By.xpath()
└─ NO → Use By.cssSelector() with complex pattern
```

---

## ⚡ **CSS vs XPath - Quick Comparison**

| Feature | CSS | XPath |
|---------|-----|-------|
| **Speed** | ⚡⚡⚡ Faster | ⚡ Slower |
| **Readability** | ⭐⭐⭐ Better | ⭐⭐ Good |
| **Parent selection** | ❌ No | ✅ Yes |
| **Text selection** | ❌ Limited | ✅ Excellent |
| **Complex logic** | ⭐⭐ Good | ⭐⭐⭐ Excellent |
| **Browser support** | ✅ Excellent | ✅ Good |
| **Learning curve** | ⭐⭐⭐ Easy | ⭐⭐ Moderate |

---

## 📝 **Common Patterns**

### **Login Form Example**

```java
// CSS Approach
driver.findElement(By.cssSelector("#username")).sendKeys("admin");
driver.findElement(By.cssSelector("[type='password']")).sendKeys("pass");
driver.findElement(By.cssSelector("button.btn-primary")).click();

// XPath Approach
driver.findElement(By.xpath("//input[@id='username']")).sendKeys("admin");
driver.findElement(By.xpath("//input[@type='password']")).sendKeys("pass");
driver.findElement(By.xpath("//button[text()='Login']")).click();

// Link Text Approach (for navigation)
driver.findElement(By.linkText("Register here")).click();
```

### **Table Operations**

```java
// CSS: Get all rows
List<WebElement> rows = driver.findElements(
    By.cssSelector("#dataTable tbody tr")
);

// CSS: Specific cell
WebElement cell = driver.findElement(
    By.cssSelector("#dataTable tbody tr:nth-child(2) td:nth-child(3)")
);

// XPath: Row by text
WebElement row = driver.findElement(
    By.xpath("//td[contains(text(), 'UPI')]/parent::tr")
);

// XPath: Cell in row by text
WebElement cell = driver.findElement(
    By.xpath("//td[contains(text(), 'UPI')]/parent::tr/td[4]")
);
```

### **Dynamic Elements**

```java
// CSS: Contains in class
By.cssSelector("[class*='error']")

// CSS: Starts with in ID
By.cssSelector("[id^='dynamic']")

// XPath: Contains in any attribute
By.xpath("//*[contains(@class, 'error')]")

// XPath: Text contains
By.xpath("//*[contains(text(), 'Error')]")
```

---

## 🔍 **Debugging Locators**

### **In Browser Console**

```javascript
// Test CSS selector
$$("css_selector_here")  // Returns array of matches

// Test XPath
$x("xpath_here")  // Returns array of matches

// Examples:
$$("#username")
$$("[type='password']")
$x("//button[text()='Login']")
```

### **In Selenium**

```java
// Check if element exists
List<WebElement> elements = driver.findElements(By.cssSelector("selector"));
if (elements.size() > 0) {
    System.out.println("Found " + elements.size() + " elements");
} else {
    System.out.println("No elements found!");
}

// Get element info
WebElement element = driver.findElement(By.id("username"));
System.out.println("Tag: " + element.getTagName());
System.out.println("ID: " + element.getAttribute("id"));
System.out.println("Class: " + element.getAttribute("class"));
System.out.println("Name: " + element.getAttribute("name"));
```

---

## 💡 **Best Practices**

### **Priority Order (Best to Worst)**

1. ⭐⭐⭐ **ID** - `By.id("username")`
2. ⭐⭐⭐ **Name** - `By.name("password")`
3. ⭐⭐⭐ **CSS Selector** - `By.cssSelector("#username")`
4. ⭐⭐ **XPath** - `By.xpath("//input[@id='username']")`
5. ⭐⭐ **Link Text** - `By.linkText("Login")` (links only)
6. ⭐ **Class Name** - `By.className("btn")` (can be unstable)
7. ⭐ **Tag Name** - `By.tagName("input")` (too generic)

### **Do's**

✅ Use ID when available  
✅ Use CSS for performance  
✅ Use XPath for complex navigation  
✅ Keep selectors simple  
✅ Test selectors in browser first  
✅ Use relative paths, not absolute  
✅ Add comments for complex selectors  

### **Don'ts**

❌ Don't use absolute XPath  
❌ Don't use index-based selection if avoidable  
❌ Don't make selectors too complex  
❌ Don't use multiple locators for same element  
❌ Don't ignore performance  
❌ Don't forget to handle NoSuchElementException  

---

## 📚 **Quick Templates**

### **Template 1: Find by Attribute**

```java
// CSS
By.cssSelector("[attribute='value']")

// XPath
By.xpath("//tag[@attribute='value']")
```

### **Template 2: Find by Text**

```java
// Link
By.linkText("exact text")
By.partialLinkText("partial")

// XPath
By.xpath("//tag[text()='exact text']")
By.xpath("//tag[contains(text(), 'partial')]")
```

### **Template 3: Find Child**

```java
// CSS (direct child)
By.cssSelector("parent > child")

// CSS (any descendant)
By.cssSelector("parent child")

// XPath
By.xpath("//parent/child")  // Direct
By.xpath("//parent//child")  // Any level
```

### **Template 4: Find by Multiple Conditions**

```java
// CSS (AND)
By.cssSelector("tag.class[attribute='value']")

// XPath (AND)
By.xpath("//tag[@attr1='value1' and @attr2='value2']")

// XPath (OR)
By.xpath("//tag[@attr1='value1' or @attr2='value2']")
```

---

## 🎓 **Practice Exercises**

For each element on login page, write 3 different locators:

1. **Username field**
   - By.id("username")
   - By.cssSelector("#username")
   - By.xpath("//input[@id='username']")

2. **Password field**
   - By.id("password")
   - By.cssSelector("[type='password']")
   - By.xpath("//input[@type='password']")

3. **Login button**
   - By.id("loginBtn")
   - By.cssSelector(".btn-primary")
   - By.xpath("//button[text()='Login']")

---

## 🚀 **Ready to Use!**

**Save this guide for quick reference while coding!**

📌 **Pin it** | 🔖 **Bookmark it** | 📄 **Print it**

**Happy Locating!** 🎯
