package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CredentialTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.firefoxdriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new FirefoxDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("login-button")).getText().contains("Login"));
	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * Write a Selenium test that logs in an existing user,
	 * creates a credential and verifies that the credential details are visible in the credential list.
	 */
	@Test
	public void testAddingCredential() {
		doMockSignUp("Large File","Test","LFT7","123");
		doLogIn("LFT7", "123");
		selectCredentialTab();

		WebElement clickNewCredential = driver.findElement(By.id("add-new-credential"));
		clickNewCredential.click();

		inputCredential("url", "username", "password");

		WebElement saveCredential = driver.findElement(By.id("save-credential"));
		saveCredential.click();

		WebElement resultSuccess = driver.findElement(By.id("result-success"));
		resultSuccess.click();

		selectCredentialTab();
		Assertions.assertTrue(driver.findElement(By.id("edit-credential-button")).getText().contains("Edit"));
	}

	/**
	 * Write a Selenium test that logs in an existing user with existing credentials,
	 * clicks the edit credential button on an existing credential, changes the credential data,
	 * saves the changes, and verifies that the changes appear in the credential list.
	 */
	@Test
	public void testEditingCredential() {
		doMockSignUp("Large File","Test","LFT8","123");
		doLogIn("LFT8", "123");
		selectCredentialTab();
		WebElement clickNewCredential = driver.findElement(By.id("add-new-credential"));
		clickNewCredential.click();
		inputCredential("url", "username", "password");
		WebElement saveCredential = driver.findElement(By.id("save-credential"));
		saveCredential.click();

		WebElement resultSuccess = driver.findElement(By.id("result-success"));
		resultSuccess.click();
		selectCredentialTab();

		WebElement editCredential = driver.findElement(By.id("edit-credential-button"));
		editCredential.click();

		inputCredential("", " update", "");

		WebElement updateCredential = driver.findElement(By.id("save-credential"));
		updateCredential.click();

		clickResultSuccess();
		selectCredentialTab();

		Assertions.assertTrue(driver.findElement(By.id("th-credential-username")).getText().contains("username update"));
	}

	/**
	 * Write a Selenium test that logs in an existing user with existing credentials,
	 * clicks the delete credential button on an existing credential,
	 * and verifies that the credential no longer appears in the credential list.
	 */
	@Test
	public void testDeletingCredential() {
		doMockSignUp("Large File","Test","LFT1","123");
		doLogIn("LFT1", "123");
		selectCredentialTab();
		WebElement clickNewCredential = driver.findElement(By.id("add-new-credential"));
		clickNewCredential.click();
		inputCredential("url", "username", "password");
		WebElement saveCredential = driver.findElement(By.id("save-credential"));
		saveCredential.click();

		WebElement resultSuccess = driver.findElement(By.id("result-success"));
		resultSuccess.click();
		selectCredentialTab();
		WebElement deleteCredential = driver.findElement(By.id("delete-credential-button"));
		deleteCredential.click();

		clickResultSuccess();
		selectCredentialTab();

		List<WebElement> thCredentialUsername = driver.findElements(By.id("th-credential-username"));
		Assertions.assertTrue(thCredentialUsername.isEmpty());
	}

	private void clickResultSuccess() {
		WebElement resultSuccess = driver.findElement(By.id("result-success"));
		resultSuccess.click();
	}

	private void inputCredential(String urlParam, String usernameParam, String passwordParam) {
		WebElement url = driver.findElement(By.id("credential-url"));
		url.click();
		url.sendKeys(urlParam);

		WebElement username = driver.findElement(By.id("credential-username"));
		username.click();
		username.sendKeys(usernameParam);

		WebElement password = driver.findElement(By.id("credential-password"));
		password.click();
		password.sendKeys(passwordParam);
	}

	private void selectCredentialTab() {
		WebElement selectCredentialTab = driver.findElement(By.id("nav-credentials-tab"));
		selectCredentialTab.click();
	}
}
