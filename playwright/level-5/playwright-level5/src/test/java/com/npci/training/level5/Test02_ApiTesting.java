package com.npci.training.level5;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import com.npci.training.tests.BaseTest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Level 5 - Test 02: API Testing with Playwright
 * 
 * Topics:
 * - APIRequestContext
 * - GET, POST, PUT, DELETE
 * - Headers and authentication
 * - Request/Response handling
 * - Combine UI + API testing
 * 
 * Duration: 35 minutes
 */
@DisplayName("API Testing with Playwright")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test02_ApiTesting extends BaseTest {
    
    @Test
    @Order(1)
    @DisplayName("Test 1: GET request")
    public void test01_GetRequest() {
        System.out.println("\n=== Test 01: GET Request ===");
        
        // Simple GET
        APIResponse response = request.get("/users?page=2");
        
        System.out.println("Status: " + response.status());
        System.out.println("Status Text: " + response.statusText());
        System.out.println("Headers: " + response.headers());
        System.out.println("Body: " + response.text());
        
        // Assertions
        assertEquals(200, response.status());
        assertTrue(response.ok());
        assertTrue(response.text().contains("data"));
        
        System.out.println("\n✓ GET request successful!");
        
        System.out.println("\n💡 API TESTING:");
        System.out.println("""
            
            // Create API context
            APIRequestContext request = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                    .setBaseURL("https://api.example.com")
            );
            
            // GET request
            APIResponse response = request.get("/users");
            
            // Assertions
            assertEquals(200, response.status());
            assertTrue(response.ok());
            String body = response.text();
            assertTrue(body.contains("users"));
            
            // Parse JSON
            JsonNode json = new ObjectMapper().readTree(response.body());
            assertEquals("John", json.get("name").asText());
            """);
        
        System.out.println("✓ GET request test passed!\n");
    }
    
    @Test
    @Order(2)
    @DisplayName("Test 2: POST request")
    public void test02_PostRequest() {
        System.out.println("\n=== Test 02: POST Request ===");
        
        // POST with JSON body
        String jsonBody = """
            {
                "name": "Rajesh Kumar",
                "job": "Software Engineer"
            }
            """;
        
        APIResponse response = request.post("/users",
            RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData(jsonBody));
        
        System.out.println("Status: " + response.status());
        System.out.println("Body: " + response.text());
        
        // Assertions
        assertEquals(201, response.status());
        assertTrue(response.text().contains("Rajesh Kumar"));
        
        System.out.println("\n✓ POST request successful!");
        
        System.out.println("\n💡 POST REQUEST:");
        System.out.println("""
            
            // POST with JSON
            APIResponse response = request.post("/users",
                RequestOptions.create()
                    .setHeader("Content-Type", "application/json")
                    .setData("{\\"name\\": \\"John\\", \\"age\\": 30}"));
            
            // POST with form data
            APIResponse response = request.post("/login",
                RequestOptions.create()
                    .setHeader("Content-Type", "application/x-www-form-urlencoded")
                    .setData("username=admin&password=pass123"));
            
            // Banking example - Transfer
            String transferData = \"""
                {
                    "fromAccount": "123456",
                    "toAccount": "789012",
                    "amount": 5000,
                    "currency": "INR"
                }
                \""";
            
            APIResponse transfer = request.post("/api/transfer",
                RequestOptions.create()
                    .setHeader("Authorization", "Bearer token123")
                    .setData(transferData));
            
            assertEquals(200, transfer.status());
            assertTrue(transfer.text().contains("transactionId"));
            """);
        
        System.out.println("✓ POST request test passed!\n");
    }
    
    @Test
    @Order(3)
    @DisplayName("Test 3: PUT and DELETE requests")
    public void test03_PutDeleteRequests() {
        System.out.println("\n=== Test 03: PUT & DELETE ===");
        
        // PUT request
        String updateData = """
            {
                "name": "Rajesh Kumar Updated",
                "job": "Senior Engineer"
            }
            """;
        
        APIResponse putResponse = request.put("/users/2",
            RequestOptions.create().setData(updateData));
        
        System.out.println("PUT Status: " + putResponse.status());
        assertEquals(200, putResponse.status());
        
        // DELETE request
        APIResponse deleteResponse = request.delete("/users/2");
        
        System.out.println("DELETE Status: " + deleteResponse.status());
        assertEquals(204, deleteResponse.status());
        
        System.out.println("\n✓ PUT & DELETE successful!");
        
        System.out.println("\n💡 PUT & DELETE:");
        System.out.println("""
            
            // PUT - Update resource
            APIResponse update = request.put("/users/123",
                RequestOptions.create()
                    .setData("{\\"name\\": \\"Updated\\"}"));
            
            // PATCH - Partial update
            APIResponse patch = request.patch("/users/123",
                RequestOptions.create()
                    .setData("{\\"email\\": \\"new@email.com\\"}"));
            
            // DELETE - Remove resource
            APIResponse delete = request.delete("/users/123");
            
            Banking Examples:
            
            // Update profile
            APIResponse profile = request.put("/api/profile",
                RequestOptions.create()
                    .setHeader("Authorization", "Bearer token")
                    .setData("{\\"phone\\": \\"9876543210\\"}"));
            
            // Cancel transaction
            APIResponse cancel = request.delete("/api/transactions/TXN123",
                RequestOptions.create()
                    .setHeader("Authorization", "Bearer token"));
            """);
        
        System.out.println("✓ PUT & DELETE test passed!\n");
    }
    
    @Test
    @Order(4)
    @DisplayName("Test 4: Headers and authentication")
    public void test04_HeadersAuthentication() {
        System.out.println("\n=== Test 04: Headers & Auth ===");
        
        System.out.println("\n🔐 AUTHENTICATION:");
        System.out.println("""
            
            // Bearer token
            APIRequestContext authRequest = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                    .setBaseURL("https://api.example.com")
                    .setExtraHTTPHeaders(Map.of(
                        "Authorization", "Bearer eyJhbGc..."
                    ))
            );
            
            APIResponse response = authRequest.get("/protected");
            
            // Basic auth
            APIRequestContext basicAuth = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                    .setHttpCredentials("username", "password")
            );
            
            // API key
            APIRequestContext apiKey = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                    .setExtraHTTPHeaders(Map.of(
                        "X-API-Key", "your-api-key-here"
                    ))
            );
            
            // Custom headers
            APIResponse custom = request.get("/data",
                RequestOptions.create()
                    .setHeader("X-Custom-Header", "value")
                    .setHeader("X-Request-ID", UUID.randomUUID().toString())
            );
            
            Banking Example:
            
            // Login and get token
            APIResponse login = request.post("/api/login",
                RequestOptions.create()
                    .setData("{\\"username\\": \\"user\\", \\"password\\": \\"pass\\"}"));
            
            String token = new ObjectMapper()
                .readTree(login.body())
                .get("token")
                .asText();
            
            // Create authenticated context
            APIRequestContext authCtx = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                    .setBaseURL("https://bank.com/api")
                    .setExtraHTTPHeaders(Map.of(
                        "Authorization", "Bearer " + token
                    ))
            );
            
            // Use authenticated context
            APIResponse balance = authCtx.get("/balance");
            APIResponse transactions = authCtx.get("/transactions");
            """);
        
        System.out.println("✓ Headers & auth explained!\n");
    }
    
    @Test
    @Order(5)
    @DisplayName("Test 5: Combine UI and API testing")
    public void test05_CombineUiApi() {
        System.out.println("\n=== Test 05: Combine UI + API ===");
        
        System.out.println("\n🔄 UI + API TESTING:");
        System.out.println("""
            
            PATTERN: Setup via API, Verify via UI
            
            // 1. Create data via API (fast)
            APIResponse create = request.post("/api/users",
                RequestOptions.create()
                    .setData("{\\"name\\": \\"John\\", \\"email\\": \\"john@test.com\\"}"));
            
            String userId = new ObjectMapper()
                .readTree(create.body())
                .get("id")
                .asText();
            
            // 2. Verify via UI
            page.navigate("/users/" + userId);
            assertThat(page.locator("h1")).hasText("John");
            assertThat(page.locator(".email")).hasText("john@test.com");
            
            // 3. Cleanup via API (fast)
            request.delete("/api/users/" + userId);
            
            
            PATTERN: Action via UI, Verify via API
            
            // 1. Perform action via UI
            page.navigate("/transfer");
            page.getByLabel("Amount").fill("5000");
            page.getByRole(AriaRole.BUTTON, setName("Transfer")).click();
            
            // 2. Get transaction ID from UI
            String txnId = page.getByTestId("txn-id").textContent();
            
            // 3. Verify via API (more reliable)
            APIResponse verify = request.get("/api/transactions/" + txnId);
            JsonNode txn = new ObjectMapper().readTree(verify.body());
            
            assertEquals("success", txn.get("status").asText());
            assertEquals(5000, txn.get("amount").asInt());
            
            
            Banking Complete Example:
            
            @Test
            void testCompleteTransferFlow() {
                // 1. Setup: Create account via API
                APIResponse account = request.post("/api/accounts",
                    RequestOptions.create()
                        .setData("{\\"balance\\": 100000}"));
                
                String accountId = extractId(account);
                
                // 2. UI: Login
                page.navigate("/login");
                page.getByLabel("Username").fill("user");
                page.getByLabel("Password").fill("pass");
                page.getByRole(AriaRole.BUTTON, setName("Login")).click();
                
                // 3. UI: Transfer
                page.navigate("/transfer");
                page.getByLabel("To Account").fill("9876543210");
                page.getByLabel("Amount").fill("5000");
                page.getByRole(AriaRole.BUTTON, setName("Transfer")).click();
                
                String txnId = page.getByTestId("txn-id").textContent();
                
                // 4. API: Verify transaction
                APIResponse txnAPI = request.get("/api/transactions/" + txnId);
                JsonNode txn = parseJson(txnAPI);
                
                assertEquals("success", txn.get("status").asText());
                assertEquals(5000, txn.get("amount").asInt());
                
                // 5. API: Verify balance updated
                APIResponse balanceAPI = request.get("/api/balance/" + accountId);
                JsonNode balance = parseJson(balanceAPI);
                
                assertEquals(95000, balance.get("balance").asInt());
                
                // 6. Cleanup: Delete account via API
                request.delete("/api/accounts/" + accountId);
            }
            
            Benefits:
            ✅ Fast test setup (API)
            ✅ Test realistic flows (UI)
            ✅ Reliable verification (API)
            ✅ Fast cleanup (API)
            ✅ Best of both worlds!
            """);
        
        System.out.println("✓ UI + API combination explained!\n");
    }
    
    @Test
    @Order(6)
    @DisplayName("Test 6: API testing best practices")
    public void test06_ApiTestingBestPractices() {
        System.out.println("\n=== Test 06: Best Practices ===");
        
        System.out.println("\n✅ API TESTING BEST PRACTICES:");
        System.out.println("""
            
            1. USE BASE URL:
               APIRequestContext request = playwright.request().newContext(
                   new APIRequest.NewContextOptions()
                       .setBaseURL("https://api.example.com")
               );
               // Then: request.get("/users") instead of full URL
            
            2. REUSE CONTEXT:
               // Create once in @BeforeAll or @BeforeEach
               // Use across all tests
               // Dispose in @AfterAll or @AfterEach
            
            3. EXTRACT COMMON HEADERS:
               Map<String, String> commonHeaders = Map.of(
                   "Content-Type", "application/json",
                   "X-API-Version", "v1"
               );
               
               APIRequestContext request = playwright.request().newContext(
                   new APIRequest.NewContextOptions()
                       .setExtraHTTPHeaders(commonHeaders)
               );
            
            4. PARSE JSON PROPERLY:
               ObjectMapper mapper = new ObjectMapper();
               JsonNode json = mapper.readTree(response.body());
               String value = json.get("field").asText();
            
            5. VERIFY STATUS CODES:
               assertEquals(200, response.status(), "Expected OK");
               assertTrue(response.ok(), "Response should be OK");
            
            6. VERIFY RESPONSE BODY:
               String body = response.text();
               assertTrue(body.contains("expected"));
               
               // Or parse and check
               JsonNode json = parseJson(response);
               assertEquals("value", json.get("key").asText());
            
            7. HANDLE ERRORS:
               if (!response.ok()) {
                   System.out.println("Error: " + response.status());
                   System.out.println("Body: " + response.text());
               }
            
            8. USE FOR SETUP/TEARDOWN:
               @BeforeEach
               void setup() {
                   // Create test data via API
                   request.post("/api/users", ...);
               }
               
               @AfterEach
               void teardown() {
                   // Cleanup via API
                   request.delete("/api/users/" + userId);
               }
            
            9. COMBINE WITH UI:
               - Setup via API (fast)
               - Test via UI (realistic)
               - Verify via API (reliable)
               - Cleanup via API (fast)
            
            10. TEST REALISTIC SCENARIOS:
                - Success cases
                - Error cases (400, 401, 403, 404, 500)
                - Edge cases (empty, null, large data)
                - Performance (response time)
            """);
        
        System.out.println("✓ Best practices explained!\n");
    }
}

/*
 * API TESTING SUMMARY:
 * 
 * 1. API REQUEST CONTEXT:
 *    APIRequestContext request = playwright.request().newContext();
 *    - GET, POST, PUT, PATCH, DELETE
 *    - Headers, auth, body
 * 
 * 2. METHODS:
 *    request.get(url)
 *    request.post(url, options)
 *    request.put(url, options)
 *    request.delete(url)
 * 
 * 3. RESPONSE:
 *    response.status()    - Status code
 *    response.ok()        - Is 2xx?
 *    response.text()      - Body as text
 *    response.body()      - Body as bytes
 *    response.headers()   - Response headers
 * 
 * 4. COMBINE UI + API:
 *    - Setup via API
 *    - Test via UI
 *    - Verify via API
 *    - Cleanup via API
 * 
 * 5. BENEFITS:
 *    ✅ Fast test setup
 *    ✅ Reliable verification
 *    ✅ Quick cleanup
 *    ✅ Best of both worlds
 * 
 * RUN COMMANDS:
 * mvn test -Dtest=Test02_ApiTesting
 */
