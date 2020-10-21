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
import org.una.aeropuertocliente.DTOs.AerolineaDTO;
import org.una.aeropuertocliente.utility.JSONUtils;
/**
 *
 * @author Esteban Vargas
 */
public class AerolineaWebService {
    
    private static final HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).build();
    private static final String serviceURL = "http://localhost:8099/aerolineas";
    
    public static void getAllAerolineas() throws InterruptedException, ExecutionException, JsonParseException, JsonMappingException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL)).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<AerolineaDTO> aerolineas = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<AerolineaDTO>>() {});
        aerolineas.forEach(System.out::println);
        response.join();
    }

    public static void getAerolineaById() throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/{id}")).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Aerolinea No Encontrada");

        else
        {
            AerolineaDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), AerolineaDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
    
    public static void getAerolineaByNombreAerolinea() throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/nombreAerolinea/{termino}")).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Aerolinea No Encontrada");

        else
        {
            AerolineaDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), AerolineaDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
    
    public static void getAerolineaByNombreResponsable() throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/nombreResponsable/{termino}")).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Aerolinea No Encontrada");

        else
        {
            AerolineaDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), AerolineaDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

    public static void getAerolineaByEstado() throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/Estado/{termino}")).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Aerolinea No Encontrada");

        else
        {
            AerolineaDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), AerolineaDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

    public static void createAerolinea(String nombreAerolinea, String nombreResponsable) throws InterruptedException, ExecutionException, JsonProcessingException
    {
        AerolineaDTO bean = new AerolineaDTO();
        
        bean.setNombreAerolinea(nombreAerolinea);
        bean.setNombreResponsable(nombreResponsable);

        String inputJson = JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.get().body());

    }

    public static void updateAerolinea(AerolineaDTO bean) throws InterruptedException, ExecutionException, IOException
    {
        String inputJson=JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/{id}"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());

        if(response.get().statusCode() == 500)
            System.out.println("No se pudo actualizar la Aerolinea");

        else {
            bean = JSONUtils.covertFromJsonToObject(response.get().body(), AerolineaDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

}