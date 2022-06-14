import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import java.io.IOException;
import java.util.List;

public class Web_Order {
    public static void main(String[] args) throws InterruptedException, IOException {

        //1. Launch Chrome browser.
        System.setProperty("webdriver.chrome.driver", "/Users/sayalimammadova/Desktop/BrowserDrivers/chromedriver");
        WebDriver driver = new ChromeDriver();

        //2. Navigate to http://secure.smartbearsoftware.com/samples/TestComplete12/WebOrders/Login.aspx
        driver.navigate().to("http://secure.smartbearsoftware.com/samples/TestComplete12/WebOrders/Login.aspx");
        driver.manage().window().maximize();

        //3. Login using Tester as username and test as password
        driver.findElement(By.id("ctl00_MainContent_username")).sendKeys("Tester");
        driver.findElement(By.id("ctl00_MainContent_password")).sendKeys("test");
        driver.findElement(By.id("ctl00_MainContent_login_button")).click();

        //4. Click on Order link
        driver.findElement(By.linkText("Order")).click();

        //5. Enter a random product quantity between 1 and 100
        int quantity = (int)(Math.random() * 99)+1;
        driver.findElement(By.name("ctl00$MainContent$fmwOrder$txtQuantity")).sendKeys(Keys.BACK_SPACE);
        driver.findElement(By.name("ctl00$MainContent$fmwOrder$txtQuantity")).sendKeys(quantity+"");
        Thread.sleep(2000);

        //6. Click on Calculate and verify that the Total value is correct.
        driver.findElement(By.xpath("//input[@value='Calculate']")).click();

        //Price per unit is 100.  The discount of 8 % is applied to quantities of 10+.
        double total = Double.parseDouble(driver.findElement(By.id("ctl00_MainContent_fmwOrder_txtTotal")).getAttribute("value"));
if(quantity>=10)
    Assert.assertTrue(total == (quantity * 100 * 0.92));
else
    Assert.assertTrue(total == quantity * 100);

//7. Generate and enter random first name and last name.
//8. Generate and Enter random street address
//9. Generate and Enter random city
//10. Generate and Enter random state
//11. Generate and Enter a random 5 digit zip code
//
//EXTRA: As an extra challenge, for steps 7-11 download 1000 row of corresponding realistic data
// from mockaroo.com in a csv format and load it to your program and use the random set of data from there each time.
        List<String[]> users = Utility.read("MOCK_DATA.csv");
        String[] user = users.get(randNumber(0, users.size()));
        driver.findElement(By.id("ctl00_MainContent_fmwOrder_txtName")).sendKeys(user[0]);
        driver.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox2")).sendKeys(user[1]);
        driver.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox3")).sendKeys(user[2]);
        driver.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox4")).sendKeys(user[3]);
        driver.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox5")).sendKeys(user[4]);

//. Select the card type randomly. On each run your script should select a random type.
        int cardType = randNumber(0,3);

        //Generate and enter the random card number:
        //      If Visa is selected, the card number should start with 4.
        //      If MasterCard is selected, card number should start with 5.
        //      If American Express is selected, card number should start with 3.
        //      Card numbers should be 16 digits for Visa and MasterCard, 15 for American Express.
        String Visa = "4" + (100000000000000L + ((long) (Math.random() * 900000000000000L)));
        String masterCard = "5" + (100000000000000L + ((long) (Math.random() * 900000000000000L)));
        String americanExpress = "3" + (10000000000000L + ((long) (Math.random() * 90000000000000L)));
        if (cardType == 0){

            driver.findElement(By.id("ctl00_MainContent_fmwOrder_cardList_0")).click();
            driver.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox6")).sendKeys( Visa + Keys.ENTER);
        }else if(cardType == 1){

            driver.findElement(By.id("ctl00_MainContent_fmwOrder_cardList_1")).click();
            driver.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox6")).sendKeys( masterCard + Keys.ENTER);
        }else if (cardType == 2){

            driver.findElement(By.id("ctl00_MainContent_fmwOrder_cardList_2")).click();
            driver.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox6")).sendKeys( americanExpress + Keys.ENTER);
        }

        //14. Enter a valid expiration date (newer than the current date)
        driver.findElement(By.id("ctl00_MainContent_fmwOrder_InsertButton")).click();
       String month = String.valueOf(randNumber(1,12));
       //month should be 2 digits
       if(month.length() == 1)
          month = "0" + month;
       String year = String.valueOf(randNumber(23,27));
        driver.findElement(By.name("ctl00$MainContent$fmwOrder$TextBox1")).sendKeys(month+"/"+year);
        Thread.sleep(2000);

        //Click on Process
        driver.findElement(By.id("ctl00_MainContent_fmwOrder_InsertButton")).click();
        Thread.sleep(2000);

//16. Verify that “New order has been successfully added” message appeared on the page.
        String actual = driver.findElement(By.tagName("strong")).getText();
        String expected = "New order has been successfully added.";
        Assert.assertEquals(actual, expected);

        //17. Log out of the application.
        driver.findElement(By.id("ctl00_logout")).click();
        Thread.sleep(2000);
        driver.close();

    }

// method for select random number within given range
public static int randNumber (int min, int max){
        int range = max-min;
        return (int)(Math.random() * range) + min;

}
}
