import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver

waiting {
    timeout = 8
}

environments {
    remote {
        driver = {
            def capabilities = new DesiredCapabilities()
            capabilities.browserName = "chrome"
            capabilities.version = "80.0"
            capabilities.setCapability("enableVNC", true)
            capabilities.setCapability("enableVideo", Boolean.getBoolean("remote.webdriver.enableVideo"))
            capabilities.setAcceptInsecureCerts(Boolean.getBoolean("remote.webdriver.acceptInsecure"))
            new RemoteWebDriver(URI.create(System.getProperty("remote.webdriver.url")).toURL(), capabilities)
        }
    }
    localFirefox {
        driver = { new FirefoxDriver() }
    }
}

