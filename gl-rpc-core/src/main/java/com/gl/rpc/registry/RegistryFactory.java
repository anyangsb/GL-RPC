package com.gl.rpc.registry;

import com.gl.rpc.spi.SpiLoader;

public class RegistryFactory {

    static {
        SpiLoader.load(Registry.class);
    }

    private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();

    public static Registry getRegistry(String key) {
        return SpiLoader.getInstance(Registry.class,key);
    }
}
