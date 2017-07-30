package me.tlwv2.admintool.customrules;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class RuleTable implements ConfigurationSerializable{
    @SuppressWarnings("rawtypes")
    private HashMap<String, Rule> table;

    private static final String KEY = "table";

    @SuppressWarnings("rawtypes")
    public RuleTable(){
        table = new HashMap<String, Rule>();

        //Defaults
        table.put("onePlayerSleep", new Rule<Boolean>(true));
    }

    //Checks

    public boolean exists(String key){
        return table.containsKey(key);
    }

    public boolean isCorrectType(String key, String object){
        return table.get(key).isCorrectType(object);
    }

    //Set and get

    public void set(String key, String o){
        table.get(key).set(o);
    }

    @SuppressWarnings("rawtypes")
    public Rule get(String key){
        return table.get(key);
    }

    @SuppressWarnings("unchecked")
    public Rule<Boolean> getBoolean(String key){
        return get(key);
    }

    @SuppressWarnings("unchecked")
    public Rule<Integer> getInteger(String key){
        return get(key);
    }

    @SuppressWarnings("unchecked")
    public Rule<Double> getDouble(String key){
        return get(key);
    }

    public boolean getRawBoolean(String key){
        return getBoolean(key).getValue();
    }

    public int getRawInteger(String key){
        return getInteger(key).getValue();
    }

    public double getRawDouble(String key){
        return getDouble(key).getValue();
    }

    @SuppressWarnings("rawtypes")
    public HashMap<String, Rule> getAll(){
        return table;
    }

    //SERIALIZATION
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put(KEY, table);

        return map;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public RuleTable(Map<String, Object> map){
        table = (HashMap<String, Rule>) map.get(KEY);
    }
}
