package ru.nsu.ccfit.boltava.socket;

import java.net.InetSocketAddress;

public class TouSegment {

    private InetSocketAddress address;

    private int sequenceNumber;
    private int acknowledgementNumber;
    private SegmentType type;
    private byte[] payload = new byte[0];

    public TouSegment(byte[] segmentContent, InetSocketAddress address) {
        this.address = address;
        parseSegmentContent(segmentContent);
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public int getAcknowledgementNumber() {
        return acknowledgementNumber;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public SegmentType getType() {
        return type;
    }

    public byte[] getPayload() {
        return payload;
    }

    public byte[] toBytes() {
        byte[] segment = new byte[TouProtocolUtils.SEGMENT_HEADER_LENGTH + payload.length];
        TouProtocolUtils.writeSequenceNumber(segment, sequenceNumber);
        TouProtocolUtils.writeAckNumber(segment, acknowledgementNumber);
        TouProtocolUtils.setAckFlag(segment);
        // write payload into segment
        System.arraycopy(payload, 0, segment, TouProtocolUtils.SEGMENT_HEADER_LENGTH, payload.length);
        return  segment;
    }

    private void parseSegmentContent(byte[] content) {
        sequenceNumber = TouProtocolUtils.readSequenceNumber(content);
        acknowledgementNumber = TouProtocolUtils.readAckNumber(content);
        payload = new byte[content.length - TouProtocolUtils.SEGMENT_HEADER_LENGTH];

        System.arraycopy(
                content,
                TouProtocolUtils.SEGMENT_HEADER_LENGTH,
                payload, 0,
                payload.length
        );

        if (TouProtocolUtils.hasFinFlag(content)) {
            type = SegmentType.FIN;
        } else if (TouProtocolUtils.hasSynFlag(content)) {
            if (TouProtocolUtils.hasAckFlag(content)) {
                type = SegmentType.SYNACK;
            } else {
                type= SegmentType.SYN;
            }
        } else {
            type = SegmentType.ACK;
        }
    }

    public enum SegmentType {
        SYN,
        ACK,
        FIN,
        SYNACK
    }

}
