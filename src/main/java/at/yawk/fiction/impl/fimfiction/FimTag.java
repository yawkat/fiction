package at.yawk.fiction.impl.fimfiction;

import java.net.URL;
import lombok.Data;

/**
 * @author yawkat
 */
@Data
public class FimTag {
    private String id;
    private String name;
    // for whatever reason, some icons have spaces in their url, which URI does not support. We use URL for that reason.
    private URL icon;
}
