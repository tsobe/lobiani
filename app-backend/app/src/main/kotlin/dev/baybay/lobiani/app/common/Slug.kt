package dev.baybay.lobiani.app.common

import javax.validation.constraints.AssertTrue

data class Slug(val value: String) {

    @AssertTrue(message = "Slug must consist of lowercase alpha-numeric and dash('-') characters")
    fun hasValidSlug(): Boolean {
        return value.matches("^[a-z0-9-]+$".toRegex())
    }
}
