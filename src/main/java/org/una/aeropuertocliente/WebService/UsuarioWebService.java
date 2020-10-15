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
import org.una.aeropuertocliente.DTOs.UsuarioDTO;
import org.una.aeropuertocliente.utility.JSONUtils;
/**
 *
 * @author Esteban Vargas
 */
public class UsuarioWebService {
    
    private static final HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).build();
    private static final String serviceURL = "http://localhost:8099/usuarios";
    
    //sending request to retrieve all the products available.
    public static void getAllUsuarios() throws InterruptedException, ExecutionException, JsonParseException, JsonMappingException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL)).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));
        List<UsuarioDTO> usuarios = JSONUtils.convertFromJsonToList(response.get().body(), new TypeReference<List<UsuarioDTO>>() {});
        usuarios.forEach(System.out::println);
        response.join();
    }

    //sending request retrieve the product based on the productId
    public static void getUsuarioById() throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/{id}")).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Usuario No Encontrado");

        else
        {
            UsuarioDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), UsuarioDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
    
    public static void getUsuarioByCedulaAproximate() throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/cedula/{termino}")).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Usuario No Encontrado");

        else
        {
            UsuarioDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), UsuarioDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

    public static void getUsuarioByNombreCompletoAproximateIgnoreCase() throws InterruptedException, ExecutionException, IOException
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL+"/nombre/{termino}")).GET().build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(req, BodyHandlers.ofString());
        response.thenAccept(res -> System.out.println(res));

        if(response.get().statusCode() == 500)
            System.out.println("Usuario No Encontrado");

        else
        {
            UsuarioDTO bean = JSONUtils.covertFromJsonToObject(response.get().body(), UsuarioDTO.class);
            System.out.println(bean);
        }
        response.join();
    }
    
    //send request to add the product details.
    public static void createUsuario(String cedula, String nombreCompleto, String passwordEnciptado, Long rolId) throws InterruptedException, ExecutionException, JsonProcessingException
    {
        UsuarioDTO bean = new UsuarioDTO();
        
        bean.setCedula(cedula);
        bean.setNombreCompleto(nombreCompleto);
        bean.setPasswordEncriptado(passwordEnciptado);
        bean.setRolId(rolId);

        String inputJson = JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());
        System.out.println(response.get().body());

    }

    //send request to update the product details.
    public static void updateUsuario(UsuarioDTO bean) throws InterruptedException, ExecutionException, IOException
    {
        String inputJson=JSONUtils.covertFromObjectToJson(bean);
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL+"/{id}"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(inputJson)).build();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());

        if(response.get().statusCode() == 500)
            System.out.println("No se pudo actualizar el Usuario");

        else {
            bean = JSONUtils.covertFromJsonToObject(response.get().body(), UsuarioDTO.class);
            System.out.println(bean);
        }
        response.join();
    }

}