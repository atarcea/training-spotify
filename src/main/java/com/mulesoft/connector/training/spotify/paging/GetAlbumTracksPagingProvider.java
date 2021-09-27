/**
 * (c) 2003-2021 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */
package com.mulesoft.connector.training.spotify.paging;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mulesoft.connector.training.spotify.internal.connection.SpotifyConnection;
import com.mulesoft.connector.training.spotify.internal.error.SpotifyErrorType;
import com.mulesoft.connector.training.spotify.internal.util.Constants;
import org.apache.olingo.commons.api.format.ContentType;
import org.mule.runtime.api.exception.MuleRuntimeException;
import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.core.api.util.IOUtils;
import org.mule.runtime.extension.api.error.MuleErrors;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.mule.runtime.extension.api.runtime.streaming.PagingProvider;
import org.mule.runtime.http.api.HttpConstants;
import org.mule.runtime.http.api.HttpHeaders;
import org.mule.runtime.http.api.client.HttpRequestOptions;
import org.mule.runtime.http.api.domain.message.request.HttpRequest;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class GetAlbumTracksPagingProvider implements PagingProvider<SpotifyConnection, Map<String, Object>> {
    private static final Logger logger = LoggerFactory.getLogger(GetAlbumTracksPagingProvider.class);
    private static final String ITEMS = "items";
    private static final String NEXT = "next";
    private static final String OFFSET = "offset";
    private static final String TOTAL = "total";
    private static final String HREF = "href";

    private Map<String, String> advancedQueryParams;
    private String albumId;
    private Integer readTimeout;
    private TimeUnit readTimeoutTimeUnit;
    private boolean firstPage = true;
    private String nextOffset;
    private String defaultOffset;
    private final Gson gson = new Gson();


    public GetAlbumTracksPagingProvider(Map<String, String> advancedQueryParams, String albumId, Integer readTimeout, TimeUnit readTimeoutTimeUnit) {
        this.advancedQueryParams = advancedQueryParams;
        this.albumId = albumId;
        this.readTimeout = readTimeout;
        this.readTimeoutTimeUnit = readTimeoutTimeUnit;
    }

    @Override
    public List<Map<String, Object>> getPage(SpotifyConnection connection) {

        if (!firstPage && nextOffset == null) {
            return new ArrayList<>();
        } else {
            firstPage = false;
        }

        final Map<String, Object> result = getAlbumTracksResponse(connection);
        defaultOffset = getOffsetAttributeFromUrl(result.get(HREF).toString());

        nextOffset = null;

        if (result.containsKey(NEXT) && result.get(NEXT) != null) {
            nextOffset = getOffsetAttributeFromUrl(result.get(NEXT).toString());
            if (nextOffset != null) {
                advancedQueryParams.put(OFFSET, nextOffset);
            }
        }
        try {
            return (List<Map<String, Object>>) result.get(ITEMS);
        } catch (ClassCastException e) {
            throw new ModuleException("Could not parse the result", MuleErrors.TRANSFORMATION, e);
        }

    }

    @Override
    public java.util.Optional<Integer> getTotalResults(SpotifyConnection connection) {

        final Map<String, Object> httpResponse = getAlbumTracksResponse(connection);

        if (httpResponse.get(TOTAL) != null) {

            try {
                int totalElements = (int) Math.round((Double) httpResponse.get(TOTAL));
                int skypedElements = Integer.parseInt(defaultOffset);

                return Optional.of(totalElements - skypedElements);
            } catch (ClassCastException e) {
                throw new ModuleException("Could not parse the result", MuleErrors.TRANSFORMATION, e);
            }
        }

        return Optional.empty();
    }

    @Override
    public void close(SpotifyConnection connection) {
    }

    private String getOffsetAttributeFromUrl(String url) {
        try {
            final String queryParams = URI.create(url).getQuery();

            for (String param : queryParams.split(Constants.AND_SIGH)) {
                if (param.contains(OFFSET)) {
                    return param.split(Constants.EQUAL_SIGH)[1];
                }
            }
        } catch (Exception e) {
            logger.warn("Could not extract next page token");
        }
        return null;
    }

    private Map<String, Object> getAlbumTracksResponse(SpotifyConnection connection) {
        final HttpRequest httpRequest = connection.getAuthorizedRequestBuilder()
                .addHeader(HttpHeaders.Names.CONTENT_TYPE, ContentType.APPLICATION_JSON.toContentTypeString())
                .method(HttpConstants.Method.GET)
                .uri(Constants.ALBUM_URL + Constants.SLASH + albumId + Constants.SLASH + Constants.TRACKS)
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
        final Type resultType = new TypeToken<Map<String, Object>>() {
        }.getType();

        return gson.fromJson(IOUtils.toString(httpResponse.getEntity().getContent()), resultType);
    }
}
