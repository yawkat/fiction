package at.yawk.fiction;

/**
 * @author yawkat
 */
public interface Metadatable {
    Metadata getMetadata();

    Metadatable withMetadata(Metadata metadata);
}
