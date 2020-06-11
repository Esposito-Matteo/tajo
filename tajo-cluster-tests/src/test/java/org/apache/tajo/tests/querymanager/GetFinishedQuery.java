package org.apache.tajo.tests.querymanager;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.tajo.QueryId;
import org.apache.tajo.TajoProtos;
import org.apache.tajo.client.QueryStatus;
import org.apache.tajo.client.TajoClientUtil;
import org.apache.tajo.conf.TajoConf;
import org.apache.tajo.engine.query.QueryContext;
import org.apache.tajo.exception.QueryNotFoundException;
import org.apache.tajo.exception.TajoException;
import org.apache.tajo.ipc.ClientProtos;
import org.apache.tajo.jdbc.TajoMemoryResultSet;
import org.apache.tajo.master.QueryInProgress;
import org.apache.tajo.master.QueryInfo;
import org.apache.tajo.master.QueryManager;
import org.apache.tajo.master.TajoMaster;
import org.apache.tajo.session.Session;
import org.apache.tajo.tests.QueryTestCaseBase;
import org.apache.tajo.util.StringUtils;
import org.apache.tajo.util.history.History;
import org.apache.tajo.util.history.HistoryWriter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

public class GetFinishedQuery extends QueryTestCaseBase {


    public GetFinishedQuery() {
        ClientProtos.SubmitQueryResponse res = client.executeQuery("CREATE DATABASE \"TestSelectQuery\"");
        ClientProtos.SubmitQueryResponse res2 = client.executeQuery("CREATE TABLE table3 (c1 int, c2 varchar);");
    }

    @Before
    public void setUp() throws Exception {


    }
    /*

                Session session = new Session("sessionTest",res3.getUserName(),       getCurrentDatabase());
            queryId = queryManager.scheduleQuery(
                    session,
                    context ,
                    "SELECT * FROM table3;",
                    "",
                    null).getQueryId();

           if (!expected) {
                    assertEquals(ClientProtos.SubmitQueryResponse.ResultType.FETCH, res.getResultType());
                    QueryStatus status = TajoClientUtil.waitCompletion(client, queryId);
                    assertEquals(TajoProtos.QueryState.QUERY_SUCCEEDED, status.getState());
                    client.closeQuery(queryId);
                } else {
                 //   assertEquals(ClientProtos.SubmitQueryResponse.ResultType.ENCLOSED, res.getResultType());
               QueryInProgress queryInProgress = queryManager.getQueryInProgress(queryId);
               Collection<QueryInProgress> queryInfo = queryManager.getSubmittedQueries();
               queryManager.stopQuery(queryId);
               queryInfo = queryManager.getSubmittedQueries();
               // QueryInfo queryInfo = queryManager.getSubmittedQueries(queryId);
                 ///   assertNotNull(queryInfo);
                    //assertTrue(StringUtils.isEmpty(queryInfo.getQueryMasterHost()));
                    client.closeQuery(queryId);
                }



           /*     TajoMemoryResultSet set = (TajoMemoryResultSet) executeString("CREATE DATABASE \"TestSelectQuery\"");
                QueryInfo queryInfo =  testingCluster.getMaster().getContext().getQueryJobManager().getFinishedQuery(queryId);
                System.out.println("test");*/


    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void validQueryID() throws QueryNotFoundException {

        try {
            ClientProtos.SubmitQueryResponse res3 = client.executeQuery("INSERT INTO table3 values (2, A);");
            ClientProtos.SubmitQueryResponse res4 = client.executeQuery("SELECT * FROM table3;");
            QueryId queryId = new QueryId(res4.getQueryId());

            QueryManager queryManager = testingCluster.getMaster().getContext().getQueryJobManager();
            QueryInfo queryInfo = queryManager.getFinishedQuery(queryId);
            Assert.assertTrue(queryInfo.getQueryState().equals(TajoProtos.QueryState.QUERY_SUCCEEDED));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void erroneusSQLQueryID() throws QueryNotFoundException {
        Boolean retv = false;
        try {
            ClientProtos.SubmitQueryResponse res3 = client.executeQuery("INSERT INTO table3 values (2, A);");
            QueryId queryId = new QueryId(res3.getQueryId());

            QueryManager queryManager = testingCluster.getMaster().getContext().getQueryJobManager();
            QueryInfo queryInfo = queryManager.getFinishedQuery(queryId);
            if (queryInfo == null) {
                retv = true;
            } else {
                retv = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
        Assert.assertTrue(retv);
    }


    @Test
    public void nullQueryID() throws QueryNotFoundException {
        Boolean retv = false;
        try {
            QueryManager queryManager = testingCluster.getMaster().getContext().getQueryJobManager();
            QueryInfo queryInfo = queryManager.getFinishedQuery(null);
            if (queryInfo == null) {
                retv = true;
            } else {
                retv = false;
            }

        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertTrue(retv);
    }
}


