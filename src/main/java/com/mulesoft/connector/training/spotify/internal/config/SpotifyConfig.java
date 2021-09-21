package com.mulesoft.connector.training.spotify.internal.config;


import com.mulesoft.connector.training.spotify.internal.connection.providers.ClientCredentialsConnectionProvider;
import com.mulesoft.connector.training.spotify.internal.operation.GetAlbumTracksOperation;
import com.mulesoft.connector.training.spotify.internal.params.ReadTimeoutParams;
import org.mule.runtime.extension.api.annotation.Configuration;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;

@ConnectionProviders({ClientCredentialsConnectionProvider.class})
@Operations({GetAlbumTracksOperation.class})
@DisplayName("Spotify Config")
@Configuration(name = "spotify-config")
public class SpotifyConfig {

    @ParameterGroup(name = "Read Timeout ")
    @Placement(order = 1)
    private ReadTimeoutParams readTimeoutParams;
}
