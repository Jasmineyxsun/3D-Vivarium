import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.jogamp.opengl.util.*;

import com.jogamp.opengl.util.gl2.GLUT;

import java.util.*;

//Author: Yuxin Sun
//Create the food model for fishes in the Vivarium
public class FoodModel extends Component implements Animate{
	private float scale;
	private float angle;
	public float x,y,z;
	private float vy;
	public boolean eaten;
	public Component food;
	private float collisionScalar = -0.04f, foodScalar;
	Vivarium v;

	public FoodModel(Point3D position, float scale1) {
		super(new Point3D(position));
	    scale = scale1;
	    angle = 0;
	    eaten = false;
	    x = (float)position.x();
	    y = (float)position.y();
	    z = (float)position.z();
	    super.x = x;
	    super.y = y;
	    super.z = z;
	    vy = (float) 0.01;
	    
	    double r1 = -2 + Math.random() * 4;
	    double r2 = -2 + Math.random() * 4;
		food = new Component(new Point3D(r1,0,r2), new FoodDisplayable(scale));
		food.setColor(new FloatColor(0.9f, 0.6f, 0.6f));
		this.addChild(food);
	}
	public void setModelStates(ArrayList<Configuration> config_list) {
		// TODO Auto-generated method stub
		if (config_list.size() > 1) {
			this.setConfiguration(config_list.get(0));
		}
		
	}
	
	public void collision(GL2 gl) {
		Point3D distance = new Point3D(0, 0, 0);
		if (Vivarium.addFood) {
			for (FishModel fish : Vivarium.fish) {
				distance = new Point3D(Math.abs(x - fish.x), Math.abs(y - fish.y), Math.abs(z - fish.z));
				if (!eaten) {
					if (distance.norm() < 0.1) {
						eaten = true;
					}
				}
			}
		}
//		Point3D distance = new Point3D(0, 0, 0);
//		for (FoodModel food : Vivarium.food) {
//			distance = new Point3D(Math.abs(x - food.x), Math.abs(y - food.y), Math.abs(z - food.z));
//			if (distance.norm() < 0.2) {
//				collisionScalar = 0.05f;
//			}
//		}
	}
	
	 public void collisionDetectionWall()
	  {
		 // Implement Code here
		  if (y - this.scale < -2) {
			  vy = 0;
		  }	  
	  }

	@Override
	public void animationUpdate(GL2 gl) {
		  collision(gl);
		// TODO Auto-generated method stub
		  collisionDetectionWall();
		  y -= vy;
	}
	
	public void draw(GL2 gl) {
		if (this.eaten) {
			return;
		}
		gl.glPushMatrix();
		gl.glPushAttrib(GL2.GL_CURRENT_BIT);
	    gl.glTranslatef(0, y, 0);
	    food.draw(gl);
		gl.glPopAttrib();
		gl.glPopMatrix();
	}
}