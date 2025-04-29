package markup;

import java.util.List;

public class Paragraph implements DocBookInterface {
    private final List<MarkdownInterface> list;

    // to separate interface InParagraphInterface
    public Paragraph(List<MarkdownInterface> list) {
        this.list = list;
    }

    public void toMarkdown(StringBuilder sb) {
        for (MarkdownInterface list : list) {
            list.toMarkdown(sb);
        }
    }

    @Override
    public void toDocBook(StringBuilder sb) {
        sb.append("<para>");
        for (MarkdownInterface list : list) {
            list.toDocBook(sb);
        }
        sb.append("</para>");
    }
}

