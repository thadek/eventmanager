package com.m8.event.manager.rest.serializer;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.m8.event.manager.entity.Subcategoria;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class SubcategoriaListDeserializer extends StdDeserializer<List<Subcategoria>> {

    //DESERIALIZADOR JSON PERSONALIZADO DE LA CATEGORIA

    public SubcategoriaListDeserializer(){
        this(null);
    }

    public SubcategoriaListDeserializer(Class<?> t){
        super(t);
    }

    @Override
    public List<Subcategoria> deserialize(
             JsonParser jsonparser, DeserializationContext context) throws IOException , JsonProcessingException {

        return new ArrayList<>();
    }

}
