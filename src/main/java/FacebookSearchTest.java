/**
 * @author Johan Lund
 * @project FacebookTests
 * @date 2023-04-26
 */

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;

public class FacebookSearchTest {

    static Logger logger = LoggerFactory.getLogger(FacebookSearchTest.class);



    public static void main(String[] args) throws Exception {
        logger.debug("Starting my test");


        String email = null;
        String password = null;
        try {
            // Read the email and password from config.json
            Gson gson = new Gson();
            JsonElement config = gson.fromJson(new FileReader("config.json"), JsonElement.class);
            email = config.getAsJsonObject().get("email").getAsString();
            password = config.getAsJsonObject().get("password").getAsString();
        } catch (IOException e) {
            // Handle any exceptions that might occur while reading the file
            logger.info("Logback initialized", e);
            System.exit(1);
        }
        WebDriver driver = null;
        try {
            // Set the path to the ChromeDriver executable
            System.setProperty("/users/johanlund/Downloads/Chromedriver_mac64\\chromedriver", "path/to/chromedriver");

            // Creates an instance of ChromeOptions and add the desired option
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-notifications");
            options.addArguments("--remote-debugging-port=9222");

            // Launch ChromeDriver
            driver = new ChromeDriver(options);
            driver.manage().window().maximize();
        } catch (WebDriverException e) {
            // Handle any exceptions that might occur while launching the ChromeDriver
            e.printStackTrace();
        }


        // Go to the Facebook login page
        assert driver != null;
        driver.get("https://www.facebook.com/login.php");

        WebElement button = driver.findElement(By.xpath("//button[@data-testid='cookie-policy-manage-dialog-accept-button']"));
        button.click();

        // Enter email address
        WebElement emailField = driver.findElement(By.id("email"));
        emailField.sendKeys(email);

        // Enter password
        WebElement passwordField = driver.findElement(By.id("pass"));
        passwordField.sendKeys(password);

        // Click the "Log In" button
        WebElement loginButton = driver.findElement(By.name("login"));
        loginButton.click();

        // Wait for the login process to complete
        Thread.sleep(5000);

        // Click on the search button in the top left corner.
        WebElement searchClick = driver.findElement(new By.ByXPath("/html/body/div[1]/div/div[1]/div/div[2]/div[3]/div/div/div/div/div/label"));
        searchClick.click();
        Thread.sleep(3000);
        // Enter the string "Luleå Tekniska Universitet" to search for.
        WebElement searchFor = driver.findElement(new By.ByXPath("/html/body/div[1]/div/div[1]/div/div[2]/div[3]/div/div/div[1]/div/div/label/input"));
        searchFor.sendKeys("Luleå Tekniska Universitet");
        Thread.sleep(3000);
        // Click on the button that makes the actual search.
        WebElement buttonSearchClick = driver.findElement(new By.ByCssSelector("#jsc_c_2 > div > a > div"));
        buttonSearchClick.click();
        Thread.sleep(3000);
        try {
            int i = 0;
            boolean siteFound = false;
            while (i < 4 && !siteFound) {
                driver.navigate().refresh();
                Thread.sleep(1000);
                if (driver.getPageSource().contains("Skandinaviens nordligaste tekniska universitet")) {
                    siteFound = true;
                    System.out.println("The site was found!");
                }
                i++;

            }
            if (!siteFound) {
                System.out.println("The site was not found!");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Thread.sleep(5000);
        // Close the browser
        driver.quit();
    }
}
