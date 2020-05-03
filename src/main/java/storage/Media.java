package storage;

public class Media {
    private int media_id;
    private String media_mime_type;
    private byte[] media_content;

    public Media(int media_id, String media_mime_type, byte[] media_content) {
        this.media_id = media_id;
        this.media_mime_type = media_mime_type;
        this.media_content = media_content;
    }

    public int getMediaId() {
        return media_id;
    }

    public String getMediaMimeType() {
        return media_mime_type;
    }

    public byte[] getMediaContent() {
        return media_content;
    }
}
