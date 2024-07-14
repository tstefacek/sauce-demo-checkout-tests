package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class OrderCompletedPage {
    WebDriver driver;

    @FindBy(css = "[data-test='complete-header']")
    private WebElement completeHeader;

    @FindBy(css = "[data-test='complete-text']")
    private WebElement completeText;

    public OrderCompletedPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isPageOpened() {
        return driver.getCurrentUrl().contains("checkout-complete.html");
    }

    public String getCompleteHeader() {
        return completeHeader.getText();
    }

    public String getCompleteText() {
        return completeText.getText();
    }

}
