package org.una.aeropuertocliente.WebService;

import com.fasterxml.jackson.core.JsonParseException;
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
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.Date;
import java.util.List;
import org.una.aeropuertocliente.DTOs.AvionDTO;
import org.una.aeropuertocliente.DTOs.ParametroSistemaDTO;
import org.una.aeropuertocliente.utility.JSONUtils;
/**
 *
 * @author Esteban Vargas
 */
public class ParametroSistemaWebService {
    
    private static final HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).build();
    private static final String serviceURL = "http://localhost:8099/parametroSistema";
    
    public static void getAllParametrosSistema(String finalToken) throws InterruptedException, ExecutionException, JsonParseException, JsonMappingException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findAll"))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<AvionDTO> parametrosSistema = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<AvionDTO>>() {});
        parametrosSistema.forEach(System.out::println);
        response.join();
    }
     
    public static void getParametroSistemaById(long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findById/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Parametros de Sistema No Encontrado");

        else
        {
            ParametroSistemaDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), ParametroSistemaDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
    
     public static void getParametroSistemaByNombre(String nombre, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByNombre/"+nombre))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Parametros de Sistema No Encontrado");

        else
        {
            List<ParametroSistemaDTO> beans = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<ParametroSistemaDTO>>() {});
            beans.forEach(System.out::println);
        }
        response.join();
    }
     
    public static void getParametroSistemaByEstado(boolean estado, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByEstado/"+estado))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Parametros de Sistema No Encontrado");

        else
        {
            List<ParametroSistemaDTO> beans = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<ParametroSistemaDTO>>() {});
            beans.forEach(System.out::println);
        }
        response.join();
    }
    
    public static void getParametroSistemaByFechaRegistroBetween(Date fechaInicial, Date fechaFinal, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByFechaRegistroBetween/"+fechaInicial+"/"+fechaFinal))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Parametros de Sistema No Encontrado");

        else
        {
            List<ParametroSistemaDTO> beans = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<ParametroSistemaDTO>>() {});
            beans.forEach(System.out::println);
        }
        response.join();
    }

    public static void createParametroSistema(String nombre, String valor, String descripcion, String finalToken) throws InterruptedException, ExecutionException, JsonProcessingException
    {
        ParametroSistemaDTO bean = new ParametroSistemaDTO();
        
        bean.setNombre(nombre);
        bean.setValor(valor);
        bean.setDescripcion(descripcion);

        String inputJson = JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken)
        .POST(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.get().body());

    }

    public static void updateParametroSistema(ParametroSistemaDTO bean, long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        String inputJson=JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken)
        .PUT(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());

        if(response.get().statusCode() == 500)
            System.out.println("No se pudo actualizar el Parametro de Sistema");

        else {
            bean = JSONUtils.covertFromJsonToObject(response.get().body(), ParametroSistemaDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

}