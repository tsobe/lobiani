package app

import geb.spock.GebSpec

class GebTest extends GebSpec {

    def "aloha"() {
        when:
        go "http://gebish.org"

        then:
        assert title == "Geb - Very Groovy Browser Automation"

        when:
        $("div.menu a.manuals").click()
        waitFor { !$("#manuals-menu").hasClass("animating") }

        $("#manuals-menu a")[0].click()

        then:
        assert title.startsWith("The Book Of Geb")
    }
}
