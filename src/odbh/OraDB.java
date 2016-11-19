/* 
 * Copyright (c) 2016, lesha
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package odbh;

import java.sql.*;

/**
 *
 * @author Tribein
 */
public class OraDB {

    String mainQuery = 
"select rownum, z.* from (WITH d AS (SELECT a.inst_id, a.sid, a.serial# serial, a.status, COALESCE(a.username,b.pname,'N/A') username, NVL(b.pname,'N/A') process, NVL(module,'N/A') module, NVL(a.machine,'N/A') machine, DECODE(a.blocking_session_status,'UNKNOWN',-1,a.blocking_session) blocked_by, DECODE(a.blocking_session_status,'UNKNOWN',-1,a.blocking_instance) blocking_instance, c.blocked, a.type, ROUND(b.pga_used_mem /1024/1024,3) pga_used_mb, ROUND(b.pga_alloc_mem/1024/1024,3) pga_alloc_mb, ROUND(b.pga_max_mem /1024/1024,3) pga_max_mb, TO_CHAR(a.logon_time,'YYYY.MM.DD HH24:MI:SS') logon_time, a.state, ROUND(DECODE(a.state,'WAITED KNOWN TIME',a.time_since_last_wait_micro,'WAITED SHORT TIME',a.time_since_last_wait_micro,a.wait_time_micro)/1000000,3) seconds, DECODE(a.state,'WAITED KNOWN TIME','ON CPU','WAITED SHORT TIME','ON CPU',a.event) \"EVENT\" FROM gv$session a LEFT JOIN gv$process b ON (a.paddr =b.addr AND a.inst_id=b.inst_id) LEFT JOIN (SELECT blocking_instance,  blocking_session,  COUNT(sid) blocked FROM gv$session WHERE blocking_session IS NOT NULL GROUP BY blocking_instance,  blocking_session ) c ON (a.sid =c.blocking_session AND a.inst_id=c.blocking_instance) ORDER BY blocked DESC nulls last )  SELECT inst_id, DECODE(level,1,TO_CHAR(sid),lpad(sid,2*level+LENGTH(TO_CHAR(sid)),' ')) sid, serial, status, username, process, module, machine, logon_time, state, event, seconds, pga_used_mb, pga_alloc_mb, pga_max_mb, type  FROM d START WITH d.blocked IS NOT NULL  AND (d.blocked_by IS NULL or d.blocked_by = -1) CONNECT BY prior trim(d.inst_id ||' ' ||d.sid)=trim(d.blocking_instance ||' ' ||d.blocked_by)  UNION ALL  SELECT *  FROM (SELECT inst_id, TO_CHAR(sid), serial, status, username, process, module, machine, logon_time, state, event,	 seconds, pga_used_mb, pga_alloc_mb, pga_max_mb, type FROM d WHERE blocked_by IS NULL AND blocked  IS NULL ORDER BY type DESC, status ASC, d.sid ASC, inst_id ASC )) z";
    String [] minorQuery = {
//WAITS
"select seq#,   decode(state,'WAITED SHORT TIME','ON CPU','WAITED KNOWN TIME','ON CPU',event) \"EVENT\",   decode(state,'WAITED SHORT TIME',0,'WAITED KNOWN TIME',0,p1) \"P1\",   decode(state,'WAITED SHORT TIME','--','WAITED KNOWN TIME','--',p1text) \"P1TEXT\",   decode(state,'WAITED SHORT TIME',0,'WAITED KNOWN TIME',0,p2) \"P2\",   decode(state,'WAITED SHORT TIME','--','WAITED KNOWN TIME','--',p2text) \"P2TEXT\",   decode(state,'WAITED SHORT TIME',0,'WAITED KNOWN TIME',0,p3) \"P3\",   decode(state,'WAITED SHORT TIME','--','WAITED KNOWN TIME','--',p3text) \"P3TEXT\",   round(decode(state,'WAITED SHORT TIME',time_since_last_wait_micro,'WAITED KNOWN TIME',time_since_last_wait_micro,wait_time_micro)/1000000,3) \"WAIT_TIME\",   time_since_last_wait_micro from gv$session_wait where inst_id  = ? and sid= ? union all select seq#,   event,   p1,   p1text,   p2,   p2text,   p3,   p3text,   round(wait_time_micro/1000000,3),   time_since_last_wait_micro from gv$session_wait_history where inst_id = ? and sid = ? ",
//EVENTS
"select total_waits,   event,   round(time_waited_micro/1000,3) total_time_wait_milli,   round(time_waited_micro/total_waits/1000,3) avg_time_wait_milli,   total_timeouts from gv$session_event where wait_class# <>6 and time_waited_micro <> 0 and round(time_waited_micro/1000,3) > 0 and inst_id = ?  and sid = ?  order by time_waited_micro desc, total_timeouts desc",
//STATS
"select name, value, decode(bitand(class,1),1,' USER ',null)|| decode(bitand(class,2),2,' REDO ',null)|| decode(bitand(class,4),4,' ENQUEUE ',null)|| decode(bitand(class,8),8,' CACHE ',null)|| decode(bitand(class,16),16,' OS ',null)|| decode(bitand(class,32),32,' RAC ',null)|| decode(bitand(class,64),64,' SQL ',null)|| decode(bitand(class,128),128,' DEBUG ',null) statclass		 from gv$sesstat  left join v$statname using (statistic#)  where inst_id = ? and sid = ? and value<>0 order by 2 desc",
//APP
"select decode(a.command,NULL,'N/A',(select command_name from v$sqlcommand where command_type=a.command)) cmd,a.last_call_et state_time,a.program,b.spid OSPID,nvl(a.process,'N/A') RMPID,nvl(a.resource_consumer_group,'N/A') rg,a.action,decode(a.plsql_entry_object_id,NULL,'SQL',(select object_type||' '||owner||'.'||object_name||decode(procedure_name,null,'','.'||procedure_name) from dba_procedures where object_id=a.plsql_entry_object_id and rownum<2)) plsql_entry_object,decode(a.plsql_object_id,NULL,'SQL',(select object_type||' '||owner||'.'||object_name||decode(procedure_name,null,'','.'||procedure_name) from dba_procedures where object_id=a.plsql_object_id and rownum <2)) plsql_object,(select object_type||' '||owner||'.'||object_name||decode(procedure_name,null,'','.'||procedure_name) from dba_procedures where object_id=a.plsql_entry_object_id and subprogram_id=a.plsql_entry_subprogram_id) plsql_entry_subprogram_id,(select object_type||' '||owner||'.'||object_name||decode(procedure_name,null,'','.'||procedure_name) from dba_procedures where object_id=a.plsql_object_id and subprogram_id=a.plsql_subprogram_id) plsql_subprogram_id,a.sql_trace,a.sql_trace_waits,a.sql_trace_binds,a.pddl_status parallel_ddl,a.pdml_status parallel_dml,a.pq_status parallel_query,b.tracefile,a.service_name,a.server from gv$session a left join gv$process b on (a.paddr  =b.addr and a.inst_id=b.inst_id) where a.inst_id   = ? and a.sid = ?",
//TEMP
"select b.tablespace,   b.segtype,   round (sum( nvl(b.blocks,0) * c.value / 1024 / 1024 ), 2 ) size_mb from gv$session a left join gv$sort_usage b on (a.saddr  = b.session_addr and a.inst_id=b.inst_id) left join gv$parameter c on(c.name       = 'db_block_size') where a.inst_id     = ? and a.sid   = ? and B.SEGFILE# is not null group by b.tablespace,b.segtype order by 3 desc,1,2",
//UNDO
"select b.xidusn,   sum(b.used_urec) records,   sum(round(b.used_ublk*c.value/1024/1024,3)) mb from gv$session a left join gv$transaction b on (a.saddr  = b.ses_addr and a.inst_id=b.inst_id) left join gv$parameter c ON (c.NAME       ='db_block_size') WHERE A.INST_ID = ?  and a.sid = ? and b.used_urec is not null group by b.xidusn order by 3 desc",
//LONGOPS
"select round(100*sofar/totalwork) \"STATUS\",   message,sql_id,sql_plan_line_id,   sql_plan_operation || ' ' || sql_plan_options sql_op,   to_char(sofar,'FM999G999G999G999G999') so_far,   to_char(totalwork,'FM999G999G999G999G999') total,   TO_CHAR(start_time,'YYYY-MM-DD HH24:MI:SS') start_time,   time_remaining from gv$session_longops  where inst_id = ? and sid = ? and totalwork != 0 order by sofar/totalwork asc",
//SQL
"select 'ACTIVE' \"STATE\", s.sql_exec_id,s.sql_exec_start,q.sql_id,s.sql_hash_value,replace(q.sql_fulltext,chr(0)) sql_text from gv$session s left join gv$sql q on (s.sql_address    = q.address and s.sql_hash_value = q.hash_value) where  s.inst_id = ? and s.sid = ? and rownum = 1 union all select 'PREVIOUS' \"STATE\", s.prev_exec_id,s.prev_exec_start,q.sql_id,s.prev_hash_value,replace(q.sql_fulltext,chr(0)) sql_text from gv$session s left join gv$sql q on (s.prev_sql_addr    = q.address and s.prev_hash_value = q.hash_value) where  s.inst_id = ? and s.sid = ? and rownum = 1",
//PLAN
"select inst_id,sql_id,   child_number,   plan_hash_value,   lpad('  ',4*(depth-1))   ||operation   ||' '   ||options   ||' '   ||object_name   ||' '   ||other_tag   ||' '   ||decode(id, 0, 'Cost = '   ||position) \"Query Plan\",   lpad(   case     when cardinality > 1000000     then to_char(trunc(cardinality/1000000))       || 'M'     when cardinality > 1000     then to_char(trunc(cardinality/1000))       || 'K'     else cardinality       || ' '   end , 6 , ' ' ) as \"Rows\",   nvl(cpu_cost,0) \"CPU_COST\",   nvl(io_cost,0) \"I/O_COST\",   time,   nvl(round(temp_space/1024/1024,3),0) \"TEMP_MB\",   nvl(round(bytes     /1024/1024,3),0) \"MB\" from gv$sql_plan sp where sp.sql_id =   (select coalesce(sql_id,prev_sql_id)   from gv$session   where inst_id  = ?   and sid = ?   ) order by sp.plan_hash_value, sp.child_address,sp.id",
//BINDS
"select gv$sql_bind_capture.sql_id,position,   datatype_string,   value_string value,   to_char(last_captured,'HH24:MI:SS') time_captured from gv$sql_bind_capture where last_captured is not null and sql_id           =   (select coalesce(sql_id,prev_sql_id)   from gv$session   where inst_id  = ?   and sid = ?   ) order by gv$sql_bind_capture.sql_id,last_captured,   position",
//TRN
"select inst_id,   start_time start_time,   status,   name,   space,   recursive,   noundo,   ptx parallel,   used_ublk undo_blocks,   used_urec undo_records,   log_io,   phy_io from gv$transaction where ses_addr =   (select saddr from gv$session where inst_id= ? and sid= ? )",
//LOCKS
"SELECT 'WAITING FOR' idx, owner \"OWNER/LOCK\",  object_name||nvl2(subobject_name,'.'||subobject_name,'') \"OBJECT/DESCRIPTION\", object_type \"TYPE/TIME\", name \"FILE NAME/LOCKED MODE\", TO_CHAR(row_wait_row#) \"WAIT ROW/BLOCK MODE\",  TO_CHAR(dbms_rowid.rowid_create ( 1, row_wait_obj#, row_wait_file#, row_wait_block#, row_wait_row# )) \"ROWID/REQUEST MODE\" from gv$session left join all_objects on (row_wait_obj# = object_id) left join v$datafile on (row_wait_file#=file#) where inst_id = ? and sid = ? and object_name is not null union all SELECT 'LOCKED OBJECT@'||to_char(inst_id) idx, owner, object_name, object_type, DECODE(locked_mode,0,'NONE',1,'NULL',2,'ROW-S',3,'ROW-X',4,'SHARE',5,'S/ROW-X',6,'EXCLUSIVE','UKNOWN') lm, NULL,NULL FROM gv$locked_object LEFT JOIN dba_objects USING (object_id) WHERE inst_id = ? AND session_id = ? union all select  'LOCK@'||inst_id idx, type, description,   to_char(ctime) time_sec, decode(lmode,0,'NONE',1,'NULL',2,'ROW-S',3,'ROW-X',4,'SHARE',5,'S/ROW-X',6,'EXCLUSIVE','UKNOWN') lm,   decode(block,0,'REQUESTING/NONBLOCKING',1,'EXCLUSIVE',2,'GLOBAL','UNKNOWN') block,  decode(request,0,'NONE',1,'NULL',2,'ROW-S',3,'ROW-X',4,'SHARE',5,'S/ROW-X',6,'EXCLUSIVE','UKNOWN') request  from gv$lock left join v$lock_type lt using (type) where inst_id = ? and sid = ?",
//PQ
"SELECT   qcinst_id,   qcsid,   qcserial#,   sid,   serial# p_id,   server_group,   server_set,   server#,   degree,   req_degree FROM   gv$px_session WHERE   (     qcinst_id = ?   AND qcsid = ?   ) OR   (     (qcsid,qcinst_id) IN     (       SELECT         qcsid,         qcinst_id       FROM         gv$px_session       WHERE         inst_id = ?       AND sid = ?     )   ) ORDER BY   server_group,   server_set,   server#",
//PGA
"select category,round(allocated/1024/1024) ALLOCATED_MB,round(used/1024/1024) USED_MB,round(max_allocated/1024/1024) MAX_ALLOCATED_MB from gv$process_memory where (inst_id,pid,serial#) in (select inst_id,pid,serial# from gv$process where addr=(select paddr from gv$session where inst_id= ? and sid= ? )) order by 2 desc",
//CONN
"select  authentication_type, network_service_banner, client_charset, client_connection, client_oci_library, client_version, client_driver, client_lobattr  from gv$session_connect_info where inst_id = ? and sid= ? ",
//SQLMON
"select dbms_sqltune.report_sql_monitor(inst_id => ?, session_id => ? ,session_serial => ? ,type => 'TEXT',report_level => 'ALL') as report from dual"
};
    String dbUsername = "dbsnmp";
    String dbPassword = "dbsnmp";
    String dbService = "";
    String dbHostname = "";
    String dbPort = "";
    Connection con;
    Statement mainStatement;
    Statement [] minorStatement;
    PreparedStatement [] minorPreparedStatement;  
    Object[][] retVal;

    public OraDB() {
        this.minorPreparedStatement = new PreparedStatement[16];
    }

    public int initmyjdb(String dbhost, String dbsrv, String dbusername, String dbpassword, String dbport) {
        
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            if(con != null && !con.isClosed()){
                if(mainStatement != null && !mainStatement.isClosed()){
                    mainStatement.close();
                }
                if(minorPreparedStatement!= null){
                    for(PreparedStatement minorPrepStmt : minorPreparedStatement){
                        if(minorPrepStmt!=null && !minorPrepStmt.isClosed()){
                            minorPrepStmt.close();
                        }
                    }
                }
                if(minorStatement != null){
                    for (Statement minorStmt : minorStatement) {
                        if(minorStmt != null && ! minorStmt.isClosed()){
                            minorStmt.close();
                        }
                    }
                }
                con.close();
            }
            con = DriverManager.getConnection("jdbc:oracle:thin:@" + dbhost + ":"+ dbport + dbsrv, dbusername, dbpassword);
            return 0;
        } catch (Exception e) {
            System.out.println(e);
            return 1;
        }
    }

    public Object[][] mainQuery() {
        ResultSet mainRS;
        try {
        if(mainStatement == null || mainStatement.isClosed()){
            mainStatement = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        }    
        mainRS = mainStatement.executeQuery(mainQuery);
        ResultSetMetaData mainrsmd = mainRS.getMetaData();
        int mainRSColCount = mainrsmd.getColumnCount();
        int mainRSRowsCount;
        if (mainRS.last()) {
            mainRSRowsCount = mainRS.getRow();
            mainRS.beforeFirst();
        } else {
            mainRSRowsCount = 0;
        }
        retVal = new Object[mainRSRowsCount][mainRSColCount];
        int j = 0;
        while (mainRS.next()) {
            for (int i = 1; i <= mainRSColCount; i++) {
                switch(i){
                    case 1://ROWNUM
                        retVal[j][i - 1] = mainRS.getInt(i);
                        break;
                    case 2://INST_ID
                        retVal[j][i - 1] = mainRS.getInt(i);
                        break;
                    case 4://SERIAL
                        retVal[j][i - 1] = mainRS.getInt(i);
                        break;                        
                    case 13://SECONDS
                        retVal[j][i - 1] = mainRS.getFloat(i);
                        break;                                                
                    case 14://PGA_USED_MEM
                        retVal[j][i - 1] = mainRS.getFloat(i);
                        break;                                
                    case 15://PGA_ALLOC_MEM
                        retVal[j][i - 1] = mainRS.getFloat(i);
                        break;                                
                    case 16://PGA_MAX_MEM
                        retVal[j][i - 1] = mainRS.getFloat(i);
                        break;                                                        
                    default:
                        retVal[j][i - 1] = mainRS.getString(i);
                        break;
                }   
            }
            j++;
        }
        } catch (Exception e) {
            System.out.println(e);
        }
        return retVal;
    }

    public Object [][] minorQuery(int tabID, int [] inp ){
        ResultSet minorRS;
        int minorRSColCount,minorRSRowsCount;
        try {
        if( minorPreparedStatement[tabID]==null || minorPreparedStatement[tabID].isClosed()){
            minorPreparedStatement[tabID] = con.prepareStatement(minorQuery[tabID],ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        }
        switch(tabID){
            case 0://WAITS
                minorPreparedStatement[tabID].setInt(1, inp[0]);
                minorPreparedStatement[tabID].setInt(3, inp[0]);
                minorPreparedStatement[tabID].setInt(2, inp[1]);
                minorPreparedStatement[tabID].setInt(4, inp[1]);
                break;
            case 7://SQL
                minorPreparedStatement[tabID].setInt(1, inp[0]);
                minorPreparedStatement[tabID].setInt(3, inp[0]);
                minorPreparedStatement[tabID].setInt(2, inp[1]);
                minorPreparedStatement[tabID].setInt(4, inp[1]);
                break; 
            case 11://LOCKS
                minorPreparedStatement[tabID].setInt(1, inp[0]);
                minorPreparedStatement[tabID].setInt(3, inp[0]);
                minorPreparedStatement[tabID].setInt(5, inp[0]);
                minorPreparedStatement[tabID].setInt(2, inp[1]);
                minorPreparedStatement[tabID].setInt(4, inp[1]);
                minorPreparedStatement[tabID].setInt(6, inp[1]);
                break;                 
            case 12://PQ
                minorPreparedStatement[tabID].setInt(1, inp[0]);
                minorPreparedStatement[tabID].setInt(3, inp[0]);
                minorPreparedStatement[tabID].setInt(2, inp[1]);
                minorPreparedStatement[tabID].setInt(4, inp[1]);
                break; 
            case 15://SQLMON
                minorPreparedStatement[tabID].setInt(1, inp[0]);
                minorPreparedStatement[tabID].setInt(2, inp[1]);
                minorPreparedStatement[tabID].setInt(3, inp[2]);
                break;                
            default:
                minorPreparedStatement[tabID].setInt(1, inp[0]);
                minorPreparedStatement[tabID].setInt(2, inp[1]);
                break;
        }
        minorPreparedStatement[tabID].execute();
        minorRS = minorPreparedStatement[tabID].getResultSet();
        ResultSetMetaData minorRSMetaData = minorRS.getMetaData();
        minorRSColCount = minorRSMetaData.getColumnCount();
        if (minorRS.last()) {
            minorRSRowsCount = minorRS.getRow();
            minorRS.beforeFirst();
        } else {
            minorRSRowsCount = 0;
        }
        if(minorRSRowsCount==0 || minorRSColCount==0){
            return null;
        }else{
            retVal = new Object[minorRSRowsCount][minorRSColCount];
            int j = 0;
            while (minorRS.next()) {
                for (int i = 1; i <= minorRSColCount; i++) {
                    retVal[j][i - 1] = minorRS.getString(i);
                }
                j++;
            }
            return retVal;        
        }
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
