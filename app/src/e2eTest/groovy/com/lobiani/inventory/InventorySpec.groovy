package com.lobiani.inventory

import geb.spock.GebSpec

class InventorySpec extends GebSpec {

    def "new inventory item defined and then deleted"() {
        given:
        def slug = "the-matrix-trilogy-blu-ray"
        def definitionsPage = to InventoryItemDefinitionsPage


        when:
        definitionsPage.openNewItemDefinitionPage()

        then:
        at NewInventoryItemDefinitionPage


        def newDefinitionPage = page as NewInventoryItemDefinitionPage

        when:
        newDefinitionPage.enterSlug(slug)
        newDefinitionPage.save()

        then:
        at definitionsPage
        definitionsPage.hasDefinition slug


        when:
        definitionsPage.deleteDefinition slug

        then:
        at definitionsPage
        !definitionsPage.hasDefinition(slug)


        cleanup:
        if (definitionsPage?.hasDefinition(slug)) {
            definitionsPage.deleteDefinition slug
        }
    }

}
