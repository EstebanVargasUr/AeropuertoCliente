package org.una.aeropuertocliente.WebService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.una.aeropuertocliente.utility.JSONUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Date;
import org.una.aeropuertocliente.DTOs.AreaTrabajoAvionDTO;
import org.una.aeropuertocliente.DTOs.AreaTrabajoDTO;
import org.una.aeropuertocliente.DTOs.AvionDTO;

/**
 *
 * @author Adrian
 */

public class AreaTrabajoAvionWebService {
    
    private static final HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).build();
    private static final String serviceURL = "http://localhost:8099/areasTrabajoAviones";
    
    public static void getAreaTrabajoAvionById(long id) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findById/"+id)).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Area de trabajo del avion No Encontrada");

        else
        {
            AreaTrabajoAvionDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), AreaTrabajoAvionDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
    
    public static void getAreaTrabajoAvionByFechaRegistroBetween(Date fechaInicial, Date fechaFinal) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByFechaRegistroBetween/"+fechaInicial+"/"+fechaFinal)).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Area trabajo del avion No Encontrada");

        else
        {
            AreaTrabajoAvionDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), AreaTrabajoAvionDTO.class);
            System.out.println(bean);
        }
        response.join();
    }


    public static void createAreaTrabajoAvion(AreaTrabajoDTO areaTrabajo, AvionDTO avion) throws InterruptedException, ExecutionException, JsonProcessingException
    {
        AreaTrabajoAvionDTO bean = new AreaTrabajoAvionDTO();
        
        bean.setAreaTrabajo(areaTrabajo);
        bean.setAvion(avion);

        String inputJson = JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.get().body());

    }

    public static void updateAreaTrabajoAvion(AreaTrabajoAvionDTO bean, long id) throws InterruptedException, ExecutionException, IOException
    {
        String inputJson=JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"+id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());

        if(response.get().statusCode() == 500)
            System.out.println("No se pudo actualizar el Area de trabajo del avion");

        else {
            bean = JSONUtils.covertFromJsonToObject(response.get().body(), AreaTrabajoAvionDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

}

