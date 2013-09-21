package org.wikisearch.logic;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author santiago
 *
 * Esta clase brinda las funcionalidades basicas para el uso de la API de
 * wikipedia
 */
public class WikiSearch {

    public static String url = "http://en.wikipedia.org/w/api.php?";
    public static String responseFormat = "xml";

    /**
     *
     * @param keyword
     * @return A list of results
     */
    public static String doSearch(String keyword) {
        HttpClient client = new DefaultHttpClient();
        String responseBody = null;
        
        //%20 es el equivalente de espacio en url (tambien puede ir un signo +)
        keyword = keyword.replaceAll(" ", "%20");
        HttpGet request = new HttpGet(url + "action=query&list=search&srwhat=text&format=" + responseFormat + "&srsearch=" + keyword);
        try {
            HttpResponse response = client.execute(request);
            responseBody = EntityUtils.toString(response.getEntity());
        } catch (IOException ex) {
            Logger.getLogger(WikiSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
        return processXML(responseBody);
    }

    /**
     * Esta funcion procesa una respuesta en formato JSON
     *
     * @param json
     * @return
     */
    public static String processJSON(String json) {
        return "";
    }

    public static String processXML(String xml) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        String title = null;
        String resume = null;
        Random rand = new Random();
        int value = rand.nextInt(3);

        try {
            db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));
            doc.getDocumentElement().normalize();

            Element rootElement = doc.getDocumentElement();
            NodeList query = rootElement.getElementsByTagName("query");
            Element e = (Element) query.item(0);
            NodeList search = e.getElementsByTagName("search");
            Element e1 = (Element) search.item(0);
            NodeList ps = e1.getElementsByTagName("p");
            Element e2 = (Element) ps.item(value);
            title = e2.getAttribute("title");
            resume = e2.getAttribute("snippet");
            resume = resume.replaceAll("\\<.*?>", "");
            resume = resume.replaceAll("&.*?;", "");
        } catch (SAXException ex) {
            Logger.getLogger(WikiSearch.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WikiSearch.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(WikiSearch.class.getName()).log(Level.SEVERE, null, ex);
        }

        return title + " " + resume;
    }
    
}
