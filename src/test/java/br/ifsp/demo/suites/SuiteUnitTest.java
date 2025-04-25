package br.ifsp.demo.suites;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectPackages({"br.ifsp.demo.application.service"})
@SuiteDisplayName("All unit tests")
@IncludeTags({"UnitTest"})
public class SuiteUnitTest { }
