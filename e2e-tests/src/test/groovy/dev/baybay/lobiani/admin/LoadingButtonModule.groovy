package dev.baybay.lobiani.admin

import geb.module.FormElement
import geb.navigator.Navigator

/**
 * Module for a "loadable" button. {@link LoadingButtonModule#click() Clicking} on it will wait until
 * loading is completed.
 */
class LoadingButtonModule extends geb.Module {

    static content = {
        loader(required: false) { $(".v-btn__loader") }
    }

    private def isLoading() {
        !loader.empty
    }

    @Override
    Navigator click() {
        waitFor { !isLoading() && module(FormElement).enabled }
        return super.click()
    }
}
