package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class FinalCheckoutPage {

    private final WebDriver driver;

    @FindBy(css = ".summary_value_label[data-test='payment-info-value']")
    private WebElement paymentInfo;

    @FindBy(css = ".summary_value_label[data-test='shipping-info-value']")
    private WebElement shippingInfo;

    @FindBy(css = ".summary_total_label[data-test='total-label']")
    private WebElement total;

    @FindBy(id = "finish")
    private WebElement finish;

    public FinalCheckoutPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isPageOpened() {
        return driver.getCurrentUrl().contains("checkout-step-two.html");
    }

    public String getPaymentInfoValue() {
        return paymentInfo.getText();
    }

    public String getShippingInfoValue() {
        return shippingInfo.getText();
    }

    public String getTotalLabel() {
        return total.getText();
    }

    public void finishCheckout() {
        finish.click();
    }


}
