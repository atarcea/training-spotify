package com.mulesoft.connector.training.spotify.internal.operation;

import com.mulesoft.connector.training.spotify.internal.connection.SpotifyConnection;
import com.mulesoft.connector.training.spotify.internal.error.SpotifyErrorType;
import com.mulesoft.connector.training.spotify.internal.error.provider.SpotifyErrorTypeProvider;
import com.mulesoft.connector.training.spotify.internal.util.Constants;
import org.apache.olingo.commons.api.format.ContentType;
import org.mule.runtime.api.exception.MuleRuntimeException;
import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.param.*;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Example;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.mule.runtime.http.api.HttpConstants;
import org.mule.runtime.http.api.HttpHeaders;
import org.mule.runtime.http.api.client.HttpRequestOptions;
import org.mule.runtime.http.api.domain.message.request.HttpRequest;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.mule.runtime.extension.api.annotation.param.display.Placement.ADVANCED_TAB;

public class GetAlbumTracksOperation {

    private static final String ALBUM_URL = Constants.SPOTIFY_BASE_URI + Constants.SLASH + "albums";
    private static final String TRACKS = "tracks";


    @MediaType(MediaType.APPLICATION_JSON)
    @Throws(SpotifyErrorTypeProvider.class)
    public InputStream getAlbumTracks(
            @Connection SpotifyConnection connection,
            @Example("4aawyAB9vmqN3uQ7FjRGTy") @DisplayName("Album ID") String albumId,
            @Placement(tab = Placement.ADVANCED_TAB) @Optional @NullSafe Map<String, String> advancedQueryParams,
            @ConfigOverride @Optional @Placement(tab = ADVANCED_TAB, order = 1)
            @Summary("Read timeout value.") Integer readTimeout,
            @ConfigOverride @Optional @Placement(tab = ADVANCED_TAB, order = 2)
            @Summary("The time unit value used by the read timeout.") TimeUnit readTimeoutTimeUnit
    ) {

        final HttpRequest httpRequest = connection.getAuthorizedRequestBuilder()
                .addHeader(HttpHeaders.Names.CONTENT_TYPE, ContentType.APPLICATION_JSON.toContentTypeString())
                .method(HttpConstants.Method.GET)
                .uri(ALBUM_URL + Constants.SLASH + albumId + Constants.SLASH + TRACKS)
                .queryParams(new MultiMap<>(advancedQueryParams))
                .build();

           final HttpRequestOptions httpRequestOptions = HttpRequestOptions.builder().responseTimeout(Math.toIntExact(readTimeoutTimeUnit.toMillis(readTimeout))).build();

        HttpResponse httpResponse = null;

        try {
            httpResponse = connection.getHttpClient().send(httpRequest, httpRequestOptions);
            connection.handleResponse(httpResponse);

        } catch (TimeoutException e) {
            throw new ModuleException("Request timeout exceeded.", SpotifyErrorType.TIMEOUT);
        } catch (IOException e) {
            throw new MuleRuntimeException(e);
        }

        // TODO: 20/09/2021 implement paginate operation
        return httpResponse.getEntity().getContent();

    }
}