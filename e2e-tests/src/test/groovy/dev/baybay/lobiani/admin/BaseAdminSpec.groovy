package dev.baybay.lobiani.admin

import geb.spock.GebSpec
import spock.lang.Shared
import spock.lang.Stepwise

class BaseAdminSpec extends GebSpec {

    @Shared
    def sessionManager = new SessionManager(this)

    void setup() {
        if (!isSpecStepwise()) {
            sessionManager.start()
        }
    }

    void cleanup() {
        if (!isSpecStepwise()) {
            sessionManager.stop()
        }
    }

    void setupSpec() {
        if (isSpecStepwise()) {
            sessionManager.start()
        }
    }

    void cleanupSpec() {
        if (isSpecStepwise()) {
            sessionManager.stop()
        }
    }

    private isSpecStepwise() {
        this.class.getAnnotation(Stepwise) != null
    }
}
