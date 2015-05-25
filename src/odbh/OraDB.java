/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package odbh;

import java.sql.*;

/**
 *
 */
public class OraDB {

    String mainquery = 
"WITH d AS (SELECT a.inst_id, a.sid, a.serial# serial, a.status, COALESCE(a.username,b.pname,'N/A') username, NVL(b.pname,'N/A') process, NVL(module,'N/A') module, NVL(a.machine,'N/A') machine, b.spid \"OSPID\",  	nvl(a.process,'N/A') RMPID, b.pid \"ORAPID\", b.serial# \"PSERIAL\", a.service_name, a.server, decode(a.blocking_session,'UNKNOWN',null,a.blocking_session) blocked_by, decode(a.blocking_instance,'UNKNOWN',null,a.blocking_instance) blocking_instance, c.blocked, a.type, ROUND(b.pga_used_mem /1024/1024,3) pga_used_mb, ROUND(b.pga_alloc_mem/1024/1024,3) pga_alloc_mb, ROUND(b.pga_max_mem /1024/1024,3) pga_max_mb, TO_CHAR(a.logon_time,'DD.MM.YYYY HH24:MI:SS') logon_time, a.state, ROUND(DECODE(a.state,'WAITED KNOWN TIME',a.time_since_last_wait_micro,'WAITED SHORT TIME',a.time_since_last_wait_micro,a.wait_time_micro)/1000000,3) seconds, DECODE(a.state,'WAITED KNOWN TIME','ON CPU','WAITED SHORT TIME','ON CPU',a.event) \"EVENT\", DECODE(a.time_remaining_micro,-1,-1,ROUND(a.time_remaining_micro/1000000,3)) time_remaining FROM gv$session a LEFT JOIN gv$process b ON (a.paddr =b.addr AND a.inst_id=b.inst_id) LEFT JOIN (SELECT blocking_instance,  blocking_session,  COUNT(sid) blocked FROM gv$session WHERE blocking_session IS NOT NULL GROUP BY blocking_instance,  blocking_session ) c ON (a.sid =c.blocking_session AND a.inst_id=c.blocking_instance) ORDER BY blocked DESC nulls last )  SELECT inst_id, DECODE(level,1,TO_CHAR(sid),lpad(sid,2*level+LENGTH(TO_CHAR(sid)),' ')) sid, serial, status, username, process, module, machine, logon_time, state, event, seconds, ospid, rmpid, pga_used_mb, pga_alloc_mb, pga_max_mb, type, service_name, server, orapid, pserial  FROM d START WITH d.blocked IS NOT NULL  AND d.blocked_by IS NULL CONNECT BY prior trim(d.inst_id ||' ' ||d.sid)=trim(d.blocking_instance ||' ' ||d.blocked_by)  UNION ALL  SELECT *  FROM (SELECT inst_id, TO_CHAR(sid), serial, status, username, process, module, machine, logon_time, state, event,	 seconds, ospid, rmpid, pga_used_mb, pga_alloc_mb, pga_max_mb, type, service_name, server, orapid, pserial FROM d WHERE blocked_by IS NULL AND blocked  IS NULL ORDER BY type DESC, status ASC, d.sid ASC, inst_id ASC )";
    String [] minorquery = {
//WAITS
"select seq#,   decode(state,'WAITED SHORT TIME','ON CPU','WAITED KNOWN TIME','ON CPU',event) \"EVENT\",   decode(state,'WAITED SHORT TIME',0,'WAITED KNOWN TIME',0,p1) \"P1\",   decode(state,'WAITED SHORT TIME','--','WAITED KNOWN TIME','--',p1text) \"P1TEXT\",   decode(state,'WAITED SHORT TIME',0,'WAITED KNOWN TIME',0,p2) \"P2\",   decode(state,'WAITED SHORT TIME','--','WAITED KNOWN TIME','--',p2text) \"P2TEXT\",   decode(state,'WAITED SHORT TIME',0,'WAITED KNOWN TIME',0,p3) \"P3\",   decode(state,'WAITED SHORT TIME','--','WAITED KNOWN TIME','--',p3text) \"P3TEXT\",   round(decode(state,'WAITED SHORT TIME',time_since_last_wait_micro,'WAITED KNOWN TIME',time_since_last_wait_micro,wait_time_micro)/1000000,3) \"WAIT_TIME\",   time_since_last_wait_micro from gv$session_wait where inst_id  = ? and sid= ? union all select seq#,   event,   p1,   p1text,   p2,   p2text,   p3,   p3text,   round(wait_time_micro/1000000,3),   time_since_last_wait_micro from gv$session_wait_history where inst_id = ? and sid = ? ",
//EVENTS
"select total_waits,   event,   round(time_waited_micro/1000,3) total_time_wait_milli,   round(time_waited_micro/total_waits/1000,3) avg_time_wait_milli,   total_timeouts from gv$session_event where wait_class# <>6 and time_waited_micro <> 0 and round(time_waited_micro/1000,3) > 0 and inst_id = ?  and sid = ?  order by time_waited_micro desc, total_timeouts desc",
//STATS
"select name, value, decode(bitand(class,1),1,' USER ',null)|| decode(bitand(class,2),2,' REDO ',null)|| decode(bitand(class,4),4,' ENQUEUE ',null)|| decode(bitand(class,8),8,' CACHE ',null)|| decode(bitand(class,16),16,' OS ',null)|| decode(bitand(class,32),32,' RAC ',null)|| decode(bitand(class,64),64,' SQL ',null)|| decode(bitand(class,128),128,' DEBUG ',null) statclass		 from gv$sesstat  left join v$statname using (statistic#)  where inst_id = ? and sid = ? and value<>0 order by 2 desc",
//APP
"select decode(a.command,  0,null,  1,	 'CREATE TABLE	', 2,	 'INSERT', 3,	 'SELECT	', 4,	 'CREATE CLUSTER', 5,	 'ALTER CLUSTER	', 6,	 'UPDATE', 7,	 'DELETE	', 8,	 'DROP CLUSTER', 9,	 'CREATE INDEX	', 10,	 'DROP INDEX', 11,	 'ALTER INDEX	', 12,	 'DROP TABLE', 13,	 'CREATE SEQUENCE	', 14,	 'ALTER SEQUENCE', 15,	 'ALTER TABLE	', 16,	 'DROP SEQUENCE', 17,	 'GRANT OBJECT	', 18,	 'REVOKE OBJECT', 19,	 'CREATE SYNONYM	', 20,	 'DROP SYNONYM', 21,	 'CREATE VIEW	', 22,	 'DROP VIEW', 23,	 'VALIDATE INDEX	', 24,	 'CREATE PROCEDURE', 25,	 'ALTER PROCEDURE	', 26,	 'LOCK', 27,	 'NO-OP	', 28,	 'RENAME', 29,	 'COMMENT	', 30,	 'AUDIT OBJECT', 31,	 'NOAUDIT OBJECT	', 32,	 'CREATE DATABASE LINK', 33,	 'DROP DATABASE LINK	', 34,	 'CREATE DATABASE', 35,	 'ALTER DATABASE	', 36,	 'CREATE ROLLBACK SEG', 37,	 'ALTER ROLLBACK SEG	', 38,	 'DROP ROLLBACK SEG', 39,	 'CREATE TABLESPACE	', 40,	 'ALTER TABLESPACE', 41,	 'DROP TABLESPACE	', 42,	 'ALTER SESSION', 43,	 'ALTER USER	', 44,	 'COMMIT', 45,	 'ROLLBACK	', 46,	 'SAVEPOINT', 47,	 'PL/SQL EXECUTE	', 48,	 'SET TRANSACTION', 49,	 'ALTER SYSTEM	', 50,	 'EXPLAIN', 51,	 'CREATE USER	', 52,	 'CREATE ROLE', 53,	 'DROP USER	', 54,	 'DROP ROLE', 55,	 'SET ROLE	', 56,	 'CREATE SCHEMA', 57,	 'CREATE CONTROL FILE	', 59,	 'CREATE TRIGGER', 60,	 'ALTER TRIGGER	', 61,	 'DROP TRIGGER', 62,	 'ANALYZE TABLE	', 63,	 'ANALYZE INDEX', 64,	 'ANALYZE CLUSTER	', 65,	 'CREATE PROFILE', 66,	 'DROP PROFILE	', 67,	 'ALTER PROFILE', 68,	 'DROP PROCEDURE	', 70,	 'ALTER RESOURCE COST', 71,	 'CREATE MATERIALIZED VIEW LOG	', 72,	 'ALTER MATERIALIZED VIEW LOG', 73,	 'DROP MATERIALIZED VIEW LOG	', 74,	 'CREATE MATERIALIZED VIEW', 75,	 'ALTER MATERIALIZED VIEW	', 76,	 'DROP MATERIALIZED VIEW', 77,	 'CREATE TYPE	', 78,	 'DROP TYPE', 79,	 'ALTER ROLE	', 80,	 'ALTER TYPE', 81,	 'CREATE TYPE BODY	', 82,	 'ALTER TYPE BODY', 83,	 'DROP TYPE BODY	', 84,	 'DROP LIBRARY', 85,	 'TRUNCATE TABLE	', 86,	 'TRUNCATE CLUSTER', 91,	 'CREATE FUNCTION	', 92,	 'ALTER FUNCTION', 93,	 'DROP FUNCTION	', 94,	 'CREATE PACKAGE', 95,	 'ALTER PACKAGE	', 96,	 'DROP PACKAGE', 97,	 'CREATE PACKAGE BODY	', 98,	 'ALTER PACKAGE BODY', 99,	 'DROP PACKAGE BODY	', 100,	 'LOGON', 101,	 'LOGOFF	', 102,	 'LOGOFF BY CLEANUP', 103,	 'SESSION REC	', 104,	 'SYSTEM AUDIT', 105,	 'SYSTEM NOAUDIT	', 106,	 'AUDIT DEFAULT', 107,	 'NOAUDIT DEFAULT	', 108,	 'SYSTEM GRANT', 109,	 'SYSTEM REVOKE	', 110,	 'CREATE PUBLIC SYNONYM', 111,	 'DROP PUBLIC SYNONYM	', 112,	 'CREATE PUBLIC DATABASE LINK', 113,	 'DROP PUBLIC DATABASE LINK	', 114,	 'GRANT ROLE', 115,	 'REVOKE ROLE	', 116,	 'EXECUTE PROCEDURE', 117,	 'USER COMMENT	', 118,	 'ENABLE TRIGGER', 119,	 'DISABLE TRIGGER	', 120,	 'ENABLE ALL TRIGGERS', 121,	 'DISABLE ALL TRIGGERS	', 122,	 'NETWORK ERROR', 123,	 'EXECUTE TYPE	', 157,	 'CREATE DIRECTORY', 158,	 'DROP DIRECTORY	', 159,	 'CREATE LIBRARY', 160,	 'CREATE JAVA	', 161,	 'ALTER JAVA', 162,	 'DROP JAVA	', 163,	 'CREATE OPERATOR', 164,	 'CREATE INDEXTYPE	', 165,	 'DROP INDEXTYPE', 167,	 'DROP OPERATOR	', 168,	 'ASSOCIATE STATISTICS', 169,	 'DISASSOCIATE STATISTICS	', 170,	 'CALL METHOD', 171,	 'CREATE SUMMARY	', 172,	 'ALTER SUMMARY', 173,	 'DROP SUMMARY	', 174,	 'CREATE DIMENSION', 175,	 'ALTER DIMENSION	', 176,	 'DROP DIMENSION', 177,	 'CREATE CONTEXT	', 178,	 'DROP CONTEXT', 179,	 'ALTER OUTLINE	', 180,	 'CREATE OUTLINE', 181,	 'DROP OUTLINE	', 182,	 'UPDATE INDEXES', 183,	 'ALTER OPERATOR', to_char(a.command) ) \"COMMAND\" ,   a.last_call_et state_time,   a.program,   a.module,   a.action,   a.sql_trace,   a.sql_trace_waits,   a.sql_trace_binds,   a.pddl_status parallel_ddl,   a.pdml_status parallel_dml,   a.pq_status parallel_query,   b.tracefile from gv$session a left join gv$process b on (a.paddr  =b.addr and a.inst_id=b.inst_id) where a.inst_id   = ? and a.sid = ?",
//TEMP
"select b.tablespace,   b.segtype,   round (sum( nvl(b.blocks,0) * c.value / 1024 / 1024 ), 2 ) size_mb from gv$session a left join gv$sort_usage b on (a.saddr  = b.session_addr and a.inst_id=b.inst_id) left join gv$parameter c on(c.name       = 'db_block_size') where a.inst_id     = ? and a.sid   = ? and B.SEGFILE# is not null group by b.tablespace,b.segtype order by 3 desc,1,2",
//UNDO
"select b.xidusn,   sum(b.used_urec) records,   sum(round(b.used_ublk*c.value/1024/1024,3)) mb from gv$session a left join gv$transaction b on (a.saddr  = b.ses_addr and a.inst_id=b.inst_id) left join gv$parameter c ON (c.NAME       ='db_block_size') WHERE A.INST_ID = ?  and a.sid = ? and b.used_urec is not null group by b.xidusn order by 3 desc",
//LONGOPS
"select round(100*sofar/totalwork) \"STATUS\",   message,sql_id,sql_plan_line_id,   sql_plan_operation || ' ' || sql_plan_options sql_op,   to_char(sofar,'FM999G999G999G999G999') so_far,   to_char(totalwork,'FM999G999G999G999G999') total,   TO_CHAR(start_time,'YYYY-MM-DD HH24:MI:SS') start_time,   time_remaining from gv$session_longops  where inst_id = ? and sid = ? and serial# = ? and totalwork != 0 order by sofar/totalwork asc",
//SQL
"select 'ACTIVE' \"STATE\", s.sql_exec_id,s.sql_exec_start,q.sql_id,s.sql_hash_value,replace(q.sql_fulltext,chr(0)) sql_text from gv$session s left join gv$sql q on (s.sql_address    = q.address and s.sql_hash_value = q.hash_value) where  s.inst_id = ? and s.sid = ? and rownum = 1 union all select 'PREVIOUS' \"STATE\", s.prev_exec_id,s.prev_exec_start,q.sql_id,s.prev_hash_value,replace(q.sql_fulltext,chr(0)) sql_text from gv$session s left join gv$sql q on (s.prev_sql_addr    = q.address and s.prev_hash_value = q.hash_value) where  s.inst_id = ? and s.sid = ? and rownum = 1",
//PLAN
"select inst_id,sql_id,   child_number,   plan_hash_value,   lpad('  ',4*(depth-1))   ||operation   ||' '   ||options   ||' '   ||object_name   ||' '   ||other_tag   ||' '   ||decode(id, 0, 'Cost = '   ||position) \"Query Plan\",   lpad(   case     when cardinality > 1000000     then to_char(trunc(cardinality/1000000))       || 'M'     when cardinality > 1000     then to_char(trunc(cardinality/1000))       || 'K'     else cardinality       || ' '   end , 6 , ' ' ) as \"Rows\",   nvl(cpu_cost,0) \"CPU_COST\",   nvl(io_cost,0) \"I/O_COST\",   time,   nvl(round(temp_space/1024/1024,3),0) \"TEMP_MB\",   nvl(round(bytes     /1024/1024,3),0) \"MB\" from gv$sql_plan sp where sp.sql_id =   (select coalesce(sql_id,prev_sql_id)   from gv$session   where inst_id  = ?   and sid = ?   ) order by sp.plan_hash_value, sp.child_address,sp.id",
//BINDS
"select gv$sql_bind_capture.sql_id,position,   datatype_string,   value_string value,   to_char(last_captured,'HH24:MI:SS') time_captured from gv$sql_bind_capture where last_captured is not null and sql_id           =   (select coalesce(sql_id,prev_sql_id)   from gv$session   where inst_id  = ?   and sid = ?   ) order by gv$sql_bind_capture.sql_id,last_captured,   position",
//TRN
"select inst_id,   start_time start_time,   status,   name,   space,   recursive,   noundo,   ptx parallel,   used_ublk undo_blocks,   used_urec undo_records,   log_io,   phy_io from gv$transaction where ses_addr =   (select saddr from gv$session where inst_id= ? and sid= ? )",
//LOCKS
"select  inst_id,type,description,  case lmode when 0 then 'NONE' when 1 then 'NULL' when 2 then 'ROW-S' when 3 then 'ROW-X' when 4 then 'SHARE' when 5 then 'S/ROW-X' when 6 then 'EXCLUSIVE' else 'UKNOWN' end lock_mode, ctime time_sec, case block when 0 then 'REQUESTING/NONBLOCKING' WHEN 1 then 'EXCLUSIVE' when 2 then 'GLOBAL' else 'UNKNOWN' end block, case request when 0 then 'NONE' when 1 then 'NULL' when 2 then 'ROW-S' when 3 then 'ROW-X' when 4 then 'SHARE' when 5 then 'S/ROW-X' when 6 then 'EXCLUSIVE' else 'UKNOWN' end request from gv$lock left join v$lock_type lt using (type) where inst_id = ? and sid = ? order by block, ctime desc, request",
//PQ
"SELECT   qcinst_id,   qcsid,   qcserial#,   sid,   serial# p_id,   server_group,   server_set,   server#,   degree,   req_degree FROM   gv$px_session WHERE   (     qcinst_id = ?   AND qcsid = ?   ) OR   (     (qcsid,qcinst_id) IN     (       SELECT         qcsid,         qcinst_id       FROM         gv$px_session       WHERE         inst_id = ?       AND sid = ?     )   ) ORDER BY   server_group,   server_set,   server#",
//PGA
"select category, round(allocated/1024/1024) ALLOCATED_MB, round(used/1024/1024) USED_MB, round(max_allocated/1024/1024) MAX_ALLOCATED_MB from gv$process_memory  where inst_id = ? and pid = ? and serial# = ? order by 2 desc",
//CONN
"select  authentication_type, network_service_banner, client_charset, client_connection, client_oci_library, client_version, client_driver, client_lobattr  from gv$session_connect_info where inst_id = ? and sid= ? ",
//SQLMON
"select dbms_sqltune.report_sql_monitor(inst_id => ?, session_id => ? ,session_serial => ? ,type => 'TEXT',report_level => 'ALL') as report from dual"
};
    String dbusername = "dbsnmp";
    String dbpassword = "dbsnmp";
    String dbsid = "";
    String dbhost = "";
    String dbport = "1521";
    Connection con;
    Statement mainstmt;
    Statement [] minorstmt;
    PreparedStatement [] minorprepstmt;  
    Object[][] retval;

    public OraDB() {
        this.minorprepstmt = new PreparedStatement[16];
    }

    public int initmyjdb(String dbhost, String dbsid, String dbusername, String dbpassword) {
        
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            if(con != null && !con.isClosed()){
                if(mainstmt != null && !mainstmt.isClosed()){
                    mainstmt.close();
                }
                if(minorstmt != null){
                    for (Statement minorstmt1 : minorstmt) {
                        if(minorstmt1 != null && ! minorstmt1.isClosed()){
                            minorstmt1.close();
                        }
                    }
                }
                con.close();
            }
            con = DriverManager.getConnection("jdbc:oracle:thin:@" + dbhost + ":"+ dbport +":" + dbsid, dbusername, dbpassword);
            return 0;
        } catch (Exception e) {
            System.out.println(e);
            return 1;
        }
    }

    public Object[][] mainquery() {
        ResultSet mainrs;
        int mainrescolcount;
        try {
        if(mainstmt == null || mainstmt.isClosed()){
            mainstmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        }    
        mainrs = mainstmt.executeQuery(mainquery);
        ResultSetMetaData mainrsmd = mainrs.getMetaData();
        int mainrscolcount = mainrsmd.getColumnCount();
        int mainrsrowcount;
        if (mainrs.last()) {
            mainrsrowcount = mainrs.getRow();
            mainrs.beforeFirst();
        } else {
            mainrsrowcount = 0;
        }
        retval = new Object[mainrsrowcount][mainrscolcount];
        int j = 0;
        while (mainrs.next()) {
            for (int i = 1; i <= mainrscolcount; i++) {
                retval[j][i - 1] = mainrs.getString(i);
            }
            j++;
        }
        } catch (Exception e) {
            System.out.println(e);
        }
        return retval;
    }

    public Object [][] minorquery(int tabid, int [] inp ){
        ResultSet minorrs;
        int minorrscolcount,minorrsrowcount;
        try {
        if( minorprepstmt[tabid]==null || minorprepstmt[tabid].isClosed()){
            minorprepstmt[tabid] = con.prepareStatement(minorquery[tabid],ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        }
        switch(tabid){
            case 0:
                minorprepstmt[tabid].setInt(1, inp[0]);
                minorprepstmt[tabid].setInt(3, inp[0]);
                minorprepstmt[tabid].setInt(2, inp[1]);
                minorprepstmt[tabid].setInt(4, inp[1]);
                break;
            case 6:
                minorprepstmt[tabid].setInt(1, inp[0]);
                minorprepstmt[tabid].setInt(2, inp[1]);
                minorprepstmt[tabid].setInt(3, inp[2]);
                break; 
            case 7:
                minorprepstmt[tabid].setInt(1, inp[0]);
                minorprepstmt[tabid].setInt(3, inp[0]);
                minorprepstmt[tabid].setInt(2, inp[1]);
                minorprepstmt[tabid].setInt(4, inp[1]);
                break; 
            case 12:
                minorprepstmt[tabid].setInt(1, inp[0]);
                minorprepstmt[tabid].setInt(3, inp[0]);
                minorprepstmt[tabid].setInt(2, inp[1]);
                minorprepstmt[tabid].setInt(4, inp[1]);
                break; 
            case 13:
                minorprepstmt[tabid].setInt(1, inp[0]);
                minorprepstmt[tabid].setInt(2, inp[3]);
                minorprepstmt[tabid].setInt(3, inp[4]);
                break; 
            case 15:
                minorprepstmt[tabid].setInt(1, inp[0]);
                minorprepstmt[tabid].setInt(2, inp[1]);
                minorprepstmt[tabid].setInt(3, inp[2]);
                break;                
            default:
                minorprepstmt[tabid].setInt(1, inp[0]);
                minorprepstmt[tabid].setInt(2, inp[1]);
                break;
        }
        minorprepstmt[tabid].execute();
        minorrs = minorprepstmt[tabid].getResultSet();
        ResultSetMetaData minorrsmd = minorrs.getMetaData();
        minorrscolcount = minorrsmd.getColumnCount();
        if (minorrs.last()) {
            minorrsrowcount = minorrs.getRow();
            minorrs.beforeFirst();
        } else {
            minorrsrowcount = 0;
        }
        if(minorrsrowcount==0 || minorrscolcount==0){
            return null;
        }else{
            retval = new Object[minorrsrowcount][minorrscolcount];
            int j = 0;
            while (minorrs.next()) {
                for (int i = 1; i <= minorrscolcount; i++) {
                    retval[j][i - 1] = minorrs.getString(i);
                }
                j++;
            }
            return retval;        
        }
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
        
    }
}
