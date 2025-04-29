package markup;

import java.util.List;

public class Strong extends Main {
    public Strong(List<MarkdownInterface> list) {
        super(list);
    }

    @Override
    public void toMarkdown(StringBuilder sb) {
        super.toMarkdown(sb, "__", "__");
    }

    @Override
    public void toDocBook(StringBuilder sb) {
        super.toDocBook(sb, "<emphasis role=\'bold\'>", "</emphasis>");
    }
}
