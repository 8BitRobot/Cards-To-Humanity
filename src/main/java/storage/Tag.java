package storage;

public class Tag {
    private int tag_id;
    private String content;
    private int cards_tagged;
    private long creation_time;

    public Tag(int tag_id, String content, int cards_tagged, long creation_time) {
        this.tag_id = tag_id;
        this.content = content;
        this.cards_tagged = cards_tagged;
        this.creation_time = creation_time;
    }

    public int getTagId() {
        return tag_id;
    }

    public void setTagId(int tag_id) {
        this.tag_id = tag_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCardsTagged() {
        return cards_tagged;
    }

    public void setCardsTagged(int cards_tagged) {
        this.cards_tagged = cards_tagged;
    }

    public long getCreationTime() {
        return creation_time;
    }

    public void setCreationTime(long creation_time) {
        this.creation_time = creation_time;
    }
}
