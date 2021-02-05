package dev.baybay.lobiani.testutil

import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class ActualCaptor<T> extends TypeSafeMatcher<T> {

    T actualItem
    Class<T> expectedType

    ActualCaptor(Class<T> expectedType) {
        super(expectedType)
        this.expectedType = expectedType
    }

    @Override
    protected boolean matchesSafely(T item) {
        actualItem = item
        return true
    }

    @Override
    void describeTo(Description description) {
        description.appendText String.format("item of type [%s]", expectedType.getSimpleName())
    }
}
