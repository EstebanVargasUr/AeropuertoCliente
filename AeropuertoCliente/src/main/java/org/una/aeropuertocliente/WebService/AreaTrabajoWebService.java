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
import org.una.aeropuertocliente.DTOs.AreaTrabajoDTO;
import org.una.aeropuertocliente.utility.JSONUtils;
/**
 *
 * @author Esteban Vargas
 */
public class AreaTrabajoWebService {
    
    private static final HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).build();
    private static final String serviceURL = "http://localhost:8099/areasTrabajo";
    
    public static void getAllAreasTrabajo() throws InterruptedException, ExecutionException, JsonParseException, JsonMappingException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findAll")).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<AreaTrabajoDTO> areasTrabajo = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<AreaTrabajoDTO>>() {});
        areasTrabajo.forEach(System.out::println);
        response.join();
    }

    public static void getAreaTrabajoById(long id) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findById/"+id)).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Area de Trabajo No Encontrada");

        else
        {
            AreaTrabajoDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), AreaTrabajoDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
    
    public static void getAreaTrabajoByNombreArea(String nombre) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByNombreArea/"+nombre)).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Area de Trabajo No Encontrada");

        else
        {
            AreaTrabajoDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), AreaTrabajoDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
    
    public static void getAreaTrabajoByNombreResponsable(String nombre) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByNombreResponsable/"+nombre)).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Area de Trabajo No Encontrada");

        else
        {
            AreaTrabajoDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), AreaTrabajoDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

    public static void createAerolinea(String nombreArea, String nombreResponsable) throws InterruptedException, ExecutionException, JsonProcessingException
    {
        AreaTrabajoDTO bean = new AreaTrabajoDTO();
        
        bean.setNombreArea(nombreArea);
        bean.setNombreResponsable(nombreResponsable);

        String inputJson = JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.get().body());

    }

    public static void updateAerolinea(AreaTrabajoDTO bean, long id) throws InterruptedException, ExecutionException, IOException
    {
        String inputJson=JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"+id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());

        if(response.get().statusCode() == 500)
            System.out.println("No se pudo actualizar la Area de Trabajo");

        else {
            bean = JSONUtils.covertFromJsonToObject(response.get().body(), AreaTrabajoDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

}