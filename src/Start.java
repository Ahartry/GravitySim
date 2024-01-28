import javax.swing.UIManager;

public class Start {
    
    public static void main(String[] args) throws Exception {
        try {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception a) {}
        App app = new App();
        app.run();
    }
}
