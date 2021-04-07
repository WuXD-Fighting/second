package algorithm.word;

public class TreeEntity {
	private String text;
	private String style;
	private int Level;
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
	public int getLevel() {
		return Level;
	}
	public void setLevel(int level) {
		Level = level;
	}
	@Override
	public String toString() {
		return "TreeEntity [text=" + text + ", style=" + style + ", Level=" + Level + "]";
	}
	
}
