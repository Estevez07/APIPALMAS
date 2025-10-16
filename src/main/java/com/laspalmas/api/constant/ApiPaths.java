package com.laspalmas.api.constant;

public class ApiPaths {

      private ApiPaths() {} 

    // Base Auth
    public static final String AUTH = "/auth";
    public static final String AUTH_VERIFY = AUTH + "/verify";

    // AuthController
    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";
    public static final String FORGOT_PASSWORD ="/forgot_password";

    // VerificationController
    public static final String VERIFY_REGISTER = "/register";
    public static final String VERIFY_FORGOT_PASSWORD = "/forgot_password";

    // UsuarioController
    public static final String USUARIOS = "/usuarios";
    public static final String USUARIOS_CON_PEDIDOS = "/con-pedidos";

    // PedidoController
    public static final String PEDIDOS = "/pedidos";
    public static final String PEDIDOS_ID = "/{id}";
    public static final String PEDIDOS_CREDENCIAL = "/{credencial}";

    // MensajeController
    public static final String MENSAJES = "/mensajes";
    public static final String MENSAJES_ENVIAR = "/enviar";
    public static final String MENSAJES_ENTRE =  "/entre";
    public static final String MENSAJES_ID = "/{id}";

    // OAuth2Controller
    public static final String LOGIN_FAILED = "/login/failed";
}
