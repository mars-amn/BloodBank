package abdullah.elamien.bloodbank.models;

import android.os.Parcel;
import android.os.Parcelable;

public class NewsAndArticles implements Parcelable {
    public static final Creator<NewsAndArticles> CREATOR = new Creator<NewsAndArticles>() {
        @Override
        public NewsAndArticles createFromParcel(Parcel in) {
            return new NewsAndArticles(in);
        }

        @Override
        public NewsAndArticles[] newArray(int size) {
            return new NewsAndArticles[size];
        }
    };
    private String articleBody;
    private String articleImage;
    private String articleTitle;

    public NewsAndArticles() {
    }

    protected NewsAndArticles(Parcel in) {
        articleBody = in.readString();
        articleImage = in.readString();
        articleTitle = in.readString();
    }

    public String getArticleBody() {
        return articleBody;
    }

    public void setArticleBody(String articleBody) {
        this.articleBody = articleBody;
    }

    public String getArticleImage() {
        return articleImage;
    }

    public void setArticleImage(String articleImage) {
        this.articleImage = articleImage;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(articleBody);
        dest.writeString(articleImage);
        dest.writeString(articleTitle);
    }
}
