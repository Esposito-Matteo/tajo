package nta.schema;

import nta.catalog.Schema;
import nta.catalog.TableMeta;
import nta.catalog.TableMetaImpl;
import nta.catalog.proto.CatalogProtos.DataType;
import nta.catalog.proto.CatalogProtos.StoreType;

public class BaseTableSchema {

	TableMeta tableMeta;
	
	public BaseTableSchema() {
		Schema schema = new Schema();
		schema.addColumn("sys_uptime", DataType.LONG);
		schema.addColumn("unix_secs", DataType.LONG);
		schema.addColumn("unix_nsecs", DataType.LONG);
		schema.addColumn("eng_type", DataType.BYTE);
		schema.addColumn("end_id", DataType.BYTE);
		schema.addColumn("itval", DataType.INT);
		
		schema.addColumn("srcaddColumnr", DataType.IPv4);
		schema.addColumn("dstaddColumnr", DataType.IPv4);
		schema.addColumn("src_net", DataType.INT);
		schema.addColumn("dst_net", DataType.INT);
		schema.addColumn("input", DataType.INT);
		schema.addColumn("output", DataType.INT);
		schema.addColumn("dPkts", DataType.LONG);
		schema.addColumn("dOctets", DataType.LONG);
		schema.addColumn("first", DataType.LONG);
		schema.addColumn("last", DataType.LONG);
		schema.addColumn("srcPort", DataType.INT);
		schema.addColumn("dstPort", DataType.INT);
		schema.addColumn("tcp_flags", DataType.BYTE);
		schema.addColumn("prot", DataType.BYTE);
		schema.addColumn("tos", DataType.BYTE);
		schema.addColumn("tag", DataType.LONG);
		
		tableMeta = new TableMetaImpl(schema, StoreType.CSV);
	}
	
	public TableMeta getSchema() {
		return this.tableMeta;
	}
}
