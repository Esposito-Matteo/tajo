package org.apache.tajo.tests;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.tajo.QueryId;
import org.apache.tajo.client.QueryClientImpl;
import org.apache.tajo.client.SessionConnection;
import org.apache.tajo.error.Errors;
import org.apache.tajo.ipc.ClientProtos;
import org.apache.tajo.service.ServiceTracker;
import org.apache.tajo.service.ServiceTrackerFactory;
import org.apache.tajo.util.KeyValueSet;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
@RunWith(value = Parameterized.class)
public class executeQueryTest extends QueryTestCaseBase{
    Pair<Errors.ResultCode,String> pair;

    public executeQueryTest(Pair<Errors.ResultCode, String> pair) {
        this.pair = pair;
    }

    @Parameterized.Parameters
    public  static Collection<?> getParameters() {

        return Arrays.asList(new ImmutablePair<>(Errors.ResultCode.OK,"SELECT * FROM table3;"),
                new ImmutablePair<>(Errors.ResultCode.INTERNAL_ERROR,""),
                new ImmutablePair<>(Errors.ResultCode.UNDEFINED_COLUMN,"INSERT INTO table3 values (2, A);"),
                new ImmutablePair<>(null,null));
    }

    @Before
    public void setUp() {
        ClientProtos.SubmitQueryResponse res = client.executeQuery("CREATE DATABASE \"TestSelectQuery\"");
        ClientProtos.SubmitQueryResponse res2 = client.executeQuery("CREATE TABLE table3 (c1 int, c2 varchar);");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testMethod() {
        try {
            ServiceTracker serviceTracker = ServiceTrackerFactory.get(getConf());
            QueryClientImpl queryClient = new QueryClientImpl(new SessionConnection(serviceTracker, getCurrentDatabase(), new KeyValueSet()));
            ClientProtos.SubmitQueryResponse res;
            res = queryClient.executeQuery(pair.getRight());
            Assert.assertEquals(pair.getLeft(), res.getState().getReturnCode());
        }catch(NullPointerException e){
            Assert.assertEquals(pair.getLeft(), null);
        }

    }
}
