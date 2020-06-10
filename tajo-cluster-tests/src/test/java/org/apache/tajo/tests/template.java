package org.apache.tajo.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import java.util.Collection;
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(Parameterized.class)
public class template {




        @Parameterized.Parameters
        public  static Collection<?> getParameters() {

            return null;
        }

        @Before
        public void setUp() {
        }

        @After
        public void tearDown() {
        }

        @Test
        public void testMethod() {
        }

    }

