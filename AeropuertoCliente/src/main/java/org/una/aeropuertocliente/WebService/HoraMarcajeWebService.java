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
import org.una.aeropuertocliente.DTOs.HoraMarcajeDTO;
import org.una.aeropuertocliente.DTOs.HorarioDTO;
import org.una.aeropuertocliente.DTOs.UsuarioDTO;
import org.una.aeropuertocliente.utility.JSONUtils;
/**
 *
 * @author Esteban Vargas
 */
public class HoraMarcajeWebService {
    
    private static final HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).build();
    private static final String serviceURL = "http://localhost:8099/horaMarcaje";
    

    public static void getHoraMarcajeById(long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findById/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Hora de Marcaje No Encontrada");

        else
        {
            HoraMarcajeDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), HoraMarcajeDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
    
    public static void getHoraMarcajeByFechaRegistroBetween(Date fechaInicial, Date fechaFinal, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByFechaRegistroBetween/"+fechaInicial+"/"+fechaFinal))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Hora de Marcaje No Encontrada");

        else
        {
             List<HoraMarcajeDTO> beans = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<HoraMarcajeDTO>>() {});
            beans.forEach(System.out::println);
        }
        response.join();
    }
    
    public static List<HoraMarcajeDTO> getHoraMarcajeByFechaRegistroBetweenAndUsuarioId(Date fechaInicial, Date fechaFinal, long id,String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        SimpleDateFormat DateFor = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate= DateFor.format(fechaInicial);
        String stringDate2= DateFor.format(fechaFinal);
        
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByFechaRegistroBetweenAndUsuarioId/"+stringDate+"/"+stringDate2+"/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        System.out.println("Codigo:" + response.get().statusCode() );
        if(response.get().statusCode() == 500)
            System.out.println("Hora de marcaje No Encontrada");

        else
        {
            List<HoraMarcajeDTO> beans = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<HoraMarcajeDTO>>() {});
            beans.forEach(System.out::println);
            return beans;
        }
        return null;
    }
    
    public static List<HoraMarcajeDTO> getHoraMarcajeByUsuarioId(long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        List<HoraMarcajeDTO> beans = null;
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByUsuarioId/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Hora de Marcaje No Encontrada");

        else
        {
            if (response.get().body().isBlank()) 
            {
                System.out.println("NO TIENE HORAS DE MARCAJE");
            }
            else
            {
                beans = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<HoraMarcajeDTO>>() {});
                beans.forEach(System.out::println);
            }
             
        }
        response.join();
        return beans;
    }
    
    public static HoraMarcajeDTO getUltimaHoraMarcajeByUsuarioId(long idUsuario, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HoraMarcajeDTO bean = new HoraMarcajeDTO();
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findUltimaHoraMarcajeByUsuarioId/"+idUsuario))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Hora de Marcaje No Encontrada");

        else
        {
            if (response.get().body().isBlank()) 
            {
                System.out.println("NO TIENE HORAS DE MARCAJE");
            }
            else
            {
                bean = JSONUtils.covertFromJsonToObject(response.get().body(), HoraMarcajeDTO.class);
                System.out.println(bean);
            } 
        }
        
        response.join();
        return bean;
    }
    
    public static void createHoraMarcaje(HoraMarcajeDTO bean, UsuarioDTO usuario, String finalToken) throws InterruptedException, ExecutionException, JsonProcessingException
    {     
        bean.setUsuario(usuario);
        String inputJson = JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken)
        .POST(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.get().body());

    }

    public static void updateHoraMarcaje(HoraMarcajeDTO bean, long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        String inputJson=JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken)
        .PUT(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());

        if(response.get().statusCode() == 500)
            System.out.println("No se pudo actualizar la Hora de Marcaje");

        else {
            bean = JSONUtils.covertFromJsonToObject(response.get().body(), HoraMarcajeDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

}