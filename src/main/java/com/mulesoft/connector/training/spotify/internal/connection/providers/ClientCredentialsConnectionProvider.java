package com.mulesoft.connector.training.spotify.internal.connection.providers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mulesoft.connector.training.spotify.api.ProxyConfiguration;
import com.mulesoft.connector.training.spotify.internal.connection.SpotifyConnection;
import com.mulesoft.connector.training.spotify.internal.util.Constants;
import org.apache.olingo.commons.api.format.ContentType;
import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.lifecycle.Disposable;
import org.mule.runtime.api.lifecycle.Initialisable;
import org.mule.runtime.api.tls.TlsContextFactory;
import org.mule.runtime.core.api.util.Base64;
import org.mule.runtime.core.api.util.IOUtils;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.RefName;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Example;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.http.api.HttpConstants;
import org.mule.runtime.http.api.HttpHeaders;
import org.mule.runtime.http.api.HttpService;
import org.mule.runtime.http.api.client.HttpClient;
import org.mule.runtime.http.api.client.HttpClientConfiguration;
import org.mule.runtime.http.api.domain.entity.ByteArrayHttpEntity;
import org.mule.runtime.http.api.domain.entity.HttpEntity;
import org.mule.runtime.http.api.domain.message.request.HttpRequest;
import org.mule.runtime.http.api.domain.message.request.HttpRequestBuilder;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.extension.api.annotation.param.display.Placement.ADVANCED_TAB;
import static org.mule.runtime.extension.api.annotation.param.display.Placement.DEFAULT_TAB;

@Alias("oauth-client-credentials")
@DisplayName("OAuth v2.0 Client Credentials")
public class ClientCredentialsConnectionProvider implements CachedConnectionProvider<SpotifyConnection>, Initialisable, Disposable {

    @Parameter
    @Placement(tab = DEFAULT_TAB, order = 2)
    @DisplayName("Client ID")
    @Summary("The client ID used to create the connection.")
    @Example("862s66a319f84f0Aa0670ce3e68bvs4e")
    private String clientId;

    @Parameter
    @DisplayName("Client Secret")
    @Summary("The client secret used to create the connection.")
    @Example("862s66a319f84f0Aa0670ce3e68bvs4e")
    @Placement(tab = DEFAULT_TAB, order = 1)
    private String clientSecret;

    @Parameter
    @DisplayName("Token URL")
    @Summary("Token URL used to create the connection.")
    @Optional(defaultValue = "https://accounts.spotify.com/api/token")
    @Placement(tab = DEFAULT_TAB, order = 3)
    private String accessTokenUrl;

    /**
     * Proxy configuration for the connector.
     */
    @Parameter
    @Placement(tab = ADVANCED_TAB)
    @DisplayName("Proxy configuration")
    @Summary("Configuration for executing requests through a proxy.")
    @Optional
    @Expression(NOT_SUPPORTED)
    protected ProxyConfiguration proxyConfiguration;

    /**
     * Protocol to use for communication. Valid values are HTTP and HTTPS.
     * Default value is HTTP. When using HTTPS the HTTP communication is going
     * to be secured using TLS / SSL. If HTTPS was configured as protocol then
     * the user needs to configure at least the keystore in the tls:context
     * child element of this listener-config.
     */
    @Parameter
    @Placement(tab = "Security", order = 1)
    @DisplayName("TLS configuration")
    @Summary("If the HTTPS was configured as protocol, then the user needs to configure at least the keystore configuration")
    @Optional
    @Expression(NOT_SUPPORTED)
    protected TlsContextFactory tlsContextFactory;

    private HttpClient httpClient;

    @Inject
    private HttpService httpService;

    @RefName
    protected String configName;

    private static final String ACCESS_TOKEN = "access_token";

    @Override
    public SpotifyConnection connect() throws ConnectionException {

        String accessToken = null;

        try {
            final HttpEntity httpEntity = new ByteArrayHttpEntity(Constants.GRANT_TYPE.getBytes());

            final HttpRequestBuilder httpRequestBuilder = HttpRequest.builder()
                    .method(HttpConstants.Method.POST)
                    .addHeader(HttpHeaders.Names.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.toContentTypeString())
                    .addHeader(Constants.AUTHORIZATION, getEncodedClientCredentials())
                    .uri(accessTokenUrl)
                    .entity(httpEntity);

            final HttpResponse httpResponse = httpClient.send(httpRequestBuilder.build());

            final Map<String, Object> responseBody = getResponseBody(httpResponse);

            accessToken = (String) responseBody.get(ACCESS_TOKEN);

        } catch (TimeoutException | IOException ex) {
            throw new ConnectionException(ex);
        }
        return new SpotifyConnection(accessToken, httpClient);
    }

    @Override
    public void disconnect(SpotifyConnection spotifyConnection) {
    }

    @Override
    public ConnectionValidationResult validate(SpotifyConnection spotifyConnection) {
        try {
            spotifyConnection.validate();
            return ConnectionValidationResult.success();
        } catch (Exception e) {
            return ConnectionValidationResult.failure(e.getMessage(), e);
        }
    }

    @Override
    public void dispose() {
        if (httpClient != null) {
            httpClient.stop();
        }
    }

    @Override
    public void initialise()  {
        HttpClientConfiguration.Builder httpClientConfiguration = new HttpClientConfiguration.Builder()
                .setName(configName);
//        todo :proxy + TLS
        httpClient = httpService.getClientFactory().create(httpClientConfiguration.build());
        httpClient.start();
    }

    private String getEncodedClientCredentials() throws TimeoutException, IOException {
        final String clientCredentials = clientId + Constants.COLON + clientSecret;

        return  Constants.BASIC_AUTHORIZATION + Constants.WHITE_SPACE +
                Base64.encodeBytes(clientCredentials.getBytes(), Base64.DONT_BREAK_LINES);
    }

    private Map<String, Object> getResponseBody(HttpResponse httpResponse ) {
        final Type resultType = new TypeToken<Map<String, Object>>() {}.getType();
        final Gson gson = new Gson();
        return  gson.fromJson(IOUtils.toString(httpResponse.getEntity().getContent()), resultType);
    }
}