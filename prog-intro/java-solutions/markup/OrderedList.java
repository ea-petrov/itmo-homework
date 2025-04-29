package markup;

import java.util.List;

public class OrderedList extends MainList {
    public OrderedList(List<ListItem> list) {
        super(list);
    }

    @Override
    public void toDocBook(StringBuilder sb) {
        super.toDocBook(sb, "<orderedlist>", "</orderedlist>");
    }
}
