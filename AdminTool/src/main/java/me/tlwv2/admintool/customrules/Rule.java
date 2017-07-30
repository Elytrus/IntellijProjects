package me.tlwv2.admintool.customrules;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import me.tlwv2.admintool.AdminTool;

public class Rule<T> implements ConfigurationSerializable{
    private T value;
    private Type type;

    private static final String KEY = "value";
    private static final String TYPEKEY = "type";

    public Rule(T value){
        this.value = value;
        this.type = Type.parse(value.getClass());
    }

    //////////////////// Special

    @SuppressWarnings("unchecked")
    public T cast(String arg){
        return (T) type.cast(arg);
    }

    //typecorrecteh
    public boolean isCorrectType(String other){
        try{
            type.cast(other);
        }
        catch(Exception e){
            return false;
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    public void set(String o){
        this.value = (T) type.cast(o);
    }

    @Override
    public String toString(){
        return value.toString();
    }

    //SERIALIZAION

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put(KEY, value);
        map.put(TYPEKEY, type.toString());

        return map;
    }

    @SuppressWarnings("unchecked")
    public Rule(Map<String, Object> map) throws ClassNotFoundException{
        value = (T) map.get(KEY);
        type = Type.parse(map.get(TYPEKEY));
    }

    ///////////////////////////

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Type getType(){
        return type;
    }

    public enum Type{
        INT(Integer::parseInt),
        DOUBLE(Double::parseDouble),
        BOOLEAN(Boolean::parseBoolean);

        static final String INT_NAME = Integer.class.getName();
        static final String DOUBLE_NAME = Double.class.getName();
        static final String BOOLEAN_NAME = Boolean.class.getName();

        private Function<String, Object> cast;

        private Type(Function<String, Object> cast){
            this.cast = cast;
        }

        public static Type parse(Object object){
            if (INT_NAME.equals(object)) {
                return INT;
            } else if (DOUBLE_NAME.equals(object)) {
                return DOUBLE;
            } else if (BOOLEAN_NAME.equals(object)) {
                return BOOLEAN;
            } else {
                throw new IllegalArgumentException("§4Error 1: Invalid Argument Type" + AdminTool.askMSG);
            }
        }

        public static Type parse(Class<?> clazz){
            return parse(clazz.getName());
        }

        public Object cast(String object){
            return this.cast.apply(object);
        }

        @Override
        public String toString(){
            switch(this){
                case INT:
                    return INT_NAME;
                case DOUBLE:
                    return DOUBLE_NAME;
                case BOOLEAN:
                    return BOOLEAN_NAME;
                default:
                    throw new IllegalArgumentException("§4Error 1a: Invalid Enum Type" + AdminTool.askMSG);
            }
        }

        public String simpleName(){
            switch(this){
                case INT:
                    return "Integer";
                case DOUBLE:
                    return "Double";
                case BOOLEAN:
                    return "Boolean";
                default:
                    throw new IllegalArgumentException("§4Error 1b: Invalid Enum Type" + AdminTool.askMSG);
            }
        }
    }
}
