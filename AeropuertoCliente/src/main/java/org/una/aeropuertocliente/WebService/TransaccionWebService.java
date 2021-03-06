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
import org.una.aeropuertocliente.DTOs.TransaccionDTO;
import org.una.aeropuertocliente.DTOs.UsuarioDTO;
import org.una.aeropuertocliente.utility.JSONUtils;
/**
 *
 * @author Esteban Vargas
 */
public class TransaccionWebService {
    
    private static final HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).build();
    private static final String serviceURL = "http://localhost:8099/transacciones";

    public static TransaccionDTO getTransaccionById(long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findById/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Transaccion No Encontrada");

        else
        {
            TransaccionDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), TransaccionDTO.class);
            System.out.println(bean);
            return bean;
        }
        response.join();
        return null;
    }
    
     public static List<TransaccionDTO> getTransaccionByEstadoAndTipo(boolean estado,String tipo, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByEstadoAndTipo/"+estado+"/"+tipo))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Transaccion No Encontrada");

        else
        {
            List<TransaccionDTO> beans = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<TransaccionDTO>>() {});
            beans.forEach(System.out::println);
             return beans;
        }
        response.join();
         return null;
    }
     
    public static List<TransaccionDTO> getTransaccionByFechaRegistroBetweenAndTipo(Date fechaInicial, Date fechaFinal,String tipo, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        SimpleDateFormat DateFor = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate= DateFor.format(fechaInicial);
        String stringDate2= DateFor.format(fechaFinal);
        
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByFechaRegistroBetweenAndTipo/"+stringDate+"/"+stringDate2+"/"+tipo))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Transacción No Encontrada");

        else
        {
            List<TransaccionDTO> beans = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<TransaccionDTO>>() {});
            beans.forEach(System.out::println);
            return beans;
        }
        response.join();
        return null;
    }

    public static List<TransaccionDTO> getTransaccionByFechaRegistroBetweenAndTipoAndUsuarioUsuarioJefeId(Date fechaInicial, Date fechaFinal,String tipo,long idJefe, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        SimpleDateFormat DateFor = new SimpleDateFormat("yyyy-MM-dd");
        String stringDate= DateFor.format(fechaInicial);
        String stringDate2= DateFor.format(fechaFinal);
        
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByFechaRegistroBetweenAndTipoAndUsuarioUsuarioJefeId/"+stringDate+"/"+stringDate2+"/"+tipo+"/"+idJefe))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Transacción No Encontrada");

        else
        {
            List<TransaccionDTO> beans = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<TransaccionDTO>>() {});
            beans.forEach(System.out::println);
            return beans;
        }
        response.join();
        return null;
    }
    
    public static List<TransaccionDTO> getTransaccionByUsuarioAndTipo(long id,String tipo, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByUsuarioIdAndTipo/"+id+"/"+tipo))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Transaccion No Encontrada");

        else
        {
            List<TransaccionDTO> beans = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<TransaccionDTO>>() {});
            beans.forEach(System.out::println);
             return beans;
        }
        response.join();
         return null;
    }
     
    public static List<TransaccionDTO> getTransaccionByUsuarioAndTipoAndUsuarioUsuarioJefeId(long id,String tipo,long idJefe, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByUsuarioIdAndTipoAndUsuarioUsuarioJefeId/"+id+"/"+tipo+"/"+idJefe))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Transaccion No Encontrada");

        else
        {
            List<TransaccionDTO> beans = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<TransaccionDTO>>() {});
            beans.forEach(System.out::println);
             return beans;
        }
        response.join();
         return null;
    }
    
    public static void createTransaccion(String informacion,String tipo, UsuarioDTO usuarioId, String finalToken) throws InterruptedException, ExecutionException, JsonProcessingException
    {
        TransaccionDTO bean = new TransaccionDTO();
        
        bean.setInformacion(informacion);
        bean.setUsuario(usuarioId);
        bean.setTipo(tipo);

        String inputJson = JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken)
        .POST(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.get().body());

    }

    public static void updateTransaccion(TransaccionDTO bean, long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        String inputJson=JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken)
        .PUT(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());

        if(response.get().statusCode() == 500)
            System.out.println("No se pudo actualizar la Transaccion");

        else {
            bean = JSONUtils.covertFromJsonToObject(response.get().body(), TransaccionDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

}