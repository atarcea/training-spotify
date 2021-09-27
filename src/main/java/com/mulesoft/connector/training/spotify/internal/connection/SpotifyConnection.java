package com.mulesoft.connector.training.spotify.internal.connection;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.mulesoft.connector.training.spotify.internal.error.SpotifyErrorType;
import com.mulesoft.connector.training.spotify.internal.util.Constants;
import org.apache.olingo.commons.api.format.ContentType;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.exception.MuleRuntimeException;
import org.mule.runtime.core.api.util.IOUtils;
import org.mule.runtime.core.api.util.StringUtils;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.mule.runtime.http.api.HttpConstants;
import org.mule.runtime.http.api.HttpHeaders;
import org.mule.runtime.http.api.client.HttpClient;
import org.mule.runtime.http.api.domain.message.request.HttpRequest;
import org.mule.runtime.http.api.domain.message.request.HttpRequestBuilder;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;
import org.mule.runtime.soap.api.exception.BadRequestException;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.stream.Collectors;

import static org.mule.runtime.api.i18n.I18nMessageFactory.createStaticMessage;


public class SpotifyConnection {

    private static final String TEST_TOKEN_URL = Constants.SPOTIFY_BASE_URI + "/browse/new-releases";
    private static final String BEARER = "Bearer";
    private static final String TOKEN_EXPIRED = "Token expired";
    private String token;
    private HttpClient httpClient;
    private final Gson gson = new Gson();

    public SpotifyConnection(String token, HttpClient httpClient) {
        this.token = token;
        this.httpClient = httpClient;
    }

    public String getToken() {
        return token;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }


    public void validate() throws Exception {

        if (getHttpClient() == null || StringUtils.isEmpty(getToken())) {
            throw new ModuleException(Constants.NULL_AUTHENTICATION_CONTEXT, SpotifyErrorType.INVALID_CONNECTION);
        }

        final HttpRequest httpRequest = this.getAuthorizedRequestBuilder()
                .method(HttpConstants.Method.GET)
                .uri(TEST_TOKEN_URL)
                .addHeader(HttpHeaders.Names.CONTENT_TYPE, ContentType.APPLICATION_JSON.toContentTypeString())
                .build();

        final HttpResponse httpResponse = httpClient.send(httpRequest);

        if (isTokenExpired(httpResponse)) {
            throw new ModuleException(TOKEN_EXPIRED, SpotifyErrorType.INVALID_CONNECTION);
        }
    }

    public HttpResponse handleResponse(HttpResponse response) {

       final int statusCode = response.getStatusCode();

        if (response.getStatusCode() >= 200 && statusCode < 300) {
            return response;
        }

        final String errorMessage = getErrorMessage(response);

        switch (statusCode) {
            case 400:
                throw new ModuleException(errorMessage, SpotifyErrorType.BAD_REQUEST, new BadRequestException(errorMessage));
            case 401:
                throw new ModuleException(errorMessage, SpotifyErrorType.INVALID_CONNECTION, new ConnectionException(TOKEN_EXPIRED));
            default:
                throw new MuleRuntimeException(createStaticMessage(errorMessage, "GENERIC ERROR - Status Code: " + statusCode));

        }
    }

    private boolean isTokenExpired(HttpResponse httpResponse) {

        if (isTokenExpiredStatus(httpResponse)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private boolean isTokenExpiredStatus(HttpResponse httpResponse) {

       final int statusCode = httpResponse.getStatusCode();

        if (statusCode == HttpConstants.HttpStatus.UNAUTHORIZED.getStatusCode()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private String getErrorMessage(HttpResponse httpResponse) {

        final Type resultType = new TypeToken<Map<String, Object>>() {
        }.getType();

        final Map<String, Object> httpResponseBody = gson.fromJson(IOUtils.toString(httpResponse.getEntity().getContent()), resultType);

        if (httpResponseBody.get(Constants.ERROR) != null) {

            final Map<String, LinkedTreeMap<String, String>> errorResponse = httpResponseBody.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> (LinkedTreeMap<String, String>) entry.getValue()));

            final LinkedTreeMap<String, String> errorObject = errorResponse.get(Constants.ERROR);

            if (errorObject != null && errorObject.containsKey(Constants.MESSAGE)) {
                return errorObject.get(Constants.MESSAGE);
            }
        }
        return null;
    }

    public HttpRequestBuilder getAuthorizedRequestBuilder() {

        return HttpRequest.builder()
                .addHeader(HttpHeaders.Names.AUTHORIZATION, BEARER +Constants.WHITE_SPACE + token);
    }
}