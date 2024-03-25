import java.awt.Color;

public class GravBody {

    public GravBody(double Velx, double Vely, double Locx, double Locy, double Mass, double Radius, boolean Fixed, Color colorInput, String nameInput){
        velx = Velx;
        vely = Vely;
        locx = Locx * 1000;
        locy = Locy * 1000;
        mass = Mass * 1000000;
        radius = Radius * 1000;
        fixed = Fixed;
        color = colorInput;
        name = nameInput;
    }

    public GravBody(GravBody body){
        velx = body.getVelx();
        vely = body.getVely();
        locx = body.getLocx();
        locy = body.getLocy();
        mass = body.getMass();
        radius = body.getRadius();
        fixed = body.getFixed();
        color = body.getColor();
        name = body.getName();
    }

    //declares the variables of each GravBody
    double velx;
    double vely;

    double locx;
    double locy;

    double mass;
    double radius;

    boolean fixed;

    double orbitalEnergy;
    
    boolean orbitCompleted = false;

    String name;

    Color color;

    //setters and getters
    public void setVelx(double x){
        this.velx = x;
    }
    public void setVely(double y){
        this.vely = y;
    }
    public void setLocx(double x){
        this.locx = x;
    }
    public void setLocy(double y){
        this.locy = y;
    }
    public void setMass(double mass){
        this.mass = mass;
    }
    public void setRadius(double radius){
        this.radius = radius;
    }
    public void setFixed(boolean fixed){
        this.fixed = fixed;
    }
    public void setOrbitalEnergy(double x){
        this.orbitalEnergy = x;
    }
    public void setColor(Color a){
        this.color = a;
    }
    public void setName(String n){
        this.name = n;
    }

    public double getVelx(){
        return this.velx;
    }
    public double getVely(){
        return this.vely;
    }
    public double getLocx(){
        return this.locx;
    }
    public double getLocy(){
        return this.locy;
    }
    public double getMass(){
        return this.mass;
    }
    public double getRadius(){
        return this.radius;
    }
    public boolean getFixed(){
        return this.fixed;
    }
    public double getOrbitalEnergy(){
        return this.orbitalEnergy;
    }
    public String getName(){
        return this.name;
    }
    public Color getColor(){
        return this.color;
    }

}