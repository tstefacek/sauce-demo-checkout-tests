package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ItemPage {

    @FindBy(css = ".btn_inventory")
    private WebElement addToCartButton;

    public ItemPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void addToCart() {
        addToCartButton.click();
    }

    public String getButtonText() {
        return addToCartButton.getText();
    }
}
