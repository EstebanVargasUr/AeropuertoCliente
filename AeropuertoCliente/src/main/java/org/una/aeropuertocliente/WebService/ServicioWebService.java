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
import org.una.aeropuertocliente.DTOs.ServicioDTO;
import org.una.aeropuertocliente.utility.JSONUtils;
/**
 *
 * @author Esteban Vargas
 */
public class ServicioWebService {
    
    private static final HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).build();
    private static final String serviceURL = "http://localhost:8099/servicios";
    
    public static ServicioDTO getServicioById(long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        ServicioDTO bean = new ServicioDTO();
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findById/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Servicio No Encontrado");

        else
        {
            if (response.get().body().isBlank()) {
                System.out.println("No existen vuelos con este Id");
            }
            else {
                bean = JSONUtils.covertFromJsonToObject(response.get().body(), ServicioDTO.class);
                System.out.println(bean);
            }
        }
        response.join();
        return bean;
    }
    
     public static List<ServicioDTO> getServicioByEstado(boolean estado, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByEstado/"+estado))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<ServicioDTO> servicios = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<ServicioDTO>>() {});
        servicios.forEach(System.out::println);
        response.join();
        return servicios;
    }
    
    public static List<ServicioDTO> getServicioByEstadoCobro(boolean estado, String finalToken) throws InterruptedException, ExecutionException, IOException
    {      
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByEstadoCobro/"+estado))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<ServicioDTO> servicios = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<ServicioDTO>>() {});
        servicios.forEach(System.out::println);
        response.join();
        return servicios;     
    }
    
    public static List<ServicioDTO> getServicioByFechaRegistroBetween(Date fechaInicial, Date fechaFinal, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        SimpleDateFormat DateFor = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate= DateFor.format(fechaInicial);
        String stringDate2= DateFor.format(fechaFinal);
        
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByFechaRegistroBetween/"+stringDate+"/"+stringDate2))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<ServicioDTO> servicios = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<ServicioDTO>>() {});
        servicios.forEach(System.out::println);
        response.join();
        return servicios; 
    }

    public static List<ServicioDTO> getServicioByAvionId(long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByAvionId/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<ServicioDTO> servicios = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<ServicioDTO>>() {});
        servicios.forEach(System.out::println);
        response.join();
        return servicios;
    }

    public static List<ServicioDTO> getServicioByTipoServicioId(long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByTipoServicioId/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<ServicioDTO> servicios = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<ServicioDTO>>() {});
        servicios.forEach(System.out::println);
        response.join();
        return servicios; 
    }

    public static List<ServicioDTO> getServicioByTipoServicioIdAndAvionId(long idTipo, long idAvion, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByTipoServicioIdAndAvionId/"+idTipo+"/"+idAvion))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<ServicioDTO> servicios = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<ServicioDTO>>() {});
        servicios.forEach(System.out::println);
        response.join();
        return servicios; 
    }
    
    public static void createServicio(ServicioDTO bean, String finalToken) throws InterruptedException, ExecutionException, JsonProcessingException
    {
        String inputJson = JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken)
        .POST(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.get().body());

    }

    public static void updateServicio(ServicioDTO bean, long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        String inputJson=JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken)
        .PUT(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());

        if(response.get().statusCode() == 500)
            System.out.println("No se pudo actualizar el Servicio");

        else {
            bean = JSONUtils.covertFromJsonToObject(response.get().body(), ServicioDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

    }
