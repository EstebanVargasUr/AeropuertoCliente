package org.una.aeropuertocliente.WebService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.una.aeropuertocliente.DTOs.RolDTO;
import org.una.aeropuertocliente.utility.JSONUtils;
/**
 *
 * @author Esteban Vargas
 */
public class RolWebService {
    
    private static final HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).build();
    private static final String serviceURL = "http://localhost:8099/roles";
    
    public static void getAllRoles() throws InterruptedException, ExecutionException, JsonParseException, JsonMappingException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/")).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<RolDTO> roles = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<RolDTO>>() {});
        roles.forEach(System.out::println);
        response.join();
    }

    public static void getRolById() throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/{id}")).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Rol No Encontrado");

        else
        {
            RolDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), RolDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
    
    public static void getRolByTipo() throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/tipo/{termino}")).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Rol No Encontrado");

        else
        {
            RolDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), RolDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
    
    public static void getRolByEstado() throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/estado/{termino}")).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Rol No Encontrado");

        else
        {
            RolDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), RolDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

    public static void createRol(String tipo) throws InterruptedException, ExecutionException, JsonProcessingException
    {
        RolDTO bean = new RolDTO();
        
        bean.setTipo(tipo);

        String inputJson = JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.get().body());

    }

    public static void updateRol(RolDTO bean) throws InterruptedException, ExecutionException, IOException
    {
        String inputJson=JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/{id}"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());

        if(response.get().statusCode() == 500)
            System.out.println("No se pudo actualizar el Rol");

        else {
            bean = JSONUtils.covertFromJsonToObject(response.get().body(), RolDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

}