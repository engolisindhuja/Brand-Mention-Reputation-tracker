package models;

public class Mention {

    private int id;
    private String platform;
    private String content;
    private String sentiment;
    private String postedOn;

    public Mention(int id, String platform, String content, String sentiment, String postedOn) {
        this.id = id;
        this.platform = platform;
        this.content = content;
        this.sentiment = sentiment;
        this.postedOn = postedOn;
    }

    public int getId() { return id; }
    public String getPlatform() { return platform; }
    public String getContent() { return content; }
    public String getSentiment() { return sentiment; }
    public String getPostedOn() { return postedOn; }
}
