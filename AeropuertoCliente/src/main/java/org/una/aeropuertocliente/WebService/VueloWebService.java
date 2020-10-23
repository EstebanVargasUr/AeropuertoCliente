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
import org.una.aeropuertocliente.DTOs.VueloDTO;
import org.una.aeropuertocliente.utility.JSONUtils;
/**
 *
 * @author Esteban Vargas
 */
public class VueloWebService {
    
    private static final HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).build();
    private static final String serviceURL = "http://localhost:8099/vuelos";

    public static void getVueloById(long id) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findById/"+id)).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Vuelo No Encontrado");

        else
        {
            VueloDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), VueloDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
    
    public static void getVueloByAeropuerto(String nombre) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByAeropuerto/"+nombre)).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Vuelo No Encontrado");

        else
        {
            VueloDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), VueloDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
    
    public static void getVueloByEstado(boolean estado) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByEstado/"+estado)).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Vuelo No Encontrado");

        else
        {
            VueloDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), VueloDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

    public static void getVueloByFechaSalida() throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/FechaSalida/{term}")).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Vuelo No Encontrado");

        else
        {
            VueloDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), VueloDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
    
    public static void getVueloByFechaLlegada() throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/FechaLlegada/{term}")).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Vuelo No Encontrado");

        else
        {
            VueloDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), VueloDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

    public static void createVuelo(Float duracion, String aeropuerto, Long distancia, Long avionId) throws InterruptedException, ExecutionException, JsonProcessingException
    {
        VueloDTO bean = new VueloDTO();
        
        bean.setDuracion(duracion);
        bean.setAeropuerto(aeropuerto);
        bean.setDistancia(distancia);
        bean.setAvionId(avionId);

        String inputJson = JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.get().body());

    }

    public static void updateVuelo(VueloDTO bean, long id) throws InterruptedException, ExecutionException, IOException
    {
        String inputJson=JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"+id))
                .header("Content-Type", "application/json")
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