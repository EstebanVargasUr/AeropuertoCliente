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
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Date;
import org.una.aeropuertocliente.DTOs.AlertaDTO;
import org.una.aeropuertocliente.DTOs.VueloDTO;
import org.una.aeropuertocliente.utility.JSONUtils;
/**
 *
 * @author Adrian
 */
public class AlertaWebService {
    
    private static final HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).build();
    private static final String serviceURL = "http://localhost:8099/alertas";
    
    public static void getAlertaById(long id) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findById/"+id)).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Alerta No Encontrada");

        else
        {
            AlertaDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), AlertaDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
    
    public static void getAlertaByFechaRegistroBetween(Date fechaInicial, Date fechaFinal) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByFechaRegistroBetween/"+fechaInicial+"/"+fechaFinal)).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Alerta No Encontrado");

        else
        {
            AlertaDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), AlertaDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

    public static void getAlertaByEstado(boolean estado) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByEstado/"+estado)).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Alerta No Encontrado");

        else
        {
            AlertaDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), AlertaDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
    
    public static void getAlertaByVueloId(long id) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByVueloId/"+id)).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Alerta No Encontrado");

        else
        {
            AlertaDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), AlertaDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
    
    public static void createAlerta(String areaTrabajo, VueloDTO vuelo) throws InterruptedException, ExecutionException, JsonProcessingException
    {
        AlertaDTO bean = new AlertaDTO();
        
        bean.setDescripcion(areaTrabajo);
        bean.setVuelo(vuelo);

        String inputJson = JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.get().body());

    }

    public static void updateAlerta(AlertaDTO bean, long id) throws InterruptedException, ExecutionException, IOException
    {
        String inputJson=JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"+id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());

        if(response.get().statusCode() == 500)
            System.out.println("No se pudo actualizar la Alerta");

        else {
            bean = JSONUtils.covertFromJsonToObject(response.get().body(), AlertaDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

}


