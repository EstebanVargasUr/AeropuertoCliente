package org.una.aeropuertocliente.WebService;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import org.una.aeropuertocliente.DTOs.AuthenticationRequest;
import org.una.aeropuertocliente.DTOs.AuthenticationResponse;
import org.una.aeropuertocliente.utility.JSONUtils;
import org.una.aeropuertocliente.utility.Mensaje;

/**
 *
 * @author Esteban Vargas
 */
public class AutenticationWebService {
    private static final HttpClient client = HttpClient.newBuilder().version(Version.HTTP_2).build();
    private static final String serviceURL = "http://localhost:8099/login";
    
    private static Mensaje msg = new Mensaje();
    
    public static AuthenticationResponse login(String cedula,String password, StackPane root) throws InterruptedException, ExecutionException, JsonProcessingException, IOException
    {
            AuthenticationRequest bean = new AuthenticationRequest();
            
            bean.setCedula(cedula);
            bean.setPassword(password);
            
            String inputJson = JSONUtils.covertFromObjectToJson(bean);
            HttpRequest request = HttpRequest.newBuilder(URI.create(serviceURL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(inputJson)).build();
            CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,HttpResponse.BodyHandlers.ofString());            
            System.out.println(response.get().body());
            
            if(response.get().statusCode() == 500)
                CargaGraficaMensaje( "Usuario no encontrado", root);
            if (response.get().statusCode() == 401) {
               CargaGraficaMensaje( "Contraseña incorrecta", root);
            }
            else
            {
                AuthenticationResponse authenticationResponse = JSONUtils.covertFromJsonToObject(response.get().body(), AuthenticationResponse.class);
                return authenticationResponse;
            }
            return null;
    }
    
        private static void CargaGraficaMensaje(String cuerpo, StackPane root){
        Platform.runLater(new Runnable() {
        @Override public void run() {
            
            msg.alerta(root, "Información", cuerpo);
            
        }
        });
    }
}
