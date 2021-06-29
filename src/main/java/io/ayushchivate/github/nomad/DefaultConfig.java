package io.ayushchivate.github.nomad;

import net.dohaw.corelib.Config;

public class DefaultConfig extends Config {

    public DefaultConfig() {
        super("config.yml");
    }

    public int getInventorySize() {
        return config.getInt("Inventory Size");
    }
}
