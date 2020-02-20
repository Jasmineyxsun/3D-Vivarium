
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.jogamp.opengl.util.*;

import com.jogamp.opengl.util.gl2.GLUT;

import java.util.*;

public class ExampleModel extends Component implements Animate{

	private double rotateSpeed = 1;
	
	public ExampleModel(Point3D p, float scale) {
		super(new Point3D(p));
		
		Component fishBody = new Component(new Point3D(0, 0, 0), new FishTailDisplayable(scale));
		fishBody.setColor(new FloatColor(0.3f, 0.6f, 1f));
		this.addChild(fishBody);
		
		this.setYNegativeExtent(-30);
		this.setYPositiveExtent(30);
		
//		this.setExtentSwitch(false);
	}

	@Override
	public void setModelStates(ArrayList<Configuration> config_list) {
		if (config_list.size() > 1) {
			this.setConfiguration(config_list.get(0));
		}
	}
//	private Quaternion orientation = new Quaternion ((float)(Math.acos(0.3/2)),
//													(float)(Math.sin(0.3/2)), 
//													(float)(Math.sin(0.3/2)), 
//													(float)(Math.asin(0.3/2)));
	private Quaternion orientation = new Quaternion();
	private Point3D original_orientation = new Point3D(0, 0, 1);
	private Point3D target_orientation = new Point3D(0, 1, 0);
	
	@Override
	public void animationUpdate(GL2 gl) {
		//here!!!
		if (this.checkRotationReachedExtent(Axis.Y)) {
			rotateSpeed = -rotateSpeed;
		}
		
		this.rotate(Axis.Y, rotateSpeed);
	}
	
}
