
import javax.media.opengl.*;
import com.jogamp.opengl.util.*;
import java.util.*;

//Author: Yuxin Sun
//Create the overall Vivarium
public class Vivarium implements Displayable, Animate {
	private Tank tank;
	public static ArrayList<TurtleModel> turtle = new ArrayList<TurtleModel>();
	public static ArrayList<FishModel> fish = new ArrayList<FishModel>();
	public static ArrayList<FoodModel> food = new ArrayList<FoodModel>();
	public static boolean addFood = false;
	

	public Vivarium() {
		tank = new Tank(4.0f, 4.0f, 4.0f);
		fish.add(new FishModel(new Point3D(0, 0, 0), 0.2f));
		fish.add(new FishModel(new Point3D(1 ,1 ,1), 0.2f));
		turtle.add(new TurtleModel(new Point3D(-1, -1, -1), 0.4f));
		
		food.add(new FoodModel(new Point3D(0 ,2, 0), 0.05f));
		food.add(new FoodModel(new Point3D(1 ,2, 1), 0.05f));
		food.add(new FoodModel(new Point3D(1.5 ,2, 1.5), 0.05f));
	
	}

	public void initialize(GL2 gl) {
		tank.initialize(gl);
		for (FishModel object : fish) {
			object.initialize(gl);
		}
		for (TurtleModel object : turtle) {
			object.initialize(gl);
		}
		for (FoodModel object : food) {
			object.initialize(gl);
		}
	}

	public void update(GL2 gl) {
		tank.update(gl);
		for (FishModel object : fish) {
			object.update(gl);
		}
		for (TurtleModel object : turtle) {
			object.update(gl);
		}
		if (addFood) {
			for (FoodModel object : food) {
				object.update(gl);
			}
		}
	}

	public void draw(GL2 gl) {
		tank.draw(gl);
		for (FishModel object : fish) {
			object.draw(gl);
		}
		for (TurtleModel object : turtle) {
			object.draw(gl);
		}
		if (addFood) {
			for (FoodModel object : food) {
				object.draw(gl);
			}
		}
	}

	@Override
	public void setModelStates(ArrayList<Configuration> config_list) {
		// assign configurations in config_list to all Components in here
	}

	@Override
	public void animationUpdate(GL2 gl) {
		for (Component example : fish) {
			if (example instanceof Animate) {
				((Animate) example).animationUpdate(gl);
			}
		}
		for (Component example : turtle) {
			if (example instanceof Animate) {
				((Animate) example).animationUpdate(gl);
			}
		}
		if (addFood) {
			for (FoodModel example : food) {
				if (example instanceof Animate) {
					((Animate) example).animationUpdate(gl);
				}
			}
		}
		
	}

	public void addFood() {
		// TODO Auto-generated method stub
		Vivarium.addFood = ! Vivarium.addFood;		
	}
}
