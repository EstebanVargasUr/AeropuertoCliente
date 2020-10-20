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
import org.una.aeropuertocliente.DTOs.ServicioDTO;
import org.una.aeropuertocliente.utility.JSONUtils;
/**
 *
 * @author Esteban Vargas
 */
public class ServicioWebService {
    
    private static final HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).build();
    private static final String serviceURL = "http://localhost:8099/servicios";
    
    public static void getServicioById() throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/{id}")).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Servicio No Encontrado");

        else
        {
            ServicioDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), ServicioDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
    
     public static void getServicioByEstado() throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/{Estado}")).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Servicio No Encontrado");

        else
        {
            ServicioDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), ServicioDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
    
    public static void getServicioByEstadoCobro() throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/{estadoCobro}")).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Servicio No Encontrado");

        else
        {
            ServicioDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), ServicioDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
    
    public static void getServicioByFechaRegistroBetween() throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/{fecha}")).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Servicio No Encontrado");

        else
        {
            ServicioDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), ServicioDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

    public static void getServicioByAvionId() throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/avion/{term}")).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Servicio No Encontrado");

        else
        {
            ServicioDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), ServicioDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

    public static void getServicioByTipoServicioId() throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/tiposervicio/{term}")).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Servicio No Encontrado");

        else
        {
            ServicioDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), ServicioDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
    
    public static void createServicio(boolean estadocobro, String factura, String responsable, String observacion, Long avionId, Long tipoServicioId) throws InterruptedException, ExecutionException, JsonProcessingException
    {
        ServicioDTO bean = new ServicioDTO();
        
        bean.setEstadoCobro(estadocobro);
        bean.setFactura(factura);
        bean.setResponsable(responsable);
        bean.setObservacion(observacion);
        bean.setAvionId(avionId);
        bean.setTipoServicioid(tipoServicioId);

        String inputJson = JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.get().body());

    }

    public static void updateServicio(ServicioDTO bean) throws InterruptedException, ExecutionException, IOException
    {
        String inputJson=JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/{id}"))
                .header("Content-Type", "application/json")
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