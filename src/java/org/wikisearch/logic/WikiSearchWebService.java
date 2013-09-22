/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wikisearch.logic;

import java.util.ArrayList;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author santiago
 */
@WebService(serviceName = "WikiSearchWebService")
public class WikiSearchWebService {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "doSearch")
    public ArrayList<String> doSearch(@WebParam(name = "keyword") String keyword) {
        return WikiSearch.doSearch(keyword);
    }
}
