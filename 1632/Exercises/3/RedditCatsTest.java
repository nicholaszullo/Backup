// Generated by Selenium IDE
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;
public class RedditCatsTest {
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;
  @Before
  public void setUp() {
    driver = new FirefoxDriver();
    js = (JavascriptExecutor) driver;
    vars = new HashMap<String, Object>();
  }
  @After
  public void tearDown() {
    driver.quit();
  }
  @Test
  public void fUNJOINBUTTONEXISTS() {
    driver.get("https://www.reddit.com/r/cats/");
    driver.manage().window().setSize(new Dimension(1550, 838));
    assertThat(driver.findElement(By.xpath("//button[contains(.,\'Join\')]")).getText(), is("Join"));
  }
  @Test
  public void fUNRULE3() {
    driver.get("https://www.reddit.com/r/cats/");
    driver.manage().window().setSize(new Dimension(1000, 1000));
    assertThat(driver.findElement(By.cssSelector(".\\_8ZLJI1-ZiP7pHJ_yO1L4Z:nth-child(3) .tbIApBd2DM_drfZQJjIum")).getText(), is("No NSFW, animal abuse, or cruelty"));
  }
  @Test
  public void fUNRULES11ITEMS() {
    driver.get("https://www.reddit.com/r/cats/");
    driver.manage().window().setSize(new Dimension(737, 705));
    {
      List<WebElement> elements = driver.findElements(By.xpath("//div[2]/div[11]/div/div/div"));
      assert(elements.size() > 0);
    }
    {
      List<WebElement> elements = driver.findElements(By.xpath("//div[2]/div[12]/div/div/div"));
      assert(elements.size() == 0);
    }
  }
  @Test
  public void fUNSEARCHSMELLYCAT() {
    driver.get("https://www.reddit.com/");
    driver.manage().window().setSize(new Dimension(550, 696));
    driver.findElement(By.id("header-search-bar")).sendKeys("smelly cat");
    driver.findElement(By.id("header-search-bar")).sendKeys(Keys.ENTER);
    assertThat(driver.findElement(By.cssSelector(".\\_3j9XjJayuKq7dJ8huVnCuS")).getText(), is("smelly cat"));
  }
  @Test
  public void fUNSIGNUPLINK() {
    driver.get("https://www.reddit.com/r/cats");
    driver.manage().window().setSize(new Dimension(1550, 838));
    {
      WebElement element = driver.findElement(By.linkText("Sign Up"));
      String attribute = element.getAttribute("href");
      vars.put("x", attribute);
    }
    assertEquals(vars.get("x").toString(), "https://www.reddit.com/register/?dest=https%3A%2F%2Fwww.reddit.com%2Fr%2Fcats%2F");
  }
  @Test
  public void fUNTITLE() {
    driver.get("https://www.reddit.com/r/cats/");
    assertThat(driver.getTitle(), is("Cats"));
  }
}