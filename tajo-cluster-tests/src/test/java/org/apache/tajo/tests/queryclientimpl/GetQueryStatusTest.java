package org.apache.tajo.tests.queryclientimpl;

import org.apache.tajo.QueryId;
import org.apache.tajo.TajoProtos;
import org.apache.tajo.client.QueryClientImpl;
import org.apache.tajo.client.SessionConnection;
import org.apache.tajo.engine.query.QueryContext;
import org.apache.tajo.exception.QueryNotFoundException;
import org.apache.tajo.ipc.ClientProtos;
import org.apache.tajo.master.QueryManager;
import org.apache.tajo.plan.logical.LogicalRootNode;
import org.apache.tajo.service.ServiceTracker;
import org.apache.tajo.service.ServiceTrackerFactory;
import org.apache.tajo.session.Session;
import org.apache.tajo.tests.QueryTestCaseBase;
import org.apache.tajo.util.KeyValueSet;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.util.Collection;

public class GetQueryStatusTest extends QueryTestCaseBase {
    private QueryClientImpl queryClient;

    public GetQueryStatusTest() {
        ServiceTracker serviceTracker = ServiceTrackerFactory.get(getConf());
        queryClient = new QueryClientImpl(new SessionConnection(serviceTracker, getCurrentDatabase(), new KeyValueSet()));
    }



    @Before
    public void setUp() throws Exception {

        /*  buikding Quey ID finished */
        ClientProtos.SubmitQueryResponse res = queryClient.executeQuery("CREATE DATABASE \"TestSelectQuery\"");
        ClientProtos.SubmitQueryResponse res2 = queryClient.executeQuery("CREATE TABLE table3 (c1 int, c2 char);");
        ClientProtos.SubmitQueryResponse res3 = queryClient.executeQuery("INSERT INTO table3 values (3, 'A');");
    }/*
        //ClientProtos.SubmitQueryResponse res6 = client.executeQuery("INSERT INTO table3 values (3, A);");
        //ClientProtos.SubmitQueryResponse res7 = client.executeQuery("INSERT INTO table3 values (3, \"as\");");
        ClientProtos.SubmitQueryResponse res4 = queryClient.executeQuery("SELECT * FROM table3;");
        ClientProtos.SubmitQueryResponse res5 = queryClient.executeQuery("");
        QueryId queryId = new QueryId(res4.getQueryId());
        QueryId failedQueryId = new QueryId(res5.getQueryId());

        Query in Progress
        QueryContext context = new QueryContext(getConf());
        QueryManager queryManager = testingCluster.getMaster().getContext().getQueryJobManager();
        Session session = new Session("sessionTest", res3.getUserName(), getCurrentDatabase());
        QueryId queryInProgressID = queryManager.scheduleQuery(
                session,
                context,
                "SELECT * FROM table3;",
                null,
                new LogicalRootNode(2)).getQueryId();

        queryManager.stopQuery(queryInProgressID);
        ServiceTracker serviceTracker = ServiceTrackerFactory.get(getConf());

        QueryClientImpl queryClient = new QueryClientImpl(new SessionConnection(serviceTracker, getCurrentDatabase(), new KeyValueSet()));
        String result = queryClient.getQueryStatus(new QueryId("a", 74293847)).getState().name();
        System.out.println("test");

    }
*/
    @Test
    public void invalidQueryId() {
        try {
            queryClient.getQueryStatus(new QueryId("a", 74293847)).getState().name();
            Assert.fail();
        } catch (QueryNotFoundException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void validQueryId() {
        ClientProtos.SubmitQueryResponse res4 = queryClient.executeQuery("SELECT * FROM table3;");
        try {
            String querySuccededStrign = queryClient.getQueryStatus(new QueryId(res4.getQueryId())).getState().name();
            Assert.assertTrue(querySuccededStrign.equals(TajoProtos.QueryState.QUERY_SUCCEEDED.toString()));
        } catch (QueryNotFoundException e) {
            Assert.fail();
        }
    }

    @Test
    public void nullQueryId() {
        ClientProtos.SubmitQueryResponse res4 = queryClient.executeQuery("SELECT * FROM table3;");
        try {
            String querySuccededStrign = queryClient.getQueryStatus(null).getState().name();
            Assert.fail();
        } catch (NullPointerException | QueryNotFoundException e) {
            Assert.assertTrue(true);
        }
    }




    @After
    public void tearDown() {
    }

    @Test
    public void testMethod() {
    }

}
