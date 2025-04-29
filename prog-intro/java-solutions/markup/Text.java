package markup;

public class Text implements MarkdownInterface {
    private final String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public void toMarkdown(StringBuilder sb) {
        sb.append(text);
    }

    @Override
    public void toDocBook(StringBuilder sb) {
        sb.append(text);
    }
}
