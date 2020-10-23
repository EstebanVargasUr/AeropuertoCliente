package org.una.aeropuertocliente.WebService;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.una.aeropuertocliente.DTOs.AuthenticationRequest;
import org.una.aeropuertocliente.utility.JSONUtils;

/**
 *
 * @author Esteban Vargas
 */
public class AutenticationWebService {
    private static final HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).build();
    private static final String serviceURL = "http://localhost:8099/login";
    
    public static String login(String cedula,String password) throws InterruptedException, ExecutionException, JsonProcessingException
    {
            AuthenticationRequest bean = new AuthenticationRequest();
            
            bean.setCedula(cedula);
            bean.setPassword(password);
            
            String inputJson = JSONUtils.covertFromObjectToJson(bean);
            HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(inputJson)).build();
            CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());            
            System.out.println(response.get().body());
            return response.get().body();
    }
}
