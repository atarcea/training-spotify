package com.mulesoft.connector.training.spotify.internal.error.provider;

import com.mulesoft.connector.training.spotify.internal.error.SpotifyErrorType;
import org.mule.runtime.extension.api.annotation.error.ErrorTypeProvider;
import org.mule.runtime.extension.api.error.ErrorTypeDefinition;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class SpotifyErrorTypeProvider implements ErrorTypeProvider {
    @Override
    public Set<ErrorTypeDefinition> getErrorTypes() {
        return Stream.of(SpotifyErrorType.values()).collect(toSet());
    }
}
