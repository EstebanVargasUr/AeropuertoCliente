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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.una.aeropuertocliente.DTOs.AvionDTO;
import org.una.aeropuertocliente.DTOs.VueloDTO;
import org.una.aeropuertocliente.utility.JSONUtils;
/**
 *
 * @author Esteban Vargas
 */
public class VueloWebService {
    
    private static final HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).build();
    private static final String serviceURL = "http://localhost:8099/vuelos";

    public static VueloDTO getVueloById(long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        VueloDTO bean = null;
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findById/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Vuelo No Encontrado");

        else
        {
            bean = JSONUtils.covertFromJsonToObject(response.get().body(), VueloDTO.class);
            System.out.println(bean);
        }
        response.join();
        return bean;
    }
    
    public static List<VueloDTO> getVueloByAeropuerto(String nombre, String finalToken) throws InterruptedException, ExecutionException, IOException
    {        
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByAeropuerto/"+nombre))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<VueloDTO> vuelos = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<VueloDTO>>() {});
        vuelos.forEach(System.out::println);
        response.join();
        return vuelos;
    }
    
    public static List<VueloDTO> getVueloByEstado(boolean estado, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByEstado/"+estado))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<VueloDTO> vuelos = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<VueloDTO>>() {});
        vuelos.forEach(System.out::println);
        response.join();
        return vuelos;
    }

    public static List<VueloDTO> getVueloByFechaSalidaBetween(Date fechaInicial, Date fechaFinal, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        SimpleDateFormat DateFor = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate= DateFor.format(fechaInicial);
        String stringDate2= DateFor.format(fechaFinal);
        
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByFechaSalidaBetween/"+stringDate+"/"+stringDate2))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<VueloDTO> vuelos = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<VueloDTO>>() {});
        vuelos.forEach(System.out::println);
        response.join();
        return vuelos;
    }
    
    public static List<VueloDTO> getVueloByFechaLlegadaBetween(Date fechaInicial, Date fechaFinal, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        SimpleDateFormat DateFor = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate= DateFor.format(fechaInicial);
        String stringDate2= DateFor.format(fechaFinal);
        
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByFechaLlegadaBetween/"+stringDate+"/"+stringDate2))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<VueloDTO> vuelos = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<VueloDTO>>() {});
        vuelos.forEach(System.out::println);
        response.join();
        return vuelos;
    }
    
    public static List<VueloDTO> getVueloByAvionId(long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {        
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByAvionId/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<VueloDTO> vuelos = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<VueloDTO>>() {});
        vuelos.forEach(System.out::println);
        response.join();
        return vuelos;
    }

    public static void createVuelo(VueloDTO bean, String finalToken) throws InterruptedException, ExecutionException, JsonProcessingException
    {
        String inputJson = JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken)
        .POST(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.get().body());

    }

    public static void updateVuelo(VueloDTO bean, long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        String inputJson=JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken)
        .PUT(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());

        if(response.get().statusCode() == 500)
            System.out.println("No se pudo actualizar el Vuelo");

        else {
            bean = JSONUtils.covertFromJsonToObject(response.get().body(), VueloDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

}