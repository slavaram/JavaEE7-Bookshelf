package ru.vramaz.bookshelf.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/v1.0/")
public class Root {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getRootText() {
        StringBuilder result = new StringBuilder();
        result.append("supported http methods (CRUD):");
        result.append("\n");
        result.append("POST for Create");
        result.append("\n");
        result.append("GET for Read");
        result.append("\n");
        result.append("PUT for Update");
        result.append("\n");
        result.append("DELETE for Delete");
        result.append("\n\n");
        result.append("supported media types:");
        result.append("\n");
        result.append("text/plain");
        result.append("\n");
        result.append("application/xml");
        result.append("\n");
        result.append("application/json");
        result.append("\n\n");
        result.append("wadl location:");
        result.append("\n");
        result.append("/rest/application.wadl");
        result.append("\n\n");
        result.append("branches:");
        result.append("\n");
        result.append("/rest/v1.0/books/");
        result.append("\n");
        result.append("/rest/v1.0/users/");
        return result.toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getRootXml() {
        StringBuilder result = new StringBuilder();
        result.append("<bks:root xmlns:bks='http://ru.vramaz.bookshelf'>");
        result.append("<http-crud>");
        result.append("<create>POST</create>");
        result.append("<read>GET</read>");
        result.append("<update>PUT</update>");
        result.append("<delete>DELETE</delete>");
        result.append("</http-crud>");
        result.append("<metadata-types>");
        result.append("<type>text/plain</type>");
        result.append("<type>application/xml</type>");
        result.append("<type>application/json</type>");
        result.append("</metadata-types>");
        result.append("<wadl-location>/rest/application.wadl</wadl-location>");
        result.append("<branches>");
        result.append("<branch>/rest/v1.0/books/</branch>");
        result.append("<branch>/rest/v1.0/users/</branch>");
        result.append("</branches>");
        result.append("</bks:root>");
        return result.toString();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getRootJson() {
        return "{json: is coming}";
    }

}
