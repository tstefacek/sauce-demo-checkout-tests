import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import io.qameta.allure.Link;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.qameta.allure.testng.Tag;
import io.qameta.allure.testng.Tags;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.*;
import utils.DriverFactory;


@Epic("Checkout flow on Saucedemo")
public class CheckoutFlowTest {
    private Logger logger;
    private static final String SITE = "https://www.saucedemo.com";
    private WebDriver driver;
    private LoginPage loginPage;
    private ProductsPage productsPage;
    private ItemPage itemPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private FinalCheckoutPage finalCheckoutPage;
    private OrderCompletedPage orderCompletedPage;

    @BeforeClass
    public void setUp() {
        logger = LoggerFactory.getLogger(this.getClass());

        logger.trace("Setting up WebDriver...");

        driver = DriverFactory.createDriver(DriverFactory.BrowserType.CHROME);

        logger.trace("Setting up page classes for the Page Object Model...");
        loginPage = new LoginPage(driver);
        productsPage = new ProductsPage(driver);
        itemPage = new ItemPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        finalCheckoutPage = new FinalCheckoutPage(driver);
        orderCompletedPage = new OrderCompletedPage(driver);

        logger.trace("Completed set up of WebDriver and page classes...");

        driver.get(SITE);

        logger.debug("Navigated to site {}" , SITE);
    }

    @Feature("Login flow")
    @Story("Login")
    @Description("Test to verify login functionality")
    @Link("https://www.saucedemo.com/")
    @Tag("login")
    @io.qameta.allure.Owner("John Smith")
    @Severity(SeverityLevel.BLOCKER)
    @Step("Login and verify")
    @Test
    public void testLogin() {
        logger.debug("***** Starting testLogin *****");

        loginPage.login("standard_user", "secret_sauce");

        if(!productsPage.isPageOpened()) {
            logger.error("Login failed! Incorrect username or password.");
        }
        Assert.assertTrue(productsPage.isPageOpened(), "Login failed!");

        logger.info("User logged in successfully.");
        logger.info("Username: {} and Password: {}", "standard_user", "secret_sauce");
    }

    @Feature("Add items flow")
    @Story("Add items")
    @Description("Test to add a backpack to the cart")
    @Link("https://www.saucedemo.com/inventory.html")
    @Tags({@Tag("items"), @Tag("add item")})
    @Owner("Sarah Jones")
    @Severity(SeverityLevel.NORMAL)
    @Step("Add item to cart")
    @Test(dependsOnMethods = "testLogin")
    public void testAddBackpackToCart() {
        logger.debug("***** Starting testAddBackpackToCart *****");

        productsPage.navigateToItemPage("Sauce Labs Backpack");
        itemPage.addToCart();

        if(!itemPage.getButtonText().equals("Remove")) {
            logger.warn("Button text on backpack item page did not change");
        }

        Assert.assertEquals(itemPage.getButtonText(), "Remove",
                "Button text did not change");

        Allure.step("Navigate back to products page");

        logger.info("Added Sauce Labs Backpack to cart.");

        driver.navigate().back();

        logger.info("Navigated back to the products page after adding one item to cart.");
    }

    @Feature("Add items flow")
    @Story("Add items")
    @Description("Test to add a fleece jacket to the cart")
    @Link("https://www.saucedemo.com/inventory.html")
    @Tags({@Tag("items"), @Tag("add item")})
    @Owner("Sarah Jones")
    @Severity(SeverityLevel.NORMAL)
    @Step("Add item to cart")
    @Test(dependsOnMethods = "testAddBackpackToCart")
    public void testAddFleeceJacketToCart() {
        logger.debug("***** Starting testAddFleeceJacketToCart *****");

        productsPage.navigateToItemPage("Sauce Labs Fleece Jacket");
        itemPage.addToCart();

        if(!itemPage.getButtonText().equals("Remove")) {
            logger.warn("Button text on fleece jacket item page did not change");
        }

        Assert.assertEquals(itemPage.getButtonText(), "Remove",
                "Button text did not change");

        Allure.step("Navigate back to products page");

        logger.info("Added Sauce Labs Fleece jacket to cart.");

        driver.navigate().back();

        logger.info("Navigated back to the products page after adding second item to cart.");
    }

    @Feature("View cart flow")
    @Story("View cart")
    @Description("Test to verify the cart contents")
    @Link("https://www.saucedemo.com/cart.html")
    @Tags({@Tag("cart"), @Tag("checkout")})
    @Owner("John Smith")
    @Severity(SeverityLevel.CRITICAL)
    @Step("Validate items in cart")
    @Test(dependsOnMethods = {"testAddBackpackToCart", "testAddFleeceJacketToCart"})
    public void testCart() {
        logger.debug("***** Starting testCart *****");

        Allure.step("Navigate to cart and verify state", step -> {
            productsPage.navigateToCart();

            if(!cartPage.isPageOpened()) {
                logger.error("Cart page not loaded.");
            }

            Assert.assertTrue(cartPage.isPageOpened(), "Cart page is not loaded");
            Assert.assertEquals(cartPage.getCartItemCount(), "2", "Incorrect number of items");
            Assert.assertEquals(cartPage.getContinueButtonText(), "Checkout", "Incorrect button on cart page");

            Allure.addAttachment("Cart item count: ", cartPage.getCartItemCount());
        });

        if (!cartPage.itemInCart("Sauce Labs Fleece Jacket") || !cartPage.itemInCart("Sauce Labs Backpack")) {
            logger.warn("Either {} or {} not foind in cart.", "Sauce Labs Backpack", "Sauce Labs Fleece Jacket");
        }

        Allure.step("Verify items in cart", step -> {
            Assert.assertTrue(cartPage.itemInCart("Sauce Labs Fleece Jacket"));
            Assert.assertTrue(cartPage.itemInCart("Sauce Labs Backpack"));
        });

        logger.info("Validated items in cart.");
    }

    @Feature("Checkout flow")
    @Story("Checkout")
    @Description("Test to verify the checkout functionality")
    @Link("https://www.saucedemo.com/checkout-step-one.html")
    @Tag("checkout")
    @Flaky
    @Severity(SeverityLevel.MINOR)
    @Step("Verify checkout page")
    @Test(dependsOnMethods = "testCart")
    public void testCheckout() {
        logger.debug("***** Starting testCheckout *****");

        Allure.step("Navigate to checkout and enter details", step -> {
            cartPage.continueCheckout();

            if(!checkoutPage.isPageOpened()) {
                logger.error("First checkout page not loaded.");
            }

            Assert.assertTrue(checkoutPage.isPageOpened(), "Checkout page is not loaded");

            checkoutPage.enterDetails("John", "Smith", "123456");
        });

        Allure.step("Verify entered details", step -> {
            Assert.assertEquals(checkoutPage.getFirstNameValue(), "John", "Incorrect first name");
            Assert.assertEquals(checkoutPage.getLastNameValue(), "Smith", "Incorrect last name");
            Assert.assertEquals(checkoutPage.getZipCodeValue(), "123456", "Incorrect zip code");
        });

        logger.info("First checkout page completed.");
    }

    @Feature("Checkout flow")
    @Story("Checkout")
    @Description("Test to verify checkout functionality")
    @Link("https://www.saucedemo.com/checkout-step-two.html")
    @Tag("checkout")
    @Owner("John Smith")
    @Test(dependsOnMethods = "testCheckout")
    @Severity(SeverityLevel.CRITICAL)
    @Step("Verify final checkout page")
    public void testFinalCheckout() {
        logger.debug("***** Starting testFinalCheckout *****");

        checkoutPage.continueCheckout();

        if(!finalCheckoutPage.isPageOpened()) {
            logger.error("Second checkout page not loaded.");
        }

        Assert.assertTrue(finalCheckoutPage.isPageOpened(), "Checkout page is not loaded");

        Assert.assertEquals(finalCheckoutPage.getPaymentInfoValue(), "SauceCard #31337");
        Assert.assertEquals(finalCheckoutPage.getShippingInfoValue(), "Free Pony Express Delivery!");
        Assert.assertEquals(finalCheckoutPage.getTotalLabel(), "Total: $86.38");

        logger.info("Second checkout page completed.");
    }

    @Feature("Checkout flow")
    @Story("Order Completion")
    @Description("Test to verify order completion functionality")
    @Link("https://www.saucedemo.com/checkout-complete.html")
    @Tags({@Tag("order completion"), @Tag("checkout")})
    @Owner("Alice Cooper")
    @Flaky
    @Severity(SeverityLevel.TRIVIAL)
    @Step("Verify order completion page")
    @Test(dependsOnMethods = "testFinalCheckout")
    public void testOrderCompletion() {
        logger.debug("***** Starting testOrderCompletion *****");

        finalCheckoutPage.finishCheckout();

        if(!orderCompletedPage.isPageOpened()) {
            logger.error("Order completed page not loaded.");
        }

        Assert.assertTrue(orderCompletedPage.isPageOpened(), "Order confirmation page is not loaded");

        Assert.assertEquals(orderCompletedPage.getCompleteHeader(), "Thank you for your order!");
        Assert.assertEquals(orderCompletedPage.getCompleteText(), "Your order has been dispatched, and will arrive just as fast as the pony can get there!");

        logger.info("Order checkout flow completed.");
    }

    @AfterClass
    public void tearDown() {
        logger.trace("Quitting WebDriver...");

        if (driver != null) {
            driver.quit();
        }

        logger.trace("Quit WebDriver....");
    }
}
