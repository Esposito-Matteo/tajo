package org.apache.tajo.tests.queryclientimpl;

import org.apache.tajo.client.QueryClientImpl;
import org.apache.tajo.client.SessionConnection;
import org.apache.tajo.exception.TajoException;
import org.apache.tajo.exception.TajoInternalError;
import org.apache.tajo.ipc.ClientProtos;
import org.apache.tajo.service.ServiceTracker;
import org.apache.tajo.service.ServiceTrackerFactory;
import org.apache.tajo.tests.QueryTestCaseBase;
import org.apache.tajo.util.KeyValueSet;
import org.junit.Assert;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExecuteQueryAndGetResultTest extends QueryTestCaseBase {
    QueryClientImpl queryClient;
    public ExecuteQueryAndGetResultTest(){
        ServiceTracker serviceTracker = ServiceTrackerFactory.get(getConf());
        queryClient = new QueryClientImpl(new SessionConnection(serviceTracker,getCurrentDatabase(),new KeyValueSet()));
        ClientProtos.SubmitQueryResponse res = queryClient.executeQuery("CREATE DATABASE \"TestSelectQuery\"");
        ClientProtos.SubmitQueryResponse res2 = queryClient.executeQuery("CREATE TABLE table3 (c1 int, c2 char);");
    }


    @Test
    public void validCase() throws TajoException, SQLException {
        ClientProtos.SubmitQueryResponse res3 = queryClient.executeQuery("INSERT INTO table3 values (3, 'A');");
        ResultSet queryAndGetResult = queryClient.executeQueryAndGetResult("SELECT * FROM table3;");
        queryAndGetResult.next();
        int i =queryAndGetResult.getInt(1);
        String c = queryAndGetResult.getString(2);
        Boolean test1 = (i ==3);
        Boolean test2 = c.equals("A");
        Assert.assertTrue(test1&&test2);
    }

    @Test
    public void nullCase() throws TajoException, SQLException {
        try {
            ResultSet queryAndGetResult = queryClient.executeQueryAndGetResult(null);
            Assert.fail();
        } catch (NullPointerException e) {
            Assert.assertTrue(true);
        }
    }


    @Test
    public void invalidSQLCase() throws TajoException, SQLException {
        try {
            ResultSet queryAndGetResult = queryClient.executeQueryAndGetResult("INSERT INTO table3 values (3, 3);");
            Assert.fail();
        } catch (TajoInternalError e) {
            Assert.assertTrue(true);
        }
    }
}
