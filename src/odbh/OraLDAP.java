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
    LdapContext ldapCtx;
    HashMap <String,String> tnsLines;
    HashMap <String,String> tnsPort;
    HashMap <String,String> tnsService;
    Map <String, String> sortedTNSLines;
            
    public int initLDAP(String ldaphost, String ldapport) {
        if(ldaphost == null || ldapport==null){
            return 1;
        }              
        Hashtable<String,String> env=new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://"+ldaphost+":"+ldapport);
        try{
            ldapCtx = new javax.naming.ldap.InitialLdapContext(env,null);        
        }catch (Exception e) {
            System.out.println(e);
            return 1;
        }
        return 0;
    }
    
    public Map <String,String> getTNSRecs(){
        String searchBase = "cn=OracleContext,DC=world";
        String[] returnedAttrs ={ "cn","orclNetDescString" };
        String searchfilter = "(&(objectClass=orclNetService)(cn=*))";                
        SearchControls searchCtls = new SearchControls();
        searchCtls.setReturningAttributes(returnedAttrs);
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        HashMap <String,String> result = new HashMap<>();
        try{
            NamingEnumeration<SearchResult> answer = ldapCtx.search(searchBase, searchfilter, searchCtls);
            while (answer.hasMoreElements()) {
                SearchResult sr = (SearchResult) answer.next();
                Attributes attrs = sr.getAttributes();
                if (attrs != null) {
                    result.put(attrs.get(returnedAttrs[0]).toString(),attrs.get(returnedAttrs[1]).toString());
                }
                ldapCtx.close();
            }
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
        if ( ! result.isEmpty() ) {
            Pattern hostPattern = Pattern.compile("host=([A-Za-z0-9\\.\\-\\_]+)");
            Pattern portPattern = Pattern.compile("port=([0-9]{1,5})");
            Pattern servicePattern = Pattern.compile("sid=([A-Za-z0-9\\-\\_]+)|service_name=([A-Za-z0-9\\-\\_]+)");
            tnsLines = new HashMap<>();
            tnsPort = new HashMap<>();
            tnsService = new HashMap<>();
            Set<String> keys = result.keySet();
            for (String k : keys){
                Matcher hostMatcher = hostPattern.matcher(result.get(k));
                if (hostMatcher.find()){
                    tnsLines.put(k.substring(3,k.length()).trim(),hostMatcher.group(1));
                }                
                Matcher portMatcher = portPattern.matcher(result.get(k));
                if (portMatcher.find()){
                    tnsPort.put(k.substring(3,k.length()).trim(),portMatcher.group(1));
                }
                Matcher serviceMatcher = servicePattern.matcher(result.get(k));
                if (serviceMatcher.find()){
                    tnsService.put(k.substring(3,k.length()).trim(),((serviceMatcher.group(1)==null)? "/"+serviceMatcher.group(2) : ":"+serviceMatcher.group(1)));
                }
            }
            sortedTNSLines = new TreeMap<>(tnsLines);
        } else {
            System.out.println("No attributes!");
        }
        return sortedTNSLines;
    }
    
    public Map <String,String> getTNSPorts(){
        return tnsPort;
    }    
    public Map <String,String> getTNSServices(){
        return tnsService;
    }        
}