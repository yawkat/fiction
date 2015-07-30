package at.yawk.fiction;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.net.URI;
import lombok.Data;

/**
 * @author yawkat
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
public class Image {
    URI imageUrl;
    URI thumbnailUrl;

    public Image() {}

    public Image(URI imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Image(URI imageUrl, URI thumbnailUrl) {
        this.imageUrl = imageUrl;
        this.thumbnailUrl = thumbnailUrl;
    }
}
