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
import org.una.aeropuertocliente.DTOs.AvionDTO;
import org.una.aeropuertocliente.DTOs.ServicioDTO;
import org.una.aeropuertocliente.DTOs.TipoServicioDTO;
import org.una.aeropuertocliente.utility.JSONUtils;
/**
 *
 * @author Esteban Vargas
 */
public class ServicioWebService {
    
    private static final HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).build();
    private static final String serviceURL = "http://localhost:8099/servicios";
    
    public static void getServicioById(long id) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findById/"+id)).GET().build();
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
    
     public static void getServicioByEstado(boolean estado) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByEstado/"+estado)).GET().build();
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
    
    public static void getServicioByEstadoCobro(boolean estado) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByEstadoCobro/"+estado)).GET().build();
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

    public static void getServicioByAvionId(long id) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByAvionId/"+id)).GET().build();
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

    public static void getServicioByTipoServicioId(long id) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByTipoServicioId/"+id)).GET().build();
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

    public static void getServicioByTipoServicioIdAndAvionId(long idTipo, long idAvion) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByTipoServicioIdAndAvionId/"+idTipo+"/"+idAvion)).GET().build();
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
    
    public static void createServicio(boolean estadocobro, String factura, String responsable, String observacion, AvionDTO avionId, TipoServicioDTO tipoServicioId) throws InterruptedException, ExecutionException, JsonProcessingException
    {
        ServicioDTO bean = new ServicioDTO();
        
        bean.setEstadoCobro(estadocobro);
        bean.setFactura(factura);
        bean.setResponsable(responsable);
        bean.setObservacion(observacion);
        bean.setAvion(avionId);
        bean.setTipoServicio(tipoServicioId);

        String inputJson = JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.get().body());

    }

    public static void updateServicio(ServicioDTO bean, long id) throws InterruptedException, ExecutionException, IOException
    {
        String inputJson=JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"+id))
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