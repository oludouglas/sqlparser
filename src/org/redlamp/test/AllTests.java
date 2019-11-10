package org.redlamp.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses(value = { ScannerTests.class, TokenizerTest.class, ParserTest.class })
public class AllTests {

}
