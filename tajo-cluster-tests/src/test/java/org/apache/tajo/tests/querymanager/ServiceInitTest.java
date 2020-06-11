package org.apache.tajo.tests.querymanager;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.hadoop.conf.Configuration;
import org.apache.tajo.conf.TajoConf;
import org.apache.tajo.master.QueryManager;
import org.apache.tajo.master.TajoMaster;
import org.apache.tajo.pullserver.PullServerConstants;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.powermock.utils.Asserts;

import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class ServiceInitTest {

    private Pair<Boolean,TajoConf> pair;
    private  TajoMaster tajoMaster;

    public ServiceInitTest(Pair<Boolean, TajoConf> pair) {
        this.pair = pair;
    }

    @Parameterized.Parameters
    public  static Collection<?> getParameters() {
        TajoConf conf = new TajoConf();
        return Arrays.asList(new ImmutablePair<>(true,conf),
                new ImmutablePair<>(false,conf));
    }

    @Before
    public void setUp() throws Exception {

        TajoConf tajoConf = pair.getRight();
            tajoMaster = new TajoMaster();
            tajoMaster.serviceInit(tajoConf);
    }

    @After
    public void tearDown() throws Exception {
        tajoMaster.serviceStop();
        tajoMaster.close();
    }

    @Test
    public void testMethod() {
      TajoConf tajoConf = pair.getRight();
      Boolean expected = pair.getLeft();
      Boolean result;
      try {

          QueryManager queryManager = new QueryManager(tajoMaster.getContext());
          if(!expected){
              queryManager.init(null);

          }else {
              queryManager.init(tajoConf);
          }
          result = true;
      }catch(Exception e){
          e.printStackTrace();
          result = false;
        }

        Assert.assertEquals(expected,result);
    }


}
