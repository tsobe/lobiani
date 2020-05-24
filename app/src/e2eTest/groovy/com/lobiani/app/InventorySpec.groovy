package com.lobiani.app

import geb.spock.GebSpec

class InventorySpec extends GebSpec {

    def "hello"() {
        when:
        go "hello?name=Gela"

        then:
        $("body").text() == "Hello Gela"
    }
}
