/**
 * (c) 2003-2021 MuleSoft, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1 a copy of which has been included with this distribution in the LICENSE.md file.
 */
package com.mulesoft.connector.training.spotify.metadata;

import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;

public class AlbumTracksOutputMetadataResolver implements OutputTypeResolver<Object> {

    @Override
    public MetadataType getOutputType(MetadataContext context, Object key) {
        return null;
    }

    @Override
    public String getCategoryName() {
        return "AlbumTracksOutputMetadataResolver";
    }
}
