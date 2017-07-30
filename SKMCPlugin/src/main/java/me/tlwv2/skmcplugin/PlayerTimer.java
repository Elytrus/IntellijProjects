package me.tlwv2.skmcplugin;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerTimer implements ConfigurationSerializable{
    private static final String CNTKEY = "count";
    private static final String DELKEY = "delay";
    private static final String DELSKEY = "delaycount";
    private static final String CNTSKEY = "countcount";

    private int delays;
    private int counts;

    public ConcurrentHashMap<String, Integer> countA = new ConcurrentHashMap<String, Integer>();
    public ConcurrentHashMap<String, Integer> delayA = new ConcurrentHashMap<String, Integer>();

    public PlayerTimer(int delays, int counts){
        super();

        this.delays = delays;
        this.counts = counts;
    }

    public PlayerTimer(int delays){
        this(delays, 1);
    }

    public void begin(Player p){
        countA.put(p.getUniqueId().toString(), counts);
    }

    public void use(Player p){
        String pUUID = p.getUniqueId().toString();

        if(!countA.containsKey(pUUID))
            this.begin(p);

        if(countA.get(pUUID) > 1)
            countA.put(pUUID, countA.get(pUUID) - 1);
        else{
            countA.remove(pUUID);
            delayA.put(pUUID, delays);
        }

        System.out.println(countA.toString());
    }

    public void update(){
        delayA.forEach((p, i) -> {
            if(i < 2){
                delayA.remove(p);
                countA.put(p, counts);
            }
            else
                delayA.put(p, i - 1);
        });
    }

    public boolean ready(Player p){
        if(!countA.containsKey(p.getUniqueId().toString()) && !delayA.containsKey(p.getUniqueId().toString()))
            begin(p);

        return countA.containsKey(p.getUniqueId().toString());
    }

    public int attsLeft(Player p){
        if(!ready(p)) return 0;

        return countA.get(p.getUniqueId().toString());
    }

    public int timeLeft(Player p){
        if(ready(p)) return 0;

        return delayA.get(p.getUniqueId().toString());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put(CNTKEY, countA);
        map.put(DELKEY, delayA);
        map.put(CNTSKEY, counts);
        map.put(DELSKEY, delays);

        return map;
    }

    @SuppressWarnings("unchecked")
    public static PlayerTimer deserialize(Map<String, Object> map){
        PlayerTimer pt = new PlayerTimer((int)map.get(DELSKEY), (int)map.get(CNTSKEY));
        pt.countA.putAll((Map<String, Integer>)map.get(CNTKEY));
        pt.delayA.putAll((Map<String, Integer>)map.get(DELKEY));

        return pt;
    }
}
