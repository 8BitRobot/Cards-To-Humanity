package storage;

public class Tag {
    private int tag_id;
    private String content;
    private int cards_tagged;

    public Tag(int tag_id, String content, int cards_tagged) {
        this.tag_id = tag_id;
        this.content = content;
        this.cards_tagged = cards_tagged;
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
}
