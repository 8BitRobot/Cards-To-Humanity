package storage;

public class Card {
    private int card_id;
    private int user_id;
    private int media_id;
    private String title;
    private String caption;

    private int likes;
    private String[] tags;

    public Card(int card_id, int user_id, int media_id, String title, String caption, int likes, String[] tags) {
        this.card_id = card_id;
        this.user_id = user_id;
        this.media_id = media_id;
        this.title = title;
        this.caption = caption;

        this.likes = likes;
        this.tags = tags;
    }

    public int getCardId() {
        return card_id;
    }

    public int getUserId() {
        return user_id;
    }

    public int getMediaId() {
        return media_id;
    }

    public String getTitle() {
        return title;
    }

    public String getCaption() {
        return caption;
    }

    public String toString() {
        String repr =  "Card #" + card_id + "\n" +
                       "user_id: " + user_id + "\n" +
                       "media_id: " + media_id + "\n" +
                       "title: " + title + "\n" +
                       "caption: " + caption + "\n" +
                       "likes: " + likes + "\n" +
                       "tags:\n";
        for (int i = 0; i < tags.length; i++) {
            repr += "    - " + tags[i] + "\n";
        }
        return repr;
    }
}
