package org.apache.tajo.tests.querymanager;

import org.apache.tajo.QueryId;
import org.apache.tajo.engine.query.QueryContext;
import org.apache.tajo.ipc.ClientProtos;
import org.apache.tajo.master.QueryInProgress;
import org.apache.tajo.master.QueryManager;
import org.apache.tajo.master.TajoMaster;
import org.apache.tajo.session.Session;
import org.apache.tajo.tests.QueryTestCaseBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

public class GetQueryInProgressTest extends QueryTestCaseBase {
    ClientProtos.SubmitQueryResponse res;

    public GetQueryInProgressTest() {
        res = client.executeQuery("CREATE DATABASE \"TestSelectQuery\"");
        ClientProtos.SubmitQueryResponse res2 = client.executeQuery("CREATE TABLE table3 (c1 int, c2 varchar);");
    }

    //null,queryId terminata, query id schedulata

    @Test
    public void testInProgressQuery() throws Exception {
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
        Assert.assertTrue(queryInfo.contains(queryInProgress));
    }


    @Test
    public void nullQuery() throws Exception {
        QueryContext context = new QueryContext(getConf());
        QueryManager queryManager = testingCluster.getMaster().getContext().getQueryJobManager();
        Boolean retv;
        try {
            QueryInProgress queryInProgress = queryManager.getQueryInProgress(null);
            retv = false;
        } catch (Exception e) {
            retv = true;
        }
        Assert.assertTrue(retv);
    }

    @Test
    public void stoppedQuery() throws Exception {

        QueryContext context = new QueryContext(getConf());
        QueryManager queryManager = testingCluster.getMaster().getContext().getQueryJobManager();
        ClientProtos.SubmitQueryResponse res3 = client.executeQuery("INSERT INTO table3 values (2, 'a');");
        Boolean retv;
        QueryInProgress queryInProgress = queryManager.getQueryInProgress(new QueryId(res3.getQueryId()));
        if (queryInProgress == null) {
            retv = true;
        } else {
            retv = false;
        }
        Assert.assertTrue(retv);


    }

}

