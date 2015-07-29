package at.yawk.fiction.impl.fanfiction;

import at.yawk.fiction.impl.PageParser;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.nodes.Element;

/**
 * @author yawkat
 */
class SubCategoryPageParser extends PageParser<List<FfnSubCategory>> {
    @Override
    protected List<FfnSubCategory> create() {
        return new ArrayList<>();
    }

    @Override
    protected void parse(Element root, List<FfnSubCategory> target) throws Exception {
        for (Element entry : root.select("#list_output tr td > div")) {
            FfnSubCategory subCategory = new FfnSubCategory();
            subCategory.setName(entry.select("a").text());
            String countText = extractGroup(entry.select("span").text(), "\\((\\d+(\\.\\d+)?[KM]?)\\)");
            subCategory.setEstimatedStoryCount(parseUnitedDouble(countText));
            target.add(subCategory);
        }
    }

    private static int parseUnitedDouble(String text) {
        char unit = text.charAt(text.length() - 1);
        if (Character.isDigit(unit)) {
            return Integer.parseInt(text);
        } else {
            int mul;
            switch (unit) {
            case 'K':
                mul = 1_000;
                break;
            case 'M':
                mul = 1_000_000;
                break;
            default:
                throw new UnsupportedOperationException("Unsupported unit '" + unit + "'");
            }
            return Math.round(Float.parseFloat(text.substring(0, text.length() - 1)) * mul);
        }
    }
}
