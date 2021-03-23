package dev.baybay.lobiani.admin

import geb.spock.GebSpec
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * Base class for specs involving access to admin (protected) area.
 * Automatically takes care of login/logout actions for the spec (if it's stepwise) or feature (if spec is not stepwise).
 */
class AdminSpec extends GebSpec {

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
