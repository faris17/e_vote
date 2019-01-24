package id.kunya.informatikavote.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muhammad on 12/12/18.
 */

public class Posts {
    public String id;
    public Render title;
    public Render content;
    public String featured_media;
    public Render guid;
    public String link;
    public Links _links;
    public ArrayList<String> categories;

    public String getId() {
        return id;
    }

    public Render getTitle() {
        return title;
    }

    public Render getContent() {
        return content;
    }

    public String getFeatured_media() {
        return featured_media;
    }

    public Render getGuid() {
        return guid;
    }

    public String getLink() {
        return link;
    }

    public Links get_links() {
        return _links;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public class Render{
        private String rendered;

        public String getRendered() {
            return rendered;
        }
    }


    public class Links{

        @SerializedName("wp:featuredmedia")
        public List<Attachment> featuredmedia;

        public List<Attachment> getFeaturedmedia() {
            return featuredmedia;
        }

        public class Attachment{
            public String href;

            public String getHref() {
                return href;
            }
        }
    }

}
