package storage;

public class Card {
    private int card_id;
    private int user_id;
    private String media_url;
    private String title;
    private String caption;

    private int likes;
    private String[] tags;

    public Card(int card_id, int user_id, String media_url, String title, String caption, int likes, String[] tags) {
        this.card_id = card_id;
        this.user_id = user_id;
        this.media_url = media_url;
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

    public String getMediaUrl() {
        return media_url;
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
                       "media_url: " + media_url + "\n" +
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
