package org.apache.tajo.tests;

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

@RunWith(Parameterized.class)
public class getFinishedQuery extends QueryTestCaseBase {

    private Pair<Boolean, TajoConf> pair;
    private TajoMaster tajoMaster;

    public getFinishedQuery(Pair<Boolean, TajoConf> pair) {
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



    }
    private   QueryId queryId;
    @After
    public void tearDown() throws Exception {
        tajoMaster.serviceStop();
        tajoMaster.close();
    }

    @Test
    public void testMethod() throws QueryNotFoundException {
        TajoConf tajoConf = pair.getRight();
        Boolean expected = pair.getLeft();
        Boolean result;
        try {

                ClientProtos.SubmitQueryResponse res = client.executeQuery("CREATE DATABASE \"TestSelectQuery\"");
            ClientProtos.SubmitQueryResponse res2 = client.executeQuery("CREATE TABLE table3 (c1 int, c2 varchar);");
            ClientProtos.SubmitQueryResponse res3 = client.executeQuery("INSERT INTO table3 values (2, A);");
            ClientProtos.SubmitQueryResponse res4 = client.executeQuery("SELECT * FROM table3;");

            QueryId queryId = new QueryId(res4.getQueryId());

            HistoryWriter writer =  testingCluster.getMaster().getContext().getHistoryWriter();
       /*     writer.appendHistory(new History() {
                @Override
                public HistoryType getHistoryType() {
                    HistoryType type = new HistoryType();
                    return null;
                }
            });*/
           // testingCluster.getMaster().getContext().getGlobalEngine().
            QueryContext context = new QueryContext(getConf());
                QueryManager queryManager = testingCluster.getMaster().getContext().getQueryJobManager();

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

        }catch(Exception e){
            e.printStackTrace();
            result = false;
        }

        Assert.assertEquals(expected,true);
    }

    }


