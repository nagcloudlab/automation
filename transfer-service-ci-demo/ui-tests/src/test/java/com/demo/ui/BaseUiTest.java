package com.demo.ui;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import java.nio.file.Path;

public abstract class BaseUiTest {

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;
    protected String baseUrl;

    @BeforeEach
    void setup(TestInfo testInfo) {
        baseUrl = System.getProperty(
                "baseUrl",
                System.getenv().getOrDefault("BASE_URL", "http://localhost:8080")
        );

        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(true)
        );

        context = browser.newContext(
                new Browser.NewContextOptions()
                        .setRecordVideoDir(Path.of("target/videos"))
        );

        page = context.newPage();
    }

    @AfterEach
    void teardown(TestInfo testInfo) {
        // Screenshot on failure (always for demo clarity)
        page.screenshot(new Page.ScreenshotOptions()
                .setPath(Path.of("target/screenshots/" + testInfo.getDisplayName() + ".png")));

        context.close();
        browser.close();
        playwright.close();
    }
}
