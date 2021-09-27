package com.mulesoft.connector.training.spotify.metadata;

import com.mulesoft.connector.training.spotify.metadata.type.Tracks;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataResolvingException;
import org.mule.runtime.api.metadata.resolving.AttributesTypeResolver;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;

public class AlbumTracksOutputMetadataResolver implements OutputTypeResolver<Object>, AttributesTypeResolver<Object> {

    @Override
    public String getResolverName() {
        return OutputTypeResolver.super.getResolverName();
    }

    @Override
    public MetadataType getOutputType(MetadataContext context, Object key) {
        return context.getTypeLoader().load(Tracks.class);
    }

    @Override
    public String getCategoryName() {
        return "AlbumTracksOutputMetadataResolver";
    }

    @Override
    public MetadataType getAttributesType(MetadataContext context, Object key) throws MetadataResolvingException, ConnectionException {
         return context.getTypeLoader().load(Tracks.class);
    }
}
