package com.mulesoft.connector.training.spotify.internal.error;

import org.mule.runtime.extension.api.error.ErrorTypeDefinition;
import org.mule.runtime.extension.api.error.MuleErrors;

public enum SpotifyErrorType implements ErrorTypeDefinition<SpotifyErrorType> {

    INVALID_INPUT(MuleErrors.ANY),
    INVALID_CONNECTION(MuleErrors.CONNECTIVITY),
    TIMEOUT(MuleErrors.ANY);

    private ErrorTypeDefinition<? extends Enum<?>> parent;

    SpotifyErrorType(ErrorTypeDefinition<? extends Enum<?>> parent) {
        this.parent = parent;
    }
}