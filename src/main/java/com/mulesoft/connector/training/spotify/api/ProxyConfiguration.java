/**
 * (c) 2003-2021 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */
package com.mulesoft.connector.training.spotify.api;

import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Password;

import java.util.Objects;

@DisplayName("Proxy")
@Xml(prefix = "proxy")
public class ProxyConfiguration {

    /**
     * Host where the proxy requests will be sent.
     */
    @Parameter
    @DisplayName("Proxy host")
    private String host;

    /**
     * Port where the proxy requests will be sent.
     */
    @Parameter
    @DisplayName("Proxy port")
    private int port;

    /**
     * The username to authenticate against the proxy.
     */
    @Parameter
    @DisplayName("Proxy username")
    @Optional
    private String username;

    /**
     * The password to authenticate against the proxy.
     */
    @Parameter
    @Password
    @DisplayName("Proxy password")
    @Optional
    private String password;

    public ProxyConfiguration() {

    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProxyConfiguration that = (ProxyConfiguration) o;
        return port == that.port &&
                Objects.equals(host, that.host) &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port, username, password);
    }
}
