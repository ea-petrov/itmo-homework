package markup;

import java.util.List;

public abstract class MainList implements DocBookInterface {
    private final List<ListItem> list;

    public MainList(List<ListItem> list) {
        this.list = list;
    }

    public void toDocBook(StringBuilder sb, String first, String second) {
        sb.append(first);
        for (ListItem elem : list) {
            elem.toDocBook(sb);
        }
        sb.append(second);
    }
}
