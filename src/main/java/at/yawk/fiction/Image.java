package at.yawk.fiction;

import java.net.URI;
import lombok.Data;

/**
 * @author yawkat
 */
@Data
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
