package dev.baybay.lobiani.testutil

class TestDataHelper {

    static def newInventoryItem(slug = "the-matrix-trilogy") {
        return [slug: slug]
    }

    static def newProduct(slug = "the-matrix-trilogy") {
        [slug       : slug,
         title      : "$slug-title",
         description: "$slug-description"]
    }
}
