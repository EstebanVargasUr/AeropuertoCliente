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
import java.sql.Time;
import java.util.Date;
import java.util.List;
import org.una.aeropuertocliente.DTOs.HorarioDTO;
import org.una.aeropuertocliente.DTOs.UsuarioDTO;
import org.una.aeropuertocliente.utility.JSONUtils;
/**
 *
 * @author Esteban Vargas
 */
public class HorarioWebService {
    
    UsuarioWebService usuarioWebService;
    
    private static final HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).build();
    private static final String serviceURL = "http://localhost:8099/horarios";
    

    public static void getHorarioById(long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findById/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Horario No Encontrado");

        else
        {
            HorarioDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), HorarioDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
    
    public static void getHorariosByEstado(boolean estado, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByEstado/"+estado))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Horario No Encontrado");

        else
        {
            List<HorarioDTO> beans = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<HorarioDTO>>() {});
            beans.forEach(System.out::println);
        }
        response.join();
    }
    
    public static void getHorarioByFechaRegistroBetween(Date fechaInicial, Date fechaFinal, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByFechaRegistroBetween/"+fechaInicial+"/"+fechaFinal))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Horario No Encontrado");

        else
        {
            List<HorarioDTO> beans = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<HorarioDTO>>() {});
            beans.forEach(System.out::println);
        }
        response.join();
    }
    
    public static List<HorarioDTO> getHorarioByUsuarioId(long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByUsuarioId/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<HorarioDTO> horarios = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<HorarioDTO>>() {});
        horarios.forEach(System.out::println);
        response.join();
        return horarios;
    }
    
    public static List<HorarioDTO> getHorarioByEstadoAndUsuario(boolean estado, long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/findByEstadoAndUsuarioId/"+estado+"/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<HorarioDTO> horarios = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<HorarioDTO>>() {});
        horarios.forEach(System.out::println);
        response.join();
        return horarios;
    }
    
    public static void createHorario(Short diaEntrada, Short diaSalida, Time horaEntrada, Time horaSalida, UsuarioDTO usuario, String finalToken) throws InterruptedException, ExecutionException, JsonProcessingException, IOException
    {
        HorarioDTO bean = new HorarioDTO();
        
        bean.setDiaEntrada(diaEntrada);
        bean.setDiaSalida(diaSalida);
        bean.setHoraEntrada(horaEntrada);
        bean.setHoraSalida(horaSalida);        
        bean.setUsuario(usuario);

        String inputJson = JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken)
        .POST(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.get().body());

    }

    public static void updateHorario(HorarioDTO bean, long id, String finalToken) throws InterruptedException, ExecutionException, IOException
    {
        String inputJson=JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"+id))
        .setHeader("Content-Type", "application/json").setHeader("AUTHORIZATION", "Bearer " + finalToken)
        .PUT(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());

        if(response.get().statusCode() == 500)
            System.out.println("No se pudo actualizar el Horario");

        else {
            bean = JSONUtils.covertFromJsonToObject(response.get().body(), HorarioDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

}