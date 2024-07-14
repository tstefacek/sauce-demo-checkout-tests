package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class ProductsPage {
    private final WebDriver driver;

    @FindBy(css = ".shopping_cart_link")
    private WebElement cartButton;

    @FindAll({@FindBy(id = "item_0_title_link"), @FindBy(id = "item_1_title_link"),
            @FindBy(id = "item_2_title_link"), @FindBy(id = "item_3_title_link"),
            @FindBy(id = "item_4_title_link"), @FindBy(id = "item_5_title_link"),})
    private List<WebElement> allInventoryItemsUsingFindAll;

    @FindBys({
            @FindBy(className = "inventory_item"),
            @FindBy(className = "inventory_item_name"),
    })
    private List<WebElement> allInventoryItemsUsingFindBys;

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isPageOpened() {
        return driver.getCurrentUrl().contains("inventory.html");
    }

    public void navigateToItemPage(String itemName) {
        WebElement itemLink = driver.findElement(By.linkText(itemName));
        itemLink.click();
    }

    public void navigateToCart() {
        cartButton.click();
    }

    public List<WebElement> getAllInventoryItemsUsingFindAll() {
        return allInventoryItemsUsingFindAll;
    }

    public List<WebElement> getAllInventoryItemsUsingFindBys() {
        return allInventoryItemsUsingFindBys;
    }

}
