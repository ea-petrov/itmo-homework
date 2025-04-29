package markup;

import java.util.List;

public class ListItem {
    private final List<DocBookInterface> list;
    private static final String DokBookFirstTag = "<listitem>";
    private static final String DokBookLastTag = "</listitem>";

    public ListItem(List<DocBookInterface> list) {
        this.list = list;
    }

    public void toDocBook(StringBuilder sb) {
        sb.append(DokBookFirstTag);
        for (DocBookInterface elem : list) {
            elem.toDocBook(sb);
        }
        sb.append(DokBookLastTag);
    }
}
