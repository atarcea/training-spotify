package com.mulesoft.connector.training.spotify.internal.extension;

import com.mulesoft.connector.training.spotify.internal.config.SpotifyConfig;
import com.mulesoft.connector.training.spotify.internal.error.SpotifyErrorType;
import org.mule.runtime.api.meta.Category;
import org.mule.runtime.extension.api.annotation.Configurations;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;
import org.mule.runtime.extension.api.annotation.error.ErrorTypes;

@Configurations(SpotifyConfig.class)
@Xml(prefix = "spotify")
@Extension(name = "Spotify", category = Category.SELECT)
@ErrorTypes(SpotifyErrorType.class)
public class SpotifyConnector {
}
