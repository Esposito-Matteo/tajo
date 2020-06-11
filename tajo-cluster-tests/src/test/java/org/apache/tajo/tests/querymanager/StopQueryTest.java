package org.apache.tajo.tests.querymanager;

import org.apache.tajo.QueryId;
import org.apache.tajo.engine.query.QueryContext;
import org.apache.tajo.ipc.ClientProtos;
import org.apache.tajo.master.QueryInProgress;
import org.apache.tajo.master.QueryManager;
import org.apache.tajo.session.Session;
import org.apache.tajo.tests.QueryTestCaseBase;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

public class StopQueryTest extends QueryTestCaseBase {


    ClientProtos.SubmitQueryResponse res;

    public StopQueryTest() {
        res = client.executeQuery("CREATE DATABASE \"TestSelectQuery\"");
        ClientProtos.SubmitQueryResponse res2 = client.executeQuery("CREATE TABLE table3 (c1 int, c2 varchar);");
    }


    @Test
    public void validQueryId() throws Exception {
        QueryContext context = new QueryContext(getConf());
        QueryManager queryManager = testingCluster.getMaster().getContext().getQueryJobManager();

        Session session = new Session("sessionTest", res.getUserName(), getCurrentDatabase());
        QueryId queryId = queryManager.scheduleQuery(
                session,
                context,
                "SELECT * FROM table3;",
                "",
                null).getQueryId();
        QueryInProgress queryInProgress = queryManager.getQueryInProgress(queryId);
        Collection<QueryInProgress> queryInfo = queryManager.getSubmittedQueries();
        if (queryInfo.size() > 0) {
            queryManager.stopQuery(queryId);
            queryInfo = queryManager.getSubmittedQueries();
            Assert.assertTrue(queryInfo.isEmpty());
        } else {
            Assert.fail();
        }
    }

    @Test
    public void nullQueryId() throws Exception {
        QueryContext context = new QueryContext(getConf());
        QueryManager queryManager = testingCluster.getMaster().getContext().getQueryJobManager();
        Boolean retv;
        try {
            queryManager.stopQuery(null);
            retv = false;
        } catch (NullPointerException e) {
            retv = true;
        }
        Assert.assertTrue(retv);
    }

    @Test
    public void finishedQueryId() throws Exception {
        QueryContext context = new QueryContext(getConf());
        QueryManager queryManager = testingCluster.getMaster().getContext().getQueryJobManager();
        ClientProtos.SubmitQueryResponse res3 = client.executeQuery("INSERT INTO table3 values (2, 'a');");
        Boolean retv;
        try {
            queryManager.stopQuery(new QueryId(res3.getQueryId()));
            retv = true;
        } catch (NullPointerException e) {
            retv = false;
        }
        Assert.assertTrue(retv);
    }

}
