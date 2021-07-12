package com.m8.event.manager.rest.serializer;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.m8.event.manager.entity.Categoria;
import com.m8.event.manager.entity.Subcategoria;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;


public class SubcategoriaListSerializer extends StdSerializer<List<Subcategoria>> {

    //SERIALIZADOR JSON PERSONALIZADO DE LA CATEGORIA

    public SubcategoriaListSerializer(){
        this(null);
    }

    public SubcategoriaListSerializer(Class<List<Subcategoria>> t){
        super(t);
    }

    @Override
    public void serialize(
            List<Subcategoria> cat, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
        jsonGenerator.writeStartObject();

        String listaSubcat="";

         //Ordeno la lista de subcategorias en un string
        Iterator<Subcategoria> it = cat.iterator();
        while(it.hasNext()){
            String st =it.next().getNombre();
            if(!it.hasNext()){
                listaSubcat+= st+".";
            }else{
                listaSubcat+= st+", ";
            }
        }

        jsonGenerator.writeStringField("subcategorias",listaSubcat );
        jsonGenerator.writeEndObject();
    }

}
