package nta.engine.ipc.protocolrecords;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import nta.catalog.Schema;
import nta.catalog.TableMeta;
import nta.catalog.TableMetaImpl;
import nta.catalog.proto.CatalogProtos.DataType;
import nta.catalog.proto.CatalogProtos.StoreType;

import org.apache.hadoop.fs.Path;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Hyunsik Choi
 *
 */
public class TestFragment {
  private Schema schema1;
  private TableMeta meta1;
  
  @Before
  public final void setUp() throws Exception {
    schema1 = new Schema(); 
    schema1.addColumn("id", DataType.INT);
    schema1.addColumn("name", DataType.STRING);
    meta1 = new TableMetaImpl(schema1, StoreType.CSV);
  }

  @Test
  public final void testGetAndSetFields() {    
    Fragment tablet1 = new Fragment("table1_1", new Path("/table0"), meta1, 0, 500);

    assertEquals("table1_1", tablet1.getId());
    assertEquals(new Path("/table0"), tablet1.getPath());
    assertEquals(meta1, tablet1.getMeta());
    assertEquals(0, tablet1.getStartOffset());
    assertEquals(500, tablet1.getLength());
  }

  @Test
  public final void testTabletTabletProto() {
    Fragment tablet1 = new Fragment("table1_1", new Path("/table0"), meta1, 0, 500);
    
    Fragment tablet2 = new Fragment(tablet1.getProto());
    assertEquals("table1_1", tablet2.getId());
    assertEquals(new Path("/table0"), tablet2.getPath());
    assertEquals(meta1, tablet2.getMeta());
    assertEquals(0, tablet2.getStartOffset());
    assertEquals(500, tablet2.getLength());
  }

  @Test
  public final void testCompareTo() {
    final int num = 10;
    Fragment [] tablets = new Fragment[num];
    for (int i = num - 1; i >= 0; i--) {
      tablets[i]
          = new Fragment("tablet1_"+i, new Path("tablet0"), meta1, i * 500, 
              (i+1) * 500);
    }
    
    Arrays.sort(tablets);

    for(int i = 0; i < num; i++) {
      assertEquals("tablet1_"+i, tablets[i].getId());
    }
  }
}
