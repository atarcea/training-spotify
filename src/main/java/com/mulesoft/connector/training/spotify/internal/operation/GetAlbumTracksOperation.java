package com.mulesoft.connector.training.spotify.internal.operation;

import com.mulesoft.connector.training.spotify.internal.connection.SpotifyConnection;
import com.mulesoft.connector.training.spotify.internal.error.provider.SpotifyErrorTypeProvider;
import com.mulesoft.connector.training.spotify.metadata.AlbumTracksOutputMetadataResolver;
import com.mulesoft.connector.training.spotify.paging.GetAlbumTracksPagingProvider;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.metadata.OutputResolver;
import org.mule.runtime.extension.api.annotation.param.ConfigOverride;
import org.mule.runtime.extension.api.annotation.param.NullSafe;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Example;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.extension.api.runtime.streaming.PagingProvider;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.mule.runtime.extension.api.annotation.param.display.Placement.ADVANCED_TAB;

public class GetAlbumTracksOperation {


    @OutputResolver(output = AlbumTracksOutputMetadataResolver.class)
    @Throws(SpotifyErrorTypeProvider.class)
    public PagingProvider<SpotifyConnection, Map<String, Object>> getAlbumTracks(
            @Example("4aawyAB9vmqN3uQ7FjRGTy") @DisplayName("Album ID") String albumId,
            @Placement(tab = Placement.ADVANCED_TAB) @Optional @NullSafe Map<String, String> advancedQueryParams,
            @ConfigOverride @Optional @Placement(tab = ADVANCED_TAB, order = 1)
            @Summary("Read timeout value.") Integer readTimeout,
            @ConfigOverride @Optional @Placement(tab = ADVANCED_TAB, order = 2)
            @Summary("The time unit value used by the read timeout.") TimeUnit readTimeoutTimeUnit
    ) {

        return new GetAlbumTracksPagingProvider(advancedQueryParams, albumId, readTimeout, readTimeoutTimeUnit);

    }
}