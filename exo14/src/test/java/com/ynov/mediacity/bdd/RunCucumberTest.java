package com.ynov.mediacity.bdd;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

// Lance les scenarios .feature avec JUnit 5
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
class RunCucumberTest {
}
