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
import org.una.aeropuertocliente.DTOs.TipoServicioDTO;
import org.una.aeropuertocliente.utility.JSONUtils;
/**
 *
 * @author Esteban Vargas
 */
public class TipoServicioWebService {
    
    private static final HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).build();
    private static final String serviceURL = "http://localhost:8099/tiposServicios";
    
    public static void getAllTiposServicios(String finalToken) throws InterruptedException, ExecutionException, JsonParseException, JsonMappingException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findAll"))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<TipoServicioDTO> tiposServicios = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<TipoServicioDTO>>() {});
        tiposServicios.forEach(System.out::println);
        response.join();
    }

    public static TipoServicioDTO getTipoServicioById(long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        TipoServicioDTO bean = null;
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findById/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Tipo de Servicio No Encontrado");

        else
        {
            bean = JSONUtils.covertFromJsonToObject(response.get().body(), TipoServicioDTO.class);
            System.out.println(bean);
        }
        response.join();
        return bean;
    }
    
    public static TipoServicioDTO getTipoServicioByNombre(String nombre, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        TipoServicioDTO bean = null;
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByNombre/"+nombre))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Tipo de Servicio No Encontrado");

        else
        {
            bean = JSONUtils.covertFromJsonToObject(response.get().body(), TipoServicioDTO.class);
            System.out.println(bean);
        }
        response.join();
        return bean;
    }
 
    public static void createTipoServicio(String nombre, Long duracion, String finalToken) throws InterruptedException, ExecutionException, JsonProcessingException
    {
        TipoServicioDTO bean = new TipoServicioDTO();
        
        bean.setNombre(nombre);
        bean.setDuracion(duracion);

        String inputJson = JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken)
        .POST(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.get().body());

    }

    public static void updateTipoServicio(TipoServicioDTO bean, long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        String inputJson=JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken)
        .PUT(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());

        if(response.get().statusCode() == 500)
            System.out.println("No se pudo actualizar el Tipo de Servicio");

        else {
            bean = JSONUtils.covertFromJsonToObject(response.get().body(), TipoServicioDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

}