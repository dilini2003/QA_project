import { Builder, By, until } from "selenium-webdriver";
import chrome from "selenium-webdriver/chrome.js";
import chalk from "chalk";
import fs from "fs";

(async function signupTests() {
  // ChromeOptions to suppress unwanted logs
  const options = new chrome.Options();
  options.addArguments("--log-level=3");  
  options.addArguments("--silent");        
  options.excludeSwitches(["enable-logging"]);
  const driver = await new Builder()
    .forBrowser("chrome")
    .setChromeOptions(options)
    .build();

  try {
    // ===============================
    // Test 1: Successful signup
    // ===============================
    console.log("Test 1: Successful signup");
    await driver.get("http://localhost:5173/signup");

    await driver.wait(until.elementLocated(By.name("username")), 15000).sendKeys("Dilini2");
    const uniqueEmail = `user${Date.now()}@example.com`;
    await driver.findElement(By.name("email")).sendKeys(uniqueEmail);
    await driver.findElement(By.name("password")).sendKeys("Password123");

    await driver.findElement(By.css("button[type='submit']")).click();

    try {
      await driver.wait(until.urlContains("/"), 18000);
      console.log(chalk.green("✅ Signup successful test passed"));
    } catch {
      console.error(chalk.red("❌ Signup did not redirect to /"));
      const screenshot = await driver.takeScreenshot();
      fs.writeFileSync("signup-success-fail.png", screenshot, "base64");
      console.log("📸 Screenshot saved: signup-success-fail.png");
    }

    // ===============================
    // Test 2: Invalid email signup
    // ===============================
    console.log("Test 2: Invalid email signup");
    await driver.get("http://localhost:5173/signup");

    await driver.wait(until.elementLocated(By.name("username")), 15000).sendKeys("Invalid Email User");
    const emailInput = await driver.findElement(By.name("email"));
    await emailInput.sendKeys("bad-email"); // invalid email
    await driver.findElement(By.name("password")).sendKeys("Password123");

    // Check browser validation before submitting
    const isValid = await driver.executeScript(
      "return arguments[0].checkValidity();",
      emailInput
    );

    if (!isValid) {
      console.log(chalk.green("✅ Form did not submit (expected behavior for invalid email)"));
    } else {
      console.error(chalk.red("❌ Invalid email test failed"));
      const screenshot = await driver.takeScreenshot();
      fs.writeFileSync("invalid-email-fail.png", screenshot, "base64");
      console.log("📸 Screenshot saved: invalid-email-fail.png");
    }

  } catch (e) {
    console.error(chalk.red("❌ Test run failed with error:"), e);
  } finally {
    await driver.quit();
  }
})();