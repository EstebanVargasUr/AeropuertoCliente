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
import java.text.SimpleDateFormat;
import java.util.Date;
import org.una.aeropuertocliente.DTOs.AvionDTO;
import org.una.aeropuertocliente.utility.JSONUtils;
/**
 *
 * @author Esteban Vargas
 */
public class AvionWebService {
        
    private static final HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).build();
    private static final String serviceURL = "http://localhost:8099/aviones";
    
    public static List<AvionDTO> getAllAviones(String finalToken) throws InterruptedException, ExecutionException, JsonParseException, JsonMappingException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findAll"))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<AvionDTO> aviones = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<AvionDTO>>() {});
        aviones.forEach(System.out::println);
        response.join();
        return aviones;
    }

    public static AvionDTO getAvionById(long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        AvionDTO bean = new AvionDTO();
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findById/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Avion No Encontrado");

        else
        {
            if (response.get().body().isBlank()) {
                System.out.println("No existen vuelos con este Id");
            }
            else {
                bean = JSONUtils.covertFromJsonToObject(response.get().body(), AvionDTO.class);
                System.out.println(bean);
            }
        }
        response.join();
        return bean;
    }
    
    public static List<AvionDTO> getAvionByMatricula(String matricula, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByMatricula/"+matricula))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Avion No Encontrado");

        else
        {
            List<AvionDTO> beans = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<AvionDTO>>() {});
            beans.forEach(System.out::println);
            return beans;
        }
        response.join();
        return null;
    }
    
    public static List<AvionDTO> getAvionByTipoAvion(String id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByTipoAvion/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Avion No Encontrado");

        else
        {
            List<AvionDTO> beans = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<AvionDTO>>() {});
            beans.forEach(System.out::println);
            return beans;
        }
        response.join();
        return null;
    }

    public static List<AvionDTO> getAvionByFechaRegistroBetween(Date fechaInicial, Date fechaFinal, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        SimpleDateFormat DateFor = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate= DateFor.format(fechaInicial);
        String stringDate2= DateFor.format(fechaFinal);
        
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByFechaRegistroBetween/"+stringDate+"/"+stringDate2))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Avion No Encontrado");

        else
        {
            List<AvionDTO> beans = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<AvionDTO>>() {});
            beans.forEach(System.out::println);
            return beans;
        }
        response.join();
        return null;
    }
    
    public static List<AvionDTO> getAvionByEstado(Boolean estado, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByEstado/"+estado))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Avion No Encontrado");

        else
        {
            List<AvionDTO> beans = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<AvionDTO>>() {});
            beans.forEach(System.out::println);
            return beans;
        }
        response.join();
        return null;
    }
    
    public static List<AvionDTO> getAvionByAerolineaId(String id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByAerolinea/"+id))
                .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<AvionDTO> aviones = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<AvionDTO>>() {});
        aviones.forEach(System.out::println);
        response.join();
        return aviones;
    }

    public static void createAvion(AvionDTO bean, String finalToken) throws InterruptedException, ExecutionException, JsonProcessingException
    {
        String inputJson = JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken)
        .POST(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.get().body());

    }

    public static void updateAvion(AvionDTO bean, long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        String inputJson=JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken)
        .PUT(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());

        if(response.get().statusCode() == 500)
            System.out.println("No se pudo actualizar el Avion");

        else {
            bean = JSONUtils.covertFromJsonToObject(response.get().body(), AvionDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

}