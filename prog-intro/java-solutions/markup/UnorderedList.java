package markup;

import java.util.List;

public class UnorderedList extends MainList {
    public UnorderedList(List<ListItem> list) {
        super(list);
    }

    @Override
    public void toDocBook(StringBuilder sb) {
        super.toDocBook(sb, "<itemizedlist>", "</itemizedlist>");

    }
}
