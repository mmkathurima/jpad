package com.timestored.sqldash.chart;

import com.google.common.base.MoreObjects;
import net.jcip.annotations.Immutable;


@Immutable
class ExampleView {
    private final TestCase testCase;
    private final String name;
    private final String description;

    ExampleView(String name, String description, TestCase testCase) {
        this.name = name;
        this.description = description;
        this.testCase = testCase;
    }

    public String getName() {
        return this.name;
    }


    public String getDescription() {
        return this.description;
    }


    public TestCase getTestCase() {
        return this.testCase;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("name", this.name).add("description", this.description).add("testCase", this.testCase).toString();
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\sqldash\chart\ExampleView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */