package markup;

import java.util.List;

public class Strikeout extends Main {
    public Strikeout(List<MarkdownInterface> list) {
        super(list);
    }

    @Override
    public void toMarkdown(StringBuilder sb) {
        super.toMarkdown(sb, "~", "~");
    }

    @Override
    public void toDocBook(StringBuilder sb) {
        super.toDocBook(sb, "<emphasis role=\'strikeout\'>", "</emphasis>");
    }
}
