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
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Date;
import java.util.List;
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
    
    public static void getAlertaById(long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findById/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
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
    
    public static void getAlertaByFechaRegistroBetween(Date fechaInicial, Date fechaFinal, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByFechaRegistroBetween/"+fechaInicial+"/"+fechaFinal))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Alerta No Encontrado");

        else
        {
            List<AlertaDTO> alertas = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<AlertaDTO>>() {});
            alertas.forEach(System.out::println);
        }
        response.join();
    }

    public static void getAlertaByEstado(boolean estado, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByEstado/"+estado))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Alerta No Encontrado");

        else
        {
            List<AlertaDTO> alertas = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<AlertaDTO>>() {});
            alertas.forEach(System.out::println);
        }
        response.join();
    }
    
    public static void getAlertaByVueloId(long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByVueloId/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Alerta No Encontrado");

        else
        {
            List<AlertaDTO> alertas = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<AlertaDTO>>() {});
            alertas.forEach(System.out::println);
        }
        response.join();
    }
    
    public static void createAlerta(String areaTrabajo, VueloDTO vuelo, String finalToken) throws InterruptedException, ExecutionException, JsonProcessingException
    {
        AlertaDTO bean = new AlertaDTO();
        
        bean.setDescripcion(areaTrabajo);
        bean.setVuelo(vuelo);

        String inputJson = JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken)
        .POST(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.get().body());

    }

    public static void updateAlerta(AlertaDTO bean, long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        String inputJson=JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken)
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


