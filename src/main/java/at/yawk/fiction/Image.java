package at.yawk.fiction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.net.URI;
import javax.annotation.Nullable;
import lombok.Data;

/**
 * @author yawkat
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS)
@JsonIgnoreProperties("referer")
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
