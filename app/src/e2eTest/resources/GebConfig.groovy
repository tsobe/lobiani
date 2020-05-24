import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver

waiting {
    timeout = 3
}

environments {
    remote {
        driver = {
            def capabilities = new DesiredCapabilities()
            capabilities.browserName = "chrome"
            capabilities.version = "80.0"
            capabilities.setCapability("enableVNC", true)
            capabilities.setCapability("enableVideo", false)

            new RemoteWebDriver(getWebDriverURL(), capabilities)
        }
    }
}

static def getWebDriverURL() {
    URI.create(System.getProperty("remote.webdriver.url", "http://localhost:4444/wd/hub")).toURL()
}
