package algorithm.word;

public class WordEntity {
	private String text;
	private String style;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	@Override
	public String toString() {
		return "WordEntity [text=" + text + ", style=" + style + "]";
	}
	public WordEntity(String text, String style) {
		super();
		this.text = text;
		this.style = style;
	}
	
}
