create external table ${0} (id bigint, name text, score float, type text) using text
with ('text.delimiter'='|', 'text.null'='NULL') location ${table.path};

