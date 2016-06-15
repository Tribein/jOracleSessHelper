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

import javax.naming.ldap.*;
import javax.naming.directory.*;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map;

/**
 *
 * @author Tribein
 */

public class OraLDAP {
    LdapContext ldapctx;
    HashMap <String,String> tnslines;
    HashMap <String,String> tnsports;
    HashMap <String,String> tnsservices;
    Map <String, String> sortedtnslines;
            
    public int initmyldap(String ldaphost, String ldapport) {
        if(ldaphost == null || ldapport==null){
            return 1;
        }              
        Hashtable<String,String> env=new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://"+ldaphost+":"+ldapport);
        try{
            ldapctx = new javax.naming.ldap.InitialLdapContext(env,null);        
        }catch (Exception e) {
            System.out.println(e);
            return 1;
        }
        return 0;
    }
    
    public Map <String,String> gettnsrecs(){
        String searchbase = "cn=OracleContext,DC=world";
        String returnedattrs[] ={ "cn","orclNetDescString" };
        String searchfilter = "(&(objectClass=orclNetService)(cn=*))";                
        SearchControls searchctls = new SearchControls();
        searchctls.setReturningAttributes(returnedattrs);
        searchctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        HashMap <String,String> result = new HashMap<>();
        try{
            NamingEnumeration<SearchResult> answer = ldapctx.search(searchbase, searchfilter, searchctls);
            while (answer.hasMoreElements()) {
                SearchResult sr = (SearchResult) answer.next();
                Attributes attrs = sr.getAttributes();
                if (attrs != null) {
                    result.put(attrs.get(returnedattrs[0]).toString(),attrs.get(returnedattrs[1]).toString());
                }
                ldapctx.close();
            }
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
        if ( ! result.isEmpty() ) {
            Pattern hostpattern = Pattern.compile("host=([A-Za-z0-9\\.\\-\\_]+)");
            Pattern portpattern = Pattern.compile("port=([0-9]{1,5})");
            Pattern servicepattern = Pattern.compile("sid=([A-Za-z0-9\\-\\_]+)|service_name=([A-Za-z0-9\\-\\_]+)");
            tnslines = new HashMap<>();
            tnsports = new HashMap<>();
            tnsservices = new HashMap<>();
            Set<String> keys = result.keySet();
            for (String k : keys){
                Matcher hostmatcher = hostpattern.matcher(result.get(k));
                if (hostmatcher.find()){
                    tnslines.put(k.substring(3,k.length()).trim(),hostmatcher.group(1));
                }                
                Matcher portmatcher = portpattern.matcher(result.get(k));
                if (portmatcher.find()){
                    tnsports.put(k.substring(3,k.length()).trim(),portmatcher.group(1));
                }
                Matcher servicematcher = servicepattern.matcher(result.get(k));
                if (servicematcher.find()){
                    tnsservices.put(k.substring(3,k.length()).trim(),((servicematcher.group(1)==null)? "/"+servicematcher.group(2) : ":"+servicematcher.group(1)));
                }
            }
            sortedtnslines = new TreeMap<>(tnslines);
        } else {
            System.out.println("No attributes!");
        }
        return sortedtnslines;
    }
    
    public Map <String,String> gettnsports(){
        return tnsports;
    }    
    public Map <String,String> gettnsservices(){
        return tnsservices;
    }        
}