import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.jogamp.opengl.util.*;

import com.jogamp.opengl.util.gl2.GLUT;

import java.util.*;
//Author: Yuxin Sun
//Create the turtle model for fishes in the Vivarium
public class TurtleModel extends Component implements Animate{
	
	public ArrayList<Component> turtle = new ArrayList<Component>();
	
    private float angle;
    private float scale;
    public float x,y,z;
    private float vx,vy,vz;
	private float directionx, directiony, directionz;
    private Random rand = new Random();
	private double rotateSpeed = 0.65;
	public Component turtleLeftleg;
	public Component turtleRightleg;
	public Component turtleBody;
	private Quaternion orientation = new Quaternion();
	private Point3D originalfacing;
	private float fishScalar = -0.1f;
	private boolean fish;
	

	public TurtleModel(Point3D position, float scale1) {
		super(new Point3D(position));
	    scale = scale1;
	    angle = 0;
	    x = (float)position.x();
	    y = (float)position.y();
	    z = (float)position.z();
	    originalfacing = this.position();
	    directionx = rand.nextFloat();
	    directiony = rand.nextFloat();
	    directionz = rand.nextFloat();
//	    super.x = x;
//	    super.y = y;
//	    super.z = z;
	    vx  = 0.03f;
	    vy = 0.03f;
	    vz = 0.03f;
	    
		Component turtleHead = new Component(new Point3D(-0.4,0,0), new TurtleHeadDisplayable(scale));
		turtleHead.setColor(new FloatColor(0.42f, 0.48f, 0.14f));
		turtle.add(turtleHead);
		
		turtleBody = new Component (new Point3D(0,0,0), new TurtleBodyDisplayable(scale));
		turtleBody.setColor(new FloatColor(0.42f, 0.48f, 0.14f));
		
		turtleLeftleg = new Component(new Point3D(-0.2,0,-0.3), new TurtleLegDisplayable(scale, true));
		turtleLeftleg.setColor(new FloatColor(0.42f, 0.48f, 0.14f));
		
		turtleRightleg = new Component(new Point3D(-0.2,0,0.3), new TurtleLegDisplayable(scale, false));
		turtleRightleg.setColor(new FloatColor(0.42f, 0.48f, 0.14f));
		
		Component turtleLeftLeg2 = new Component(new Point3D(0.36,0,-0.25), new TurtleLeg2Displayable(scale, true));
		turtleLeftLeg2.setColor(new FloatColor(0.42f, 0.48f, 0.14f));
		
		Component turtleRightLeg2 = new Component(new Point3D(0.36,0,0.25), new TurtleLeg2Displayable(scale, false));
		turtleRightLeg2.setColor(new FloatColor(0.42f, 0.48f, 0.14f));
		
		turtle.add(turtleBody);
		this.addChild(turtleBody);
		turtleBody.addChildren(turtleHead,turtleLeftleg,turtleRightleg,turtleLeftLeg2,turtleRightLeg2);
		
		turtleBody.setYNegativeExtent(0);
		turtleBody.setYPositiveExtent(0);
		turtleLeftleg.setXNegativeExtent(-15);
		turtleLeftleg.setXPositiveExtent(15);
		turtleRightleg.setXNegativeExtent(-15);
		turtleRightleg.setXPositiveExtent(15);
	}

	@Override
	public void setModelStates(ArrayList<Configuration> config_list) {
		// TODO Auto-generated method stub
		if (config_list.size() > 1) {
			this.setConfiguration(config_list.get(0));
		}
		
	}

	 public void collisionDetectionWall(){
		 // Implement Code here
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
		if (x == this.originalfacing.x() & y == this.originalfacing.y() & z == this.originalfacing.z()) {
			rotationFirst();
		}		  
		 collisionDetectionWall();
		 rotation();
		 this.angle = (this.angle+5) % 360;
		 noFishLeft();
		  
		if ((turtleLeftleg.checkRotationReachedExtent(Axis.X))
				&& (turtleRightleg.checkRotationReachedExtent(Axis.X))){
			rotateSpeed = -rotateSpeed;
		}
		turtleLeftleg.rotate(Axis.X, rotateSpeed);
		turtleRightleg.rotate(Axis.X, -rotateSpeed);
	}
	
	private void collision(GL2 gl) {
		Point3D distance = new Point3D(0, 0, 0);
//		for (FishModel f: Vivarium.fish) {
//			distance = new Point3D(Math.abs(x - f.x), Math.abs(y - f.y), Math.abs(z - f.z));
//			if (distance.norm() < 0.5f) {
//				f.eaten = true;
//			}
//		}
	}
	private void noFishLeft() {
		FishModel fishFirst = Vivarium.fish.get(0);
		fish = fishFirst.fishEaten;
		for (FishModel f : Vivarium.fish) {
			fish = fish & f.fishEaten;
			if (fish) {
				fishScalar = 0;
			}
		}
		
	}
	
	public void potential(GL2 gl) {
		Point3D turtleMe = new Point3D(this.x, this.y, this.z);
		ArrayList <Point3D> list = new ArrayList <Point3D> ();
		for (FishModel fish : Vivarium.fish) {
			Point3D fishOther = new Point3D(fish.x, fish.y, fish.z);
			list.add(potentialFunction(turtleMe, fishOther,fishScalar));
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
	
	private Point3D potentialFunction(Point3D p, Point3D q, float scale) {
		float x = (float) (scale*(p.x() - q.x())*Math.pow(Math.E,-1*(Math.pow((p.x()-q.x()), 2) + Math.pow((p.y()-q.y()), 2) + Math.pow((p.z()-q.z()), 2)) ));
		float y = (float) (scale*(p.y() - q.y())*Math.pow(Math.E,-1*(Math.pow((p.x()-q.x()), 2) + Math.pow((p.y()-q.y()), 2) + Math.pow((p.z()-q.z()), 2)) ));
		float z = (float) (scale*(p.z() - q.z())*Math.pow(Math.E,-1*(Math.pow((p.x()-q.x()), 2) + Math.pow((p.y()-q.y()), 2) + Math.pow((p.z()-q.z()), 2)) ));
		Point3D result = new Point3D(x, y, z);
		return result;
	}
	
	public void draw(GL2 gl) {
		gl.glPushMatrix();
		gl.glPushAttrib(GL2.GL_CURRENT_BIT);
	    gl.glTranslatef(x, y, z);
	    gl.glMultMatrixf(this.preMatrix, 0);
		turtleBody.draw(gl);
		gl.glPopAttrib();
		gl.glPopMatrix();
	}
	
}
	
