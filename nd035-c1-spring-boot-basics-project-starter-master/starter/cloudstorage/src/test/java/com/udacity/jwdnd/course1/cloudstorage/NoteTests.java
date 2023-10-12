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
class NoteTests {

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
	 * creates a note and verifies that the note details are visible in the note list.
	 */
	@Test
	public void testAddingNotes() {
		doMockSignUp("Large File","Test","LFT4","123");
		doLogIn("LFT4", "123");
		selectNoteTab();
		WebElement clickNewNote = driver.findElement(By.id("add-new-note"));
		clickNewNote.click();

		inputNote("note-title", "note-description");

		WebElement saveNote = driver.findElement(By.id("save-note"));
		saveNote.click();

		WebElement resultSuccess = driver.findElement(By.id("result-success"));
		resultSuccess.click();

		selectNoteTab();
		Assertions.assertTrue(driver.findElement(By.id("edit-note-button")).getText().contains("Edit"));
	}

	/**
	 * Write a Selenium test that logs in an existing user with existing notes,
	 * clicks the edit note button on an existing note, changes the note data,
	 * saves the changes, and verifies that the changes appear in the note list.
	 */
	@Test
	public void testEditingNotes() {
		doMockSignUp("Large File","Test","LFT5","123");
		doLogIn("LFT5", "123");
		selectNoteTab();
		WebElement clickNewNote = driver.findElement(By.id("add-new-note"));
		clickNewNote.click();
		inputNote("note", "note");
		WebElement saveNote = driver.findElement(By.id("save-note"));
		saveNote.click();

		WebElement resultSuccess = driver.findElement(By.id("result-success"));
		resultSuccess.click();
		selectNoteTab();

		WebElement editNote = driver.findElement(By.id("edit-note-button"));
		editNote.click();

		inputNote("1", "update");

		WebElement updateNote = driver.findElement(By.id("save-note"));
		updateNote.click();
		// Visit the sign-up page.
		clickResultSuccess();
		selectNoteTab();

		Assertions.assertTrue(driver.findElement(By.id("th-note-title")).getText().contains("note1"));
	}

	/**
	 * Write a Selenium test that logs in an existing user with existing notes,
	 * clicks the delete note button on an existing note,
	 * and verifies that the note no longer appears in the note list.
	 */
	@Test
	public void testDeletingNotes() {
		doMockSignUp("Large File","Test","LFT6","123");
		doLogIn("LFT6", "123");
		selectNoteTab();
		WebElement clickNewNote = driver.findElement(By.id("add-new-note"));
		clickNewNote.click();
		inputNote("note title", "note description");
		WebElement saveNote = driver.findElement(By.id("save-note"));
		saveNote.click();

		WebElement resultSuccess = driver.findElement(By.id("result-success"));
		resultSuccess.click();
		selectNoteTab();

		WebElement deleteNote = driver.findElement(By.id("delete-note-button"));
		deleteNote.click();
		clickResultSuccess();
		selectNoteTab();

		List<WebElement> thNoteTitle = driver.findElements(By.id("th-note-title"));
		Assertions.assertTrue(thNoteTitle.isEmpty());
	}

	private void selectNoteTab() {
		WebElement selectNotesTab = driver.findElement(By.id("nav-notes-tab"));
		selectNotesTab.click();
	}

	private void clickResultSuccess() {
		WebElement resultSuccess = driver.findElement(By.id("result-success"));
		resultSuccess.click();
	}

	private void inputNote(String noteTitleParam, String noteDescriptionParam) {
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		noteTitle.click();
		noteTitle.sendKeys(noteTitleParam);

		WebElement noteDescription = driver.findElement(By.id("note-description"));
		noteDescription.click();
		noteDescription.sendKeys(noteDescriptionParam);
	}
}
