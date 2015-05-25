/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 */

public class OraLDAP {
    LdapContext ldapctx;
    HashMap <String,String> tnslines;
    Map <String, String> sortedtnslines;
            
    public int initmyldap(String ldaphost) {
        if(ldaphost == null){
            ldaphost = "ldap.example.org";
        }        
        Hashtable<String,String> env=new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://"+ldaphost+":389");
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
//
        try{
            NamingEnumeration<SearchResult> answer = ldapctx.search(searchbase, searchfilter, searchctls);
            while (answer.hasMoreElements()) {
                SearchResult sr = (SearchResult) answer.next();
                Attributes attrs = sr.getAttributes();
                if (attrs != null) {
                    result.put(attrs.get(returnedattrs[0]).toString(),attrs.get(returnedattrs[1]).toString());
                    //NamingEnumeration<?> ne = attrs.getAll();
                    //while (ne.hasMore()) {
                        //Attribute attr = (Attribute) ne.next();
                        //if (attr.size() == 1) {
                        //} else {
                            //HashSet<String> s = new HashSet<String>();
                            //NamingEnumeration n =  attr.getAll();
                            //while (n.hasMoreElements()) {
                                //s.add((String)n.nextElement());
                            //}
                            //amap.put(attr.getID(), s);
                        //}
                    //}
                    //ne.close();
                }
                ldapctx.close();
            }
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
        if ( ! result.isEmpty() ) {
            Pattern pattern = Pattern.compile("host=([A-Za-z0-9\\.\\-\\_]*)");
            tnslines = new HashMap<>();
            Set<String> keys = result.keySet();
            for (String k : keys){
                Matcher matcher = pattern.matcher(result.get(k));
                if (matcher.find()){
                    tnslines.put(k.substring(3,k.length()).trim(),matcher.group(1));
                }                
            }
            sortedtnslines = new TreeMap<>(tnslines);
        } else {
            System.out.println("No attributes!");
        }
//
        return sortedtnslines;
    }
}