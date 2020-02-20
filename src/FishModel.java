import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.jogamp.opengl.util.*;

import com.jogamp.opengl.util.gl2.GLUT;
import com.sun.tools.javac.code.Attribute.Array;

import java.util.*;

//Author: Yuxin Sun
//Create the fish model for fishes in the Vivarium
public class FishModel extends Component implements Animate{
	private float scale;
	private float angle;
	//Movement parameters
	public float x,y,z;
	private float vx,vy,vz;
	private float directionx, directiony, directionz;
	private Random rand = new Random();
	private double rotateSpeed = 1;
	//Component of Fish
	public Component fishTail;
	public Component fishBody;
	private Point3D originalfacing;
	public float[] rotationMatrix = new float[16];
	private Quaternion orientation = new Quaternion();

	private float collisionScalar = 0.005f, turtleScalar = 0.1f, foodScalar;
	public boolean fishEaten;
	//boolean to detect whether there is food in the tank
	private boolean food;

	public FishModel(Point3D position, float scale1) {
		super(new Point3D(position));
	    scale = scale1;
	    angle = 0;
	    fishEaten = false;
	    x = (float)position.x();
	    y = (float)position.y();
	    z = (float)position.z();
	    originalfacing = this.position();
	    directionx = rand.nextFloat();
	    directiony = rand.nextFloat();
	    directionz = rand.nextFloat();
	    vx = 0.03f;
	    vy = 0.03f;
	    vz = 0.03f;

	    //Implement the fish body and tail by creating two components
		fishBody = new Component(new Point3D(0,0,0), new FishBodyDisplayable(scale));
		fishBody.setColor(new FloatColor(0.5742f, 0.4375f,  0.8555f));
		fishTail = new Component (new Point3D(0.23,0,0.02), new FishTailDisplayable(scale));
		fishTail.setColor(new FloatColor(0.5742f, 0.4375f,  0.8555f));
		this.addChild(fishBody);
		fishBody.addChild(fishTail);
		fishBody.setYNegativeExtent(0);
		fishBody.setYPositiveExtent(0);
		fishTail.setYNegativeExtent(-30);
		fishTail.setYPositiveExtent(30);
	}
	public void setModelStates(ArrayList<Configuration> config_list) {
		// TODO Auto-generated method stub
		if (config_list.size() > 1) {
			this.setConfiguration(config_list.get(0));
		}
		
	}
	
	//Change the direction of the fish when touching the wall
	 public void collisionDetectionWall(){
		 // Implement Code here
		 //randomly assign a new direction
		 float n = rand.nextFloat();
		 while (n < 0.2f) {
			 n = rand.nextFloat();
		 }
		  if (x - this.scale < -2 || x + this.scale > 2) {
			  x = 2 - this.scale;
			  if (directionx > 0) {
				  directionx = -1 * n;
			  }else {
				  x = -2 + this.scale;
				  directionx = n;
			  }
		  }
		  if (y - this.scale < -2 || y + this.scale > 2) {
			  y = 2 - this.scale;
			  if (directiony > 0) {
				  directiony = -1 * n;
			  }else {
				  y = -2 + this.scale;
				  directiony = n;
			  }
		  }
		  if (z - this.scale < -2 || z + this.scale > 2) {
			  z = 2 - this.scale;
			  if (directionz > 0) {
				  directionz = -1 * n;
			  }else {
				  z  = -2 + this.scale;
				  directionz = n;
			  }
		  }
		  float mag = (float)Math.sqrt(Math.pow(directionx, 2) + Math.pow(directiony, 2) + Math.pow(directionz, 2));
		  
		  	directionx = directionx/mag;
		  	directiony = directiony/mag;
		  	directionz = directionz/mag;
			x += vx * directionx;
			y += vy * directiony;
			z += vz * directionz;		  
	  }
	 

	@Override
	public void animationUpdate(GL2 gl) {
		collision(gl);
		potential(gl);
		
		//When the fish is at the original point, facing changed according to the direction of moving
		if (x == originalfacing.x() & y == originalfacing.y() & z == originalfacing.z()) {
			rotationFirst();
		}
		collisionDetectionWall();
		//General rotation
		rotation();
		  this.angle = (this.angle+5) % 360;
		  if (Vivarium.addFood) {
			  noFoodLeft();
		  }
		 
		if (fishTail.checkRotationReachedExtent(Axis.Y)){
			rotateSpeed = -rotateSpeed;
		}
		  fishTail.rotate(Axis.Y, rotateSpeed);
	}
	
	//The first rotation happens when the fish is in the original point
	private void rotationFirst() {
		Point3D originalO = new Point3D(-1, 0, 0);
		//target orientation is the direction orientation
		Point3D targetO = new Point3D(directionx, directiony, directionz);
		originalO = originalO.normalize();
		targetO = targetO.normalize();
		//Get the rotation matrix axis by using cross product
		Point3D axisVector = originalO.crossProduct(targetO);
		if (!originalO.equals(targetO)) {
		float[] axis;
		axis = new float[] {(float) axisVector.x(), (float)axisVector.y(), (float)axisVector.z()};
		//Get the rotation matrix angle by using dot product
		float angleCos = (float)originalO.dotProduct(targetO);
		if (Math.abs(angleCos) < 1) {
			double rotationAngle = Math.acos(originalO.dotProduct(targetO));
			float sin = (float) Math.sin(0.5f * rotationAngle);
			float cos = (float) Math.cos(0.5f * rotationAngle);
			Quaternion q = new Quaternion(cos, sin * axis[0], sin * axis[1], sin * axis[2]);
			this.orientation = q.multiply(this.orientation);
			this.orientation.normalize();
			this.preMatrix = this.orientation.to_matrix();
			this.originalfacing = targetO;
		}
		}
		
	}
	//rotation function that helps to fix the general facing according to the moving direction
	private void rotation() {
		Point3D originalO = this.originalfacing;
		Point3D targetO = new Point3D(directionx, directiony, directionz);
		originalO = originalO.normalize();
		targetO = targetO.normalize();
		//Get the rotation matrix axis by using cross product
		Point3D axisVector = originalO.crossProduct(targetO);
		axisVector = axisVector.normalize();
		if (!originalO.equals(targetO)) {
			float[] axis;
			axis = new float[] {(float) axisVector.x(), (float)axisVector.y(), (float)axisVector.z()};
			//Get the rotation matrix angle by using dot product
			float angleCos = (float)originalO.dotProduct(targetO);
			if (Math.abs(angleCos) < 1) {
				double rotationAngle = Math.acos(originalO.dotProduct(targetO));
				float sin = (float) Math.sin(0.5f * rotationAngle);
				float cos = (float) Math.cos(0.5f * rotationAngle);
				Quaternion q = new Quaternion(cos, sin * axis[0], sin * axis[1], sin * axis[2]);
				this.orientation = q.multiply(this.orientation);
				this.orientation.normalize();
				this.preMatrix = this.orientation.to_matrix();
				this.originalfacing = targetO;
			}
		}
	}
	
	public void noFoodLeft() {
		FoodModel firstfood = Vivarium.food.get(0);
		food = firstfood.eaten;
		for (FoodModel food1: Vivarium.food) {
			food = food & food1.eaten;
			if (food) {
				foodScalar = 0;
			}
		}
	}
	
	//set up scalars for the potential functions in order to behave attraction and avoiding
	public void collision(GL2 gl) {
		Point3D distance = new Point3D(0, 0, 0);
		for (FishModel f : Vivarium.fish) {
			distance = new Point3D(Math.abs(x - f.x), Math.abs(y - f.y), Math.abs(z - f.z));
			if (distance.norm() < 0.6f) {
				collisionScalar = 0.2f;
			}else {
				collisionScalar = -0.005f;
			}
		}
		for (TurtleModel turtle : Vivarium.turtle)  {
			distance = new Point3D(Math.abs(x - turtle.x), Math.abs(y - turtle.y), Math.abs(z - turtle.z));
			if (distance.norm() < 0.5f) {
				this.fishEaten = true;
			}
		}
		if (Vivarium.addFood) {
			foodScalar = -0.1f;
		}
	}
	
	//Use the potential function that defined below to calculate the overall potentials
	private void potential(GL2 gl) {
	Point3D fishMe = new Point3D(this.x, this.y, this.z);
	ArrayList <Point3D> list = new ArrayList<Point3D> ();
	for (FishModel f : Vivarium.fish) {
		if (!(f.equals(this))) {
			Point3D fishOther = new Point3D(f.x, f.y, f.z);
			list.add(potentialFunction(fishMe, fishOther, collisionScalar));
		}
	}
		for (TurtleModel turtle : Vivarium.turtle)  {
			Point3D turtleOther = new Point3D(turtle.x, turtle.y, turtle.z);
			list.add(potentialFunction(fishMe, turtleOther, turtleScalar));
		}
		if (Vivarium.addFood) {
			for (FoodModel food : Vivarium.food) {
				Point3D foodOther = new Point3D(food.x, food.y, food.z);
				list.add(potentialFunction(fishMe, foodOther, foodScalar));
			}
		}
		
		float sumX = 0, sumY = 0, sumZ = 0;
		for (Point3D points : list) {
			sumX += points.x();
			sumY += points.y();
			sumZ += points.z();		
		}
		
		directionx += sumX;
		directiony += sumY;
		directionz += sumZ;
	}
	private Point3D potentialFunction(Point3D p, Point3D q, float scale) {
		float x1 = (float) (scale*(p.x() - q.x())*Math.pow(Math.E,-1*(Math.pow((p.x()-q.x()), 2) + Math.pow((p.y()-q.y()), 2) + Math.pow((p.z()-q.z()), 2)) ));
		float y1 = (float) (scale*(p.y() - q.y())*Math.pow(Math.E,-1*(Math.pow((p.x()-q.x()), 2) + Math.pow((p.y()-q.y()), 2) + Math.pow((p.z()-q.z()), 2)) ));
		float z1 = (float) (scale*(p.z() - q.z())*Math.pow(Math.E,-1*(Math.pow((p.x()-q.x()), 2) + Math.pow((p.y()-q.y()), 2) + Math.pow((p.z()-q.z()), 2)) ));
		Point3D result = new Point3D(x1, y1, z1);
		return result;
	}
	
	public void draw(GL2 gl) {
		if (this.fishEaten) {
			return;
		}
		gl.glPushMatrix();
		gl.glPushAttrib(GL2.GL_CURRENT_BIT);
	    gl.glTranslatef(x, y, z);
	    gl.glMultMatrixf(this.preMatrix, 0);
	    fishBody.draw(gl);
		gl.glPopAttrib();
		gl.glPopMatrix();
		
	}
}
	