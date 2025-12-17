# 🎭 Playwright Level 5 - Advanced Features

## Production-Ready Advanced Testing

**Level:** Advanced  
**Duration:** 5-6 hours  
**Prerequisites:** Playwright Levels 1-4 completed  

---

## 🚀 **The Complete Playwright Mastery!**

This is the FINAL level covering advanced production features that separate professionals from experts!

---

## 📚 **What You'll Learn**

1. ✅ Network interception & mocking
2. ✅ API testing with Playwright
3. ✅ Screenshots, videos, traces
4. ✅ Parallel execution
5. ✅ Configuration management
6. ✅ CI/CD integration
7. ✅ Performance testing
8. ✅ Production patterns

---

## 📦 **Package Contents**

```
playwright-level5/
├── pom.xml
├── README.md
└── src/
    ├── test/java/com/npci/training/
    │   ├── tests/BaseTest.java
    │   └── level5/
    │       ├── Test01_NetworkInterception.java  # 7 tests
    │       └── Test02_ApiTesting.java           # 6 tests
    └── test/resources/
        └── playwright.properties
```

**Total:** 13 comprehensive tests covering advanced features

---

## 🎯 **Test Classes Overview**

### **Test01: Network Interception (35 mins)** - 7 Tests

**Topics:**
- Network monitoring
- Resource blocking (50% faster tests!)
- API response mocking
- Request modification
- Performance testing

**Key Features:**
```java
// 1. Monitor all network requests
page.onRequest(request -> {
    System.out.println("→ " + request.method() + " " + request.url());
});

page.onResponse(response -> {
    System.out.println("← " + response.status());
});

// 2. Block images/CSS (50% faster!)
page.route("**/*.{png,jpg,css}", route -> route.abort());

// 3. Mock API responses (test without backend!)
page.route("**/api/balance", route -> {
    route.fulfill(new Route.FulfillOptions()
        .setStatus(200)
        .setBody("{\"balance\": \"₹1,00,000\"}"));
});

// 4. Modify requests (add auth headers)
page.route("**/api/**", route -> {
    Map<String, String> headers = new HashMap<>(route.request().headers());
    headers.put("Authorization", "Bearer token123");
    route.continue_(new Route.ContinueOptions().setHeaders(headers));
});

// 5. Wait for specific requests
Response response = page.waitForResponse(
    "**/api/users",
    () -> page.click("#load-users")
);
```

**Benefits:**
- ✅ Test without backend
- ✅ 50% faster tests (block resources)
- ✅ Consistent test data
- ✅ Test error scenarios
- ✅ No test data cleanup

---

### **Test02: API Testing (35 mins)** - 6 Tests

**Topics:**
- APIRequestContext
- GET, POST, PUT, DELETE
- Headers & authentication
- Combine UI + API testing
- Best practices

**Key Features:**
```java
// 1. Create API context
APIRequestContext request = playwright.request().newContext(
    new APIRequest.NewContextOptions()
        .setBaseURL("https://api.bank.com")
        .setExtraHTTPHeaders(Map.of(
            "Authorization", "Bearer token123"
        ))
);

// 2. GET request
APIResponse response = request.get("/users");
assertEquals(200, response.status());
String body = response.text();

// 3. POST request
APIResponse create = request.post("/transfer",
    RequestOptions.create()
        .setData("{\"amount\": 5000}"));

// 4. Combine UI + API
// Setup via API (fast)
APIResponse account = request.post("/api/accounts", ...);

// Test via UI (realistic)
page.navigate("/transfer");
page.getByLabel("Amount").fill("5000");
page.click("#submit");

// Verify via API (reliable)
APIResponse verify = request.get("/api/balance");
JsonNode json = parseJson(verify);
assertEquals(95000, json.get("balance").asInt());

// Cleanup via API (fast)
request.delete("/api/accounts/" + accountId);
```

**Benefits:**
- ✅ Fast test setup (API)
- ✅ Realistic flows (UI)
- ✅ Reliable verification (API)
- ✅ Quick cleanup (API)

---

## 💰 **Complete Banking Example**

### **Test Banking Transfer with Network Mocking:**

```java
@Test
void testTransferWithMocking() {
    // Mock balance API
    page.route("**/api/balance", route -> {
        route.fulfill(setBody("{\"balance\": 100000}"));
    });
    
    // Mock transactions API
    page.route("**/api/transactions", route -> {
        String txns = """
            {"transactions": [
                {"id": "TXN001", "amount": 500, "status": "success"}
            ]}
            """;
        route.fulfill(setBody(txns));
    });
    
    // Mock transfer API
    page.route("**/api/transfer", route -> {
        if (route.request().method().equals("POST")) {
            route.fulfill(setBody("""
                {"transactionId": "TXN002", "status": "success"}
                """));
        }
    });
    
    // Now test without real backend!
    page.navigate("/banking");
    assertThat(page.getByTestId("balance")).containsText("₹1,00,000");
    
    page.goToTransfer()
        .doTransfer("savings", "9876543210", "5000", "Rent")
        .verifyTransferSuccessful();
}
```

### **Test Banking with API + UI:**

```java
@Test
void testBankingWithApiAndUi() {
    // 1. Setup via API (fast)
    APIResponse account = request.post("/api/accounts",
        RequestOptions.create()
            .setData("{\"balance\": 100000}"));
    
    String accountId = extractId(account);
    
    // 2. UI: Login and transfer
    new LoginPage(page)
        .navigate()
        .login("rajesh.kumar", "SecurePass123!")
        .goToTransfer()
        .doTransfer("savings", "9876543210", "5000", "Rent")
        .verifyTransferSuccessful();
    
    String txnId = page.getByTestId("txn-id").textContent();
    
    // 3. Verify via API (reliable)
    APIResponse verify = request.get("/api/transactions/" + txnId);
    JsonNode txn = parseJson(verify);
    
    assertEquals("success", txn.get("status").asText());
    assertEquals(5000, txn.get("amount").asInt());
    
    // 4. Verify balance via API
    APIResponse balance = request.get("/api/balance/" + accountId);
    assertEquals(95000, parseJson(balance).get("balance").asInt());
    
    // 5. Cleanup via API (fast)
    request.delete("/api/accounts/" + accountId);
}
```

---

## 🎨 **Advanced Features**

### **1. Screenshots & Videos:**
```java
// Automatic video recording (in BaseTest)
context = browser.newContext(
    new Browser.NewContextOptions()
        .setRecordVideoDir(Paths.get("videos/"))
);

// Manual screenshot
page.screenshot(new Page.ScreenshotOptions()
    .setPath(Paths.get("screenshots/error.png"))
    .setFullPage(true));

// Element screenshot
page.locator(".error-message").screenshot(
    new Locator.ScreenshotOptions()
        .setPath(Paths.get("screenshots/error-msg.png"))
);
```

### **2. Tracing:**
```java
// Start tracing (in BaseTest)
context.tracing().start(new Tracing.StartOptions()
    .setScreenshots(true)
    .setSnapshots(true)
    .setSources(true));

// Stop and save
context.tracing().stop(new Tracing.StopOptions()
    .setPath(Paths.get("traces/test.zip")));

// View trace: npx playwright show-trace trace.zip
```

### **3. Parallel Execution:**
```xml
<!-- pom.xml -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <parallel>methods</parallel>
        <threadCount>3</threadCount>
    </configuration>
</plugin>
```

### **4. Headless Mode:**
```bash
# Run in headless
mvn test -DHEADLESS=true

# Run in headed (for debugging)
mvn test -DHEADLESS=false
```

---

## 📊 **Performance Impact**

### **Network Mocking Benefits:**

**Without Mocking:**
- Backend dependency: Required
- Test speed: Slow (network calls)
- Data consistency: Unpredictable
- Error testing: Hard to simulate
- Cleanup: Required

**With Mocking:**
- Backend dependency: None!
- Test speed: 3x faster
- Data consistency: 100%
- Error testing: Easy!
- Cleanup: Not needed

### **Resource Blocking Benefits:**

```java
// Block images, CSS, fonts
page.route("**/*.{png,jpg,css,woff2}", route -> route.abort());

// Results:
// Without blocking: 5 seconds
// With blocking: 2.5 seconds
// Improvement: 50% faster!
```

### **API + UI Combination:**

**Traditional (UI only):**
1. Create account via UI (2 seconds)
2. Login via UI (1 second)
3. Transfer via UI (2 seconds)
4. Verify via UI (1 second)
5. Cleanup via UI (2 seconds)
**Total: 8 seconds**

**Modern (API + UI):**
1. Create account via API (0.1 seconds)
2. Login via UI (1 second)
3. Transfer via UI (2 seconds)
4. Verify via API (0.1 seconds)
5. Cleanup via API (0.1 seconds)
**Total: 3.3 seconds**

**Improvement: 58% faster!**

---

## ✅ **Best Practices**

### **Network Interception:**
1. ✅ Mock for consistent data
2. ✅ Block unnecessary resources (50% faster)
3. ✅ Test error scenarios
4. ✅ Monitor performance
5. ✅ Wait for specific requests

### **API Testing:**
1. ✅ Use for setup/teardown
2. ✅ Verify critical data
3. ✅ Combine with UI tests
4. ✅ Test realistic flows
5. ✅ Fast and reliable

### **Production:**
1. ✅ Run in parallel
2. ✅ Record videos on failure
3. ✅ Capture traces
4. ✅ Headless in CI/CD
5. ✅ Headed for debugging

---

## 🚀 **Quick Start**

```bash
# Extract
unzip playwright-level5.zip
cd playwright-level5

# Run all tests
mvn test

# Run in headless
mvn test -DHEADLESS=true

# Run specific test
mvn test -Dtest=Test01_NetworkInterception

# Run in parallel
mvn test -Dparallel=methods -DthreadCount=3
```

---

## 💯 **What You'll Master**

**Network:**
- [x] Request/response monitoring
- [x] Resource blocking (50% faster)
- [x] API mocking
- [x] Request modification
- [x] Performance testing

**API:**
- [x] GET, POST, PUT, DELETE
- [x] Authentication
- [x] UI + API combination
- [x] Fast setup/teardown

**Production:**
- [x] Screenshots & videos
- [x] Tracing
- [x] Parallel execution
- [x] CI/CD ready
- [x] Performance optimization

---

## 🎉 **You're Now a Playwright Expert!**

After Level 5, you know:
- ✅ All Playwright features
- ✅ Production patterns
- ✅ Advanced testing
- ✅ Performance optimization
- ✅ Professional architecture

**You can build enterprise-grade test automation!** 🏆

---

**Happy Testing!** 🚀
