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
import com.fasterxml.jackson.core.type.TypeReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
    
    public static AreaTrabajoAvionDTO getAreaTrabajoAvionById(long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findById/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Area de trabajo del avion No Encontrada");

        else
        {
            AreaTrabajoAvionDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), AreaTrabajoAvionDTO.class);
            System.out.println(bean);
            return bean;
        }
        response.join();
        return null;
    }
    
    public static List<AreaTrabajoAvionDTO> getAreaTrabajoAvionByFechaRegistroBetween(Date fechaInicial, Date fechaFinal, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        SimpleDateFormat DateFor = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate= DateFor.format(fechaInicial);
        String stringDate2= DateFor.format(fechaFinal);
        
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByFechaRegistroBetween/"+stringDate+"/"+stringDate2))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<AreaTrabajoAvionDTO> areasTrabajoAviones = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<AreaTrabajoAvionDTO>>() {});
        areasTrabajoAviones.forEach(System.out::println);
        response.join();
        return areasTrabajoAviones; 
    }

    public static AreaTrabajoAvionDTO getAreaTrabajoAvionByAvionId(long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByAvionId/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Avion No Encontrado");

        else
        {
            AreaTrabajoAvionDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), AreaTrabajoAvionDTO.class);
            System.out.println(bean);
            return bean;
        }
        return null;
    }
    
    public static List<AreaTrabajoAvionDTO> getAreaTrabajoAvionByAreaTrabajoId(long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByAreaTrabajoId/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Avion No Encontrado");

        else
        {
            List<AreaTrabajoAvionDTO> beans = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<AreaTrabajoAvionDTO>>() {});
            beans.forEach(System.out::println);
            return beans;
        }
        return null;
    }
    
    public static List<AreaTrabajoAvionDTO> getAreaTrabajoAvionByFechaRegistroAndAerolineaAndZona(Date fechaInicial, Date fechaFinal,long idAero,long idZona, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        SimpleDateFormat DateFor = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate= DateFor.format(fechaInicial);
        String stringDate2= DateFor.format(fechaFinal);
        
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByFechaRegistroAndAerolineaAndZona/"+stringDate+"/"+stringDate2+"/"+idAero+"/"+idZona))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<AreaTrabajoAvionDTO> areasTrabajoAviones = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<AreaTrabajoAvionDTO>>() {});
        areasTrabajoAviones.forEach(System.out::println);
        response.join();
        return areasTrabajoAviones; 
    }

    public static void createAreaTrabajoAvion(AreaTrabajoDTO areaTrabajo, AvionDTO avion, String finalToken) throws InterruptedException, ExecutionException, JsonProcessingException
    {
        AreaTrabajoAvionDTO bean = new AreaTrabajoAvionDTO();
        
        bean.setAreaTrabajo(areaTrabajo);
        bean.setAvion(avion);

        String inputJson = JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken)
        .POST(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.get().body());

    }

    public static void updateAreaTrabajoAvion(AreaTrabajoDTO areaTrabajo,AvionDTO avion, long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        AreaTrabajoAvionDTO bean = new AreaTrabajoAvionDTO();
        
        bean.setAreaTrabajo(areaTrabajo);
        bean.setAvion(avion);
        String inputJson=JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken)
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

