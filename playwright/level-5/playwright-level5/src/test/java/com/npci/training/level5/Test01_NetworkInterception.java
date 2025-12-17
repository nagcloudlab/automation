package com.npci.training.level5;

import com.microsoft.playwright.*;
import com.npci.training.tests.BaseTest;
import org.junit.jupiter.api.*;

import java.util.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Level 5 - Test 01: Network Interception & Mocking
 * 
 * Topics:
 * - Route interception
 * - Request/Response mocking
 * - Network monitoring
 * - API mocking for testing
 * 
 * Duration: 35 minutes
 */
@DisplayName("Network Interception & Mocking")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test01_NetworkInterception extends BaseTest {
    
    @Test
    @Order(1)
    @DisplayName("Test 1: Basic network monitoring")
    public void test01_NetworkMonitoring() {
        System.out.println("\n=== Test 01: Network Monitoring ===");
        
        List<String> requestUrls = new ArrayList<>();
        List<Integer> responseStatuses = new ArrayList<>();
        
        // Listen to all requests
        page.onRequest(request -> {
            requestUrls.add(request.url());
            System.out.println("→ Request: " + request.method() + " " + request.url());
        });
        
        // Listen to all responses
        page.onResponse(response -> {
            responseStatuses.add(response.status());
            System.out.println("← Response: " + response.status() + " " + response.url());
        });
        
        // Navigate
        page.navigate("https://the-internet.herokuapp.com/");
        
        System.out.println("\n✓ Captured " + requestUrls.size() + " requests");
        System.out.println("✓ Captured " + responseStatuses.size() + " responses");
        
        System.out.println("\n💡 NETWORK MONITORING:");
        System.out.println("""
            
            page.onRequest(request -> {
                System.out.println(request.method());
                System.out.println(request.url());
                System.out.println(request.headers());
            });
            
            page.onResponse(response -> {
                System.out.println(response.status());
                System.out.println(response.url());
                System.out.println(response.body());
            });
            
            Use Cases:
            ✅ Debug network issues
            ✅ Monitor API calls
            ✅ Track performance
            ✅ Verify requests sent
            """);
        
        System.out.println("✓ Network monitoring test passed!\n");
    }
    
    @Test
    @Order(2)
    @DisplayName("Test 2: Block specific resources")
    public void test02_BlockResources() {
        System.out.println("\n=== Test 02: Block Resources ===");
        
        // Block images and CSS
        page.route("**/*.{png,jpg,jpeg,gif,svg,css}", route -> {
            System.out.println("✗ Blocked: " + route.request().url());
            route.abort();
        });
        
        long startTime = System.currentTimeMillis();
        page.navigate("https://the-internet.herokuapp.com/");
        long loadTime = System.currentTimeMillis() - startTime;
        
        System.out.println("✓ Page loaded in " + loadTime + "ms (without images/CSS)");
        
        System.out.println("\n💡 RESOURCE BLOCKING:");
        System.out.println("""
            
            // Block images
            page.route("**/*.{png,jpg,jpeg}", route -> route.abort());
            
            // Block CSS
            page.route("**/*.css", route -> route.abort());
            
            // Block JavaScript
            page.route("**/*.js", route -> route.abort());
            
            // Block analytics
            page.route("**/analytics.js", route -> route.abort());
            page.route("**/google-analytics.com/**", route -> route.abort());
            
            Benefits:
            ✅ Faster tests (50% faster!)
            ✅ Save bandwidth
            ✅ Reduce flakiness
            ✅ Focus on functionality
            
            Banking Example:
            // Block unnecessary resources in tests
            page.route("**/ads/**", route -> route.abort());
            page.route("**/tracking/**", route -> route.abort());
            """);
        
        System.out.println("✓ Resource blocking test passed!\n");
    }
    
    @Test
    @Order(3)
    @DisplayName("Test 3: Mock API responses")
    public void test03_MockApiResponses() {
        System.out.println("\n=== Test 03: Mock API Responses ===");
        
        // Mock API response
        page.route("**/api/users", route -> {
            System.out.println("✓ Intercepted API call: " + route.request().url());
            
            String mockResponse = """
                {
                    "users": [
                        {"id": 1, "name": "John Doe", "balance": "₹1,00,000"},
                        {"id": 2, "name": "Jane Smith", "balance": "₹2,50,000"}
                    ]
                }
                """;
            
            route.fulfill(new Route.FulfillOptions()
                .setStatus(200)
                .setContentType("application/json")
                .setBody(mockResponse));
        });
        
        System.out.println("✓ Mock response set up");
        
        System.out.println("\n💡 API MOCKING:");
        System.out.println("""
            
            // Mock successful response
            page.route("**/api/login", route -> {
                route.fulfill(new Route.FulfillOptions()
                    .setStatus(200)
                    .setBody("{\\"token\\": \\"abc123\\"}"));
            });
            
            // Mock error response
            page.route("**/api/transfer", route -> {
                route.fulfill(new Route.FulfillOptions()
                    .setStatus(400)
                    .setBody("{\\"error\\": \\"Insufficient funds\\"}"));
            });
            
            // Mock with delay
            page.route("**/api/slow", route -> {
                Thread.sleep(2000);  // Simulate slow API
                route.fulfill(new Route.FulfillOptions()
                    .setStatus(200)
                    .setBody("{\\"data\\": \\"success\\"}"));
            });
            
            Use Cases:
            ✅ Test without backend
            ✅ Test error scenarios
            ✅ Test slow responses
            ✅ Consistent test data
            ✅ No test data cleanup
            
            Banking Examples:
            
            // Mock low balance
            page.route("**/api/balance", route -> {
                route.fulfill(setBody("{\\"balance\\": 100}"));
            });
            
            // Mock transaction failure
            page.route("**/api/transfer", route -> {
                route.fulfill(setStatus(400)
                    .setBody("{\\"error\\": \\"Daily limit exceeded\\"}"));
            });
            
            // Mock pending transactions
            page.route("**/api/transactions", route -> {
                route.fulfill(setBody(
                    {"transactions": [
                        {"status": "pending", "amount": 5000}
                    ]}
                    ));
            });
            """);
        
        System.out.println("✓ API mocking test passed!\n");
    }
    
    @Test
    @Order(4)
    @DisplayName("Test 4: Modify requests")
    public void test04_ModifyRequests() {
        System.out.println("\n=== Test 04: Modify Requests ===");
        
        System.out.println("\n💡 REQUEST MODIFICATION:");
        System.out.println("""
            
            // Add authentication header
            page.route("**/api/**", route -> {
                Map<String, String> headers = new HashMap<>(
                    route.request().headers());
                headers.put("Authorization", "Bearer token123");
                
                route.continue_(new Route.ContinueOptions()
                    .setHeaders(headers));
            });
            
            // Modify request body
            page.route("**/api/transfer", route -> {
                String body = route.request().postData();
                String modified = body.replace("5000", "10000");
                
                route.continue_(new Route.ContinueOptions()
                    .setPostData(modified));
            });
            
            // Change method
            page.route("**/api/data", route -> {
                route.continue_(new Route.ContinueOptions()
                    .setMethod("POST"));
            });
            
            Use Cases:
            ✅ Add authentication
            ✅ Modify test data
            ✅ Change endpoints
            ✅ Override parameters
            """);
        
        System.out.println("✓ Request modification explained!\n");
    }
    
    @Test
    @Order(5)
    @DisplayName("Test 5: Wait for specific requests")
    public void test05_WaitForRequests() {
        System.out.println("\n=== Test 05: Wait for Requests ===");
        
        System.out.println("\n💡 WAIT FOR NETWORK:");
        System.out.println("""
            
            // Wait for specific request
            page.waitForRequest(
                "https://api.example.com/data",
                () -> page.click("#submit")
            );
            
            // Wait for response
            Response response = page.waitForResponse(
                "**/api/users",
                () -> page.navigate("https://example.com")
            );
            System.out.println(response.status());
            System.out.println(response.text());
            
            // Wait for multiple requests
            page.waitForRequest(
                request -> request.url().contains("api") &&
                           request.method().equals("POST"),
                () -> page.click("#create")
            );
            
            Banking Examples:
            
            // Wait for balance API
            Response balance = page.waitForResponse(
                "**/api/balance",
                () -> page.navigate("/dashboard")
            );
            String balanceText = balance.text();
            
            // Wait for transaction API
            page.waitForRequest(
                "**/api/transfer",
                () -> page.click("#transfer-btn")
            );
            
            // Verify API was called
            Request txnRequest = page.waitForRequest(
                request -> request.url().contains("/transactions") &&
                           request.method().equals("GET"),
                () -> page.click("#view-transactions")
            );
            System.out.println("Transaction API called: " + txnRequest.url());
            """);
        
        System.out.println("✓ Wait for requests explained!\n");
    }
    
    @Test
    @Order(6)
    @DisplayName("Test 6: Network performance testing")
    public void test06_NetworkPerformance() {
        System.out.println("\n=== Test 06: Network Performance ===");
        
        System.out.println("\n⚡ PERFORMANCE TESTING:");
        System.out.println("""
            
            List<Double> responseTimes = new ArrayList<>();
            
            page.onResponse(response -> {
                // Calculate response time
                long duration = response.request().timing().responseEnd;
                responseTimes.add(duration);
                
                if (duration > 1000) {
                    System.out.println("⚠️ Slow response: " + 
                        response.url() + " (" + duration + "ms)");
                }
            });
            
            page.navigate("https://example.com");
            
            // Calculate average
            double avg = responseTimes.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
            
            System.out.println("Average response time: " + avg + "ms");
            
            // Set thresholds
            assertTrue(avg < 500, "Average response too slow!");
            
            Banking Performance Tests:
            
            // Check API response times
            page.onResponse(response -> {
                if (response.url().contains("/api/")) {
                    long time = response.request().timing().responseEnd;
                    
                    // API SLA: 200ms
                    if (time > 200) {
                        System.out.println("⚠️ API SLA violated: " + 
                            response.url() + " took " + time + "ms");
                    }
                }
            });
            
            // Monitor page load
            page.navigate("/dashboard");
            long loadTime = page.evaluate("window.performance.timing.loadEventEnd - " +
                                         "window.performance.timing.navigationStart");
            assertTrue(loadTime < 3000, "Page load too slow!");
            """);
        
        System.out.println("✓ Network performance explained!\n");
    }
    
    @Test
    @Order(7)
    @DisplayName("Test 7: Complete banking mock example")
    public void test07_BankingMockExample() {
        System.out.println("\n=== Test 07: Banking Mock Example ===");
        
        System.out.println("\n💰 COMPLETE BANKING MOCK:");
        System.out.println("""
            
            // Mock balance API
            page.route("**/api/balance", route -> {
                route.fulfill(new Route.FulfillOptions()
                    .setStatus(200)
                    .setBody("{\\"balance\\": \\"₹1,00,000\\", \\"currency\\": \\"INR\\"}"));
            });
            
            // Mock transactions API
            page.route("**/api/transactions", route -> {
                String transactions = 
                    {
                        "transactions": [
                            {
                                "id": "TXN001",
                                "type": "UPI",
                                "amount": "₹500",
                                "status": "success",
                                "date": "2024-12-17"
                            },
                            {
                                "id": "TXN002",
                                "type": "NEFT",
                                "amount": "₹5,000",
                                "status": "pending",
                                "date": "2024-12-17"
                            }
                        ]
                    }
                   ;
                route.fulfill(new Route.FulfillOptions()
                    .setStatus(200)
                    .setBody(transactions));
            });
            
            // Mock transfer API - success
            page.route("**/api/transfer", route -> {
                if (route.request().method().equals("POST")) {
                    String response = \"""
                        {
                            "transactionId": "TXN003",
                            "status": "success",
                            "message": "Transfer successful",
                            "newBalance": "₹95,000"
                        }
                        \""";
                    route.fulfill(new Route.FulfillOptions()
                        .setStatus(200)
                        .setBody(response));
                }
            });
            
            // Now run tests without real backend!
            page.navigate("/banking-app");
            
            // Test balance display
            assertThat(page.getByTestId("balance"))
                .containsText("₹1,00,000");
            
            // Test transactions
            assertThat(page.locator(".transaction-item"))
                .hasCount(2);
            
            // Test transfer
            page.getByLabel("Amount").fill("5000");
            page.getByRole(AriaRole.BUTTON, setName("Transfer")).click();
            
            assertThat(page.getByText("Transfer successful"))
                .isVisible();
            
            Benefits:
            ✅ No backend needed
            ✅ Consistent test data
            ✅ Test all scenarios
            ✅ Fast execution
            ✅ No cleanup needed
            """);
        
        System.out.println("✓ Banking mock example completed!\n");
    }
}

/*
 * NETWORK INTERCEPTION SUMMARY:
 * 
 * 1. MONITORING:
 *    page.onRequest()  - Listen to requests
 *    page.onResponse() - Listen to responses
 * 
 * 2. BLOCKING:
 *    route.abort() - Block resources
 *    - Images, CSS, JS
 *    - Analytics, ads
 *    - 50% faster tests!
 * 
 * 3. MOCKING:
 *    route.fulfill() - Mock responses
 *    - Test without backend
 *    - Consistent data
 *    - Error scenarios
 * 
 * 4. MODIFYING:
 *    route.continue_() - Modify requests
 *    - Add headers
 *    - Change body
 *    - Override params
 * 
 * 5. WAITING:
 *    page.waitForRequest()  - Wait for request
 *    page.waitForResponse() - Wait for response
 * 
 * BENEFITS:
 * ✅ Test without backend
 * ✅ 50% faster tests
 * ✅ Consistent data
 * ✅ Error scenarios
 * ✅ No cleanup
 * 
 * RUN COMMANDS:
 * mvn test -Dtest=Test01_NetworkInterception
 */
