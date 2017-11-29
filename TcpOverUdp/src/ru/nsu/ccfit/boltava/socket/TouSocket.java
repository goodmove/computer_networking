package ru.nsu.ccfit.boltava.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Random;


public class TouSocket {

    // Size of the buffer, used to store data to be sent
    private static final int BUFFER_SIZE = 64 * 1000;
    // Max segment size. Segment is a piece of data from data buffer
    private static final int MAX_SEGMENT_SIZE = 4 * 1000;


    // buffer to store data to be sent
    private byte[] receivedDataBuffer = new byte[BUFFER_SIZE];
    private int bufferStart = 0;
    private int bufferEnd = receivedDataBuffer.length;

    private DatagramSocket localSocket;
    private InetAddress address;
    private int port;

    private boolean isBound = false;
    private boolean isConnected = false;

    private long sequenceNumber = (long) new Random().nextInt();
    private long ackNumber = 0;

    private final Object lock = new Object();


    public TouSocket() {}

    public TouSocket(InetAddress address, int port) throws SocketException {
        this.localSocket = new DatagramSocket();
        isBound = true;
        this.connect(new InetSocketAddress(address, port));
    }

    public void write(byte data) {}

    public void bind(SocketAddress bindPoint) throws IOException {
        if (this.isBound) throw new IOException("Socket is already bound");

        this.localSocket = new DatagramSocket(bindPoint);
        this.isBound = true;
    }

    public void connect(SocketAddress address) {
        if (address == null) throw new IllegalArgumentException("Cannot connect to null address");
    }

    public void close() {

    }

    public SocketAddress getRemoteSocketAddress() {
        return new InetSocketAddress(address, port);
    }

    public InetAddress getInetAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public int getLocalPort() {
        return localSocket.getPort();
    }

    public InetAddress getLocalAddress() {
        return localSocket.getInetAddress();
    }

    public InputStream getInputStream() {
        return null;
    }

    public OutputStream getOutputStream() {
        return null;
    }

}
