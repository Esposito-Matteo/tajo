package org.apache.tajo.tests;

import org.apache.tajo.QueryId;
import org.apache.tajo.TajoProtos;
import org.apache.tajo.client.QueryClientImpl;
import org.apache.tajo.client.QueryStatus;
import org.apache.tajo.client.SessionConnection;
import org.apache.tajo.conf.TajoConf;
import org.apache.tajo.engine.query.QueryContext;
import org.apache.tajo.error.Errors;
import org.apache.tajo.ipc.ClientProtos;
import org.apache.tajo.master.QueryManager;
import org.apache.tajo.plan.LogicalPlan;
import org.apache.tajo.plan.logical.LogicalRootNode;
import org.apache.tajo.service.ServiceTracker;
import org.apache.tajo.service.ServiceTrackerFactory;
import org.apache.tajo.session.Session;
import org.apache.tajo.util.KeyValueSet;
import org.apache.tajo.util.history.HistoryWriter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.io.Reader;
import java.sql.ResultSet;
import java.sql.RowId;
import java.util.Collection;

public class getQueryStatusTest extends QueryTestCaseBase{

    public  static Collection<?> getParameters() {

        return null;
    }

    @Before
    public void setUp() throws Exception {

        /*  buikding Quey ID finished */
        ClientProtos.SubmitQueryResponse res = client.executeQuery("CREATE DATABASE \"TestSelectQuery\"");
        ClientProtos.SubmitQueryResponse res2 = client.executeQuery("CREATE TABLE table3 (c1 int, c2 char);");
        ClientProtos.SubmitQueryResponse res3 = client.executeQuery("INSERT INTO table3 values (3, 'A');");
        //ClientProtos.SubmitQueryResponse res6 = client.executeQuery("INSERT INTO table3 values (3, A);");
        //ClientProtos.SubmitQueryResponse res7 = client.executeQuery("INSERT INTO table3 values (3, \"as\");");
        ClientProtos.SubmitQueryResponse res4 = client.executeQuery("SELECT * FROM table3;");
        ClientProtos.SubmitQueryResponse res5 = client.executeQuery("");
        QueryId queryId = new QueryId(res4.getQueryId());
        QueryId failedQueryId = new QueryId(res5.getQueryId());
        //Errors.ResultCode.

        ResultSet queryAndGetResult = client.executeQueryAndGetResult("SELECT * FROM table3;");
       queryAndGetResult.next();
       int i =queryAndGetResult.getInt(1);
    //   queryAndGetResult.next();
       String c = queryAndGetResult.getString(2);
        String testString = resultSetToString(queryAndGetResult);
        queryAndGetResult.getString("c2");
        //("select * from table3 where c1 = 3 and c2 = 'A'");
        //int res = queryAndGetResult.getInt(0);
        if(queryAndGetResult.first())
            System.out.println("Righe!");;

        int rowId = queryAndGetResult.getInt(1);
        /* Query in Progress */
        QueryContext context = new QueryContext(getConf());
        QueryManager queryManager = testingCluster.getMaster().getContext().getQueryJobManager();
        Session session = new Session("sessionTest",res3.getUserName(),       getCurrentDatabase());
        QueryId queryInProgressID = queryManager.scheduleQuery(
                session,
                context ,
                "SELECT * FROM table3;",
                null,
                new LogicalRootNode(2)).getQueryId();

        queryManager.stopQuery(queryInProgressID);
        ServiceTracker serviceTracker = ServiceTrackerFactory.get(getConf());

        QueryClientImpl queryClient = new QueryClientImpl(new SessionConnection(serviceTracker,getCurrentDatabase(),new KeyValueSet()));
       String result = queryClient.getQueryStatus(new QueryId("a",74293847)).getState().name();
        System.out.println("test");

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testMethod() {
    }

}
