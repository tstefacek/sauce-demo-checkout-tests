package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CartPage {
    private final WebDriver driver;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(css = ".btn_action")
    private WebElement cartButton;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isPageOpened() {
        return driver.getCurrentUrl().contains("cart.html");
    }

    public String getCartItemCount() {
        return cartBadge.getText();
    }

    public boolean itemInCart(String itemName) {
        WebElement cartItem = driver.findElement(By.xpath("//*[text()='" + itemName + "']"));
        return cartItem != null;
    }

    public String getContinueButtonText() {
        return cartButton.getText();
    }

    public void continueCheckout() {
        cartButton.click();
    }


}
