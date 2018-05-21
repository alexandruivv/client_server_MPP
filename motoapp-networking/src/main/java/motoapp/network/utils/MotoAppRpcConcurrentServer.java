package motoapp.network.utils;

import motoapp.network.rpcprotocol.MotoAppClientReflectionWorker;
import motoapp.services.IMotoAppServer;

import java.net.Socket;


public class MotoAppRpcConcurrentServer extends AbsConcurrentServer {
    private IMotoAppServer chatServer;
    public MotoAppRpcConcurrentServer(int port, IMotoAppServer chatServer) {
        super(port);
        this.chatServer = chatServer;
        System.out.println("Chat- MotoAppRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
       // ChatClientRpcWorker worker=new ChatClientRpcWorker(chatServer, client);
        MotoAppClientReflectionWorker worker=new MotoAppClientReflectionWorker(chatServer, client);

        Thread tw=new Thread(worker);
        return tw;
    }
}
