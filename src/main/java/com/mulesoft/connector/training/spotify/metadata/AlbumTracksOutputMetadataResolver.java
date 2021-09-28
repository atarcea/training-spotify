package com.mulesoft.connector.training.spotify.metadata;

import org.mule.metadata.api.builder.ObjectTypeBuilder;
import org.mule.metadata.api.builder.StringTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;

public class AlbumTracksOutputMetadataResolver implements OutputTypeResolver<Object>{

    @Override
    public String getResolverName() {
        return OutputTypeResolver.super.getResolverName();
    }

    @Override
    public MetadataType getOutputType(MetadataContext context, Object key) {
        final ObjectTypeBuilder topObject = context.getTypeBuilder().objectType().id("album tracks");
        topObject.addField().key("artists").value().arrayType().of(getInnerObject("artists", context));
        topObject.addField().key("available_markets").value().arrayType().of(getInnerObject("available_markets", context));
        topObject.addField().key("disc_number").value().numberType();
        topObject.addField().key("duration_ms").value().numberType();
        topObject.addField().key("explicit").value().booleanType();
        topObject.addField().key("externalUrl").value().arrayType().of(getInnerObject("externalUrl", context));
        topObject.addField().key("href").value().stringType();
        topObject.addField().key("id").value().numberType();
        topObject.addField().key("is_local").value().booleanType();
        topObject.addField().key("name").value().stringType();
        topObject.addField().key("preview_url").value().stringType();
        topObject.addField().key("track_number").value().numberType();
        topObject.addField().key("type").value().stringType();
        topObject.addField().key("uri").value().stringType();

        return topObject.build();
    }

    private MetadataType getInnerObject(String mainObjectName, MetadataContext context) {
        final ObjectTypeBuilder objectTypeBuilder = context.getTypeBuilder().objectType().id(mainObjectName);

        switch (mainObjectName) {

            case "artists":
                objectTypeBuilder.addField().key("externalUrl").value().arrayType().of(getInnerObject("externalUrl", context));
                objectTypeBuilder.addField().key("href").value().stringType();
                objectTypeBuilder.addField().key("id").value().numberType();
                objectTypeBuilder.addField().key("name").value().stringType();
                objectTypeBuilder.addField().key("type").value().stringType();
                objectTypeBuilder.addField().key("uri").value().stringType();
                return objectTypeBuilder.build();

            case "externalUrl":
                objectTypeBuilder.addField().key("spotify").value().stringType();
                return objectTypeBuilder.build();

            case "available_markets":
                final StringTypeBuilder stringTypeBuilder = context.getTypeBuilder().stringType();
                return stringTypeBuilder.build();

            default: return null;
        }

    }

    @Override
    public String getCategoryName() {
        return "AlbumTracksOutputMetadataResolver";
    }

}
