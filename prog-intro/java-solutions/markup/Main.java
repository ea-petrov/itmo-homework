package markup;

import java.util.List;

// AbstractMarkdown
public abstract class Main implements MarkdownInterface {
    private final List<MarkdownInterface> list;

    public Main(List<MarkdownInterface> list) {
        this.list = list;
    }

    protected void toMarkdown(StringBuilder sb, String first, String second) {
        sb.append(first);
        for (MarkdownInterface elem : list) {
            elem.toMarkdown(sb);
        }
        sb.append(second);
    }

    protected void toDocBook(StringBuilder sb, String first, String second) {
        sb.append(first);
        for (MarkdownInterface elem : list) {
            elem.toDocBook(sb);
        }
        sb.append(second);
    }
}
