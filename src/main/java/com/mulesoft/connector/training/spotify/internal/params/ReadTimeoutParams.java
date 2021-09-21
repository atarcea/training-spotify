/**
 * (c) 2003-2021 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */
package com.mulesoft.connector.training.spotify.internal.params;

import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.param.display.Summary;

import java.util.concurrent.TimeUnit;

public class ReadTimeoutParams {
    /**
     * Specifies the amount of time, in milliseconds, that the consumer will wait for a response before it times out.
     */
    @Parameter
    @Optional(defaultValue = "60")
    @Placement(tab = Placement.ADVANCED_TAB, order = 1)
    private Integer readTimeout;

    @Parameter
    @Optional(defaultValue = "SECONDS")
    @DisplayName("Read Timeout Time unit")
    @Summary("The time unit value used by the read timeout.")
    @Placement(tab = Placement.ADVANCED_TAB, order = 2)
    private TimeUnit readTimeoutTimeUnit;

    public Integer getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }

    public TimeUnit getReadTimeoutTimeUnit() {
        return readTimeoutTimeUnit;
    }

    public void setReadTimeoutTimeUnit(TimeUnit readTimeoutTimeUnit) {
        this.readTimeoutTimeUnit = readTimeoutTimeUnit;
    }
}