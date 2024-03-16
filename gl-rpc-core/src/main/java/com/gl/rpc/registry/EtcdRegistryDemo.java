package com.gl.rpc.registry;


import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.kv.GetResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Etcd 注册中心
 */
public class EtcdRegistryDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Client client = Client.builder().endpoints("http://localhost:2379").build();

        KV kvClient = client.getKVClient();
        ByteSequence key = ByteSequence.from("test_key".getBytes());
        ByteSequence value = ByteSequence.from("test_value".getBytes());

        kvClient.put(key,value).get();

        CompletableFuture<GetResponse> getFuture = kvClient.get(key);

        GetResponse getResponse = getFuture.get();
        System.out.println(getResponse);
        kvClient.delete(key).get();

    }

}
