package com.test;

import com.test.Group.City;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class CityConverter implements Converter {
	@Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        City city = (City) value;
        writer.addAttribute("id", city.getId());
        writer.addAttribute("type", city.getType());
        writer.setValue(city.getName());
    }
    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        City city = new City();
        city.setName(reader.getValue());
        city.setId(reader.getAttribute("id"));
        city.setType(reader.getAttribute("type"));
        return city;
    }

    public boolean canConvert(Class clazz) {
        return clazz.equals(City.class);
    } 
}